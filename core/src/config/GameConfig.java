package config;

public class GameConfig {

    public static final float WIDTH = 480f; // pixels
    public static final float HEIGHT = 800f; // pixels

    public static final float WORLD_WIDTH = WIDTH / 80; // world units (6.0f)
    public static final float WORLD_HEIGHT = HEIGHT / 80; // world units (10.0f)

    public static final float WORLD_CENTERX = WORLD_WIDTH / 20; // world units (3.0f)
    public static final float WORLD_CENTERY = WORLD_HEIGHT / 20; // world units (5.0f)

    private GameConfig() {}
}
