package fr.eliferd.game.utils;

import fr.eliferd.engine.input.Keyboard;
import fr.eliferd.game.enums.SnakeDirectionEnum;
import static fr.eliferd.game.enums.SnakeDirectionEnum.*;

import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Map.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class SnakeDirectionController {
    private SnakeDirectionEnum _currentDirection;
    private SnakeDirectionEnum _lastDirection;

    // Define for each directions source, its forbidden target direction.
    private final Map<SnakeDirectionEnum, SnakeDirectionEnum> _forbiddenDirections = ofEntries(
            entry(UP, DOWN),
            entry(DOWN, UP),
            entry(LEFT, RIGHT),
            entry(RIGHT, LEFT)
    );

    // TODO : add more keys than the directional arrows from the keyboard
    private final Map<Integer, SnakeDirectionEnum> _directionDictionary = ofEntries(
            entry(GLFW_KEY_UP, UP),
            entry(GLFW_KEY_DOWN, DOWN),
            entry(GLFW_KEY_LEFT, LEFT),
            entry(GLFW_KEY_RIGHT, RIGHT)
    );

    public SnakeDirectionController(SnakeDirectionEnum snakeDirection) {
        this._currentDirection = snakeDirection;
        this._lastDirection = snakeDirection;
    }

    public void updateDirection() {
        int keyDown;
        SnakeDirectionEnum selectedDirection;

        try {
            keyDown = this._directionDictionary.keySet().stream().filter(Keyboard::isKeyDown).toList().getFirst();
            selectedDirection = this._directionDictionary.get(keyDown);
        } catch (NoSuchElementException e) {
            selectedDirection = this._currentDirection;
        }

        // Compute the next direction
        // We first checks if the next direction is not a forbidden one. If it is, we keep going to the old direction.
        final boolean isDirectionForbidden = selectedDirection.equals(this._forbiddenDirections.get(this._currentDirection));
        final SnakeDirectionEnum nextDirection = isDirectionForbidden ? this._currentDirection : selectedDirection;

        // last direction has to be updated right before setting the current direction.
        this._lastDirection = this._currentDirection;
        this._currentDirection = nextDirection;
    }

    public SnakeDirectionEnum getCurrentDirection() {
        return this._currentDirection;
    }

}
