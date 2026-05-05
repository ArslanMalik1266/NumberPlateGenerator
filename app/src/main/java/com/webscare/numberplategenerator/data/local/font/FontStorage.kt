package com.webscare.numberplategenerator.data.local.font

import android.content.Context
import java.io.File

class FontStorage(private val context: Context) {
    private val fontsDir: File by lazy {
        File(context.filesDir, "fonts").apply {
            if (!exists()) mkdirs()
        }
    }

    fun fileFor(slug: String): File = File(fontsDir, "$slug.ttf")

    fun exists(slug: String): Boolean = fileFor(slug).exists()

    fun allDownloadedSlugs(): Set<String> {
        return fontsDir.listFiles()
            ?.filter { it.isFile && it.extension.equals("ttf", ignoreCase = true) }
            ?.map { it.nameWithoutExtension }
            ?.toSet() ?: emptySet()
    }
}