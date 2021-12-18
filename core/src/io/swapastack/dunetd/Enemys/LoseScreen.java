package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.ScreenEnum;

public class LoseScreen implements Screen {
    private DuneTD parent;
    Stage stage;
    Skin skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));

    public LoseScreen(DuneTD duneTD) {


        Label lose = new Label("You have lost all your Lifes", skin, "black");
        lose.setFontScale(4);
        lose.setPosition(Gdx.graphics.getWidth()/2 - 300, Gdx.graphics.getHeight()/2 + 100);

        stage = new Stage();

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

        stage.addActor(lose);
        stage.addActor(b);



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
