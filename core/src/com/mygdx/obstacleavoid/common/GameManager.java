package com.mygdx.obstacleavoid.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.config.DifficultyLevel;


public class GameManager {
    private static final Logger LOGGER = new Logger(GameManager.class.getName(), Logger.DEBUG);

    public static final GameManager INSTANCE = new GameManager();

    private static final String HIGH_SCORE_KEY = "highScore";
    private static final String DIFFICULTY_KEY = "difficulty";


    private Preferences PREFS;
    private int highScore = 0;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;


    private GameManager() {
        PREFS = Gdx.app.getPreferences(ObstacleAvoidGame.class.getSimpleName());
        highScore = PREFS.getInteger(HIGH_SCORE_KEY, 0);

        String difficultyName = PREFS.getString(DIFFICULTY_KEY, DifficultyLevel.MEDIUM.name());
        difficultyLevel = DifficultyLevel.valueOf(difficultyName);
    }

    public void updateHighScore(int score) {
        if (this.highScore < score) {
            this.highScore = score;
            PREFS.putInteger(HIGH_SCORE_KEY, highScore);
            PREFS.flush();
        }
    }

    public String getHighScoreString() {
        return String.valueOf(highScore);
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void updateDifficulty(DifficultyLevel difficultyLevel) {
        if (this.difficultyLevel != difficultyLevel) {
            this.difficultyLevel = difficultyLevel;
            PREFS.putString(DIFFICULTY_KEY, difficultyLevel.name());
            PREFS.flush();
        }

    }
}
