# CameraView
[![](https://jitpack.io/v/gasblg/camera-view.svg)](https://jitpack.io/#gasblg/camera-view)

Custom View for photo camera

<img src="https://github.com/gasblg/camera-view/raw/main/media/animation.gif" width="256"/>



# Installation
**Step 1.** Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency

```gradle
dependencies {
    implementation 'com.github.gasblg:camera-view:1.0.0'
}
```


# Usage
Add `CameraView` to your XML layout:
```xml
<com.github.gasblg.cameraview.CameraView
    android:id="@+id/camera"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:body_color="@color/body_color"
    app:border_color="@color/border_color"
    app:button_color="@color/button_color"
    app:case_color="@color/case_color"
    app:description="@string/lens_name"
    app:glass_color="@color/glass_color"
    app:hole_color="@color/hole_color"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:model_color="@color/model_color"
    app:model_name="@string/model_name"
    app:model_text_color="@color/model_text_color"
    app:name="@string/camera_name"
    app:name_text_color="@color/name_text_color"
    app:objective_text_color="@color/objective_text_color"
    app:settings_text_color="@color/settings_text_color"
    app:show_settings="true" />
```
Add colors:
```xml
    <color name="body_color">#bdbdbd</color>
    <color name="button_color">#e53935</color>
    <color name="border_color">@android:color/black</color>
    <color name="case_color">#6d4c41</color>
    <color name="model_color">#bdbdbd</color>
    <color name="glass_color">#263d5afe</color>
    <color name="hole_color">@android:color/white</color>

    <color name="name_text_color">@android:color/black</color>
    <color name="model_text_color">@android:color/black</color>
    <color name="settings_text_color">@android:color/white</color>
    <color name="objective_text_color">@android:color/black</color>
```
Add strings:
```xml
    <string name="camera_name">FUJIFILM</string>
    <string name="model_name">x10</string>
    <string name="lens_name">FUJINON X 50mm LENS 1:1.2 R WR</string>
```
in your `Activity` or `Fragment`:
```kotlin
binding!!.camera.setShotListener(object : ShotListener {
     override fun onPush(
         number: Int,
         aperture: Float,
         exposure: String
     ) {
         println("shot $number\taperture $aperture\texposure $exposure")
     }
 })
```


## License
```
MIT License

Copyright (c) 2023 Anatoly Grinchenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

