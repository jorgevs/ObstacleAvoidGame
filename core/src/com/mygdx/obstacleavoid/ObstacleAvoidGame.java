package com.mygdx.obstacleavoid;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.common.GameManager;
import com.mygdx.obstacleavoid.screen.game.GameScreen;
import com.mygdx.obstacleavoid.screen.loading.LoadingScreen;

public class ObstacleAvoidGame extends Game {
    private static final Logger LOGGER = new Logger(ObstacleAvoidGame.class.getName(), Logger.DEBUG);

    private AssetManager assetManager;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);

        spriteBatch = new SpriteBatch();

        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        // If you override this method, then you must call the parent method as well.
        super.render(); // Important!
    }

    @Override
    public void dispose() {
        // dispose the actual com.mygdx.obstacleavoid.screen object
        getScreen().dispose(); // Important!

        assetManager.dispose();
        spriteBatch.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getSpriteBatch(){
        return spriteBatch;
    }
}
