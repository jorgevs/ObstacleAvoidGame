package com.mygdx.obstacleavoid.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.screen.menu.MenuScreen;

public class GameScreen extends ScreenAdapter {
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
        controller = new GameController(game);
        renderer = new GameRenderer(game.getSpriteBatch(), assetManager, controller);
    }

    @Override
    public void render(float deltaTime) {
        controller.update(deltaTime);
        renderer.render(deltaTime);

        if(controller.isGameOver()){
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void hide() {
        // NOTE: screens don't dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
