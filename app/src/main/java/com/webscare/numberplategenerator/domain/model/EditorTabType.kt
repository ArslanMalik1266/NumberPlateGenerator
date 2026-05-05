package com.webscare.numberplategenerator.domain.model

enum class EditorTabType(val id: String) {
    DIMENSION("dimension"),
    TEXT("text"),
    STYLE("style"),
    BACKGROUND("bg"),
    FLAG("flag"),
    STICKER("sticker"),
    NAME("name"),
    TYPE("type");

    companion object {
        fun fromId(id: String): EditorTabType {
            return values().find { it.id == id } ?: TEXT
        }
    }
}