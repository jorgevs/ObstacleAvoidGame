package screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import config.GameConfig;
import entity.Obstacle;
import entity.Player;
import util.GdxUtils;
import util.ViewportUtils;
import util.debug.DebugCameraController;

public class GameScreen implements Screen {
    private static final Logger LOGGER = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private float obstacleTimer;

    private DebugCameraController debugCameraController;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        player = new Player();

        // calculate position
        float startPlayerX = GameConfig.WORLD_WIDTH / 2;
        float startPlayerY = 1;

        // position player
        player.setPosition(startPlayerX, startPlayerY);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTERX, GameConfig.WORLD_CENTERY);
    }

    @Override
    public void render(float deltaTime) {

        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo(camera);

        // update delta time
        update(deltaTime);

        // clear screen
        GdxUtils.clearScreen();

        // render debug graphics
        renderDebug();


        //batch.setProjectionMatrix(camera.combined);
        //batch.begin();
//        draw();
//        batch.end();
    }

    private void update(float deltaTime) {
        updatePlayer();
        updateObstacles(deltaTime);
    }


    private void updatePlayer() {
        //LOGGER.debug("playerX: " + player.getX() + " playerY: " + player.getY());
        player.update();

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(), (player.getWidth() / 2), (GameConfig.WORLD_WIDTH - player.getWidth() / 2));
        float playerY = MathUtils.clamp(player.getY(), (player.getHeight() / 2), (GameConfig.WORLD_HEIGHT - player.getHeight() / 2));

        /*float playerX = player.getX();
        float playerY = player.getY();

        if (playerX < player.getWidth() / 2) {
            playerX = player.getWidth() / 2;
        } else if (playerX > (GameConfig.WORLD_WIDTH - player.getWidth() / 2)) {
            playerX = (GameConfig.WORLD_WIDTH - player.getWidth() / 2);
        }

        if (playerY < player.getHeight() / 2) {
            playerY = player.getHeight() / 2;
        } else if (playerY > (GameConfig.WORLD_HEIGHT - player.getHeight() / 2)) {
            playerY = (GameConfig.WORLD_HEIGHT - player.getHeight() / 2);
        }*/

        player.setPosition(playerX, playerY);
    }

    private void updateObstacles(float delta){
        for (Obstacle obstacle : obstacles){
            obstacle.update();
        }

        createNewObstacle(delta);
    }

    private void createNewObstacle(float delta){
        obstacleTimer += delta;

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0.0f;
            float max = GameConfig.WORLD_WIDTH;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = new Obstacle();
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            obstacleTimer = 0.0f;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    private void draw() {
    }

    private void renderDebug() {
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        player.drawDebug(renderer);

        for (Obstacle obstacle : obstacles){
            obstacle.drawDebug(renderer);
        }
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
