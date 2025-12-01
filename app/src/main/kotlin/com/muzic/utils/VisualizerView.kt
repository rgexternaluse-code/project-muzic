package com.muzic.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.audiofx.Visualizer
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class VisualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.CYAN
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val bytes = ByteArray(256)
    private var visualizer: Visualizer? = null
    private var animationThread: Thread? = null
    private var isRunning = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width / 2f) * 0.8f

        // Draw bars in circular pattern
        for (i in bytes.indices step 4) {
            val angle = (i / 256f) * 360f * (Math.PI / 180)
            val barHeight = ((bytes[i].toInt() and 0xff) / 256f) * 40f

            val x1 = centerX + (radius * cos(angle)).toFloat()
            val y1 = centerY + (radius * sin(angle)).toFloat()
            val x2 = centerX + ((radius + barHeight) * cos(angle)).toFloat()
            val y2 = centerY + ((radius + barHeight) * sin(angle)).toFloat()

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    fun setupVisualizer(audioSessionId: Int) {
        try {
            visualizer = Visualizer(audioSessionId)
            visualizer?.captureSize = 256
            visualizer?.setDataCaptureListener(
                object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(
                        visualizer: Visualizer?,
                        waveform: ByteArray?,
                        samplingRate: Int
                    ) {
                        if (waveform != null) {
                            System.arraycopy(waveform, 0, bytes, 0, waveform.size.coerceAtMost(bytes.size))
                            postInvalidate()
                        }
                    }

                    override fun onFftDataCapture(
                        visualizer: Visualizer?,
                        fft: ByteArray?,
                        samplingRate: Int
                    ) {}
                },
                Visualizer.getMaxCaptureRate(),
                true,
                false
            )
            visualizer?.enabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        visualizer?.enabled = false
        visualizer?.release()
        isRunning = false
        animationThread?.interrupt()
    }
}
