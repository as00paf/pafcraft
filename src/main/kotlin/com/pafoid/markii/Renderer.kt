package com.pafoid.markii

import com.pafoid.markii.assets.Shader
import com.pafoid.markii.entities.Entity
import com.pafoid.markii.models.TexturedModel
import org.joml.Matrix4f
import org.lwjgl.opengl.GL30.*

class Renderer(
    private val shader: Shader,
    private val camera: Camera,
    private val light: Light) {

    init {
        loadProjectionMatrix()
        loadViewMatrix()
    }

    private fun loadProjectionMatrix() {
        shader.start()
        shader.uploadMat4f("projectionMatrix", camera.createProjectionMatrix())
        shader.stop()
    }

    private fun loadViewMatrix() {
        shader.start()
        shader.uploadMat4f("viewMatrix", camera.createViewMatrix())
        shader.stop()
    }

    fun render(entity: Entity) {
        loadViewMatrix()
        val texturedModel = entity.model
        val model = texturedModel.rawModel

        glBindVertexArray(model.vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)
        shader.uploadMat4f("transformationMatrix", entity.transform.toMatrix())
        shader.uploadVec3f("lightPosition", light.position)
        shader.uploadVec3f("lightColor", light.color)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texturedModel.texture.getId())
        glDrawElements(GL_TRIANGLES, model.vertexCount, GL_UNSIGNED_INT, 0)
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(2)
        glBindVertexArray(0)
    }

    fun destroy() {
        shader.destroy()
    }

}