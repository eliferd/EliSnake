package fr.eliferd.game.entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class SnakeSegmentEntity extends SnakeHeadEntity {

    private AbstractEntity _parent;

    public SnakeSegmentEntity(Vector2f position, AbstractEntity parent) {
        super(position);
        this._parent = parent;
    }

    @Override
    public void update(float dt) {
        if (this._moveCooldown > 0) {
            this._moveCooldown--;
        } else {
            this._lastPosition = new Vector2f(this.getEntityPosition());
            this.getEntityPosition().set(new Vector2f(this._parent.getLastPosition()));
            this._moveCooldown = this._moveCooldownMax;
        }
    }

    @Override
    public Vector4f getEntityColor() {
        return new  Vector4f(0.1f, 0.1f, 0.1f, 1.0f);
    }
}
