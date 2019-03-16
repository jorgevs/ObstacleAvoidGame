package com.mygdx.obstacleavoid.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.obstacleavoid.config.GameConfig;

public class Player extends GameObjectBase {

    public Player(TextureRegion textureRegion) {
        super(textureRegion, GameConfig.PLAYER_BOUNDS_RADIUS);
        setSize(GameConfig.PLAYER_SIZE, GameConfig.PLAYER_SIZE);
    }
}
