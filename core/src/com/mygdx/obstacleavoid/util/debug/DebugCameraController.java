package com.mygdx.obstacleavoid.util.debug;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class DebugCameraController {
    private static final Logger LOGGER = new Logger(DebugCameraController.class.getName(), Logger.DEBUG);

    // attributes
    private Vector2 position = new Vector2();
    private Vector2 startPosition = new Vector2();
    private float zoom = 1.0f;

    private DebugCameraConfig cameraConfig;

    public DebugCameraController() {
        cameraConfig = new DebugCameraConfig();
        LOGGER.info("cameraConfig: " + cameraConfig.toString());
    }

    public void setStartPosition(float x, float y) {
        startPosition.set(x, y);
        position.set(x, y);
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.set(position, 0);
        camera.zoom = zoom;
        camera.update();
    }

    public void handleDebugInput(float delta) {
        // check if we are not on desktop, then don't handle input just return
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            return;
        }

        float moveSpeed = cameraConfig.getMoveSpeed() * delta;
        float zoomSpeed = cameraConfig.getZoomSpeed() * delta;


        // move controls
        if (cameraConfig.isLeftPressed()) {
            moveLeft(moveSpeed);
        } else if (cameraConfig.isRightPressed()) {
            moveRight(moveSpeed);
        } else if (cameraConfig.isUpPressed()) {
            moveUp(moveSpeed);
        } else if (cameraConfig.isDownPressed()) {
            moveDown(moveSpeed);
        }

        // zoom controls
        if (cameraConfig.isZoomInPressed()) {
            zoomIn(zoomSpeed);
        } else if (cameraConfig.isZoomOutPressed()) {
            zoomOut(zoomSpeed);
        }

        // reset controls
        if (cameraConfig.isResetPressed()) {
            reset();
        }

        // log controls
        if (cameraConfig.isLogPressed()) {
            logDebug();
        }
    }

    private void setPosition(float x, float y) {
        position.set(x, y);
    }

    private void moveCamera(float xSpeed, float ySpeed) {
        setPosition(position.x + xSpeed, position.y + ySpeed);
    }


    private void moveLeft(float speed) {
        moveCamera(speed, 0);
    }

    private void moveRight(float speed) {
        moveCamera(-speed, 0);
    }

    private void moveUp(float speed) {
        moveCamera(0, speed);
    }

    private void moveDown(float speed) {
        moveCamera(0, -speed);
    }


    private void zoomIn(float zoomSpeed) {
        setZoom(zoom + zoomSpeed);
    }

    private void zoomOut(float zoomSpeed) {
        setZoom(zoom - zoomSpeed);
    }

    private void setZoom(float value) {
        // this code will keep the zoom value between the MIN/MAX values
        zoom = MathUtils.clamp(value, cameraConfig.getMaxZoomIn(), cameraConfig.getMaxZoomOut());
    }

    private void reset() {
        position.set(startPosition);
        setZoom(1.0f);
    }

    private void logDebug() {
        LOGGER.debug("Position: " + position + " zoom: " + zoom);
    }
}
