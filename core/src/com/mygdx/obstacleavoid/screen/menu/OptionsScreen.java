package com.mygdx.obstacleavoid.screen.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.common.GameManager;
import com.mygdx.obstacleavoid.config.DifficultyLevel;

public class OptionsScreen extends MenuScreenBase {
    private static final Logger LOGGER = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    // Enables radioButton functionality
    private ButtonGroup<CheckBox> checkBoxGroup;
    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;


    public OptionsScreen(ObstacleAvoidGame game) {
        super(game);
    }

    @Override
    protected Actor createUI() {
        Table table = new Table();
        table.defaults().pad(15);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        Skin skin = assetManager.get(AssetDescriptors.SKIN);

        TextureRegion backgroundTextureRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundTextureRegion));

        Label label = new Label("DIFFICULTY", skin);
        easy =  createCheckBox(DifficultyLevel.EASY.name(), skin);
        medium = createCheckBox(DifficultyLevel.MEDIUM.name(), skin);
        hard = createCheckBox(DifficultyLevel.HARD.name(), skin);

        checkBoxGroup = new ButtonGroup<CheckBox>(easy, medium, hard);
        DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
        checkBoxGroup.setChecked(difficultyLevel.name());

        TextButton backButton = new TextButton("BACK", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                back();
            }
        });

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                difficultyChanged();
            }
        };

        easy.addListener(changeListener);
        medium.addListener(changeListener);
        hard.addListener(changeListener);

        // setup table
        Table contentTable = new Table(skin);
        contentTable.defaults().pad(10);
        contentTable.setBackground(RegionNames.PANEL);

        contentTable.add(label).row();
        contentTable.add(easy).row();
        contentTable.add(medium).row();
        contentTable.add(hard).row();
        contentTable.add(backButton);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void back() {
        LOGGER.debug("back()");
        game.setScreen(new MenuScreen(game));
    }

    private void difficultyChanged() {
        LOGGER.debug("difficultyChanged()");
        CheckBox checked = checkBoxGroup.getChecked();
        if (checked == easy) {
            LOGGER.debug("easy");
            GameManager.INSTANCE.updateDifficulty(DifficultyLevel.EASY);
        } else if (checked == medium) {
            LOGGER.debug("medium");
            GameManager.INSTANCE.updateDifficulty(DifficultyLevel.MEDIUM);
        } else if (checked == hard) {
            LOGGER.debug("hard");
            GameManager.INSTANCE.updateDifficulty(DifficultyLevel.HARD);
        }
    }

    private static CheckBox createCheckBox(String text, Skin skin){
        CheckBox checkBox = new CheckBox(text, skin);
        checkBox.left().pad(8);
        checkBox.getLabelCell().pad(8);
        return checkBox;
    }
}