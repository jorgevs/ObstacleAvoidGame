package com.mygdx.obstacleavoid;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.screen.GameScreen;
import com.mygdx.obstacleavoid.screen.GameScreenOld;

public class ObstacleAvoidGame extends Game {
    private static final Logger LOGGER = new Logger(ObstacleAvoidGame.class.getName(), Logger.DEBUG);

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //setScreen(new GameScreenOld());
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        // dispose the actual com.mygdx.obstacleavoid.screen object
        getScreen().dispose();
    }
}
