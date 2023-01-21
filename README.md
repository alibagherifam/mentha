![Banner](https://raw.github.com/alibagherifam/mentha/master/screenshots/mentha_banner.png)

Find nutrition facts using your phone camera!

Mentha is an Android application that finds nutrition facts of the food you are scanning. It uses
image classification and neural networks under the hood.

## Screenshots

![Screenshots](https://raw.github.com/alibagherifam/mentha/master/screenshots/mentha_screenshots.png)

## ⚙ Technologies

Image classification is based on [TensorFlow Lite](https://www.tensorflow.org/lite)
with [EfficientNet-Lite](https://tfhub.dev/tensorflow/efficientnet/lite2/classification/2) and
[MobileNet](https://tfhub.dev/iree/lite-model/mobilenet_v1_100_224/uint8/1) models.

Other Android technologies:

- Kotlin Coroutines
- AndroidX Camera
- AndroidX Lifecycle
- AndroidX Room
- KSP
- Jetpack Compose
- Material 3
- Coil
- Composite Builds
- Convention Plugins
- Version Catalog

## 📕 Terminology

According to [Wikipedia](https://en.wikipedia.org/wiki/Mentha):

> "Mentha (also known as mint) is a genus of plants in the family Lamiaceae (mint family)."

As a boy whose childhood was full of stomachaches, the word mentha means as a potion of healing to
me. So I set it as the project's name.

## 💡 Inspiration

This was the final project for my B.Sc. in Computer Engineering.

## 🤝 Contribution

Feel free to create pull requests, especially AI model improvements ;)

## 🙏 Acknowledgment

- Thanks to [Dr. Hamidreza Hamidi](http://ikiu.ac.ir/members/?id=46&lang=1) for his guidance toward
  the project.

- Thanks to [Amir H. Bagherifam](https://dribbble.com/Amirh_UiUX) for designing the logo.

License
-------

	Copyright (C) 2023 Ali Bagherifam

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
