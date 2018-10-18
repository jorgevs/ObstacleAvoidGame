package com.mygdx.obstacleavoid.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Logger;


public abstract class ActorBase extends Actor {
    private static final Logger LOGGER = new Logger(ActorBase.class.getName(), Logger.DEBUG);

    private final Circle collisionShape = new Circle();
    private TextureRegion textureRegion;

    public ActorBase() {
    }

    public void setCollisionRadius(float radius) {
        collisionShape.setRadius(radius);
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (textureRegion == null) {
            LOGGER.error("Region not set on Actor " + getClass().getName());
            return;
        }

        batch.draw(textureRegion, getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());
    }

    public Circle getCollisionShape() {
        return collisionShape;
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapeRenderer) {
        if (!getDebug()) {
            return;
        }
        Color oldColor = shapeRenderer.getColor().cpy();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.x(collisionShape.x, collisionShape.y, 0.1f);
        shapeRenderer.circle(collisionShape.x, collisionShape.y, collisionShape.radius, 30);
        shapeRenderer.setColor(oldColor);
    }

    @Override
    protected void positionChanged() {
        updateCollisionShape();
    }

    @Override
    protected void sizeChanged() {
        updateCollisionShape();
    }

    public void updateCollisionShape() {
        float halfWidth = getWidth() / 2;
        float halfHeight = getHeight() / 2;
        collisionShape.setPosition(getX() + halfWidth, getY() + halfHeight);
    }
}
