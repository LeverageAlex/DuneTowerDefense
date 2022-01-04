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
    private Image selectedImg;
    private Skin skin;
    private String cost;
    private String labelPrefix = "Cost: ";
    private boolean selected = true;
    private GameScreen gameScreen;
    private int selectNbr;

    public HUD_Drawer(GameScreen screen, Skin skin,String graphics, String graphics_selected, int selectNbr,int cost, float x, float y, float width, float height) {
        Texture tex = new Texture(graphics);
        Texture tex2 = new Texture(graphics_selected);
        this.img = new Image(tex);
        this.selectedImg = new Image(tex2);
        img.setX(x);
        img.setY(y);
        img.setWidth(width);
        img.setHeight(height);

        selectedImg.setX(x);
        selectedImg.setY(y);
        selectedImg.setWidth(width);
        selectedImg.setHeight(height);

        this.skin = skin;
        this.cost = String.valueOf(cost);

        gameScreen = screen;

        this.selectNbr = selectNbr;
    }
    public HUD_Drawer(GameScreen screen, Skin skin,String graphics, String graphics_selected, int selectNbr,String cost, float x, float y, float width, float height) {
        Texture tex = new Texture(graphics);
        Texture tex2 = new Texture(graphics_selected);
        this.img = new Image(tex);
        this.selectedImg = new Image(tex2);
        img.setX(x);
        img.setY(y);
        img.setWidth(width);
        img.setHeight(height);

        selectedImg.setX(x);
        selectedImg.setY(y);
        selectedImg.setWidth(width);
        selectedImg.setHeight(height);

        this.skin = skin;
        this.cost = (cost);
        this.labelPrefix = "";

        gameScreen = screen;

        this.selectNbr = selectNbr;
    }
    @Override
    public void draw(Batch batch, float update){
        Label spiceAmount = new Label(labelPrefix + cost,skin);
        spiceAmount.setPosition(img.getX(), img.getY() + img.getHeight());


        if(gameScreen.getSelected() == selectNbr) {
            selectedImg.draw(batch, update);
        }
        else {
            img.draw(batch, update);
        }
        spiceAmount.draw(batch, update);
    }

    public boolean collision(float x, float y) {
        //Stage starts in lower left corner, so need to fix that
        y = Gdx.graphics.getHeight() - y;
        if(img.getX() + img.getWidth() > x && x > img.getX() && img.getY() + img.getHeight() > y && img.getY() < y) {
          //  System.out.println("Hit!");

            return true;
        }
     //   img.setWidth(300);
       // System.out.printf("Leider niggt!: img X: %f, y: %f, input x: %f, y: %f\n", img.getX(), img.getY(), x, y);
        return false;
    }






}
