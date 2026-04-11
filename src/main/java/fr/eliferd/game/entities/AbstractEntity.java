package fr.eliferd.game.entities;

import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class AbstractEntity {
    private Vector2f _currentPosition;
    protected Vector2f _lastPosition;
    protected Vector2f _offset;
    public AbstractEntity(Vector2f position) {
        this._currentPosition = position;
        this._lastPosition = position;
        this._offset = new Vector2f();
    }

    public abstract void update(float dt);
    public abstract float getEntitySize();
    public abstract Vector4f getEntityColor();
    public Vector2f getEntityPosition() {
        return this._currentPosition;
    }
    public Vector2f getLastPosition() {
        return this._lastPosition;
    }
    public Vector2f getOffset() {
        return this._offset;
    }
}
