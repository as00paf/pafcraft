package com.pafoid.markii.utils

fun Float.toRadians(): Float {
    return Math.toRadians(this.toDouble()).toFloat()
}