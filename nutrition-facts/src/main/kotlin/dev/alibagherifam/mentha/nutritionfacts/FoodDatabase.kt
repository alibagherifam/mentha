package dev.alibagherifam.mentha.nutritionfacts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity

@Database(
    entities = [FoodEntity::class],
    version = 1
)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun getFoodDao(): FoodDao

    companion object {
        private var INSTANCE: FoodDatabase? = null

        fun getInstance(context: Context): FoodDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = FoodDatabase::class.java,
                    name = "food-database"
                ).createFromAsset(databaseFilePath = "database/food-database.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
