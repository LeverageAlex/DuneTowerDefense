package io.swapastack.dunetd.Towers;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public abstract class Tower implements MapIterable {
    protected int type;
    protected int x, y;
    protected double range;
    public String graphics;
    protected Scene scene;
    protected AnimationController animController;
    private int length = Integer.MAX_VALUE, color = 130;
    protected GameScreen gameScreen;
    protected int cost;
    protected ArrayList<Tower> towerList;
    protected float towerDmg;

    public abstract void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers,ArrayList<Tower> towerList, float x, float y, float z);
    public abstract Enemy fire(ArrayList<Enemy> enemiesList);





    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }



    public Matrix4 setTranslation(Vector3 vector) {
        return scene.modelInstance.transform.setTranslation(vector);
    }

    public Matrix4 setTranslation(float x, float y, float z) {
        return scene.modelInstance.transform.setTranslation(x, y, z);
    }

    public void setAnimation(String id, int loopCnt) {
        getAnimationController().setAnimation(id, loopCnt);
    }

    public Scene getScene() {
        return scene;
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

    public void setPathLength(int length) {
        this.length = length;
    }

    public int getPathLength() {
        return length;
    }

    @Override
    public int getPathColor() {
        return color;
    }

    public void setPathColor(int color) {
        this.color = color;
    }

    /**
     * Checks whether tile is free or a way to the Endportal continues to exist
     */
    public static boolean isEligibleToPlace(MapIterable[][] mapTowers , GameScreen gameScreen, int x, int z) {
        //check for no Tower Placed
        if(/*map[x][z] == null || */ /*mapTowers[x][z].getPathColor() == 0 */ mapTowers[x][z] instanceof IterableOverMap && !(mapTowers[x][z] instanceof Startportal) && !(mapTowers[x][z] instanceof Endportal)) {
            //Check if a Path is after placing still available
            int previousColor = mapTowers[x][z].getPathColor();
            mapTowers[x][z].setPathColor(130);
            if(gameScreen.pathFinder() == null) {
                mapTowers[x][z].setPathColor(previousColor);
                return false;
            }
            else {
                mapTowers[x][z].setPathColor(previousColor);
                return true;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Pythagoras to see if point is in range of the Range circle
     * @param pos
     * @return
     */
    public boolean isInRange(Vector3 pos) {
        Vector3 towerPos = getScene().modelInstance.transform.getTranslation(new Vector3());
        return isInRange(pos, towerPos);
    }

    //How to split up for testing
    public boolean isInRange(Vector3 pos, Vector3 towerPos) {
        return ((pos.x-towerPos.x)*(pos.x-towerPos.x) + (pos.z-towerPos.z)*(pos.z-towerPos.z) <= range*range );
    }

    /**
     * Deletes the Scene of tower from sceneManger and removes its references in mapTowers
     * @param sceneManager
     * @param mapTowers
     */
    public void removeTower(SceneManager sceneManager, MapIterable[][] mapTowers) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        mapTowers[Math.round(pos.x)][Math.round(pos.z)] = new IterableOverMap();
        sceneManager.removeScene(this.getScene());
        this.towerList.remove(this);

    }


    public int getCost() {
        return cost;
    }

}
