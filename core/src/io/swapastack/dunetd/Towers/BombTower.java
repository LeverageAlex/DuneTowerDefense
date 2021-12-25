package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.ConfigMgr;
import io.swapastack.dunetd.Enemys.Bullet;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.*;

public class BombTower extends Tower{
    float currentAngle = (float) (Math.PI/2);
    float rotationSpeed = (float) Math.PI/256;
    boolean readyToShoot = true;
    Timer towerTimer = new Timer();
   /* TimerTask task = new TimerTask() {
        @Override
        public void run() {
            readyToShoot = true;
        }};*/


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
        towers[Math.round(x)][Math.round(z)] = this;
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
                    if(readyToShoot) {
                        //canonTowCounter = 0;
                  //      System.out.println("bombTower shot");
                        readyToShoot = false;
                        //   currentTarget = enemy;
                        towerTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                readyToShoot = true;
                            }}, Math.round(ConfigMgr.bombTowIntervall*1000));
                        return enemy;
                    }
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
            toRotate = (float) (rotation + Math.PI + 0.07f );
            System.out.println("Critical code 1 in BombTower triggered. Check for interferences!");
        }
        //This might be odd
        if(currentAngle + 0.05f > Math.PI && rotation - 0.05f < -Math.PI) {
            toRotate = (float) (rotation - Math.PI - 0.07f);
            System.out.println("Critical code 2 in BombTower triggered. Check for interferences!");
        }

     //   System.out.println("Angle: " + currentAngle + " || wanted: " +rotation);
        // float toRotate = (float) MathUtils.clamp(rotation-currentAngle, -rotationSpeed, rotationSpeed);
        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, toRotate * -1);
        currentAngle = currentAngle + toRotate;
        return notClamped;
    }

    public void createBullet(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, List<Enemy> attackers, ArrayList<Bullet> bullets, float startX, float startY, float startZ, Enemy enemy) {
        Vector3 vec = getScene().modelInstance.transform.getTranslation(new Vector3());

    }


}
