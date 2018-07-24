package com.mygdx.obstacleavoid.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.obstacleavoid.config.GameConfig;

public class PlayerActor extends ActorBase {

    public PlayerActor() {
        setCollisionRadius(GameConfig.PLAYER_BOUNDS_RADIUS);
        setSize(GameConfig.PLAYER_SIZE, GameConfig.PLAYER_SIZE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update();
    }

    private void update() {
        float xSpeed = 0;
        float ySpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            ySpeed = GameConfig.MAX_PLAYER_Y_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            ySpeed = -GameConfig.MAX_PLAYER_Y_SPEED;
        }

        setPosition(getX() + xSpeed, getY() + ySpeed);
        //LOGGER.debug("playerX: " + player.getX() + " playerY: " + player.getY());

        // keeps the player inside the world's borders
        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(getX(), 0, GameConfig.WORLD_WIDTH - getWidth());
        float playerY = MathUtils.clamp(getY(), 0, GameConfig.WORLD_HEIGHT - getHeight());

        setPosition(playerX, playerY);
    }
}
