package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Knocker extends Tower{

    public static Knocker firstKnocker = null, secondKnocker = null;

    public Knocker(GameScreen screen) {
        graphics = "detail_tree.glb";
        type = 0;
        range = 3;
        gameScreen = screen;
        this.cost = 35;

    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setTranslation(x, y, z);
        if(firstKnocker == null) {
            firstKnocker = this;
        }
        else if(secondKnocker == null) {
            secondKnocker = this;
        }
        else {
            System.out.println("This should not be triggered. The Knocker shouldn't be alloweded to Place");
        }
     //   towers[Math.round(x)][Math.round(z)] = this;
    }

    @Override
    public void fire(ArrayList<Enemy> enemiesList) {
        //Used for Condition check of the Sandworm
    }

    public Vector3 getCoords() {
        Vector3 vec = this.getScene().modelInstance.transform.getTranslation(new Vector3());
        return vec;
    }

    public static boolean knockerPlaceAble(int x, int z) {
        if(firstKnocker == null) {
            return true;
        }
        else if (secondKnocker == null){
            //Check XOR
           // Vector3 stor = firstKnocker.getCoords();
            if((firstKnocker.getCoords().x == x ^ firstKnocker.getCoords().z == z )) {
                return true;
            }

        }

        return false;

    }
}
