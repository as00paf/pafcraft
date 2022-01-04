package com.pafoid.markii

import org.joml.Vector3f

data class Light (val position: Vector3f, val color: Vector3f = Vector3f(1f, 1f, 1f))