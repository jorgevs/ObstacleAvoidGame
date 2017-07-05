package com.mygdx.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.Background;
import com.mygdx.obstacleavoid.entity.Obstacle;
import com.mygdx.obstacleavoid.entity.Player;
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

    private final AssetManager assetManager;

    private TextureRegion playerRegion;
    private TextureRegion obstacleRegion;
    private TextureRegion backgroundRegion;

    private DebugCameraController debugCameraController;

    private final GameController gameController;

    //== constructors ==
    public GameRenderer(AssetManager assetManager, GameController gameController) {
        this.assetManager = assetManager;
        this.gameController = gameController;
        init();
    }

    //== init ==
    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        font = assetManager.get(AssetDescriptors.FONT);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);

        playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);


        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    //== public methods ==
    public void render(float deltaTime) {
        //batch.totalRenderCalls = 0;

        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo(camera);

        // adding screen touch controls
        if(Gdx.input.isTouched() && !gameController.isGameOver()){
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));

            LOGGER.debug("screenTouch: " + screenTouch);
            LOGGER.debug("worldTouch: " + worldTouch);

            Player player = gameController.getPlayer();
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0, GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldTouch.x);
        }

        // clear screen
        GdxUtils.clearScreen();

        // render game characters
        renderGamePlay();

        // render ui/hud
        renderUi();

        // render debug graphics
        renderDebug();

        //System.out.println("totalRenderCalls: " + batch.totalRenderCalls);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);

        ViewportUtils.debugPixelPerUnit(viewport);
    }

    public void dispose() {
        renderer.dispose();
        batch.dispose();
//        font.dispose();
//        playerTexture.dispose();
//        obstacleTexture.dispose();
//        backgroundTexture.dispose();
    }

    //== private methods ==
    private void renderGamePlay() {
        viewport.apply(); // important! Apply the viewport before rendering
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawCharacters();
        batch.end();
    }

    private void drawCharacters() {
        // draw background
        Background background = gameController.getBackground();
        batch.draw(backgroundRegion, background.getX(), background.getY(), background.getWidth(), background.getHeight());

        // draw player
        Player player = gameController.getPlayer();
        batch.draw(playerRegion, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        // draw obstacles
        for (Obstacle obstacle : gameController.getObstacles()) {
            batch.draw(obstacleRegion, obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

    }

    private void renderUi() {
        hudViewport.apply(); // important! Apply the viewport before rendering
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();
        drawUi();
        batch.end();
    }

    private void drawUi() {
        String livesText = "LIVES: " + gameController.getLives();
        glyphLayout.setText(font, livesText);
        font.draw(batch, livesText, 20, GameConfig.HUD_HEIGHT - glyphLayout.height);

        String scoreText = "SCORE: " + gameController.getDisplayScore();
        glyphLayout.setText(font, scoreText);
        font.draw(batch, scoreText, GameConfig.HUD_WIDTH - glyphLayout.width - 20, GameConfig.HUD_HEIGHT - glyphLayout.height);
    }

    private void renderDebug() {
        viewport.apply(); // important! Apply the viewport before rendering
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        gameController.getPlayer().drawDebug(renderer);

        for (Obstacle obstacle : gameController.getObstacles()) {
            obstacle.drawDebug(renderer);
        }
    }

    private boolean isPlayerCollidingWithObstacle() {
        for (Obstacle obstacle : gameController.getObstacles()) {
            if (!obstacle.isHit() && obstacle.isPlayerColliding(gameController.getPlayer())) {
                return true;
            }
        }

        return false;
    }
}
