package com.alibagherifam.mentha.camera

import java.text.DecimalFormat

fun Float.stringFormatted(): String =
    DecimalFormat("0").apply {
        maximumFractionDigits = 3
    }.format(this)
