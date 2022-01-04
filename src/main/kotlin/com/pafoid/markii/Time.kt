package com.pafoid.markii

import org.lwjgl.glfw.GLFW

object Time {
    fun getTime(): Float = GLFW.glfwGetTime().toFloat()
}