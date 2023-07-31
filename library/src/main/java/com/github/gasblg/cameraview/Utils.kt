package com.github.gasblg.cameraview

import java.util.NavigableMap
import java.util.TreeMap

object Utils {

    fun getApertures() =
        floatArrayOf(
            1.2f,
            1.4f,
            1.8f,
            2.0f,
            2.2f,
            2.4f,
            2.8f,
            3.2f,
            3.6f,
            4.0f,
            4.5f,
            5.0f,
            5.6f,
            6.4f,
            7.1f,
            8.0f,
            9.0f,
            10.0f,
            11.0f,
            13.0f,
            14.0f,
            16.0f,
            18.0f,
            20.0f,
            22.0f,
            32.0f
        )

    fun getExposure(): NavigableMap<Float, String> {
        val map: NavigableMap<Float, String> = TreeMap()
        map.putAll(
            mapOf(
                0.25f to "4000",
                0.3125f to "3200",
                0.4f to "2500",
                0.5f to "2000",
                0.625f to "1600",
                0.8f to "1250",
                1f to "1000",
                1.25f to "800",
                1.5625f to "640",
                2f to "500",
                2.5f to "400",
                3.125f to "320",
                4f to "250",
                5f to "200",
                6.25f to "160",
                8f to "125",
                10f to "100",
                12.5f to "80",
                16f to "60",
                20f to "50",
                25f to "40",
                33f to "30",
                40f to "25",
                50f to "20",
                77f to "13",
                100f to "10",
                125f to "8",
                166f to "6",
                200f to "5",
                250f to "4",
                333f to "3",
                400f to "2.5",
                500f to "2",
                625f to "1.6",
                770f to "1.3",
                1000f to "1\"",
                1300f to "1.3\"",
                1500f to "1.5\"",
                2000f to "2\"",
                2500f to "2.5\"",
                3000f to "3\"",
                4000f to "4\"",
                5000f to "5\"",
                6500f to "6.5\"",
                8000f to "8\"",
                10_000f to "10\"",
                13_000f to "13\"",
                15_000f to "15\"",
                20_000f to "20\"",
                25_000f to "25\"",
                30_000f to "30\"",
                40_000f to "40\"",
                50_000f to "50\"",
                60_000f to "60\"",
                120_000f to "2'00\"",
                240_000f to "4'00\"",
                480_000f to "8'00\"",
                600_000f to "15'00\"",
                1_800_000f to "30'00\"",
                3_600_000f to "60'00\"",
            )
        )

        return map
    }
}