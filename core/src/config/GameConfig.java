package config;

public class GameConfig {

    public static final float WIDTH = 480f; // pixels
    public static final float HEIGHT = 800f; // pixels

    public static final float WORLD_WIDTH = WIDTH / 80; // world units (6.0f)
    public static final float WORLD_HEIGHT = HEIGHT / 80; // world units (10.0f)

    public static final float WORLD_CENTERX = WORLD_WIDTH / 20; // world units (3.0f)
    public static final float WORLD_CENTERY = WORLD_HEIGHT / 20; // world units (5.0f)

    public static final float MAX_PLAYER_X_SPEED = 0.25f; // unit worlds
    public static final float MAX_PLAYER_Y_SPEED = 0.25f; // unit worlds

    public static final float MAX_OBSTACLE_X_SPEED = 0.25f; // unit worlds
    public static final float MAX_OBSTACLE_Y_SPEED = 0.25f; // unit worlds


    public static final float OBSTACLE_SPAWN_TIME = 0.25f; // secs

    private GameConfig() {}
}
