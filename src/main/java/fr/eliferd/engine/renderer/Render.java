package fr.eliferd.engine.renderer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Render {
    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int PC_SIZE = POS_SIZE + COLOR_SIZE;

    private int _vaoId;
    private int _vboId;

    public void init() {
        Shader.getInstance().init();

        this._vaoId = glGenVertexArrays();
        this._vboId = glGenBuffers();
    }

    public void render(float dt) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(this.verticeList().length);
        fb.put(this.verticeList()).flip();
        Shader.getInstance().useProgram();
        glBindVertexArray(this._vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, _vboId);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, PC_SIZE * Float.BYTES, 0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, PC_SIZE * Float.BYTES, (POS_SIZE * Float.BYTES));
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_VERTEX_ARRAY, 0);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    private float[] verticeList() {
        return new float[] {
                // POS                      COLORS
                -0.5f, -0.5f,               1.0f, 0.0f, 0.0f, 1.0f, // Bottom left
                -0.5f, 0.5f,                0.0f, 1.0f, 0.0f, 1.0f, // Top left
                0.5f, -0.5f,                0.0f, 0.0f, 1.0f, 1.0f, // Bottom right

                0.5f, -0.5f,                0.0f, 0.0f, 1.0f, 1.0f, // Bottom right
                0.5f, 0.5f,                 0.0f, 1.0f, 0.0f, 1.0f, // Top right
                -0.5f, 0.5f,               0.0f, 1.0f, 0.0f, 1.0f // Top left
        };
    }
}
