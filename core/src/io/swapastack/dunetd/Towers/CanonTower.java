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

public class CanonTower extends Tower {
    float currentAngle = (float) (Math.PI / 2);
    float rotationSpeed = (float) Math.PI / 256;
    float canonTowCounter;
    //Enemy currentTarget;
    Timer towerTimer = new Timer();
    Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
            readyToShoot = true;
        }};
    boolean readyToShoot = true;

    public CanonTower(GameScreen screen) {
        graphics = "weapon_cannon.glb";
        type = 2;
        range = ConfigMgr.canonTowRange;
        gameScreen = screen;
        this.cost = ConfigMgr.canonTowCost;
        towerDmg = ConfigMgr.canonTowDmg;

    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, ArrayList<Tower> towerList, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setTranslation(x, y, z);
        towers[Math.round(x)][Math.round(z)] = this;
        towerList.add(this);
        this.towerList = towerList;
    }

    @Override
    public Enemy fire(ArrayList<Enemy> enemiesList) {
        canonTowCounter++;
        for (Enemy enemy : enemiesList) {
            if (isInRange(enemy.getCoords())) {
                boolean fire = rotateTowardsVectorSmooth(enemy.getCoords());
                //Beeing able to shoot
                    if(fire) {
                        if(readyToShoot) {
                            //canonTowCounter = 0;
                            System.out.println("canonTower shot");
                            enemy.gainDamage(towerDmg);
                            readyToShoot = false;
                            //   currentTarget = enemy;
                            towerTimer.scheduleTask(task, ConfigMgr.canonTowIntervall);
                            return enemy;
                        }



                     //   System.out.println("fire");
                    }
                return null;
                // }
            }
        }
       // towerTimer.clear();
       // currentTarget = null;
        return null;
    }

    /**
     * rotates smoothly towards a given point.  Returns true if the rotation is slower than max rotation speed
     *
     * @param pointToRotate
     * @return if rotation is slower than maxRotationSpeed
     */
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
            toRotate = (float) (rotation + Math.PI -0.06f);
            System.out.println("Critical code in CanonTower triggered. Check for interferences!");
        }
        //This might be odd
        if(currentAngle + 0.05f > Math.PI && rotation - 0.05f < -Math.PI) {
            toRotate = (float) (rotation + Math.PI+0.06f);
            System.out.println("Critical code in CanonTower triggered. Check for interferences!");
        }

        // float toRotate = (float) MathUtils.clamp(rotation-currentAngle, -rotationSpeed, rotationSpeed);
        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, toRotate * -1);
        currentAngle = currentAngle + toRotate;
        return notClamped;
    }



    public void rotateTowardsVectorInstantly(Vector3 pointToRotate) {
        Vector3 towerCoords = this.scene.modelInstance.transform.getTranslation(new Vector3());
        float rotation = (float) Math.atan2((towerCoords.z - pointToRotate.z), (towerCoords.x - pointToRotate.x));
        int x = rotation-currentAngle < 0 ? -1 : 1;
        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, (rotation-currentAngle)*-1);
        currentAngle = rotation;
    }

}
