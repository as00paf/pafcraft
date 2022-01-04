package com.pafoid.markii.entities

import com.pafoid.markii.Transform
import com.pafoid.markii.models.TexturedModel
import org.joml.Vector3f

class Entity(
    val model: TexturedModel,
    val transform: Transform = Transform()
) {

    fun translate(dx: Float = 0f, dy: Float = 0f, dz: Float = 0f) {
        translate(Vector3f(dx, dy, dz))
    }

    fun translate(translation: Vector3f) {
        transform.translation.x += translation.x
        transform.translation.y += translation.y
        transform.translation.z += translation.z
    }

    fun rotate(rx: Float = 0f, ry: Float = 0f, rz: Float = 0f) {
        rotate(Vector3f(rx, ry, rz))
    }

    fun rotate(rotation: Vector3f) {
        transform.rotation.x += rotation.x
        transform.rotation.y += rotation.y
        transform.rotation.z += rotation.z
    }

}