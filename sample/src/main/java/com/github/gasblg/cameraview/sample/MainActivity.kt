package com.github.gasblg.cameraview.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.gasblg.cameraview.databinding.ActivityMainBinding
import com.github.gasblg.cameraview.ShotListener


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

       binding!!.camera.setShotListener(object : ShotListener {
            override fun onPush(
                number: Int,
                aperture: Float,
                exposure: String
            ) {
                println("shot $number\taperture $aperture\texposure $exposure")
            }
        })

    }
}