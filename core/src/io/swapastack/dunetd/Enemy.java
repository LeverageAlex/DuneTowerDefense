package io.swapastack.dunetd;

import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Enemy {
   /* protected float x = 0.005f;
   protected float y = 0.005f;
    protected float z = 0.005f;*/
    protected int level;
    public String name;
    protected int storedSpice;
    protected int health;
    protected double movementSpeed;
    public String graphics;
    public Scene scene;

    public abstract void  move(float x, float y, float z);
    public abstract void destroyDamage();
    public abstract void onKill();
  //  public abstract void gainDamage();

}
