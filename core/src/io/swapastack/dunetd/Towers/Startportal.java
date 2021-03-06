package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Towers.IterableOverMap;
import io.swapastack.dunetd.Towers.MapIterable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class Startportal extends IterableOverMap implements MapIterable {
    public String graphics = "towerRound_base.glb";
    private Scene scene;
    private float x, y, z;
    private int type = 3, length = 0, color = 0;



    public Startportal(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] map) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.scene.modelInstance.transform.setToTranslation(x, y, z).scale(1.f, 0.001f, 1.f);
        map[Math.round(x)][Math.round(z)] = this;
    }

    public void setPos(Vector3 pos) {
        this.scene.modelInstance.transform.setTranslation(pos);
    }

    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public int getType() {
        return type;
    }

    public void setPathLength(int length) {
        this.length = length;
    }

    public int getPathLength() {
        return length;
    }

    @Override
    public int getPathColor() {
        return color;
    }

    @Override
    public void setPathColor(int color) {
        this.color = color;
    }

    @Override
    public int[] getPosition() {
        int[] p = new int[2];
        p[0] = Math.round(this.x);
        p[1] = Math.round(this.z);
        return p;
    }
    @Override
    public void setPosition(int x, int y) {
        //Never trigger this
        System.out.println("Triggered SetPosition in StartPortal. Should be forbidden!");
    }
}
