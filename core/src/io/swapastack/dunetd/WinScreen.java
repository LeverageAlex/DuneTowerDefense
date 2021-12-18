package io.swapastack.dunetd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WinScreen implements Screen {
    private DuneTD parent;
    Stage stage;
    Skin skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
    private int highscore, health, enemysKilled, waves;

    public WinScreen(DuneTD duneTD, int highscore, int health, int enemysKilled, int waves) {
        this.highscore = highscore;
        this.health = health;
        this.enemysKilled = enemysKilled;
        this.waves = waves;
        this.parent = duneTD;

        Label win = new Label("VICTORY", skin, "black");
        win.setFontScale(7);
        win.setPosition(Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 + 100);

        Label hs = new Label          ("Highscore:             " + highscore  ,skin, "black");
        hs.setFontScale(3);
        Label playerHealth = new Label("Remaining health:    " + health  ,skin, "black");
        playerHealth.setFontScale(3);
        Label killed = new Label      ("Enemys Killed:          " + enemysKilled  ,skin, "black");
        killed.setFontScale(3);
        Label wav = new Label         ("Waves beaten:        " + waves  ,skin, "black");
        wav.setFontScale(3);

        hs.setPosition(Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 - 50);
        playerHealth.setPosition(Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 - 100);
        killed.setPosition(Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 - 150);
        wav.setPosition(Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 - 200);

        Button b = new TextButton("Back to main-menu", skin);
        b.addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                parent.changeScreen(ScreenEnum.MENU);
            }
        });
        stage = new Stage();

        stage.addActor(hs);
        stage.addActor(playerHealth);
        stage.addActor(killed);
        stage.addActor(wav);

        stage.addActor(b);
        stage.addActor(win);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
