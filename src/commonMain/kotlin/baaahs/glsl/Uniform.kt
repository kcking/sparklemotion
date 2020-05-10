package baaahs.glsl

import baaahs.glshaders.GlslProgram
import com.danielgergely.kgl.Kgl
import com.danielgergely.kgl.UniformLocation

class Uniform internal constructor(
    private val glslProgram: GlslProgram,
    private val uniformLocation: UniformLocation
) {
    fun set(x: Int) = withProgram { uniform1i(uniformLocation, x) }
    fun set(x: Int, y: Int) = withProgram { uniform2i(uniformLocation, x, y) }
    fun set(x: Int, y: Int, z: Int) = withProgram { uniform3i(uniformLocation, x, y, z) }
    fun set(x: Float) = withProgram { uniform1f(uniformLocation, x) }
    fun set(x: Float, y: Float) = withProgram { uniform2f(uniformLocation, x, y) }
    fun set(x: Float, y: Float, z: Float) = withProgram { uniform3f(uniformLocation, x, y, z) }
    fun set(x: Float, y: Float, z: Float, w: Float) = withProgram { uniform4f(uniformLocation, x, y, z, w) }

    fun set(textureUnit: GlslContext.TextureUnit) = textureUnit.setUniform(this)

    private fun <T> withProgram(fn: Kgl.() -> T): T {
        glslProgram.gl.useProgram(glslProgram)
        return glslProgram.gl.check(fn)
    }
}
