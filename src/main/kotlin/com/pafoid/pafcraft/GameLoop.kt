package com.pafoid.pafcraft

import com.pafoid.markii.*
import com.pafoid.markii.assets.AssetPool
import com.pafoid.markii.assets.Shader
import com.pafoid.markii.assets.Texture
import com.pafoid.markii.entities.Entity
import com.pafoid.markii.models.RawModel
import com.pafoid.markii.models.TexturedModel
import com.pafoid.markii.utils.Color
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*

class GameLoop {

    private val clearColor = Color.RED
    private val loader = Loader()

    private lateinit var shader: Shader
    private lateinit var renderer: Renderer
    private lateinit var camera: Camera
    private lateinit var light: Light

    private lateinit var rawModel: RawModel
    private lateinit var texture: Texture
    private lateinit var texturedModel: TexturedModel
    private lateinit var entity: Entity

    fun start() {
        loadResources()
        val objLoader = ObjLoader()
        rawModel = objLoader.loadObjModel(ObjLoader.DRAGON, loader)
        texturedModel = TexturedModel(rawModel, texture)
        entity = Entity(texturedModel, Transform(Vector3f(0f, 0f, -25f)))
    }

    fun draw(dt: Float) {
        clearColor()

        if (dt >= 0) {
            entity.rotate(0f, 1f)
            camera.move()
            shader.start()

            renderer.render(entity)
            shader.stop()
        }
    }

    fun destroy() {
        loader.cleanUp()
        shader.destroy()
        renderer.destroy()
    }

    private fun loadResources() {
        shader = AssetPool.getShader(Shader.TEST)

        light = Light(Vector3f(0f, 0f, -20f))
        camera = Camera()

        renderer = Renderer(shader, camera, light)

        texture = AssetPool.getTexture(Texture.WHITE)
    }

    private fun clearColor() {
        glEnable(GL_DEPTH_TEST)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
    }
}