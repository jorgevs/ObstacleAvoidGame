package com.mygdx.obstacleavoid.screen.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;

public class GameScreen implements Screen {
    private static final Logger LOGGER = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private GameController controller;
    private GameRenderer renderer;

    public GameScreen(ObstacleAvoidGame game){
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        controller = new GameController();
        renderer = new GameRenderer(assetManager, controller);
    }

    @Override
    public void render(float deltaTime) {
        controller.update(deltaTime);
        renderer.render(deltaTime);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // important
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
