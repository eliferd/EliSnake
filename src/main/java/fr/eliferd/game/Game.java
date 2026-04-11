package fr.eliferd.game;

public class Game {
    private static Game _INSTANCE = null;
    int score = 0;

    public static Game getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new Game();
        }
        return _INSTANCE;
    }
}
