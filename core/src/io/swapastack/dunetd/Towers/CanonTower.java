package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Enemys.Enemy;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public class CanonTower extends Tower {
    float currentAngle = (float) (Math.PI / 2);
    float rotationSpeed = (float) Math.PI / 256;

    public CanonTower() {
        graphics = "weapon_cannon.glb";
        type = 2;
        range = 2;
    }

    @Override
    public void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers, float x, float y, float z) {
        this.scene = createScene(sceneAssetHashMap);
        sceneManager.addScene(scene);
        this.setToTranslation(x, y, z);
        towers[Math.round(x)][Math.round(z)] = this;
    }

    @Override
    public void fire(ArrayList<Enemy> enemiesList) {
        for (Enemy enemy : enemiesList) {
            if (isInRange(enemy.getCoords())) {
                boolean fire = rotateTowardsVectorSmooth(enemy.getCoords());
                //Beeing able to shoot
                    if(fire) {
                     //   System.out.println("fire");
                    }
                return;
                // }
            }
        }
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