package io.swapastack.dunetd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HUD_Drawer extends Actor {
    private Image img;
    private Skin skin;
    private int cost;

    public HUD_Drawer(Skin skin,String graphics, int cost,float x, float y, float width, float height) {
        Texture tex = new Texture(graphics);
        this.img = new Image(tex);
        img.setX(x);
        img.setY(y);
        img.setWidth(width);
        img.setHeight(height);
        this.skin = skin;
        this.cost = cost;
    }
    @Override
    public void draw(Batch batch, float update){
        Label spiceAmount = new Label("Cost: " + cost,skin);
        spiceAmount.setPosition(img.getX(), img.getY() + img.getHeight());

        img.draw(batch, update);
        spiceAmount.draw(batch, update);
    }

    public boolean collision(float x, float y) {
        //Stage starts in lower left corner, so need to fix that
        y = Gdx.graphics.getHeight() - y;
        if(img.getX() + img.getWidth() > x && x > img.getX() && img.getY() + img.getHeight() > y && img.getY() < y) {
            System.out.println("Hit!");

            return true;
        }
     //   img.setWidth(300);
       // System.out.printf("Leider niggt!: img X: %f, y: %f, input x: %f, y: %f\n", img.getX(), img.getY(), x, y);
        return false;
    }
}
