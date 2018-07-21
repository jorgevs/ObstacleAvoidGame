package com.mygdx.obstacleavoid.screen.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.common.GameManager;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.util.GdxUtils;


public class HighScoreScreen extends ScreenAdapter {
    private static final Logger LOGGER = new Logger(HighScoreScreen.class.getName(), Logger.DEBUG);

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    public HighScoreScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getSpriteBatch());

        Gdx.input.setInputProcessor(stage);

        createUI();
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        stage.act();
        stage.draw();
    }

    private void createUI() {
        Table table = new Table();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        TextureAtlas uiAtlas = assetManager.get(AssetDescriptors.UI);
        BitmapFont font = assetManager.get(AssetDescriptors.FONT);

        TextureRegion backgroundTextureRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        TextureRegion panelTextureRegion = uiAtlas.findRegion(RegionNames.PANEL);

        TextureRegion backButtonRegion = uiAtlas.findRegion(RegionNames.BACK);
        TextureRegion backButtonPressedRegion = uiAtlas.findRegion(RegionNames.BACK_PRESSED);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // background
        table.setBackground(new TextureRegionDrawable(backgroundTextureRegion));

        // highscore text
        Label highScoreText = new Label("HIGHSCORE", labelStyle);

        // highscore label
        String highScoreString = GameManager.INSTANCE.getHighScoreString();
        Label highScoreLabel = new Label(highScoreString, labelStyle);

        // back button
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backButtonRegion), new TextureRegionDrawable(backButtonPressedRegion));
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                back();
            }
        });

        // setup tables
        Table contentTable = new Table();
        contentTable.defaults().pad(20);
        contentTable.setBackground(new TextureRegionDrawable(panelTextureRegion));

        contentTable.add(highScoreText).row();
        contentTable.add(highScoreLabel).row();
        contentTable.add(backButton).row();

        contentTable.center();

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);
    }

    private void back() {
        LOGGER.debug("back()");
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        // NOTE: screens don't dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
