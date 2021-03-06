package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import io.swapastack.dunetd.ConfigMgr;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Knocker extends Tower{

    public static Knocker firstKnocker = null, secondKnocker = null;
    private static float delaySandworm = 5.f;
    public static boolean isAvailable = true;

    public Knocker(GameScreen screen) {
        graphics = "detail_tree.glb";
        type = 0;
        range = 0;
        gameScreen = screen;
        this.cost = ConfigMgr.knockerCost;


    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, ArrayList<Tower> useless, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setTranslation(x, y, z).scale(0.5f, 0.5f, 0.5f);
        if(firstKnocker == null) {
            firstKnocker = this;
        }
        else if(secondKnocker == null) {
            secondKnocker = this;
            isAvailable = false;
            Timer timer = new Timer();
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    gameScreen.launchSandwormAttack();
                }
            }, delaySandworm);
        }
        else {
            System.out.println("This should not be triggered. The Knocker shouldn't be alloweded to Place");
        }
     //   towers[Math.round(x)][Math.round(z)] = this;
    }

    @Override
    public Enemy fire(ArrayList<Enemy> enemiesList) {
        //Used for Condition check of the Sandworm
        return null;
    }

    public Vector3 getCoords() {
        Vector3 vec = this.getScene().modelInstance.transform.getTranslation(new Vector3());
        return vec;
    }

    public static boolean knockerPlaceAble(int x, int z) {
        if(Knocker.isAvailable()) {
            if (firstKnocker == null) {
                return true;
            } else if (secondKnocker == null) {
                //Check XOR
                // Vector3 stor = firstKnocker.getCoords();
                if ((firstKnocker.getCoords().x == x ^ firstKnocker.getCoords().z == z)) {
                    return true;
                }

            }
        }
        return false;

    }

    public void removeKnocker(SceneManager sceneManager) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        sceneManager.removeScene(this.getScene());

    }

    public static boolean isAvailable() {
        return Knocker.isAvailable;

    }

    /**
     * sets the Knocker available if player is lucky
     */
    public static void gambleAvailabilty() {
        if(Math.round(Math.random()) == 1) {
            Knocker.isAvailable = true;
        }
    }

}
