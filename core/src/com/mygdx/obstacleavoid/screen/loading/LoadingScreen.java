package com.mygdx.obstacleavoid.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.screen.game.GameScreen;
import com.mygdx.obstacleavoid.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {

    // == constants ==
    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f; // world units
    private static final float PROGRESS_BAR_HEIGHT = 60; // world units

    // == attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private float progress;
    private float waitTime = 0.75f;
    private boolean changeScreen = false;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    // == constructors ==
    public LoadingScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    // == public methods ==

    @Override
    public void render(float delta) {
        update(delta);

        GdxUtils.clearScreen();
        viewport.apply();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        draw();
        shapeRenderer.end();

        if(changeScreen){
            game.setScreen(new GameScreen(game));
        }
    }

    private void update(float delta){
        waitMillis(400);

        // progress is between 0 and 1
        progress = assetManager.getProgress();

        // update() returns true when all the assets are loaded
        if(assetManager.update()){
            waitTime -= delta;

            if(waitTime <= 0){
                changeScreen = true;
                //game.setScreen(new GameScreen(game));
            }
        }
    }

    private void draw(){
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2;

        shapeRenderer.rect(progressBarX, progressBarY, PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT);
    }

    private static void waitMillis(long millis){
        try {
            Thread.sleep(millis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FillViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        assetManager.load(AssetDescriptors.FONT);
        assetManager.load(AssetDescriptors.GAME_PLAY);

        // blocks until all assets are loaded
        //assetManager.finishLoading();
    }

    @Override
    public void hide() {
        // NOTE: screens don't dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        shapeRenderer = null;
    }
}
