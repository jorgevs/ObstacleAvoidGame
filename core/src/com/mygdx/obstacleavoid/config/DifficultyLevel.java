package com.mygdx.obstacleavoid.config;


public enum DifficultyLevel {
    EASY(GameConfig.EASY_OBSTABLE_SPEED),
    MEDIUM(GameConfig.MEDIUM_OBSTABLE_SPEED),
    HARD(GameConfig.HARD_OBSTABLE_SPEED);


    private final float obstacleSpeed;

    DifficultyLevel(float obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
    }

    public float getObstacleSpeed(){
        return this.obstacleSpeed;
    }
}
