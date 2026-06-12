package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {
    fun createPdfFromImages(context: Context, images: List<String>, outputFileName: String): File? {
        if (images.isEmpty()) return null

        val pdfDocument = PdfDocument()
        val paint = Paint()

        for ((index, imagePath) in images.withIndex()) {
            val bitmap = BitmapFactory.decodeFile(imagePath.replace("file://", ""))
                ?: continue

            // A4 page dimensions in pixels at 72 PPI are 595 x 842.
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index + 1).create()
            val page = pdfDocument.startPage(pageInfo)
            
            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            pdfDocument.finishPage(page)
        }

        val outputDir = File(context.filesDir, "PDFs")
        if (!outputDir.exists()) outputDir.mkdirs()

        val file = File(outputDir, "$outputFileName.pdf")
        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            pdfDocument.close()
        }
    }
}
