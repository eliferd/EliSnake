package fr.eliferd.engine.renderer;

import fr.eliferd.engine.Window;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Camera {
    private Vector3f _cameraPosition;
    private Vector3f _cameraTarget;
    private Vector3f _cameraDirection;
    private Vector3f _up;

    private final Matrix4f _projectionMat = new Matrix4f();
    private final Matrix4f _viewMat = new Matrix4f();
    private final Matrix4f _modelMat = new Matrix4f();


    public Camera() {
        this._cameraPosition = new Vector3f(0f, 0f, 1f);
        this._cameraTarget = new Vector3f(0f, 0f, -1f);
        this._up = new Vector3f(0, 1, 0);

        this.updateProjectionMatrix();
        this.updateView();
    }

    public void updateProjectionMatrix() {
        final Vector2i size = Window.getInstance().getViewPortSize();
        this._projectionMat.identity();
        this._projectionMat.ortho(0, size.x, size.y, 0, -1, 1);
    }

    public void updateView() {
        this._viewMat.identity();
        Vector3f center = new Vector3f(this._cameraPosition).add(this._cameraTarget);
        this._viewMat.lookAt(this._cameraPosition, center, this._up);
    }

    public void setPos(float x, float y) {
        this._cameraPosition.set(x, y, this._cameraPosition.z);
        this.updateView();
    }

    public void move(float dx, float dy) {
        this._cameraPosition.add(dx, dy, 0);
        this.updateView();
    }

    public Matrix4f getCombined() {
        this._projectionMat.mul(this._viewMat, this._modelMat);
        return new Matrix4f(this._modelMat);
    }

    public Vector3f getPosition() {
        return new Vector3f(this._cameraPosition);
    }
}
