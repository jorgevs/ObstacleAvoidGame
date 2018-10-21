package com.mygdx.obstacleavoid.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.obstacleavoid.config.GameConfig;

public class ObstacleActor extends ActorBase implements Pool.Poolable {

    private float ySpeed = GameConfig.MEDIUM_OBSTABLE_SPEED;
    private boolean hit;

    public ObstacleActor() {
        setCollisionRadius(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update();
    }

    public void update() {
        setY(getY() - ySpeed);
    }

    public void setYSpeed(float ySpeed){
        this.ySpeed = ySpeed;
    }

    public boolean isPlayerColliding(PlayerActor player) {
        Circle playerBounds  = player.getCollisionShape();
        boolean overlaps = Intersector.overlaps(playerBounds, getCollisionShape());
        hit = overlaps;
        return overlaps;
    }

    public boolean isHit(){
        return hit;
    }

    @Override
    public void reset() {
        setTextureRegion(null);
        hit = false;
    }

}
