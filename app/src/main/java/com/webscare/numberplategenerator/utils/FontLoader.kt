package com.webscare.numberplategenerator.utils

import android.graphics.Typeface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun loadFontFromUrl(fontUrl: String?): FontFamily {
    if (fontUrl.isNullOrEmpty()) return FontFamily.Default
    val context = LocalContext.current
    // Font ka naam URL se nikalein (e.g., "myfont.ttf")====================================
    val fileName = fontUrl.substringAfterLast("/")
    val fontFile = java.io.File(context.filesDir, fileName)
    var fontFamily by remember(fontUrl) {
        mutableStateOf<FontFamily>(
            if (fontFile.exists()) FontFamily(Typeface.createFromFile(fontFile))
            else FontFamily.Default
        )
    }
    LaunchedEffect(fontUrl) {
        withContext(Dispatchers.IO) {
            try {
                val inputStream = URL(fontUrl).openStream()
                val tempFile = java.io.File.createTempFile("temp_font", ".ttf")
                tempFile.deleteOnExit()

                inputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val typeface = Typeface.createFromFile(tempFile)
                fontFamily = FontFamily(typeface)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    return fontFamily
}