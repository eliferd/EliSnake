package fr.eliferd.game;

import fr.eliferd.engine.Window;
import fr.eliferd.game.scenes.GameScene;

import javax.swing.*;

public class Game {
    private static Game _INSTANCE = null;
    private int _score = 0;
    private boolean _isGameOver = false;

    public void addScore(int value) {
        this._score += value;
    }

    public int getScore() {
        return this._score;
    }

    public void setGameOver() {
        this._isGameOver = true;
        int test = JOptionPane.showConfirmDialog(null, "Game Over!\nHere your score: " + this._score + "\nWould you like to play again ?", "Game over", JOptionPane.YES_NO_OPTION);
        if (test == JOptionPane.YES_OPTION) {
            this.reset();
            Window.getInstance().setScene(new GameScene());
        } else {
            Window.getInstance().closeWindow();
        }
    }

    public boolean isGameOver() {
        return this._isGameOver;
    }

    public void reset() {
        this._score = 0;
        this._isGameOver = false;
    }

    public static Game getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new Game();
        }
        return _INSTANCE;
    }
}
