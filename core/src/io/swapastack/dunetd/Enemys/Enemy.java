package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public abstract class Enemy {
    protected int level;
    public String name;
    protected int storedSpice;
    protected int health;
    protected double movementSpeed;
    public String graphics;
    protected int type;
    protected Scene scene;
    protected AnimationController animController;


    public abstract void destroyDamage();

    public abstract void onKill();

    public abstract void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, float x, float y, float z);

    //  public abstract void gainDamage();


    public Scene createScene(HashMap<String, SceneAsset> m) {
        scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    public Matrix4 setToTranslation(Vector3 vector) {
        return scene.modelInstance.transform.setToTranslation(vector);
    }

    public Matrix4 setToTranslation(float x, float y, float z) {
        return scene.modelInstance.transform.setToTranslation(x, y, z);
    }

    public void setAnimation(String id, int loopCnt) {
        getAnimationController().setAnimation(id, loopCnt);
    }

    public Scene getScene() {
        return scene;
    }

    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z);
    }

    public AnimationController getAnimationController() {
        if (animController == null) {
            animController = new AnimationController(scene.modelInstance);
        }
        return animController;
    }

    public void setPos(Vector3 pos) {
        this.scene.modelInstance.transform.setTranslation(pos);
    }

    public void printCoords() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        System.out.printf(this.getClass().getName() + " grid: x: %f, y:%f, z:%f\n", pos.x, pos.y, pos.z);
    }

    public int getType() {
        return type;
    }

    public Vector3 getCoords() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        return pos;
    }
}
