package com.example.shroom.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.abs

/**
 * Magnetic / rotation-vector compass heading in degrees (0 = North, clockwise).
 */
class CompassSensor(context: Context) {
    private val sensorManager =
        context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val headingDegrees: Flow<Float> = callbackFlow {
        val rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var gravity: FloatArray? = null
        var geomagnetic: FloatArray? = null
        var lastEmitted = Float.NaN
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        fun emitIfChanged(raw: Float) {
            var heading = (raw + 360f) % 360f
            if (lastEmitted.isNaN() || abs(shortestAngleDelta(lastEmitted, heading)) >= 1.5f) {
                lastEmitted = heading
                trySend(heading)
            }
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                        SensorManager.getOrientation(rotationMatrix, orientation)
                        val azimuthDeg = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        emitIfChanged(azimuthDeg)
                    }
                    Sensor.TYPE_ACCELEROMETER -> {
                        gravity = lowPass(event.values.clone(), gravity)
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        geomagnetic = lowPass(event.values.clone(), geomagnetic)
                        val g = gravity
                        val m = geomagnetic
                        if (g != null && m != null) {
                            if (SensorManager.getRotationMatrix(rotationMatrix, null, g, m)) {
                                SensorManager.getOrientation(rotationMatrix, orientation)
                                val azimuthDeg =
                                    Math.toDegrees(orientation[0].toDouble()).toFloat()
                                emitIfChanged(azimuthDeg)
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        val registered = when {
            rotation != null -> {
                sensorManager.registerListener(
                    listener,
                    rotation,
                    SensorManager.SENSOR_DELAY_UI,
                )
                true
            }
            accelerometer != null && magnetometer != null -> {
                sensorManager.registerListener(
                    listener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI,
                )
                sensorManager.registerListener(
                    listener,
                    magnetometer,
                    SensorManager.SENSOR_DELAY_UI,
                )
                true
            }
            else -> false
        }

        if (!registered) {
            close()
            return@callbackFlow
        }

        awaitClose { sensorManager.unregisterListener(listener) }
    }

    private fun lowPass(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] + 0.15f * (input[i] - output[i])
        }
        return output
    }

    private fun shortestAngleDelta(from: Float, to: Float): Float {
        var delta = (to - from) % 360f
        if (delta < -180f) delta += 360f
        if (delta > 180f) delta -= 360f
        return delta
    }
}
