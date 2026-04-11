package fr.eliferd.game.entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class FoodEntity extends AbstractEntity {

    public FoodEntity(Vector2f position) {
        super(position);
        this._offset = new Vector2f(5f, 5f);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public float getEntitySize() {
        return 10f;
    }

    @Override
    public Vector4f getEntityColor() {
        return new Vector4f(1f, 0.3f, 0.3f, 1.0f);
    }
}
