package fr.eliferd.game.entities;

import fr.eliferd.engine.Window;
import fr.eliferd.game.Game;
import fr.eliferd.game.World;
import fr.eliferd.game.utils.SnakeDirectionController;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.eliferd.game.enums.SnakeDirectionEnum.RIGHT;

public class SnakeHeadEntity extends AbstractEntity {

    private final float _velocity = 20f;
    private final List<SnakeSegmentEntity> _segmentList = new ArrayList<>();
    private final SnakeDirectionController _controller = new SnakeDirectionController(RIGHT);
    protected int _moveCooldownMax = 400;
    protected int _moveCooldown = this._moveCooldownMax;

    public SnakeHeadEntity(Vector2f position) {
        super(position);
    }

    @Override
    public void update(float dt) {
        this._controller.updateDirection();
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

            switch (this._controller.getCurrentDirection()) {
                case UP -> entityPosition.set(entityPosition.x, entityPosition.y - this._velocity);
                case DOWN -> entityPosition.set(entityPosition.x, entityPosition.y + this._velocity);
                case LEFT -> entityPosition.set(entityPosition.x - this._velocity, entityPosition.y);
                case RIGHT -> entityPosition.set(entityPosition.x + this._velocity, entityPosition.y);
            }

            this.handleUnallowedMoves(entityPosition);

            this._moveCooldown = this._moveCooldownMax;
        }
    }

    private void handleUnallowedMoves(Vector2f entityPosition) {
        final Vector2i viewportSize = Window.getInstance().getViewPortSize();

        // If the player hit any wall or itself : game over
        final boolean isOutOfBounds = entityPosition.x < 0 || entityPosition.x > viewportSize.x || entityPosition.y < 0 || entityPosition.y > viewportSize.y;
        final boolean hasHitSegment = this._segmentList.stream().anyMatch(seg -> seg.getEntityPosition().equals(entityPosition));
        if (isOutOfBounds || hasHitSegment) {
            Game.getInstance().setGameOver();
        }
    }

    private void grow() {
        AbstractEntity parent = null;
        Vector2f segmentPosition = null;

        if (this._segmentList.isEmpty()) {
            parent = this;
        } else {
            parent = this._segmentList.getLast();
        }

        segmentPosition = new Vector2f(parent.getEntityPosition());

        SnakeSegmentEntity segment = new SnakeSegmentEntity(segmentPosition, parent);
        World.getInstance().addEntity(segment);
        this._segmentList.add(segment);

        this.increaseSpeed();
    }

    private void increaseSpeed() {
        this._moveCooldownMax -= 50;
        this._moveCooldown = this._moveCooldownMax;
        // applying this new cooldown parameters to all the segments
        this._segmentList.forEach(s -> s.updateCooldownCoords(this._moveCooldownMax, this._moveCooldown));
    }
}
