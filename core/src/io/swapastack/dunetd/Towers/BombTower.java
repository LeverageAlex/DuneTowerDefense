package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.ConfigMgr;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;

public class BombTower extends Tower{
    float currentAngle = (float) (Math.PI/2);
    float rotationSpeed = (float) Math.PI/256;


    public BombTower(GameScreen screen) {
        graphics = "weapon_blaster.glb";
        type = 0;
        range = ConfigMgr.bombTowRange;
        gameScreen = screen;
        this.cost = ConfigMgr.bombTowCost;
        towerDmg = ConfigMgr.bombTowDmg;

    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, ArrayList<Tower> towerList, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setTranslation(x, y, z);
       // this.scene.modelInstance.mate
       // if(isEligibleToPlace(towers, gameScreen,Math.round(x), Math.round(z))) {
            towers[Math.round(x)][Math.round(z)] = this;
       // }
        towerList.add(this);
        this.towerList = towerList;
    }

    @Override
    public Enemy fire(ArrayList<Enemy> enemiesList) {
        for (Enemy enemy : enemiesList) {

            if (isInRange(enemy.getCoords())) {
             //   System.out.println("Die Koord: " + enemy.getCoords());
                boolean fire = rotateTowardsVectorSmooth(enemy.getCoords());
                //Beeing able to shoot
                if (fire) {
                    //   System.out.println("fire");
                }
                return null;
                // }
            }
        }
        return null;
    }

    public boolean rotateTowardsVectorSmooth(Vector3 pointToRotate) {
        Vector3 towerCoords = this.scene.modelInstance.transform.getTranslation(new Vector3());
        float rotation = (float) Math.atan2((towerCoords.z - pointToRotate.z), (towerCoords.x - pointToRotate.x));
        //   int x = rotation-currentAngle < 0 ? -1 : 1;
        //rotating the gun smoothly with clamp (Min/Max of Clamp is max speed of rotating towards point
        float toRotate;
        boolean notClamped = true;
        if (rotation - currentAngle < -rotationSpeed) {
            toRotate = -rotationSpeed;
            notClamped = false;
        } else if (rotation - currentAngle > rotationSpeed) {
            toRotate = rotationSpeed;
            notClamped = false;
        } else {
            toRotate = rotation - currentAngle;
        }
        if(currentAngle - 0.05f < -Math.PI && rotation + 0.05f > Math.PI) {
            toRotate = (float) (rotation + Math.PI);
            System.out.println("Critical code in BombTower triggered. Check for interferences!");
        }
        //This might be odd
        if(currentAngle + 0.05f > Math.PI && rotation - 0.05f < -Math.PI) {
            toRotate = (float) (rotation + Math.PI+0.06f);
            System.out.println("Critical code in BombTower triggered. Check for interferences!");
        }

     //   System.out.println("Angle: " + currentAngle + " || wanted: " +rotation);
        // float toRotate = (float) MathUtils.clamp(rotation-currentAngle, -rotationSpeed, rotationSpeed);
        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, toRotate * -1);
        currentAngle = currentAngle + toRotate;
        return notClamped;
    }


}
