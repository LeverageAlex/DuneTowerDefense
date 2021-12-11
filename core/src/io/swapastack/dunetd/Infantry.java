package io.swapastack.dunetd;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;


import java.util.HashMap;

public class Infantry extends Enemy{

    public Infantry init() {
        graphics = "faceted_character/scene.gltf";
        return this;
    }

    public Scene createScene(HashMap<String, SceneAsset> m) {
        scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    @Override
    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
      //  System.out.printf("%f %f %f\n",this.x, this.y, this.z);
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z);
    }

    @Override
    public void destroyDamage() {

    }

    @Override
    public void onKill() {

    }

    public void printCoords() {

        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        System.out.printf("Infantry grid: x: %f, y:%f, z:%f\n", pos.x, pos.y, pos.z);

    }

    public boolean collides(float x, float y, float z, Camera cam) {
        printCoords();

        return false;
    }
}
