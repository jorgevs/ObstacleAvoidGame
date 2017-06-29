package com.mygdx.obstacleavoid.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.obstacleavoid.config.DifficultyLevel;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.Background;
import com.mygdx.obstacleavoid.entity.Obstacle;
import com.mygdx.obstacleavoid.entity.Player;

public class GameController {
    private static final Logger LOGGER = new Logger(GameController.class.getName(), Logger.DEBUG);

    // == attributes ==
    private Player player;
    private Array<Obstacle> obstacles;
    private Pool<Obstacle> obstaclePool;
    private Background background;

    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.HARD;

    // player's initial position
    private final float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2;
    private final float startPlayerY = 1 - (GameConfig.PLAYER_SIZE / 2);


    // == constructors ==
    public GameController() {
        init();
    }

    // == init ==
    private void init() {
        player = new Player();
        player.setPosition(startPlayerX, startPlayerY);

        // obstacles
        obstacles = new Array<Obstacle>();
        obstaclePool = Pools.get(Obstacle.class, 20);

        // background
        background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
    }

    // == public methods ==
    public void update(float deltaTime) {
        if (isGameOver()) {
            return;
        }

        updatePlayer();
        updateObstacles(deltaTime);
        updateScore(deltaTime);
        updateDisplayScore(deltaTime);

        if (isPlayerCollidingWithObstacle()) {
            LOGGER.debug("Collision detected.");
            lives--;

            if (isGameOver()) {
                LOGGER.debug("Game Over!!!");
            } else {
                restart();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Background getBackground() {
        return background;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    // == private methods ==
    private void restart() {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);
    }

    private boolean isPlayerCollidingWithObstacle() {
        for (Obstacle obstacle : obstacles) {
            if (!obstacle.isHit() && obstacle.isPlayerColliding(player)) {
                return true;
            }
        }

        return false;
    }

    private void updatePlayer() {
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

        player.setPosition(player.getX() + xSpeed, player.getY() + ySpeed);
        //LOGGER.debug("playerX: " + player.getX() + " playerY: " + player.getY());

        // keeps the player inside the world's borders
        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(), 0, GameConfig.WORLD_WIDTH - player.getWidth());
        float playerY = MathUtils.clamp(player.getY(), 0, GameConfig.WORLD_HEIGHT - player.getHeight());

        player.setPosition(playerX, playerY);
    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void createNewObstacle(float delta) {
        obstacleTimer += delta;

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            float min = 0f;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            //Obstacle obstacle = new Obstacle();
            Obstacle obstacle = obstaclePool.obtain();
            obstacle.setSpeedY(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle);
            // reset the obstacle timer
            obstacleTimer = 0.0f;
        }
    }

    private void removePassedObstacles() {
        if (obstacles.size > 0) {
            /*Iterator iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                Obstacle obstacle = (Obstacle) iterator.next();
                if (obstacle.getY() < 0) {
                    iterator.remove();
                }
            }*/
            float minObstacleY = (0 - GameConfig.OBSTACLE_SIZE);
            Obstacle obstacle = obstacles.first();
            if (obstacle.getY() < minObstacleY) {
                obstacles.removeValue(obstacle, true);
                obstaclePool.free(obstacle);
            }
        }
    }

    private void updateScore(float deltaTime) {
        scoreTimer += deltaTime;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(0, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float deltaTime) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (int) (60 * deltaTime));   // 60 frames per second

        }
    }

}
