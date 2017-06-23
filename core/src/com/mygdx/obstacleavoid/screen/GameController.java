package com.mygdx.obstacleavoid.screen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.obstacleavoid.config.DifficultyLevel;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.Obstacle;
import com.mygdx.obstacleavoid.entity.Player;

import java.util.Iterator;

public class GameController {
    private static final Logger LOGGER = new Logger(GameController.class.getName(), Logger.DEBUG);

    // == attributes ==
    private Player player;
    private Array<Obstacle> obstacles;
    private Pool<Obstacle> obstaclePool;

    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.HARD;

    // == constructors ==
    public GameController() {
        init();
    }

    // == init ==
    private void init(){
        player = new Player();
        // player's initial position
        player.setPosition(GameConfig.WORLD_WIDTH / 2, 1);

        // obstacles
        obstacles = new Array<Obstacle>();
        obstaclePool = Pools.get(Obstacle.class, 20);
    }

    // == public methods ==
    public void update(float deltaTime) {
        if(isGameOver()){
            LOGGER.debug("Game Over!!!");
            return;
        }

        updatePlayer();
        updateObstacles(deltaTime);
        updateScore(deltaTime);
        updateDisplayScore(deltaTime);

        if(isPlayerCollidingWithObstacle()){
            LOGGER.debug("Collision detected.");
            lives--;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    // == private methods ==
    private boolean isPlayerCollidingWithObstacle(){
        for(Obstacle obstacle : obstacles){
            if(!obstacle.isHit() && obstacle.isPlayerColliding(player)){
                return true;
            }
        }

        return false;
    }

    private void updatePlayer() {
        //LOGGER.debug("playerX: " + player.getX() + " playerY: " + player.getY());
        player.update();

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(), (player.getWidth() / 2), (GameConfig.WORLD_WIDTH - player.getWidth() / 2));
        float playerY = MathUtils.clamp(player.getY(), (player.getHeight() / 2), (GameConfig.WORLD_HEIGHT - player.getHeight() / 2));

        player.setPosition(playerX, playerY);
    }

    private void updateObstacles(float delta){
        for (Obstacle obstacle : obstacles){
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void createNewObstacle(float delta){
        obstacleTimer += delta;

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0.0f + (Obstacle.SIZE / 2f);
            float max = GameConfig.WORLD_WIDTH - (Obstacle.SIZE / 2f);
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
        if(obstacles.size > 0) {
            /*Iterator iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                Obstacle obstacle = (Obstacle) iterator.next();
                if (obstacle.getY() < 0) {
                    iterator.remove();
                }
            }*/
            float minObstacleY = (0 - Obstacle.SIZE);
            Obstacle obstacle = obstacles.first();
            if (obstacle.getY() < minObstacleY) {
                obstacles.removeValue(obstacle, true);
                obstaclePool.free(obstacle);
            }
        }
    }

    private void updateScore(float deltaTime){
        scoreTimer += deltaTime;

        if(scoreTimer >= GameConfig.SCORE_MAX_TIME){
            score += MathUtils.random(0,5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float deltaTime) {
        if(displayScore < score){
            displayScore = Math.min(score, displayScore + (int)(60 * deltaTime));   // 60 frames per second

        }
    }

    private boolean isGameOver(){
        return lives <= 0;
    }

}
