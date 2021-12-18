package io.swapastack.dunetd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.swapastack.dunetd.Enemys.*;
import io.swapastack.dunetd.Towers.*;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.*;

/**
 * The GameScreen class.
 *
 * @author Dennis Jehle
 */
public class GameScreen implements Screen {

    private final DuneTD parent;
    private Infantry infantry;
    private BossUnit bossUnit;
    private HarvestMachine harvestMachine;

    private Infantry infantryTwo;

    private ArrayList<Enemy> attackers;

    //Towers
    private CanonTower canonTower;
    private BombTower bombTower;
    private SonicTower sonicTower;

    // GDX GLTF
    private SceneManager sceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    // libGDX
    private PerspectiveCamera camera;
    private CameraInputController cameraInputController;

    // 3D models
    String basePath = "kenney_gltf/";
    String kenneyAssetsFile = "kenney_assets.txt";
    String[] kenneyModels;
    HashMap<String, SceneAsset> sceneAssetHashMap;

    // Grid Specifications
    private int rows = 5;
    private int cols = 5;

    // SpaiR/imgui-java
    public ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    public ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    long windowHandle;

    //Custom
    MouseForCollision mouseForCollision;
    Scene beam;
    Scene[][] mapTiles;
    BoundingBox[][] mapBoxes;
    Player player;
    Startportal startPortal;
    Endportal endPortal;
    Wave wave;
    MapIterable[][] mapTowers;
    private int[][] shortestPath;
    Stage towerBuilding, HUD, placeKnockerHUD;
    Skin skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
    int phase = 0;
    HUD_Drawer[] huds;
    Label health;
    Label spiceAmount;
    Label hsAmount;
    Label waveToBeat;
    int selected = -1;
    Sandworm sand;
    ArrayList<Tower> towers = new ArrayList<>();
    private int waveCounter = 0;


    public GameScreen(DuneTD parent) {
        this.parent = parent;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     * @author Dennis Jehle
     */
    @Override
    public void show() {
        ConfigMgr.readCfg();
        this.rows = ConfigMgr.rows;
        this.cols = ConfigMgr.cols;
        // SpaiR/imgui-java
        ImGui.createContext();
        windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 120");

        // GDX GLTF - Scene Manager
        sceneManager = new SceneManager(64);

        // GDX GLTF - Light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // GDX GLTF - Image Based Lighting
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // GDX GLTF - This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        // GDX GLTF - Cubemaps
        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // GDX GLTF - Skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        // Camera
        camera = new PerspectiveCamera();
        camera.position.set(10.0f, 10.0f, 10.0f);
        camera.lookAt(Vector3.Zero);
        sceneManager.setCamera(camera);

        // Camera Input Controller
        cameraInputController = new CameraInputController(camera);

        // Set Input Processor
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(cameraInputController);
        mouseForCollision = new MouseForCollision(this);
        inputMultiplexer.addProcessor(mouseForCollision);
        // TODO: add further input processors if needed
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Load all 3D models listed in kenney_assets.txt file in blocking mode
        FileHandle assetsHandle = Gdx.files.internal("kenney_assets.txt");
        String fileContent = assetsHandle.readString();
        kenneyModels = fileContent.split("\\r?\\n");
        for (int i = 0; i < kenneyModels.length; i++) {
            parent.assetManager.load(basePath + kenneyModels[i], SceneAsset.class);
        }
        // Load example enemy models
        parent.assetManager.load("faceted_character/scene.gltf", SceneAsset.class);
        parent.assetManager.load("cute_cyborg/scene.gltf", SceneAsset.class);
        parent.assetManager.load("spaceship_orion/scene.gltf", SceneAsset.class);
        parent.assetManager.load("bullet_9_mm/scene.gltf", SceneAsset.class);
        parent.assetManager.load("sandworm/scene.gltf", SceneAsset.class);
        DuneTD.assetManager.finishLoading();

        // Create scene assets for all loaded models
        sceneAssetHashMap = new HashMap<>();
        for (int i = 0; i < kenneyModels.length; i++) {
            SceneAsset sceneAsset = parent.assetManager.get(basePath + kenneyModels[i], SceneAsset.class);
            sceneAssetHashMap.put(kenneyModels[i], sceneAsset);
        }
        SceneAsset bossCharacter = parent.assetManager.get("faceted_character/scene.gltf");
        sceneAssetHashMap.put("faceted_character/scene.gltf", bossCharacter);
        SceneAsset enemyCharacter = parent.assetManager.get("cute_cyborg/scene.gltf");
        sceneAssetHashMap.put("cute_cyborg/scene.gltf", enemyCharacter);
        SceneAsset harvesterCharacter = parent.assetManager.get("spaceship_orion/scene.gltf");
        sceneAssetHashMap.put("spaceship_orion/scene.gltf", harvesterCharacter);
        SceneAsset bullet9mm = parent.assetManager.get("bullet_9_mm/scene.gltf");
        sceneAssetHashMap.put("bullet_9_mm/scene.gltf", bullet9mm);
        SceneAsset wormChar = parent.assetManager.get("sandworm/scene.gltf");
        sceneAssetHashMap.put("sandworm/scene.gltf", wormChar);

      //   createMapExample(sceneManager);
        createMap(sceneManager);

    }

    /**
     * Called when the screen should render itself.
     *
     * @author Dennis Jehle
     * @param delta - The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // OpenGL - clear color and depth buffer
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        for (Enemy enemy: attackers) {
            enemy.getAnimationController().update(delta);
        }

        // SpaiR/imgui-java
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        // GDX GLTF - update scene manager and render scene
        sceneManager.update(delta);
        sceneManager.render();


        ImGui.begin("Performance", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.text(String.format(Locale.US,"deltaTime: %1.6f", delta));
        ImGui.end();

        ImGui.begin("Menu", ImGuiWindowFlags.AlwaysAutoResize);
        if (ImGui.button("Back to menu")) {
            parent.changeScreen(ScreenEnum.MENU);
        }


        ImGui.text("Mouse Map: " + getClickOnField().toString());
        Vector3 vec = getClickOnField();

            ImGui.text("Mouse Map Rounded X: " + Math.round(vec.x) + ", Y: " + vec.y + ", Z: " + Math.round(vec.z));
        ImGui.end();

        if(sand != null && sand.moveWorm()) {
            sand.removeWorm(sceneManager);
            sand = null;
        }

        updateEnemysMovement();

        for (Tower t : towers) {
            t.fire(attackers);
        }

        // SpaiR/imgui-java
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        health.setText(     "Health:      " + player.getHealth());
        spiceAmount.setText("Spice:       " + player.getSpice());
        hsAmount.setText(   "Highscore:   " + player.getHighscore());

        HUD.act(delta);
        HUD.draw();

        //TowerBuilderMenu
        if(phase == 0 || phase == 3) {
            towerBuilding.act(delta);
            towerBuilding.draw();
        }

        if(phase == 1 && !wave.isStarted()) {
            wave.startWave();
        }

        if(Knocker.isAvailable()) {
            placeKnockerHUD.act(delta);
            placeKnockerHUD.draw();
        }
        //To make this work, every Enemy needs to be part of a wave
        if(wave.waveKilled()) {
            onWaveSwap(); System.out.println("waveSwap");
        }

    }

    @Override
    public void resize(int width, int height) {
        // GDX GLTF - update the viewport
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void pause() {
        // TODO: implement pause logic if needed
    }

    @Override
    public void resume() {
        // TODO: implement resume logic if needed
    }

    @Override
    public void hide() {
        // TODO: implement hide logic if needed
    }


    private void createMap(SceneManager sceneManager) {

        Vector3 groundTileDimensions = new Vector3();
        mapTiles = new Scene[rows][cols];
        mapBoxes = new BoundingBox[rows][cols];
        attackers = new ArrayList<Enemy>();
        mapTowers = new MapIterable[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
              //  if(mapTowers[i][j] == null) {
                    mapTowers[i][j] = new IterableOverMap();
              //  }
            }
        }

        // Simple way to generate the example map
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                // Create a new Scene object from the tile_dirt gltf model
                Scene gridTile = new Scene(sceneAssetHashMap.get("tile_dirt.glb").scene);
                // Create a new BoundingBox, this is useful to check collisions or to get the model dimensions
                BoundingBox boundingBox = new BoundingBox();

                // Calculate the BoundingBox from the given ModelInstance
                gridTile.modelInstance.calculateBoundingBox(boundingBox);
                // Create Vector3 to store the ModelInstance dimensions
                Vector3 modelDimensions = new Vector3();
                // Read the ModelInstance BoundingBox dimensions
                boundingBox.getDimensions(modelDimensions);
                // TODO: refactor this if needed, e.g. if ground tiles are not all the same size
                groundTileDimensions.set(modelDimensions);
                // Set the ModelInstance to the respective row and cell of the map
                gridTile.modelInstance.transform.setToTranslation(k * modelDimensions.x, 0.0f, i * modelDimensions.z);
                //  gridTile.modelInstance.transform.setToTranslation( 10* modelDimensions.x, 0.0f,  5*modelDimensions.z);
                // Add the Scene object to the SceneManager for rendering
                sceneManager.addScene(gridTile);
                mapTiles[i][k] = gridTile;
                mapBoxes[i][k] = boundingBox;

                //   System.out.printf("Breite: %f \n", boundingBox.	getHeight() );
                // it could be useful to store the Scene object reference outside this method
            }
        }
        player = new Player();
        // place example sonicTower
        startPortal = new Startportal(sceneManager, sceneAssetHashMap, mapTowers,ConfigMgr.stPortalX, groundTileDimensions.y, ConfigMgr.stPortalZ);
        endPortal = new Endportal(sceneManager, sceneAssetHashMap, mapTowers,ConfigMgr.endPortalX, groundTileDimensions.y, ConfigMgr.endPortalZ);

       /* if(Tower.isEligibleToPlace(mapTowers, this,Math.round(0.0f), Math.round(0.0f))) {
            sonicTower = new SonicTower(this);
            sonicTower.init(sceneManager, sceneAssetHashMap, mapTowers, towers,0.0f, groundTileDimensions.y, 1.0f);
        }


        // place example canonTower
        canonTower = new CanonTower(this);
        canonTower.init(sceneManager, sceneAssetHashMap,mapTowers,towers,2.0f, groundTileDimensions.y, 1.0f);


        // place example bombTower
        bombTower = new BombTower(this);
        bombTower.init(sceneManager, sceneAssetHashMap, mapTowers,towers,1.0f, groundTileDimensions.y, 2.0f);*/

      /*  if(Tower.isEligibleToPlace(mapTowers, this,Math.round(1.0f), Math.round(0.0f))) {
            CanonTower canonTower2 = new CanonTower(this);
            canonTower2.init(sceneManager, sceneAssetHashMap,mapTowers,1.0f, groundTileDimensions.y, 0.0f);
        }
        else {
            System.out.println("Tower isn't allowed to be place here!");
        }*/

        pathFinder();



        // place enemy character
    /*    infantry = new Infantry();
  //      infantry.init(sceneManager, sceneAssetHashMap, shortestPath,2.0f, groundTileDimensions.y, 2.0f);
        infantry.init(sceneManager, sceneAssetHashMap, shortestPath,startPortal.getX(), groundTileDimensions.y, startPortal.getZ());
        infantry.getScene().modelInstance.calculateTransforms();
        infantry.setAnimation("RUN", -1);

        // place boss Unit character
        bossUnit = new BossUnit();
        bossUnit.init(sceneManager, sceneAssetHashMap, shortestPath,0.0f, groundTileDimensions.y, 2.0f);
        bossUnit.setAnimation("Armature|Run", -1);

        // place spaceship character
        harvestMachine = new HarvestMachine();
        harvestMachine.init(sceneManager, sceneAssetHashMap, shortestPath, startPortal.getX(), startPortal.getY()+0.25f, startPortal.getZ());
        harvestMachine.setAnimation("Action", -1);


        infantryTwo = new Infantry();
        infantryTwo.init(sceneManager, sceneAssetHashMap, shortestPath,2.0f, 0.25f, 4.0f);
        infantryTwo.setAnimation("RIDING", -1);*/

        // Bullet9mm bullet = new Bullet9mm();
        // bullet.init(sceneManager, sceneAssetHashMap, -1.F, 0.F, -1.F);


        /*attackers.add(bossUnit);
        attackers.add(harvestMachine);
        attackers.add(infantryTwo);
        attackers.add(infantry);*/

        wave = new Wave(this, player);
        wave.initEnemies(ConfigMgr.waves[waveCounter]);
      /*  LinkedList<Enemy> liste = new LinkedList<>();
        BossUnit shawn = new BossUnit();
        Infantry bob = new Infantry();
        liste.add(bob);
        liste.add(null);
        liste.add(null);
        liste.add(shawn);*/

       // wave.initEnemies(liste);
      //  wave.startWave();
       // shawn.createScene(sceneAssetHashMap);
      //  bob.createScene(sceneAssetHashMap);

        beam = new Scene(sceneAssetHashMap.get("detail_crystal.glb").scene);
        resetBeamPos();
        sceneManager.addScene(beam);

       // Knocker knocker = new Knocker(this);
       // knocker.init(sceneManager, sceneAssetHashMap, mapTowers, 3.f, 0, 0);
      //  knocker = new Knocker(this);
       // knocker.init(sceneManager, sceneAssetHashMap, mapTowers, 2.f, 0, 0);

       // sceneManager.addScene(sand.getScene());

        towerBuilding = new Stage();

        huds = new HUD_Drawer[3];
        huds[0] = new HUD_Drawer(this,skin, "hud/bombTower.png", "hud/bombTower_selected.png", 0,ConfigMgr.bombTowCost,Gdx.graphics.getWidth()/2-166, 20, 100, 100);;
        huds[1] = new HUD_Drawer(this, skin, "hud/canonTower.png", "hud/canonTower_selected.png", 1,ConfigMgr.canonTowCost,Gdx.graphics.getWidth()/2 - 64, 20, 100, 100);
        huds[2] = new HUD_Drawer(this, skin, "hud/sonicTower.png", "hud/sonicTower_selected.png", 2,ConfigMgr.sonicTowCost,Gdx.graphics.getWidth()/2 + 38, 20, 100, 100);

       for (HUD_Drawer h: huds) {
            towerBuilding.addActor(h);
        }

        HUD = new Stage();
        health = new Label("Health:    " + player.getHealth(),skin);
        health.setPosition(Gdx.graphics.getWidth() - 180, Gdx.graphics.getHeight() - 30);
        spiceAmount = new Label("Spice:     " + player.getSpice(),skin);
        spiceAmount.setPosition(Gdx.graphics.getWidth() - 180, Gdx.graphics.getHeight() - 60);
        hsAmount = new Label(   "Highscore: " + player.getHighscore(),skin);
        hsAmount.setPosition(Gdx.graphics.getWidth() - 180, Gdx.graphics.getHeight() - 90);
        HUD.addActor(health);
        HUD.addActor(spiceAmount);
        HUD.addActor(hsAmount);

        placeKnockerHUD = new Stage();
        HUD_Drawer placeKnocker = new HUD_Drawer(this, skin, "hud/sandWorm.png", "hud/sandWorm_selected.png", 3,"FREE",Gdx.graphics.getWidth() - 138, 20, 100, 100);
        placeKnockerHUD.addActor(placeKnocker);

    }


    @Override
    public void dispose() {
        // GDX GLTF - dispose resources
        sceneManager.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();

        brdfLUT.dispose();
        skybox.dispose();
    }

    /**
     * Places our Beam at the position of the mouse on the gameMap
     */
    public void resetBeamPos() {
        beam.modelInstance.transform.setToTranslation(getClickOnField());
     }

    /** Converts MouseClick to Coordinate on the x-z plane (y=0)
     * lambda of our direction-Vector which is needed to set y = 0 (x-z plane) where the gameMap is allocated at
     * @return
     */
    public Vector3 getClickOnField() {
        Vector3 camPos = new Vector3(camera.position);
        Vector3 clickDir = calculateClickDirection();
        float lambda = new Vector3(camera.position).y / clickDir.y;

        camPos = new Vector3(camPos.x - clickDir.x*lambda, 0.f, camPos.z - clickDir.z*lambda);
        return camPos;
    }

    /**
     * Calculates the directionVector based on the MousePosition to the map
     * @return directionVector with unit length (1)
     */
    private Vector3 calculateClickDirection() {
        Vector3 vecMousePos = new Vector3(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.f)));
        return vecMousePos.sub(camera.position).setLength(1f);
    }

    /**
     *
     * @param direction (a Vector3 who points towards a direction)
     * @return lambda of our direction-Vector which is needed to set y = 0 (x-z plane) where the gameMap is allocated at
     */
    private float findYCutLambda(Vector3 direction) {
        Vector3 v = new Vector3(camera.position);
        return v.y /direction.y;

    }
    public boolean inRangeofField(Vector3 vec) {
        int x = Math.round(vec.x);
     //   int y = 0;
        int z = Math.round(vec.z);
        if(x >= 0 && x < rows && z >= 0 && z < cols) {
            return true;
        }
        return false;
    }

    public int[][] getShortestPath() {
        return shortestPath;
    }

    /**
     * Modified Dijkstras Algorithm
     * Chooses always the shortest Path. If there are more than one shortest Path it choses by the order: x+1, x-1, z-1, z+1
     * @return returns the shortest Path-Coordinates as two-Dimensional Integer-Array[fieldNbr][choose x or z]
     */
    public int[][] pathFinder() {
        //mapTowers
        //Reset all params


        //Setup startPortal as start-Vertice
        startPortal.setPathLength(0);
        ArrayList<IterableOverMap> q = new ArrayList<IterableOverMap>();
        startPortal.setPathColor(1);
        q.add(startPortal);
        while(!q.isEmpty()) {
            IterableOverMap vertice = q.get(0);
            int[] coords = vertice.getPosition();
            int currentX = coords[0];
            int currentZ = coords[1];
            if(inRangeofField(new Vector3(currentX+1, 0.F, currentZ)) && mapTowers[currentX+1][currentZ].getPathColor() == 0) {
                mapTowers[currentX+1][currentZ].setPathColor(1);
                mapTowers[currentX+1][currentZ].setPathLength(vertice.getPathLength()+1);
                ((IterableOverMap)mapTowers[currentX+1][currentZ]).setVorgaenger(currentX, currentZ);
                ((IterableOverMap)mapTowers[currentX+1][currentZ]).setPosition(currentX+1, currentZ);
                q.add((IterableOverMap) mapTowers[currentX+1][currentZ]);
            }
            if(inRangeofField(new Vector3(currentX-1, 0.F, currentZ)) && mapTowers[currentX-1][currentZ].getPathColor() == 0) {
                mapTowers[currentX-1][currentZ].setPathColor(1);
                mapTowers[currentX-1][currentZ].setPathLength(vertice.getPathLength()+1);
                ((IterableOverMap)mapTowers[currentX-1][currentZ]).setVorgaenger(currentX, currentZ);
                ((IterableOverMap)mapTowers[currentX-1][currentZ]).setPosition(currentX-1, currentZ);
                q.add((IterableOverMap) mapTowers[currentX-1][currentZ]);
            }
            if(inRangeofField(new Vector3(currentX, 0.F, currentZ-1)) && mapTowers[currentX][currentZ-1].getPathColor() == 0) {
                mapTowers[currentX][currentZ-1].setPathColor(1);
                mapTowers[currentX][currentZ-1].setPathLength(vertice.getPathLength()+1);
                ((IterableOverMap)mapTowers[currentX][currentZ-1]).setVorgaenger(currentX, currentZ);
                ((IterableOverMap)mapTowers[currentX][currentZ-1]).setPosition(currentX, currentZ-1);
                q.add((IterableOverMap) mapTowers[currentX][currentZ-1]);
            }
            if(inRangeofField(new Vector3(currentX, 0.F, currentZ+1)) && mapTowers[currentX][currentZ+1].getPathColor() == 0) {
                mapTowers[currentX][currentZ+1].setPathColor(1);
                mapTowers[currentX][currentZ+1].setPathLength(vertice.getPathLength()+1);
                ((IterableOverMap)mapTowers[currentX][currentZ+1]).setVorgaenger(currentX, currentZ);
                ((IterableOverMap)mapTowers[currentX][currentZ+1]).setPosition(currentX, currentZ+1);
                q.add((IterableOverMap) mapTowers[currentX][currentZ+1]);

            }
            q.remove(vertice);
        }


        int[][] walkWay;
        if(endPortal.getPathLength() != Integer.MAX_VALUE) {
            int[] pos = endPortal.getPosition();
            walkWay = new int[endPortal.getPathLength()+1][2];
            // IterableOverMap[] arr = new IterableOverMap[endPortal.getPathLength() + 1];

            for (int i = walkWay.length - 1; i >= 0; i--) {
              //  arr[i] = (IterableOverMap) mapTowers[pos[0]][pos[1]];
                walkWay[i] = ((IterableOverMap) mapTowers[pos[0]][pos[1]]).getPosition();
                pos = ((IterableOverMap) mapTowers[pos[0]][pos[1]]).getVorgaenger();
            }

         //   System.out.println(Arrays.toString(arr));
            System.out.println("Numbered Dijkstra: " + Arrays.deepToString(walkWay));
            shortestPath = walkWay;
        }
        else {
            System.out.println("Dijkstra found no way out!");
            walkWay = null;
        }

        //CleanUp
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(mapTowers[i][j].getPathColor() != 130) {
                    mapTowers[i][j].setPathLength(Integer.MAX_VALUE);
                    mapTowers[i][j].setPathColor(0);
                }
            }
        }
        return walkWay;

    }

    /**
     * Spawns the enemy on the startPortal. Used by the Wave-Class
     * @param enemy
     */
    public void initEnemy(Enemy enemy) {
        enemy.init(sceneManager, sceneAssetHashMap, shortestPath, startPortal.getX(), startPortal.getY(), startPortal.getZ());
        attackers.add(enemy);
        enemy.setWalkAnimation();
        //Let the enemy look in the direction of the shortestPath
        enemy.rotateTowardsVectorInstantly(shortestPath[1]);
    }


    /**
     * Makes each Attacker move if possible, if dead or arrived at endPortal it updates the according values
     */
    public void updateEnemysMovement() {
        for (int i = 0; i < attackers.size(); i++ ) {
            // System.out.println("stil availaible");
            if(attackers.get(i).isAlive()) {

                if( attackers.get(i).movingAlongShortestPath()) {
                    //EndPortal arrived
                    wave.arrivedAtEndPortal(attackers.get(i));
                    System.out.println(attackers.get(i).getClass().toString() + " arrived at EndPortal and got deleted");
                    attackers.get(i).removeEnemy(sceneManager, attackers);
                }
            }
            else {
                wave.enemyKilled(attackers.get(i));
                System.out.println(attackers.get(i).getClass().toString() + " went out of live and got deleted");
                attackers.get(i).removeEnemy(sceneManager, attackers);
            }
        }
    }

    public int getPhase() {
        return phase;
    }
    public int getColliding(int x, int y) {
        for (int i = 0; i < huds.length; i++) {
            if(huds[i].collision(x, y)) return i;
        }
        return -1;
    }
    public boolean getCollidingKnocker(int x, int y) {
        return Knocker.isAvailable() && ((HUD_Drawer)placeKnockerHUD.getActors().get(0)).collision(x, y);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    /**
     * Used by MouseProcessor to place Tower to a field where the mouse clicked, if the place is a valid field
     * @param t
     * @return
     */
    public boolean placeTower(Tower t) {
        Vector3 v = getClickOnField();
        if(inRangeofField(v) && Tower.isEligibleToPlace(mapTowers, this, Math.round(v.x), Math.round(v.z))) {
            t.init(sceneManager, sceneAssetHashMap, mapTowers, towers, Math.round(v.x), 0.02f, Math.round(v.z));
            return  true;
        }
        return false;
    }

    public boolean placeKnocker(Knocker k) {
        Vector3 v = getClickOnField();
        if(inRangeofField(v) && Knocker.knockerPlaceAble(Math.round(v.x), Math.round(v.z))) {
            k.init(sceneManager, sceneAssetHashMap, mapTowers, null, Math.round(v.x), 0.1f,Math.round(v.z));
            return true;
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public void launchSandwormAttack() {
        sand = new Sandworm(sceneManager, sceneAssetHashMap, attackers, mapTowers, this, rows, cols);
        sand.removeLane(mapTowers, attackers);
    }


    /**
     * Should be called whenever a Wave Swaps
     */
    public void onWaveSwap() {
        if(waveCounter+1 < ConfigMgr.waves.length) {
            Knocker.gambleAvailabilty();
            phase = 0;
            //wave = new Wave(this, player);
            wave.reset();
            wave.initEnemies(ConfigMgr.waves[waveCounter]);
            waveCounter++;
        }
        else {
            System.out.println("you've successfully won! --- Ending");
            System.exit(0);
        }
    }


}
