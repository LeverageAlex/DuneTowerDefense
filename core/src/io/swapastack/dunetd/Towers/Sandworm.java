package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Enemys.Enemy;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Sandworm {
    private Scene scene;
    private String graphics = "sandworm/scene.gltf";
    private SceneManager sceneManager;
    private HashMap<String, SceneAsset> sceneAssetHashMap;
    private boolean direction; //false => x-Richtung , true => z-Richtung
    private float speed = 0.01f;
    private int rows, cols;
    private float currentAngle;

    public Sandworm(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, int rows, int cols) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);

        //.rotate(0, 0.f, 1, 90)
        //Only for about 10 Seconds, so it's okay
        this.sceneManager = sceneManager;
        this.sceneAssetHashMap = sceneAssetHashMap;
        Vector3 vec1 = Knocker.firstKnocker.getCoords();
        Vector3 vec2 = Knocker.secondKnocker.getCoords();

        direction = vec1.x == vec2.x;
        float y = 0.1f;
        //this.setTranslation(x, y, z);
        if(direction) {
            this.setTranslation(Math.round(vec1.x), y, -4);
        }else {
            this.setTranslation(-4, y, Math.round(vec1.z));
            rotateTowardsXAxis();
                    /*.scale(1f, 1f, 1f);*/
        }

        this.rows = rows;
        this.cols = cols;
    }


    public Matrix4 setTranslation(float x, float y, float z) {
        return scene.modelInstance.transform.setTranslation(x, y, z);
    }

    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    public Scene getScene() {
        return scene;
    }


    public void removeWorm(SceneManager sceneManager) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        sceneManager.removeScene(this.getScene());
    }

    public boolean moveWorm() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        if(direction) {
            //Walk on z-Axis
            this.scene.modelInstance.transform.setTranslation(pos.x + 0.f, pos.y + 0.f, pos.z+speed);
        }
        else {
            //walk on x-Axis
            this.scene.modelInstance.transform.setTranslation(pos.x + speed, pos.y, pos.z);
        }
        return (pos.x > rows || pos.z > cols);

    }

    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z);
    }


    public void rotateTowardsXAxis() {
        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, (1.5707964f));

    }


    public void removeLane(MapIterable[][] mapTowers, ArrayList<Enemy> enemies) {
        Vector3 pos = getCoords();
        int x = Math.round(pos.x);
        int z = Math.round(pos.z);
        if(direction) {
            //z-axis
            for (int i = 0; i < mapTowers[0].length; i++) {
                if(mapTowers[x][i] instanceof Tower) {
                    ((Tower)mapTowers[x][i]).removeTower(sceneManager, mapTowers);
                }
            }
            for (int i = 0; i < enemies.size(); i++) {
                float enemX = enemies.get(i).getCoords().x;
                float enemZ = enemies.get(i).getCoords().z;
                if(Math.round(enemX) == x) {
                    enemies.get(i).removeEnemy(sceneManager, enemies);
                    i--;
                }
            }

        }else {
            //x-axis

            for (int i = 0; i < mapTowers.length; i++) {
                if(mapTowers[i][z] instanceof Tower) {
                    ((Tower)mapTowers[i][z]).removeTower(sceneManager, mapTowers);
                }

            }

            for (int i = 0; i < enemies.size(); i++) {
                float enemX = enemies.get(i).getCoords().x;
                float enemZ = enemies.get(i).getCoords().z;
                if(Math.round(enemZ) == z) {
                    enemies.get(i).removeEnemy(sceneManager, enemies);
                    i--;
                }
            }

        }
    }


    public Vector3 getCoords() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        return pos;
    }

}
