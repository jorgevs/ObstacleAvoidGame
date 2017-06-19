package com.mygdx.obstacleavoid.entity;

import com.mygdx.obstacleavoid.config.GameConfig;

public class Obstacle extends GameObjectBase {

    private static final float BOUNDS_RADIUS = 0.15f; // world units
    private static final float SIZE = BOUNDS_RADIUS * 2; // world units

    private float speedY = GameConfig.MEDIUM_OBSTABLE_SPEED;

    private boolean hit = false;

    public Obstacle() {
        super(BOUNDS_RADIUS);
    }

    public float getWidth() {
        return SIZE;
    }

    public float getHeight() {
        return SIZE;
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
}
