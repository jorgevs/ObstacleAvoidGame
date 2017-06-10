package entity;

import config.GameConfig;

public class Obstacle extends GameObjectBase {

    private static final float BOUNDS_RADIUS = 0.3f; // world units
    private static final float SIZE = BOUNDS_RADIUS * 2; // world units

    public Obstacle() {
        super(BOUNDS_RADIUS);
    }

    public void update() {
        setPosition(getX(), getY() - GameConfig.MAX_OBSTACLE_Y_SPEED);
    }

    public float getWidth() {
        return SIZE;
    }

    public float getHeight() {
        return SIZE;
    }

}
