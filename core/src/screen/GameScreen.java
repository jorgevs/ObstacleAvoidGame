package screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import config.GameConfig;
import entity.Player;
import util.GdxUtils;
import util.ViewportUtils;

public class GameScreen implements Screen {
    private static final Logger LOGGER = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private Player player;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        player = new Player();

        // calculate position
        float startPlayerX= GameConfig.WORLD_WIDTH / 2;
        float startPlayerY= 1;

        // position player
        player.setPosition(startPlayerX, startPlayerY);
    }

    @Override
    public void render(float deltaTime) {
        GdxUtils.clearScreen();

        renderDebug();
        //batch.setProjectionMatrix(camera.combined);
        //batch.begin();
//        draw();
//        batch.end();
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
