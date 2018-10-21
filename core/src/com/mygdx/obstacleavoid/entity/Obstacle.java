package com.mygdx.obstacleavoid.entity;

import com.badlogic.gdx.utils.Pool;
import com.mygdx.obstacleavoid.config.GameConfig;

public class Obstacle extends GameObjectBase implements Pool.Poolable {

    private float speedY = GameConfig.MEDIUM_OBSTABLE_SPEED;

    private boolean hit = false;

    public Obstacle() {
        super(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void update() {
        setPosition(getX(), getY() - getSpeedY());
    }

    public boolean isPlayerColliding(Player player) {
        //return Intersector.overlaps(getBounds(), player.getBounds());
        boolean overlaps = getBounds().overlaps(player.getBounds());

        if (overlaps) {
            hit = true;
        }

        return overlaps;
    }

    public boolean isHit() {
        return hit;
    }

    @Override
    public void reset() {
        // Reset the Obstacle object, once it has been free in the pool
        hit = false;
    }
}
