package com.mygdx.obstacleavoid;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import screen.GameScreen;

public class ObstacleAvoidGame extends Game {
    private static final Logger LOGGER = new Logger(ObstacleAvoidGame.class.getName(), Logger.DEBUG);

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setScreen(new GameScreen());
    }
}
