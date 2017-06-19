package com.mygdx.obstacleavoid.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.assets.AssetPaths;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.Obstacle;
import com.mygdx.obstacleavoid.util.GdxUtils;
import com.mygdx.obstacleavoid.util.ViewportUtils;
import com.mygdx.obstacleavoid.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {
    private static final Logger LOGGER = new Logger(GameRenderer.class.getName(), Logger.DEBUG);

    // == attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private SpriteBatch batch;
    private BitmapFont font;
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private DebugCameraController debugCameraController;

    private final GameController gameController;

    //== constructors ==
    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    //== init ==
    private void init(){
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT));

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    //== public methods ==
    public void render(float deltaTime){
        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo(camera);

        // clear screen
        GdxUtils.clearScreen();

        // render ui/hud
        renderUi();

        // render debug graphics
        renderDebug();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);

        ViewportUtils.debugPixelPerUnit(viewport);
    }

    public void dispose() {
        renderer.dispose();

        batch.dispose();
        font.dispose();
    }

    //== private methods ==
    private void renderUi(){
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();
        drawUi();
        batch.end();
    }

    private void drawUi(){
        String livesText = "LIVES: " + gameController.getLives();
        glyphLayout.setText(font, livesText);
        font.draw(batch, livesText, 20, GameConfig.HUD_HEIGHT - glyphLayout.height);

        String scoreText = "SCORE: " + gameController.getDisplayScore();
        glyphLayout.setText(font, scoreText);
        font.draw(batch, scoreText, GameConfig.HUD_WIDTH - glyphLayout.width - 20, GameConfig.HUD_HEIGHT - glyphLayout.height);
    }

    private boolean isPlayerCollidingWithObstacle(){
        for(Obstacle obstacle : gameController.getObstacles()){
            if(!obstacle.isHit() && obstacle.isPlayerColliding(gameController.getPlayer())){
                return true;
            }
        }

        return false;
    }

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        gameController.getPlayer().drawDebug(renderer);

        Array<Obstacle> obstacles = gameController.getObstacles();
        for (Obstacle obstacle : obstacles){
            obstacle.drawDebug(renderer);
        }
    }

}
