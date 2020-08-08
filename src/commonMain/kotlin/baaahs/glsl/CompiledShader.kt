package baaahs.glsl

import baaahs.Logger
import com.danielgergely.kgl.GL_COMPILE_STATUS
import com.danielgergely.kgl.GL_TRUE
import com.danielgergely.kgl.Shader

class CompiledShader(
    private val gl: GlslContext,
    type: Int,
    internal val source: String
) {
    val shaderId: Shader = gl.runInContext {
        gl.check { createShader(type) ?: throw IllegalStateException() }
    }

    init {
        compile()
    }

    private fun compile() {
        gl.runInContext {
            gl.check { shaderSource(shaderId, source) }
            gl.check { compileShader(shaderId) }
        }
    }

    fun validate() {
        gl.runInContext {
            if (gl.check { getShaderParameter(shaderId, GL_COMPILE_STATUS) } != GL_TRUE) {
                val infoLog = gl.check { getShaderInfoLog(shaderId) }
                throw CompilationException(infoLog ?: "huh?")
            }
        }
    }

    companion object {
        val logger = Logger("CompiledShader")
    }
}

