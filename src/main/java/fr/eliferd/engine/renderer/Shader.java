package fr.eliferd.engine.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Map;

import static java.util.Map.entry;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private static Shader _INSTANCE = null;

    private int _vertexShaderId = -1;
    private int _fragmentShaderId = -1;
    private int _shaderProgramId = -1;

    public static Shader getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new Shader();
        }

        return _INSTANCE;
    }

    public void init() {
        this._vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        this._fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        this._shaderProgramId = glCreateProgram();

        this.compileShaders();
        this.linkProgram();
    }

    public void useProgram() {
        glUseProgram(this._shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }

    public void uploadUniformMatrix4f(String name, Matrix4f value) {
        int uniformLocation = glGetUniformLocation(this._shaderProgramId, name);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        value.get(fb);
        glUniformMatrix4fv(uniformLocation, false, fb);
    }

    private void compileShaders() {
        Map<Integer, String> shaderCompilationMap = Map.ofEntries(
                entry(this._vertexShaderId, this.getVertexShaderSource()),
                entry(this._fragmentShaderId, this.getFragmentShaderSource())
        );

        shaderCompilationMap.forEach((shaderId, shaderSource) -> {
            glShaderSource(shaderId, shaderSource);

            glCompileShader(shaderId);

            // Checking for any compilation errors
            int compileStatus = glGetShaderi(shaderId, GL_COMPILE_STATUS);
            if (compileStatus == GL_FALSE) {
                System.err.println("[Shader ERROR] Compilation failed. Operation aborted.");
                System.err.println("[Shader ERROR] " + glGetShaderInfoLog(shaderId));
                glDeleteShader(shaderId);
            }

            // Finally, attaching the shader to the program
            glAttachShader(this._shaderProgramId, shaderId);
        });
    }

    private void linkProgram() {
        glLinkProgram(this._shaderProgramId);

        // Checking for shader program linking errors
        int linkStatus = glGetProgrami(this._shaderProgramId, GL_LINK_STATUS);
        if (linkStatus == GL_FALSE) {
            System.err.println("[Shader ERROR] Failed to link Shader program. Operation aborted.");
            System.err.println("[Shader ERROR] " + glGetProgramInfoLog(this._shaderProgramId));
            glDeleteProgram(this._shaderProgramId);
        }

        glDetachShader(this._shaderProgramId, this._vertexShaderId);
        glDetachShader(this._shaderProgramId, this._fragmentShaderId);
        glValidateProgram(this._shaderProgramId);
    }

    private String getVertexShaderSource() {
        return "#version 330 core\n" +
                "layout(location=0) in vec2 aPos;\n" +
                "layout(location=1) in vec4 aColor;\n" +
                "uniform mat4 mvp;" +
                "out vec4 fColor;\n" +
                "void main() {\n" +
                "fColor = aColor;\n" +
                "gl_Position = mvp * vec4(aPos, 0.0f, 1.0f);\n" +
                "}";
    }

    private String getFragmentShaderSource() {
        return "#version 330 core\n" +
                "in vec2 fPos;\n" +
                "in vec4 fColor;\n" +
                "out vec4 color;\n" +
                "void main() {\n" +
                "color = fColor;\n" +
                "}";
    }
}
