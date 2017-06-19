package com.mygdx.obstacleavoid.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;

public class GameScreen implements Screen {
    private static final Logger LOGGER = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private GameController controller;
    private GameRenderer renderer;

    @Override
    public void show() {
        controller = new GameController();
        renderer = new GameRenderer(controller);
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
