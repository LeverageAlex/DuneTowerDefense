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

public abstract class Tower implements MapIterable {
    protected int type;
    protected int x, y;
    protected double range;
    public String graphics;
    protected Scene scene;
    protected AnimationController animController;
    private int length = Integer.MAX_VALUE, color = 130;
    protected GameScreen gameScreen;

    public abstract void init(SceneManager sceneManager, HashMap<String, SceneAsset> sceneAssetHashMap, MapIterable[][] towers,float x, float y, float z);
    public abstract void fire(ArrayList<Enemy> enemiesList);





    public Scene createScene(HashMap<String, SceneAsset> m) {
        this.scene = new Scene(m.get(graphics).scene);
        return scene;
    }



    public Matrix4 setToTranslation(Vector3 vector) {
        return scene.modelInstance.transform.setToTranslation(vector);
    }

    public Matrix4 setToTranslation(float x, float y, float z) {
        return scene.modelInstance.transform.setToTranslation(x, y, z);
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

    //Checks whether tile is free or a way to the Endportal continues to exist
    public static boolean isEligibleToPlace(MapIterable[][] mapTowers , GameScreen gameScreen, int x, int z) {
        //No Tower Placed
        if(/*map[x][z] == null || */ /*mapTowers[x][z].getPathColor() == 0 */ mapTowers[x][z] instanceof IterableOverMap) {
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

    public boolean isInRange(Vector3 pos) {
        Vector3 towerPos = getScene().modelInstance.transform.getTranslation(new Vector3());
        return ((pos.x-towerPos.x)*(pos.x-towerPos.x) + (pos.z-towerPos.z) <= range*range );
    }

    public void removeTower(SceneManager sceneManager, MapIterable[][] mapTowers) {
        Vector3 pos = scene.modelInstance.transform.getTranslation(new Vector3());
        mapTowers[Math.round(pos.x)][Math.round(pos.z)] = new IterableOverMap();
        sceneManager.removeScene(this.getScene());

    }
}
