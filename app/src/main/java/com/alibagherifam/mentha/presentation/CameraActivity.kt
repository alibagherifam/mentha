package com.alibagherifam.mentha.presentation

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.load
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.business.ImageClassifier
import com.alibagherifam.mentha.databinding.ActivityCameraBinding
import com.alibagherifam.mentha.model.Food
import com.alibagherifam.mentha.model.FoodRepository
import com.alibagherifam.mentha.utils.stringFormatted
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }

    private lateinit var binding: ActivityCameraBinding

    // Todo: is init on object creation expensive?
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var camera: Camera
    private var isFoodSummaryAnimationRunning = false
    private var currentFood: Food? = null
    private lateinit var mainThreadHandler: Handler
    private var hideFoodSummaryTask: Runnable? = null

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindCameraUseCases()
        } else {
            finish()
        }
    }

    private val repository by lazy {
        FoodRepository.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val showNutritionFactsListener = View.OnClickListener {
            currentFood?.let { openFoodDetails(it) }
        }
        binding.cardFoodSummary.setOnClickListener(showNutritionFactsListener)
        binding.btnShowNutritionFacts.setOnClickListener(showNutritionFactsListener)
        binding.btnToggleFlash.addOnCheckedChangeListener { _, isChecked ->
            camera.cameraControl.enableTorch(isChecked)
        }

        if (hasCameraPermission()) {
            bindCameraUseCases()
        } else {
            requestCameraPermission()
        }

        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    private fun getPreviewUseCase(viewFinder: PreviewView) =
        Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(viewFinder.display.rotation)
            .build().also {
                // Attach the viewfinder's surface provider to preview use case
                // Todo: Sample calls this after binding use-cases
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

    private fun getImageAnalysisUseCase(
        viewFinder: PreviewView,
        executor: Executor,
        analyzer: ImageAnalysis.Analyzer
    ) = ImageAnalysis.Builder()
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .setTargetRotation(viewFinder.display.rotation)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
        .build().also {
            it.setAnalyzer(executor, analyzer)
        }

    private fun bindCameraUseCases() = binding.viewFinder.post {
        lifecycleScope.launch {
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val previewUseCase = getPreviewUseCase(binding.viewFinder)
            val imageAnalysisUseCase = getImageAnalysisUseCase(
                binding.viewFinder,
                executor,
                ImageClassifier(
                    context = this@CameraActivity,
                    onRecognition = ::handleRecognition
                )
            )

            val cameraProvider = ProcessCameraProvider
                .getInstance(this@CameraActivity).await()

            // Unbind use-cases before rebinding
            cameraProvider.unbindAll()

            camera = cameraProvider.bindToLifecycle(
                this@CameraActivity as LifecycleOwner,
                cameraSelector,
                previewUseCase,
                imageAnalysisUseCase
            )

            binding.btnToggleFlash.isEnabled = camera.cameraInfo.hasFlashUnit()
        }
    }

    @UiThread
    private fun handleRecognition(
        newRecognition: Category?
    ) = runOnUiThread {
        val newFood = newRecognition?.takeIf { it.isFood() }?.mapToFood()
        if (newFood != null && isHidingFoodSummaryRunning()) {
            cancelHidingFoodSummary()
        }
        if ((currentFood == null) xor (newFood == null)) {
            if (newFood == null) {
                hideFoodSummary()
            } else {
                showFoodSummary()
            }
        }
        if (newFood != null && newFood != currentFood) {
            updateFoodSummary(newFood)
            currentFood = newFood
        }
    }

    private fun isHidingFoodSummaryRunning() = (hideFoodSummaryTask != null)
    private fun cancelHidingFoodSummary() {
        mainThreadHandler.removeCallbacks(hideFoodSummaryTask!!)
        hideFoodSummaryTask = null
    }

    private fun showFoodSummary() {
        setFoodSummaryVisible(true)
    }

    private fun hideFoodSummary() {
        hideFoodSummaryTask = mainThreadHandler.postDelayed(800) {
            setFoodSummaryVisible(false)
            currentFood = null
        }
    }

    private fun updateFoodSummary(food: Food) {
        binding.apply {
            tvFoodName.text = food.name
            tvFoodEnergy.text = getString(
                R.string.label_energy_in_kilo_calorie,
                food.energy.stringFormatted()
            )
            imgFoodIcon.load(food.icon ?: R.drawable.img_banana)
        }
    }

    private fun setFoodSummaryVisible(isVisible: Boolean) {
        if (isFoodSummaryAnimationRunning) {
            return
        }
        val cardFoodSummary = binding.cardFoodSummary

        val isNewStateSameAsCurrent = !(isVisible xor cardFoodSummary.isVisible)
        if (isNewStateSameAsCurrent) {
            return
        }

        createAnimator(
            view = cardFoodSummary,
            opacity = if (isVisible) 1f else 0f,
            transitionY = if (isVisible) -30f else 30f
        ).apply {
            addListener(
                onStart = {
                    isFoodSummaryAnimationRunning = true
                    cardFoodSummary.isVisible = true
                },
                onEnd = {
                    isFoodSummaryAnimationRunning = false
                    cardFoodSummary.isVisible = isVisible
                }
            )
        }.start()
    }

    private fun createAnimator(view: View, opacity: Float, transitionY: Float): AnimatorSet {
        val animOpacity = ObjectAnimator.ofFloat(view, View.ALPHA, opacity)
        val animTransitionY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, transitionY)
        return AnimatorSet().apply {
            duration = 700
            playTogether(animOpacity, animTransitionY)
        }
    }

    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(
            this, PERMISSION_CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
            Toast.makeText(
                this,
                "Camera permission is required!",
                Toast.LENGTH_LONG
            ).show()
        }
        requestPermissionsLauncher.launch(PERMISSION_CAMERA)
    }

    private fun openFoodDetails(food: Food) {
        val i = Intent(this, FoodDetailsActivity::class.java)
        i.putExtra(Food::id.name, food.id)
        startActivity(i)
    }

    private fun Category.isFood() =
        repository.foods.any { it.id == this.label }

    private fun Category.mapToFood() =
        repository.getFood(this.label)
}
