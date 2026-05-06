package com.webscare.numberplategenerator.data.datasource

import androidx.compose.ui.graphics.Color
import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.DimensionOption
import com.webscare.numberplategenerator.ui.FlagOption
import com.webscare.numberplategenerator.ui.PlateBackground
import com.webscare.numberplategenerator.ui.PlateType

object AssetDataProvider {
    val solidColors = listOf(
        0xFF9B59B6.toInt(), 0xFFFFFFFF.toInt(), 0xFFE67E22.toInt(),
        0xFF2ECC71.toInt(), 0xFF2C3E9E.toInt(), 0xFFE74C3C.toInt(),
        0xFF1ABC9C.toInt(), 0xFFF39C12.toInt(), 0xFF8E44AD.toInt(),
        0xFF2980B9.toInt(), 0xFF27AE60.toInt(), 0xFFD35400.toInt(),
        0xFFC0392B.toInt(), 0xFF7F8C8D.toInt(), 0xFF2C3E50.toInt(),
        0xFFBDC3C7.toInt(), 0xFFF1C40F.toInt(), 0xFFE91E63.toInt(),
        0xFF00BCD4.toInt(), 0xFF795548.toInt(), 0xFF607D8B.toInt(),
        0xFF4CAF50.toInt(), 0xFFFF5722.toInt(), 0xFF3F51B5.toInt(),
        0xFF009688.toInt(), 0xFFFF9800.toInt(), 0xFF673AB7.toInt(),
        0xFF000000.toInt(), 0xFF37474F.toInt(), 0xFF00FF00.toInt(),
        0xFF00FFFF.toInt(), 0xFFFF00FF.toInt(), 0xFFFFD700.toInt(),
        0xFFFF3131.toInt(), 0xFFFFB7B2.toInt(), 0xFFFFDAC1.toInt(),
        0xFFB5EAD7.toInt(), 0xFFC7CEEA.toInt(), 0xFF004953.toInt(),
        0xFF301934.toInt(), 0xFFCFB53B.toInt(), 0xFFBC8F8F.toInt(),
        0xFF556B2F.toInt(), 0xFFFF69B4.toInt(), 0xFFFFA500.toInt(),
        0xFFADFF2F.toInt(), 0xFF40E0D0.toInt(), 0xFFEE82EE.toInt(),
        0xFFF5F5DC.toInt()
    )
    val plateDimensions = listOf(
        // --- CAR BACK (3 Items - Realistic) ---
        DimensionOption("car_back_std", "Standard Wide", 520f, 110f, PlateType.CAR, isFront = false), // Standard UK/Euro size
        DimensionOption("car_back_sq", "Square (SUV/4x4)", 285f, 203f, PlateType.CAR, isFront = false), // Common for Japanese imports/SUVs
        DimensionOption("car_back_us", "US / Small", 305f, 152f, PlateType.CAR, isFront = false), // Standard US 12x6 inch

        // --- CAR FRONT (2 Items - Realistic) ---
        DimensionOption("car_front_slim", "Slim Modern", 372f, 100f, PlateType.CAR, isFront = true),
        DimensionOption("car_front_bold", "Bold Square", 305f, 155f, PlateType.CAR, isFront = true),


        // --- BIKE BACK (3 Items - Realistic) ---
        DimensionOption("bike_back_std", "Standard Bike", 228f, 178f, PlateType.BIKE, isFront = false), // Standard 9x7 inch
        DimensionOption("bike_back_custom", "Custom Small", 152f, 101f, PlateType.BIKE, isFront = false), // 6x4 inch show plate

        // --- BIKE FRONT (2 Items - Realistic) ---
        DimensionOption("bike_front_std", "Standard Front", 200f, 150f, PlateType.BIKE, isFront = true),
        DimensionOption("bike_front_sticker", "Small Sticker", 150f, 50f, PlateType.BIKE, isFront = true) // Mudguard sticker style
    )
    val flagOptions = listOf(
        FlagOption("none", "None"),
        FlagOption("pk", "Pakistan", R.drawable.flag_pk),
        FlagOption("pb", "Punjab", R.drawable.flag_punjab),
        FlagOption("sd", "Sindh", R.drawable.flag_sindh),
        FlagOption("kp", "KPK", R.drawable.flag_kpk),
        FlagOption("bl", "Balochistan", R.drawable.flag_balochistan),
        FlagOption("gb", "Gilgit-Bal.", R.drawable.flag_gilgit),
        FlagOption("aj", "Azad Kashmir", R.drawable.flag_azad_kashmir),
        FlagOption("ict", "Islamabad", R.drawable.flag_federal)
    )

    // --- Stickers List ---
    val stickerOptions = listOf(
        null, // None
        R.drawable.st_star,
        R.drawable.st_moon,
        R.drawable.st_shield,
        R.drawable.st_circle,
        R.drawable.st_diamond,
        R.drawable.st_hexagon,
        R.drawable.st_rect,
        R.drawable.st_crown,
        R.drawable.st_leaf,
        R.drawable.st_bolt,
        R.drawable.st_badge,
        R.drawable.st_flower
    )

    val plateBackgrounds = listOf(
        // --- CAR PLATES (PlateType.CAR) ---
        PlateBackground("pb_car", "Punjab", "Private — White", Color(0xFF2E7D32), Color.White, "GOVERNMENT OF PUNJAB", PlateType.CAR),
        PlateBackground("sd_car", "Sindh", "Private — White", Color(0xFF1976D2), Color.White, "GOVERNMENT OF SINDH", PlateType.CAR),
        PlateBackground("bal_car", "Balochistan", "Private — White", Color(0xFF0D47A1), Color.White, "GOVERNMENT OF BALOCHISTAN", PlateType.CAR),
        PlateBackground("kpk_car", "KPK", "Private — White", Color(0xFFB71C1C), Color.White, "GOVERNMENT OF KPK", PlateType.CAR),
        PlateBackground("ajk_car", "Azad Kashmir", "Private — White", Color(0xFF1B5E20), Color.White, "AZAD KASHMIR", PlateType.CAR),
        PlateBackground("gb_car", "Gilgit Baltistan", "Private — White", Color(0xFF006064), Color.White, "GILGIT BALTISTAN", PlateType.CAR),
        PlateBackground("ev_car", "Electric Vehicle", "Eco-Friendly Green", Color(0xFF2E7D32), Color(0xFFE8F5E9), "ELECTRIC VEHICLE", PlateType.CAR),

        // --- BIKE PLATES (PlateType.BIKE) ---
        PlateBackground("pb_bike", "Punjab Bike", "Two-wheeler", Color(0xFF2E7D32), Color.White, "GOVERNMENT OF PUNJAB", PlateType.BIKE),
        PlateBackground("sd_bike", "Sindh Bike", "Two-wheeler", Color(0xFF1976D2), Color.White, "GOVERNMENT OF SINDH", PlateType.BIKE),
        PlateBackground("bal_bike", "Balochistan Bike", "Two-wheeler", Color(0xFF0D47A1), Color.White, "GOVERNMENT OF BALOCHISTAN", PlateType.BIKE),
        PlateBackground("kpk_bike", "KPK Bike", "Two-wheeler", Color(0xFFB71C1C), Color.White, "GOVERNMENT OF KPK", PlateType.BIKE),
        PlateBackground("ajk_bike", "Azad Kashmir Bike", "Two-wheeler", Color(0xFF1B5E20), Color.White, "AZAD KASHMIR", PlateType.BIKE),
        PlateBackground("gb_bike", "Gilgit Baltistan Bike", "Two-wheeler", Color(0xFF006064), Color.White, "GILGIT BALTISTAN", PlateType.BIKE),
        PlateBackground("ev_bike", "Electric Bike", "Eco-Friendly Green", Color(0xFF2E7D32), Color(0xFFE8F5E9), "ELECTRIC VEHICLE", PlateType.BIKE)
    )
}