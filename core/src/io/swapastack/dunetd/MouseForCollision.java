package io.swapastack.dunetd;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Timer;
import io.swapastack.dunetd.Towers.*;

public class MouseForCollision implements InputProcessor {
    private GameScreen gameScreen;
    private Timer timer;
    private float delaySeconds;
    int countdown = 0;
    Timer.Task graphicalCountdown = new Timer.Task() {
        @Override
        public void run() {
            countdown--;
            gameScreen.setCountdown(countdown);
            if(countdown > 0) {
                timer.scheduleTask(graphicalCountdown, 1);
            }

        }
    };

    public MouseForCollision(GameScreen screen) {
        gameScreen = screen;
        delaySeconds = ConfigMgr.waveStartdelay;
        timer = new Timer();
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

        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //This Method is ment to place towers if game is in TowerBuilder-Phase
        //Automatically starts countdown after first Tower placed for next Wave to start
           // gameScreen.getPhase()
        //Phase == 0 || Phase == 3 => towerBuilding-Phase
        if(gameScreen.getPhase() == 0) {
            int nbr = gameScreen.getColliding(screenX, screenY);
            if(nbr >= 0 && gameScreen.getSelected() == -1) {
                gameScreen.setPhase((GameStateEnum.SELECTED));
                gameScreen.setSelected(nbr);
                return true;
            }
            else if(nbr >= 0 && gameScreen.getSelected() >= 0) {
                gameScreen.setPhase(GameStateEnum.BUILDING);
                gameScreen.setSelected(-1);
            }

        }
        //If tower selected, then arrange placement, if selected point valid
        else if(gameScreen.getSelected() >= 0 && gameScreen.getPhase() == 3) {
            Tower t = null;
            switch (gameScreen.getSelected()) {
                case 0:
                    t = new BombTower(gameScreen);
                    break;
                case 1:
                    t = new CanonTower(gameScreen);
                    break;
                case 2:
                    t = new SonicTower(gameScreen);
                    break;
            }
            if (gameScreen.getPlayer().getSpice() - t.getCost() >= 0) {
                gameScreen.setPhase(GameStateEnum.BUILDING);
                gameScreen.setSelected(-1);
                    if (gameScreen.placeTower(t)) {

                        System.out.println("Tower erfolgreich gesetzt");
                        gameScreen.getPlayer().addSpice(-t.getCost());

                        if(timer.isEmpty()) {
                            countdown = Math.round(delaySeconds);
                            gameScreen.setCountdown(countdown);
                            timer.scheduleTask(new Timer.Task() {
                                @Override
                                public void run() {
                                    countdown = 0;
                                    gameScreen.setPhase(GameStateEnum.WAVE);
                                    if (gameScreen.getSelected() < 3) {
                                        gameScreen.setSelected(-1);
                                    }
                                }
                            }, delaySeconds);

                            timer.scheduleTask(graphicalCountdown, 1);
                        }
                        return true;
                    } else {
                        System.out.println("Tower setzen fehlgeschlagen :/");
                        gameScreen.setPhase(GameStateEnum.BUILDING);
                        gameScreen.setSelected(-1);
                    }

            }
            else {
                System.out.println("Not enough Spice");
                //Unselect selected Tower
                gameScreen.setPhase(GameStateEnum.BUILDING);
                gameScreen.setSelected(-1);
            }
        }

        if(gameScreen.getCollidingKnocker(screenX, screenY)) {
            if (gameScreen.getSelected() == -1) {
                gameScreen.setSelected(3);
            }
        }
            else if(gameScreen.getSelected() == 3) {
                gameScreen.setSelected(-1);
                Knocker knocker = new Knocker(gameScreen);
                if(gameScreen.placeKnocker(knocker)) {
                    System.out.println("Successfull Knocker place");
                }
                else {
                    System.out.println("Placing went wrong!");
                }
            }


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
