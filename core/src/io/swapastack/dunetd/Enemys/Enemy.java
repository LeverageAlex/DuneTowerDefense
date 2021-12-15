package io.swapastack.dunetd.Enemys;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Towers.IterableOverMap;
import io.swapastack.dunetd.Towers.MapIterable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Enemy {
    protected int level;
    public String name;
    protected int storedSpice;
    protected int highscorePoints;
    protected int damageOnEndPortal;
    protected float health;
    protected float movementSpeed;
    public String graphics;
    protected int type;
    protected Scene scene;
    protected AnimationController animController;
    protected int[] target;
    protected int numberOnArrayField = 0;
    protected int[][] shortestPath;
    float currentAngle;
    float rotationSpeed;


    public abstract void destroyDamage();

    public abstract int arrivedAtEndPortal();

    public abstract void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, int[][] shortestPath, float x, float y, float z);

    public abstract void setWalkAnimation();

    //  public abstract void gainDamage();


    public Scene createScene(HashMap<String, SceneAsset> m) {
        scene = new Scene(m.get(graphics).scene);
        return scene;
    }

    public Matrix4 setToTranslation(Vector3 vector) {
       // Vector3 scaler = scene.modelInstance.transform.getScale(new Vector3());
        return scene.modelInstance.transform.setTranslation(vector);
    }

    public Matrix4 setToTranslation(float x, float y, float z) {
       // Vector3 scaler = scene.modelInstance.transform.getScale(new Vector3());
     //   Quaternion rotation = scene.modelInstance.transform.getRotation(new Quaternion());
        return scene.modelInstance.transform.setTranslation(x, y, z)/*.scale(scaler.x, scaler.y, scaler.z)/*.rotate(rotation)*/;
    }

    public void setAnimation(String id, int loopCnt) {
        getAnimationController().setAnimation(id, loopCnt);
    }

    public Scene getScene() {
        return scene;
    }

    public void move(float x, float y, float z) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
       // Vector3 scale = scene.modelInstance.transform.getScale(new Vector3());
        this.scene.modelInstance.transform.setTranslation(pos.x + x, pos.y + y, pos.z + z)/*.scale(scale.x, scale.y, scale.z)*/;
    }

    public AnimationController getAnimationController() {
        if (animController == null) {
            animController = new AnimationController(scene.modelInstance);
        }
        return animController;
    }

    public void setPos(Vector3 pos) {
        this.scene.modelInstance.transform.setTranslation(pos);
    }

    public void printCoords() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        System.out.printf(this.getClass().getName() + " grid: x: %f, y:%f, z:%f\n", pos.x, pos.y, pos.z);
    }

    public int getType() {
        return type;
    }

    public Vector3 getCoords() {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        return pos;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void removeEnemy(SceneManager sceneManager, ArrayList<Enemy> attackers) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        attackers.remove(this);
        sceneManager.removeScene(this.getScene());
    }



    /**
     * Walks towards the in 'target' specified point.
     * @return whether there are steps left to the target (false) or it has been arrived (true)
     */
    public boolean walk() {
        Vector3 coords = getCoords();
        if(target[1] == coords.z) {
            //walkOnX
            if(((coords.x < (float)target[0]) && ((coords.x + movementSpeed) < (float)target[0]) ) ) {
                move(movementSpeed, 0, 0);
                return false;
            }
            else if((coords.x > (float)target[0]) && ((coords.x + movementSpeed) > (float)target[0])) {
                move(-movementSpeed, 0, 0);
                return false;
            }
            else /*if(((float)coords.x + movementSpeed) >= (float)target[0])*/ {
                float toMove = movementSpeed - (((float)coords.x + movementSpeed) - (float)target[0]);
                move(toMove, 0.f, 0.f);
               // Vector3 vec = getCoords();
                //setToTranslation((float)target[0], vec.y, vec.z);
                return true;
            }
        }
        //if !walkTowardsX
        //walkOnZ
        else {
            if( (coords.z < (float)target[1] && ((coords.z + movementSpeed) < (float)target[1] )) ) {
                move(0, 0, movementSpeed);
                return false;
            }
            else if((coords.z > (float)target[1] && ((coords.z + movementSpeed) > (float)target[1] ))) {
                move(0, 0, -movementSpeed);
                return false;
            }
            else /*if(((float)coords.z + movementSpeed) >= (float)target[1]) */{
                float toMove = movementSpeed - (((float)coords.z + movementSpeed) - (float)target[1]);
               // Vector3 vec = getCoords();
                //setToTranslation(vec.x, vec.y, (float)target[1]);
                move(0.f, 0.f, toMove);
                return true;
            }
        }
    }


    /**
     * Controls the walk from startPortal to endPortal. On every step towards the target, it checks if a rotation of
     * the model is needed and then sets the next interim goal
     */
    public boolean movingAlongShortestPath() {
        //checks if Unit is on a field and ready to get next target point.
        //if(target == null) {
            target = shortestPath[numberOnArrayField];
        //}
        if(walk() ) {
            //If there are still points to move towards, rotate towards it and if successfull, activate the walk() function again
            if (numberOnArrayField + 1 < shortestPath.length) {
                if (rotateTowardsPointSmooth(shortestPath[numberOnArrayField + 1])) {
                    numberOnArrayField++;
                }
                //target = shortestPath[numberOnArrayField];

            } else if (numberOnArrayField + 1 >= shortestPath.length) {
                return true;
                //Endportal arrived
                //System.out.println("Arrived @ endportal!");
            }
        }
        return false;
    }
    /**
     * rotates smoothly towards a given point.  Returns true if the rotation is slower than max rotation speed
     * @param pointToRotate
     * @return if rotation is slower than maxRotationSpeed
     */
    @SuppressWarnings("DuplicatedCode")
    public boolean rotateTowardsPointSmooth(int[] pointToRotate) {
        Vector3 enemyCoords = this.scene.modelInstance.transform.getTranslation(new Vector3());
        float rotation = (float) Math.atan2((enemyCoords.z - ((float) pointToRotate[1])), (enemyCoords.x - ((float) pointToRotate[0])));
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

        //activate if necessary
        /*if(currentAngle - 0.05f < -Math.PI && rotation + 0.05f > Math.PI) {
            toRotate = (float) (rotation + Math.PI);
            System.out.println("Critical code in Enemy triggered. Check for interferences!");
        }*/

        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, toRotate * -1);
        currentAngle = currentAngle + toRotate;
        return notClamped;
    }

    public void rotateTowardsVectorInstantly(int[] pointToRotate) {
        Vector3 enemyCoords = this.scene.modelInstance.transform.getTranslation(new Vector3());
        float rotation = (float) Math.atan2((enemyCoords.z - ((float) pointToRotate[1])), (enemyCoords.x - ((float) pointToRotate[0])));
        this.getScene().modelInstance.transform.rotateRad(0.f, 1.F, 0.F, (rotation-currentAngle)*-1);
        currentAngle = rotation;
    }

    public int getHighscorePoints() {
        return highscorePoints;
    }

    public int getStoredSpice() {
        return storedSpice;
    }
}
