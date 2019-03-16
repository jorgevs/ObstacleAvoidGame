package com.mygdx.obstacleavoid.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public abstract class GameObjectBase extends Sprite {

    protected Circle bounds;

    public GameObjectBase(TextureRegion textureRegion, float boundRadius) {
        super(textureRegion);
        bounds = new Circle(getX(), getY(), boundRadius);
    }

    public Circle getBounds() {
        return bounds;
    }

    private void updateBounds() {
        if (bounds == null) {
            return;
        }

        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;
        // draw the object according to sprite position (left-bottom corner)
        bounds.setPosition(getX() + halfWidth, getY() + halfHeight);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateBounds();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        updateBounds();
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);
    }
}
