package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Towers.IterableOverMap;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bullet {
    private Scene scene;
    private String graphics = "bullet_9_mm/scene.gltf";
    private Vector3 direction;
    private SceneManager sceneManager;
    private List<Enemy> attackers;
    private Enemy enemy;
    private ArrayList<Bullet> bullets;
    private Vector3 directT;

    public Bullet(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, List<Enemy> attackers, ArrayList<Bullet> bullets, float startX, float startY, float startZ, Enemy enemy) {
        scene = new Scene(sceneAssetHashMap.get(graphics).scene);
        sceneManager.addScene(scene);
        this.setTranslation(startX, startY, startZ).scale(0.004f, 0.004f, 0.004f);
       // direction = new Vector3(targetX, targetY, targetZ).setLength(0.001f);
        directT = new Vector3(enemy.getCoords()).sub(startX, 0, startZ).setLength(0.015f);
        this.sceneManager = sceneManager;
        //scene.modelInstance.transform.rotate(new Vector3(),direction);
        this.attackers = attackers;
        this.enemy = enemy;
        this.bullets = bullets;
        bullets.add(this);
    }

    public void move() {
        Vector3 vec = this.scene.modelInstance.transform.getTranslation(new Vector3());

        if(enemy.collides(vec)) {
            //destroyBullet
            System.out.println("Destroyage");
            bulletDelete();
        }
        else {
            scene.modelInstance.transform.setTranslation(vec.x + directT.x, vec.y, vec.z + directT.z);
        }



    }

    public void bulletDelete() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        sceneManager.removeScene(this.scene);
        this.bullets.remove(this);
    }


    public Matrix4 setTranslation(float x, float y, float z) {
        return scene.modelInstance.transform.setTranslation(x, y, z);
    }
}
