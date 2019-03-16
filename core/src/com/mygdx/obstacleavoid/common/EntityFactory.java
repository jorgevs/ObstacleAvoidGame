package com.mygdx.obstacleavoid.common;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.obstacleavoid.assets.AssetDescriptors;
import com.mygdx.obstacleavoid.assets.RegionNames;
import com.mygdx.obstacleavoid.config.GameConfig;
import com.mygdx.obstacleavoid.entity.Background;
import com.mygdx.obstacleavoid.entity.Obstacle;
import com.mygdx.obstacleavoid.entity.Player;

public class EntityFactory {

    private final AssetManager assetManager;

    private TextureAtlas gamePlayAtlas;
    private Pool<Obstacle> obstaclePool;


    public EntityFactory(AssetManager assetManager) {
        this.assetManager = assetManager;

        init();
    }

    private void init() {
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);

        obstaclePool = new Pool<Obstacle>(40) {
            @Override
            protected Obstacle newObject() {
                return createObstacle();
            }
        };
    }

    private Obstacle createObstacle() {
        TextureRegion obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        return new Obstacle(obstacleRegion);
    }

    public Obstacle obtain() {
        return obstaclePool.obtain();
    }

    public void free(Obstacle obstacle) {
        obstaclePool.free(obstacle);
    }

    public void freeAll(Array<Obstacle> obstacles) {
        obstaclePool.freeAll(obstacles);
    }

    public Player createPlayer() {
        TextureRegion playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        return new Player(playerRegion);
    }

    public Background createBackground() {
        Background background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        return background;
    }
}
