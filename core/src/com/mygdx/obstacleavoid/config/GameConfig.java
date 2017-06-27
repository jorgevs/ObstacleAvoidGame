package com.mygdx.obstacleavoid.config;

public class GameConfig {

    public static final float WIDTH = 480f; // pixels
    public static final float HEIGHT = 800f; // pixels

    public static final float HUD_WIDTH = 480f; // world units
    public static final float HUD_HEIGHT = 800f; // world units

    public static final float WORLD_WIDTH = WIDTH / 80; // world units (6.0f)
    public static final float WORLD_HEIGHT = HEIGHT / 80; // world units (10.0f)

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2; // world units (3.0f)
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2; // world units (5.0f)

    public static final float MAX_PLAYER_X_SPEED = 0.25f; // unit worlds
    public static final float MAX_PLAYER_Y_SPEED = 0.25f; // unit worlds

    public static final float OBSTACLE_SPAWN_TIME = 0.25f; // secs - spawn obstacle every interval
    public static final float SCORE_MAX_TIME = 1.25f; // secs - add score every interval

    public static final int LIVES_START = 3;

    public static final float EASY_OBSTABLE_SPEED = 0.1f;
    public static final float MEDIUM_OBSTABLE_SPEED = 0.20f;
    public static final float HARD_OBSTABLE_SPEED = 0.30f;


    public static final float PLAYER_BOUNDS_RADIUS = 0.4f; // world units
    public static final float PLAYER_SIZE = PLAYER_BOUNDS_RADIUS * 2; // world units

    public static final float OBSTACLE_BOUNDS_RADIUS = 0.15f; // world units
    public static final float OBSTACLE_SIZE = OBSTACLE_BOUNDS_RADIUS * 2; // world units

    private GameConfig() {}
}
