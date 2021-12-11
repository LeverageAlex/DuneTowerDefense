package io.swapastack.dunetd;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MouseForCollision implements InputProcessor {
    public GameScreen gameScreen;

    public MouseForCollision(GameScreen screen) {
        gameScreen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(Input.Keys.SPACE == keycode) {
            gameScreen.resetBeamPos();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
       // System.out.println("Maus @ " + screenX + ", y: " + screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
