package com.mygdx.obstacleavoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.util.GdxUtils;

public class MenuScreen extends ScreenAdapter {
    private static final Logger LOGGER = new Logger(MenuScreen.class.getName(), Logger.DEBUG);

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    public MenuScreen(ObstacleAvoidGame game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getSpriteBatch());

        Gdx.input.setInputProcessor(stage);

        initUI();
    }

    private void initUI() {
        Table table = new Table();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        TextureAtlas uiAtlas = assetManager.get(AssetDescriptors.UI);

        TextureRegion backgroundTextureRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        TextureRegion panelTextureRegion = uiAtlas.findRegion(RegionNames.PANEL);

        table.setBackground(new TextureRegionDrawable(backgroundTextureRegion));

        // play button
        ImageButton playButton = createButton(uiAtlas, RegionNames.PLAY, RegionNames.PLAY_PRESSED);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                play();
            }
        });

        // highscore button
        ImageButton highscoreButton = createButton(uiAtlas, RegionNames.HIGH_SCORE, RegionNames.HIGH_SCORE_PRESSED);
        highscoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showHighScore();
            }
        });

        // options button
        ImageButton optionsButton = createButton(uiAtlas, RegionNames.OPTIONS, RegionNames.OPTIONS_PRESSED);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showOptions();
            }
        });

        // quit button


        // setup table
        Table buttonTable = new Table();
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(new TextureRegionDrawable(panelTextureRegion));

        buttonTable.add(playButton).row();
        buttonTable.add(highscoreButton).row();
        buttonTable.add(optionsButton).row();

        buttonTable.center();

        table.add(buttonTable);

        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void play(){
        LOGGER.debug("play()");
    }

    private void showHighScore(){
        LOGGER.debug("showHighScore()");
    }

    private void showOptions(){
        LOGGER.debug("showOptions()");
    }

    private static ImageButton createButton(TextureAtlas textureAtlas, String upRegionName, String downRegionName) {
        TextureRegion upRegion = textureAtlas.findRegion(upRegionName);
        TextureRegion downRegion = textureAtlas.findRegion(downRegionName);

        return new ImageButton(new TextureRegionDrawable(upRegion), new TextureRegionDrawable(downRegion));
    }
}
