package io.swapastack.dunetd.Enemys;

import io.swapastack.dunetd.ConfigMgr;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class HarvestMachine extends Enemy{
    public HarvestMachine() {
        graphics = "spaceship_orion/scene.gltf";
        type = 1;
        currentAngle = -(float) Math.PI/2.f;
        rotationSpeed = (float) Math.PI / 128.f;
        movementSpeed = ConfigMgr.harvMovSpeed;
        health = ConfigMgr.harvHealth;
        damageOnEndPortal = ConfigMgr.harvDmgOnEndPortal;
        storedSpice = ConfigMgr.harvStoredSpice;
        highscorePoints = ConfigMgr.harvHSPoints;

        for (int i = 0; i < ConfigMgr.levelStrength; i++) {
            health *= ConfigMgr.infLevelUpCoeff;
        }
    }

    /**
     * Instantiates the scene of the enemy and updates its moving direction
     * @param sceneManager
     * @param sceneAssetHashMap
     * @param shortestPath
     * @param x
     * @param y
     * @param z
     */
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, int[][] shortestPath,float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y+0.06f, z)
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
    public int arrivedAtEndPortal() {
        return damageOnEndPortal;
    }

}
