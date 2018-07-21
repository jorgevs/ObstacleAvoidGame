package com.mygdx.obstacleavoid.screen.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.common.GameManager;
import com.mygdx.obstacleavoid.config.DifficultyLevel;
import com.mygdx.obstacleavoid.config.GameConfig;

public class OptionsScreen extends MenuScreenBase {
    private static final Logger LOGGER = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    private Image checkMark;

    public OptionsScreen(ObstacleAvoidGame game) {
        super(game);
    }

    @Override
    protected Actor createUI() {
        Table table = new Table();
        table.defaults().pad(15);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        TextureAtlas uiAtlas = assetManager.get(AssetDescriptors.UI);
        BitmapFont font = assetManager.get(AssetDescriptors.FONT);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        Image background = new Image(backgroundRegion);

        Label label = new Label("DIFFICULTY", labelStyle);
        label.setPosition(GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT / 2 + 180, Align.center);

        final ImageButton easyButton = createButton(uiAtlas, RegionNames.EASY);
        easyButton.setPosition(GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT / 2 + 90, Align.center);
        easyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkMark.setY(easyButton.getY() + 25);
                GameManager.INSTANCE.updateDifficulty(DifficultyLevel.EASY);
            }
        });

        final ImageButton mediumButton = createButton(uiAtlas, RegionNames.MEDIUM);
        mediumButton.setPosition(GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT / 2, Align.center);
        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkMark.setY(mediumButton.getY() + 25);
                GameManager.INSTANCE.updateDifficulty(DifficultyLevel.MEDIUM);
            }
        });

        final ImageButton hardButton = createButton(uiAtlas, RegionNames.HARD);
        hardButton.setPosition(GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT / 2 - 90, Align.center);
        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkMark.setY(hardButton.getY() + 25);
                GameManager.INSTANCE.updateDifficulty(DifficultyLevel.HARD);
            }
        });

        TextureRegion checkMarkRegion = uiAtlas.findRegion(RegionNames.CHECK_MARK);
        checkMark = new Image(new TextureRegionDrawable(checkMarkRegion));
        checkMark.setPosition(mediumButton.getX() + 50, mediumButton.getY() + 40, Align.center);

        DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
        if (difficultyLevel.isEasy()) {
            checkMark.setY(easyButton.getY() + 25);
        } else if (difficultyLevel.isHard()) {
            checkMark.setY(hardButton.getY() + 25);
        }

        ImageButton backButton = new ImageButton(new TextureRegionDrawable(uiAtlas.findRegion(RegionNames.BACK)), new TextureRegionDrawable(uiAtlas.findRegion(RegionNames.BACK_PRESSED)));
        backButton.setPosition(GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT / 2 - 180, Align.center);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                back();
            }
        });

        table.addActor(background);
        table.addActor(label);
        table.addActor(easyButton);
        table.addActor(mediumButton);
        table.addActor(hardButton);
        table.addActor(checkMark);
        table.addActor(backButton);

        return table;
    }

    private void back() {
        LOGGER.debug("back()");
        game.setScreen(new MenuScreen(game));
    }

    private static ImageButton createButton(TextureAtlas atlas, String regionName) {
        TextureRegion region = atlas.findRegion(regionName);
        return new ImageButton(new TextureRegionDrawable(region));
    }

}

