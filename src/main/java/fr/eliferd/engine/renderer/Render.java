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

    private Camera _cam;

    public void init() {
        Shader.getInstance().init();
        this._cam = new Camera();
        this._cam.setPos(0, 0);

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
        Shader.getInstance().uploadUniformMatrix4f("mvp", this._cam.getCombined());
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
        // TODO
        float posx = 25f;
        float posy = 65f;
        float size = 300f;
        return new float[] {
                // POS                      COLORS
                posx, posy,                 1.0f, 0.0f, 0.0f, 1.0f, // Top left
                posx, posy + size,          0.0f, 1.0f, 0.0f, 1.0f, // Bottom left
                posx + size, posy,          0.0f, 0.0f, 1.0f, 1.0f, // Top right

                posx + size, posy,          0.0f, 0.0f, 1.0f, 1.0f, // Top right
                posx + size, posy + size,   1.0f, 1.0f, 0.0f, 1.0f, // Bottom right
                posx, posy + size,          0.0f, 1.0f, 0.0f, 1.0f // Bottom left
        };
    }
}
