package com.pafoid.markii.assets

import org.lwjgl.opengl.GL20
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

const val SPLITTER_REGEX = "(#type)( )+([a-zA-Z]+)"
const val TYPE_DELIMITER = "#type"
const val EOL_DELIMITER = "\r\n"
const val TYPE_DELIMITER_COUNT = 6
const val FRAGMENT = "fragment"
const val VERTEX = "vertex"

class ShaderLoader(private var verbose:Boolean = false) {

    fun loadShader(filePath: String):Shader {
        return try {
            loadFromFile(filePath)
        } catch(e: IOException) {
            assert(false) { "Error: Could not open file for shader: $filePath, returning default Shader" }
            Shader()
        }
    }

    private fun loadFromFile(filePath: String):Shader {
        val src = String(Files.readAllBytes(Paths.get(filePath)))
        val splitSrc = src.split(SPLITTER_REGEX.toRegex())

        var index = src.indexOf(TYPE_DELIMITER) + TYPE_DELIMITER_COUNT
        var eol = src.indexOf(EOL_DELIMITER, index)
        val firstPattern = src.substring(index, eol).trim()

        index = src.indexOf(TYPE_DELIMITER, eol) + TYPE_DELIMITER_COUNT
        eol = src.indexOf(EOL_DELIMITER, index)
        val secondPattern = src.substring(index, eol).trim()

        var vertexSrc = ""
        var fragmentSrc = ""

        if(firstPattern == VERTEX) {
            vertexSrc = splitSrc[1]
        } else if(firstPattern == FRAGMENT) {
            fragmentSrc = splitSrc[2]
        } else {
            throw IOException("Unexpected token '$firstPattern'")
        }

        if(secondPattern == VERTEX) {
            vertexSrc = splitSrc[1]
        } else if(secondPattern == FRAGMENT) {
            fragmentSrc = splitSrc[2]
        } else {
            throw IOException("Unexpected token '$secondPattern'")
        }

        if(verbose) {
            println("Vertex Source : '$vertexSrc'")
            println("Fragment Source : '$fragmentSrc'")
        }

        return compile(filePath, vertexSrc, fragmentSrc)
    }

    private fun compile(filePath: String, vertexSrc:String, fragmentSrc:String):Shader {
        // First load and compile the vertex shader
        val vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        // Pass the shader source code to the GPU
        GL20.glShaderSource(vertexShaderId, vertexSrc)
        GL20.glCompileShader(vertexShaderId)

        //Check for errors in compilation process
        var success = GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS)
        if(success == GL20.GL_FALSE) {
            val len = GL20.glGetShaderi(vertexShaderId, GL20.GL_INFO_LOG_LENGTH)
            println("Error: $filePath \r\nVertex compilation failed")
            println(GL20.glGetShaderInfoLog(vertexShaderId, len))
            assert(false) { "Vertex compilation failed" }
        }

        // Then load and compile the fragment shader
        val fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        // Pass the shader source code to the GPU
        GL20.glShaderSource(fragmentShaderId, fragmentSrc)
        GL20.glCompileShader(fragmentShaderId)

        //Check for errors in compilation process
        success = GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS)
        if(success == GL20.GL_FALSE) {
            val len = GL20.glGetShaderi(fragmentShaderId, GL20.GL_INFO_LOG_LENGTH)
            println("Error: '$filePath'\r\nFragment compilation failed")
            println(GL20.glGetShaderInfoLog(fragmentShaderId, len))
            assert(false) { "Fragment compilation failed" }
        }

        // Link shaders and check for error
        val shaderProgId = GL20.glCreateProgram()
        GL20.glAttachShader(shaderProgId, vertexShaderId)
        GL20.glAttachShader(shaderProgId, fragmentShaderId)

        // Bind attribs, new stuff
        bindAttribute(shaderProgId,0, "aPos")
        bindAttribute(shaderProgId,1, "aTexCoords")
        bindAttribute(shaderProgId,2, "aNormal")

        GL20.glLinkProgram(shaderProgId)

        // Check for linking errors
        success = GL20.glGetProgrami(shaderProgId, GL20.GL_LINK_STATUS)
        if(success == GL20.GL_FALSE) {
            val len = GL20.glGetProgrami(shaderProgId, GL20.GL_INFO_LOG_LENGTH)
            println("Error: '$filePath'\r\nLinking of shaders failed")
            println(GL20.glGetProgramInfoLog(shaderProgId, len))
            assert(false) {"Linking of shaders failed"}
        }

        return Shader(shaderProgId, vertexShaderId, fragmentShaderId)
    }

    private fun bindAttribute(shaderProgId:Int, attribute: Int, varName: String) {
        GL20.glBindAttribLocation(shaderProgId, attribute, varName)
    }
}