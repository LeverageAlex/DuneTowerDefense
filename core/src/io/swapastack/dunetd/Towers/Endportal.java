package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Towers.IterableOverMap;
import io.swapastack.dunetd.Towers.MapIterable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class Endportal extends IterableOverMap implements MapIterable{
    public String graphics = "towerSquare_topB.glb";
    private Scene scene;
    private float x, y, z;
    private int type = 4, length = Integer.MAX_VALUE, color = 0;

    public Endportal(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] map, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.scene.modelInstance.transform.setToTranslation(x, y, z).scale(1.f, 0.1f, 1.f);
        this.x = x;
        this.y = y;
        this.z = z;
        map[Math.round(x)][Math.round(z)] = this;
        }

    public void setPos(Vector3 pos) {
        this.scene.modelInstance.transform.setTranslation(pos);
    }

    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    @Override
    public int[] getPosition() {
        int[] p = new int[2];
        p[0] = Math.round(x);
        p[1] = Math.round(z);
        return p;
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
}
