package com.tapyou.myPointsGraph.ui.helpers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import com.github.mikephil.charting.charts.LineChart
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChartSaver(private val context: Context) {

    fun saveChart(lineChart: LineChart): String? {
        val fileName = "Chart_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.png"
        val bitmap = lineChart.chartBitmap

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = resolver.openOutputStream(uri)
                outputStream?.let { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            } finally {
                outputStream?.close()
            }
            return uri.toString()
        }

        return null
    }
}
