package io.swapastack.dunetd;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Towers.MapIterable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.HashMap;

public class Startportal implements MapIterable {
    public String graphics = "towerRound_base.glb";
    private Scene scene;
    private float x, y, z;
    private int type = 3, length = 0, color = 0;



    public Startportal(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] map, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.scene.modelInstance.transform.setToTranslation(x, y, z).scale(1.f, 0.001f, 1.f);
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

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }
}
