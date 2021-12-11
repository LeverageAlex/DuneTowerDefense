package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;


import java.util.HashMap;

public class Infantry extends Enemy {

    public Infantry() {
        graphics = /*"faceted_character/scene.gltf";*/ "cute_cyborg/scene.gltf";
        type = 0;
    }

    @Override
    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
      //  System.out.printf("%f %f %f\n",this.x, this.y, this.z);
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z);
    }

    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z)
                .scale(0.02f, 0.04f, 0.03f).rotate(new Vector3(0.0f, 1.0f, 0.0f), 180.0f);
    }


    @Override
    public void destroyDamage() {

    }

    @Override
    public void onKill() {

    }


    public boolean collides(float x, float y, float z, Camera cam) {
        printCoords();
        return false;
    }


}
