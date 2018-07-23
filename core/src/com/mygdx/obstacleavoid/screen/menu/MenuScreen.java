package com.mygdx.obstacleavoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
        Skin skin = assetManager.get(AssetDescriptors.SKIN);
        TextureRegion backgroundTextureRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

        table.setBackground(new TextureRegionDrawable(backgroundTextureRegion));

        // play button
        TextButton playButton = new TextButton("PLAY", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                play();
            }
        });

        // highscore button
        TextButton highScoreButton = new TextButton("HIGH SCORE", skin);
        highScoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showHighScore();
            }
        });

        // options button
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showOptions();
            }
        });

        // quit button
        TextButton quitButton = new TextButton("QUIT", skin);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                quit();
            }
        });

        // setup table
        Table buttonTable = new Table(skin);
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(RegionNames.PANEL);

        buttonTable.add(playButton).row();
        buttonTable.add(highScoreButton).row();
        buttonTable.add(optionsButton).row();
        buttonTable.add(quitButton).row();

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

    private void quit() {
        LOGGER.debug("quit()");
        Gdx.app.exit();
    }
}
