package fr.eliferd.engine.renderer;

import fr.eliferd.game.World;
import fr.eliferd.game.entities.AbstractEntity;
import org.joml.Vector2f;
import org.joml.Vector4f;
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
        Shader.getInstance().useProgram();

        glBindVertexArray(this._vaoId);

        World.getInstance().getLoadedEntityList().forEach(this::drawEntity);

        glBindVertexArray(0);
    }

    private float[] buildVerticesFromCoords(Vector2f position, Vector2f offset, float size, Vector4f colors) {
        return new float[] {
                // POS (+ offset if any) + SIZE                               COLORS
                position.x + offset.x, position.y + offset.y,                 colors.x, colors.y, colors.z, colors.w, // Top left
                position.x + offset.x, position.y + offset.y + size,          colors.x, colors.y, colors.z, colors.w, // Bottom left
                position.x + offset.x + size, position.y + offset.y,          colors.x, colors.y, colors.z, colors.w, // Top right

                position.x + offset.x + size, position.y + offset.y,          colors.x, colors.y, colors.z, colors.w, // Top right
                position.x + offset.x + size, position.y + offset.y + size,   colors.x, colors.y, colors.z, colors.w, // Bottom right
                position.x + offset.x, position.y + offset.y + size,          colors.x, colors.y, colors.z, colors.w // Bottom left
        };
    }

    private void drawEntity(AbstractEntity entity) {
        float[] verticesToDraw = this.buildVerticesFromCoords(entity.getEntityPosition(), entity.getOffset(), entity.getEntitySize(), entity.getEntityColor());
        FloatBuffer fb = BufferUtils.createFloatBuffer(verticesToDraw.length);
        fb.put(verticesToDraw).flip();
        glBindBuffer(GL_ARRAY_BUFFER, _vboId);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, PC_SIZE * Float.BYTES, 0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, PC_SIZE * Float.BYTES, (POS_SIZE * Float.BYTES));
        Shader.getInstance().uploadUniformMatrix4f("mvp", this._cam.getCombined());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_VERTEX_ARRAY, 0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }
}
