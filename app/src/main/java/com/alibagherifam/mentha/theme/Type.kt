package com.alibagherifam.mentha.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.alibagherifam.mentha.R

val IranSans = FontFamily(
    Font(R.font.iran_sans_regular),
    Font(R.font.iran_sans_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    defaultFontFamily = IranSans,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        letterSpacing = 0.sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        letterSpacing = 0.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.sp
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp
    ),
    overline = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.sp
    )
)