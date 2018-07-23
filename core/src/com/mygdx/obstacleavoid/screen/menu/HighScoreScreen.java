package com.mygdx.obstacleavoid.screen.menu;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.obstacleavoid.ObstacleAvoidGame;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.common.GameManager;


public class HighScoreScreen extends MenuScreenBase {
    private static final Logger LOGGER = new Logger(HighScoreScreen.class.getName(), Logger.DEBUG);

    public HighScoreScreen(ObstacleAvoidGame game) {
        super(game);
    }

    @Override
    protected Actor createUI() {
        Table table = new Table();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        Skin skin = assetManager.get(AssetDescriptors.SKIN);

        TextureRegion backgroundTextureRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

        // background
        table.setBackground(new TextureRegionDrawable(backgroundTextureRegion));

        // highscore text
        Label highScoreText = new Label("HIGHSCORE", skin);

        // highscore label
        String highScoreString = GameManager.INSTANCE.getHighScoreString();
        Label highScoreLabel = new Label(highScoreString, skin);

        // back button
        TextButton backButton = new TextButton("BACK", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                back();
            }
        });

        // setup tables
        Table contentTable = new Table(skin);
        contentTable.defaults().pad(20);
        contentTable.setBackground(RegionNames.PANEL);

        contentTable.add(highScoreText).row();
        contentTable.add(highScoreLabel).row();
        contentTable.add(backButton).row();

        contentTable.center();

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

}
