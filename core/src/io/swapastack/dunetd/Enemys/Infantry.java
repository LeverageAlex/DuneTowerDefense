package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.ConfigMgr;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;


import java.util.HashMap;

public class Infantry extends Enemy {

    public Infantry() {
        graphics = /*"faceted_character/scene.gltf";*/ "cute_cyborg/scene.gltf";
        type = 0;
        currentAngle = -(float) Math.PI/2.f;
        rotationSpeed = (float) Math.PI / 256.f;
        movementSpeed = ConfigMgr.infMovSpeed;
        health = ConfigMgr.infHealth;
       // target = new int[]{3, 1};
        damageOnEndPortal = ConfigMgr.infDmgOnEndPortal;
        storedSpice = ConfigMgr.infStoredSpice;
        highscorePoints = ConfigMgr.infHSPoints;
    }

    @Override
    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
      //  System.out.printf("%f %f %f\n",this.x, this.y, this.z);
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z);
    }

    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, int[][] shortestPath ,float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z)
                .scale(0.02f, 0.04f, 0.03f).rotate(new Vector3(0.0f, 1.0f, 0.0f), 180.0f);
        this.shortestPath = shortestPath;
    }

    @Override
    public void setWalkAnimation() {
        this.setAnimation("RUN", -1);
    }


    @Override
    public void destroyDamage() {

    }

    @Override
    public int arrivedAtEndPortal() {
        return damageOnEndPortal;
    }


    public boolean collides(float x, float y, float z, Camera cam) {
        printCoords();
        return false;
    }


}
