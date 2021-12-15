package io.swapastack.dunetd;


import java.io.*;
import java.nio.Buffer;

public class ConfigSaver {
    File file = new File("core/assets/config.ini");
    public static int rows, cols, stPortalX, stPortalY, stPortalZ, endPortalX, endPortalY, endPortalZ, playerHealth, playerStartSpice, canonTowCost, bombTowCost, sonicTowCost, bombTowDmg,
    canonTowDmg, sonicTowDmg, bossDmgOnEndPortal, infDmgOnEndPortal, harvDmgOnEndPortal, bossStoredSpice, infStoredSpice, harvStoredSpice;
    public static float canonTowRot, bombTowRot, canonTowRange, bombTowRange, sonicTowRange, bossMovSpeed, infMovSpeed, harvMovSpeed, bossHealth, harvHealth, infHealth, bossHSPoints, infHSPoints
            , harvHSPoints, bossLevelUpCoeff, infLevelUpCoeff, harvLevelUpCoeff;



    public ConfigSaver()  {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeCfg() {

        try {
            PrintWriter os = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            //DataOutputStream os = new DataOutputStream(out);
            //rows
            os.println("Rows=" + 5);
            //cols
            os.println("Cols=" + 5);
            //startPortal
            //x
            os.println("stPortalX="+3);
            //y
            os.println("stPortalY=" + 0);
            //z
            os.println("stPortalZ="+3);
            //endPortal
            //x
            os.println("endPortalX="+1);
            //y
            os.println("endPortalY="+0);
            //z
            os.println("endPortalZ="+1);
            //PlayerHealth
            os.println("playerHealth=" + 100);
            //PlayerStartSpice
            os.println("playerStartSpice="+150);
            //CanonTower
            //Cost
            os.println("canonTowCost="+23);
            //damage
            os.println("canonTowDmg="+3);
            //rotationSpeed
            os.println("canonTowRot="+(float) Math.PI / 256);
            //Range
            os.println("canonTowRange="+2);
            //

            //BombTower
            //Cost
            os.println("bombTowCost="+7);
            //damage
            os.println("bombTowDmg="+3);
            //rotationSpeed
            os.println("bombTowRot="+(float) Math.PI / 256);
            //Range
            os.println("bombTowRange="+3);

            //SonicTower
            //Cost
            os.println("sonicTowCost="+69);
            //damage
            os.println("sonicTowDmg="+3);
            //Range
            os.println("sonicTowRange="+1.7);

            os.println("knockerCost=" + 35);
            os.println("sandWormSpeed=" + 0.03f);

            //Enemys
            //BossUnit
            os.println("bossMovSpeed=" + 0.006f);
            os.println("bossHealth="+120.f);
            os.println("bossHSPoints="+130.f);
            os.println("bossDmgOnEndPortal="+50);
            os.println("bossStoredSpice="+40);
            os.println("bossLevelUpCoeff="+1.3);

            //Infantry
            os.println("infMovSpeed=" + 0.0028f);
            os.println("infHealth="+20.0f);
            os.println("infHSPoints="+40.f);
            os.println("infDmgOnEndPortal="+4);
            os.println("infStoredSpice="+1);
            os.println("infLevelUpCoeff="+1.3);

            //HarvestMachine
            os.println("harvMovSpeed=" + 0.001f);
            os.println("harvHealth="+50.0f);
            os.println("harvHSPoints="+70.f);
            os.println("harvDmgOnEndPortal="+10);
            os.println("harvStoredSpice="+20);
            os.println("harvLevelUpCoeff="+1.3);

            //Waves
            //overall
            os.println("nbrWaves="+3);
            os.println("spawnDelayIntervall="+1.5f);

            //specWave1
            //0: no Spawn 1: Infantry, 2: HarvestMachine, 3: BossUnit
            os.println("waveSpawnOrder=11100200003");

            //specWave2
            os.println("waveSpawnOrder=12111003");

            //specWave3
            os.println("waveSpawnOrder=121212100303");

            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigSaver cfg = new ConfigSaver();
        cfg.writeCfg();
    }

}
