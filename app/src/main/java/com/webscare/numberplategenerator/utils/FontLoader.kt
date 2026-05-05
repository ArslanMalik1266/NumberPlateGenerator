package com.webscare.numberplategenerator.utils

import android.graphics.Typeface
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontFamily
import com.webscare.numberplategenerator.data.local.font.FontStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.net.URL

@Composable
fun loadFontFromUrl(
    fontUrl: String?,
    fontStorage: FontStorage = koinInject() // Koin injection for clean architecture
): FontFamily {
    // 1. Basic Validation
    if (fontUrl.isNullOrEmpty()) return FontFamily.Default

    // 2. URL se unique filename/slug nikalein (e.g., "jameel_noori")
    val slug = fontUrl.substringAfterLast("/").substringBeforeLast(".")

    // 3. Initial State: Check if file already exists in Internal Storage
    var fontFamily by remember(fontUrl) {
        mutableStateOf(
            if (fontStorage.exists(slug)) {
                try {
                    FontFamily(Typeface.createFromFile(fontStorage.fileFor(slug)))
                } catch (e: Exception) {
                    FontFamily.Default
                }
            } else {
                FontFamily.Default
            }
        )
    }

    // 4. Download Logic: Only triggers if file is missing
    LaunchedEffect(fontUrl) {
        if (!fontStorage.exists(slug)) {
            withContext(Dispatchers.IO) {
                try {
                    val targetFile = fontStorage.fileFor(slug)

                    // Permanent download (No temp files)
                    URL(fontUrl).openStream().use { input ->
                        targetFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }

                    // Mapping downloaded file to Typeface
                    val typeface = Typeface.createFromFile(targetFile)

                    // Update UI on Main Thread
                    withContext(Dispatchers.Main) {
                        fontFamily = FontFamily(typeface)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Fallback to default on failure
                    withContext(Dispatchers.Main) {
                        fontFamily = FontFamily.Default
                    }
                }
            }
        }
    }

    return fontFamily
}