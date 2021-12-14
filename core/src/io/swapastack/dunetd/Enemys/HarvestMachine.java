package io.swapastack.dunetd.Enemys;

import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class HarvestMachine extends Enemy{
    public HarvestMachine() {
        graphics = "spaceship_orion/scene.gltf";
        type = 1;
        currentAngle = -(float) Math.PI/2.f;
        rotationSpeed = (float) Math.PI / 128.f;
        movementSpeed = 0.001f;
        health = 50.f;
        damageOnEndPortal = 10;
    }

    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, int[][] shortestPath,float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z)
                .scale(0.2f, 0.2f, 0.2f);
        this.shortestPath = shortestPath;

    }

    @Override
    public void setWalkAnimation() {
        this.setAnimation("Action", -1);
    }

    @Override
    public void destroyDamage() {

    }

    @Override
    public float arrivedAtEndPortal() {
        return damageOnEndPortal;
    }

}
