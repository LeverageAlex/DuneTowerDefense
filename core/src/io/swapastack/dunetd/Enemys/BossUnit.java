package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class BossUnit extends Enemy{

    public BossUnit() {
        name = "BossUnit";
        graphics = "faceted_character/scene.gltf";
        type = 2;
        currentAngle = -(float) (Math.PI / 2.f);
        rotationSpeed = (float) Math.PI / 256.f;
        movementSpeed = 0.006f;
        health = 120.f;
        damageOnEndPortal = 50;

    }
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, int[][] shortestPath, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z)
                .scale(0.005f, 0.005f, 0.005f);
        this.shortestPath = shortestPath;

    }

    @Override
    public void setWalkAnimation() {
        this.setAnimation("Armature|Run", -1);
    }


    @Override
    public void destroyDamage() {

    }

    @Override
    public float arrivedAtEndPortal() {
        return damageOnEndPortal;
    }


}
