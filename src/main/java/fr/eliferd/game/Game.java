package fr.eliferd.game;

public class Game {
    private static Game _INSTANCE = null;
    private int _score = 0;

    public void addScore(int value) {
        this._score += value;
    }

    public int getScore() {
        return this._score;
    }

    public static Game getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new Game();
        }
        return _INSTANCE;
    }
}
