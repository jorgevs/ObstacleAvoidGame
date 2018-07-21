package com.mygdx.obstacleavoid.screen.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.screen.game.GameScreen;

public class MenuScreen extends MenuScreenBase {
    private static final Logger LOGGER = new Logger(MenuScreen.class.getName(), Logger.DEBUG);

    public MenuScreen(ObstacleAvoidGame game) {
        super(game);
    }

    @Override
    protected Actor createUI() {
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
        // TODO add this button

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

        return table;
    }

    private void play() {
        LOGGER.debug("play()");
        game.setScreen(new GameScreen(game));
    }

    private void showHighScore() {
        LOGGER.debug("showHighScore()");
        game.setScreen(new HighScoreScreen(game));
    }

    private void showOptions() {
        LOGGER.debug("showOptions()");
        game.setScreen(new OptionsScreen(game));
    }

    private static ImageButton createButton(TextureAtlas textureAtlas, String upRegionName, String downRegionName) {
        TextureRegion upRegion = textureAtlas.findRegion(upRegionName);
        TextureRegion downRegion = textureAtlas.findRegion(downRegionName);

        return new ImageButton(new TextureRegionDrawable(upRegion), new TextureRegionDrawable(downRegion));
    }

}
