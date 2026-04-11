package fr.eliferd.game.entities;

import fr.eliferd.engine.Window;
import fr.eliferd.engine.input.Keyboard;
import fr.eliferd.game.Game;
import fr.eliferd.game.World;
import fr.eliferd.game.enums.SnakeDirectionEnum;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static fr.eliferd.game.enums.SnakeDirectionEnum.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class SnakeHeadEntity extends AbstractEntity {

    private final float _velocity = 20f;
    protected int _moveCooldownMax = 400;
    protected int _moveCooldown = this._moveCooldownMax;
    private SnakeDirectionEnum _direction = RIGHT;
    private final List<SnakeSegmentEntity> _segmentList = new ArrayList<>();
    private final Map<Integer, SnakeDirectionEnum> _directionDictionary = ofEntries(
            entry(GLFW_KEY_UP, UP),
            entry(GLFW_KEY_DOWN, DOWN),
            entry(GLFW_KEY_LEFT, LEFT),
            entry(GLFW_KEY_RIGHT, RIGHT)
    );

    public SnakeHeadEntity(Vector2f position) {
        super(position);
    }

    @Override
    public void update(float dt) {
        this.updateDirectionFromInput();
        this.collectNearbyFood();
        this.updateCrawlingProgress();
    }

    @Override
    public float getEntitySize() {
        return 20f;
    }

    @Override
    public Vector4f getEntityColor() {
        return new Vector4f(0f, 0f, 0f, 1.0f);
    }

    private void updateDirectionFromInput() {
        try {
            int keyDown = this._directionDictionary.keySet().stream().filter(Keyboard::isKeyDown).toList().getFirst();
            this._direction = this._directionDictionary.get(keyDown);
        } catch (Exception _) {
        }
    }

    private void collectNearbyFood() {
        try {
            Optional<FoodEntity> food = ((Optional) World.getInstance().getLoadedEntityList().stream()
                    .filter(entity -> entity instanceof FoodEntity && entity.getEntityPosition().equals(this.getEntityPosition()))
                    .findFirst());

            if(food.isPresent()) {
                this.grow();
                World.getInstance().removeEntity(food.get());
                Game.getInstance().addScore(20);
                System.out.println("Current score: " + Game.getInstance().getScore());
            }
        } catch (NullPointerException e) {
            System.err.println("Food not found.");
        }
    }

    private void updateCrawlingProgress() {
        if (this._moveCooldown > 0) {
            this._moveCooldown--;
        } else {
            final Vector2f entityPosition = this.getEntityPosition();
            this._lastPosition = new Vector2f(entityPosition);

            switch (this._direction) {
                case UP -> entityPosition.set(entityPosition.x, entityPosition.y - this._velocity);
                case DOWN -> entityPosition.set(entityPosition.x, entityPosition.y + this._velocity);
                case LEFT -> entityPosition.set(entityPosition.x - this._velocity, entityPosition.y);
                case RIGHT -> entityPosition.set(entityPosition.x + this._velocity, entityPosition.y);
            }

            final Vector2i viewportSize = Window.getInstance().getViewPortSize();

            if (entityPosition.x < 0) {
                entityPosition.set(viewportSize.x,  entityPosition.y);
            } else if (entityPosition.x > viewportSize.x) {
                entityPosition.set(0, entityPosition.y);
            } else if (entityPosition.y < 0) {
                entityPosition.set(entityPosition.x, viewportSize.y);
            } else if (entityPosition.y > viewportSize.y) {
                entityPosition.set(entityPosition.x, 0);
            }

            this._moveCooldown = this._moveCooldownMax;
        }
    }

    private void grow() {
        AbstractEntity parent = null;

        if (this._segmentList.isEmpty()) {
            parent = this;
        } else {
            parent = this._segmentList.getLast();
        }

        SnakeSegmentEntity segment = new SnakeSegmentEntity(new Vector2f(parent.getEntityPosition()), parent);
        World.getInstance().addEntity(segment);
        this._segmentList.add(segment);
    }
}
