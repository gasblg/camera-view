package com.github.gasblg.cameraview

interface ShotListener {
    fun onPush(
        number: Int,
        aperture: Float,
        exposure: String
    )
}