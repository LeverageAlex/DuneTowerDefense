import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.ConfigMgr;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.Enemys.BossUnit;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.Enemys.Infantry;
import io.swapastack.dunetd.Enemys.Wave;
import io.swapastack.dunetd.GameScreen;
import io.swapastack.dunetd.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EnemyTest {


    BossUnit boss = new BossUnit() {
        @Override
        public Vector3 getCoords() {
            return new Vector3(2.8f, 2, 3.2f);
        }
    };

    Infantry infantry = new Infantry() {
        public Vector3 getCoords() {
            return new Vector3(1f, 2, 0f);
        }
    };

    @BeforeAll
    public static void configInit() {
        ConfigMgr.readCfg("C:\\Users\\Alex\\Documents\\_Studium\\Softwaregrundprojekt\\Einzelprojekt\\code\\alexander-fink\\core\\assets\\config.ini");
    }


    @Test
    public void EnemyCollisionTest() {
        Enemy e = new BossUnit() {
            @Override
            public Vector3 getCoords() {
                return new Vector3(2.8f, 2, 3.2f);
            }
        };
        Assertions.assertTrue(e.collides(new Vector3(3, 2, 3), 1.f));
        Assertions.assertFalse(e.collides(new Vector3(2, 1, 1), 1.f));
    }

    @Test
    public void waveTest() {
        Wave w = new Wave(new GameScreen(new DuneTD()), new Player());
        Assertions.assertFalse(w.isStarted());
        Assertions.assertTrue(w.enemiesLeft() == 0);
        w.gotEaten();
        Assertions.assertFalse(w.enemiesLeft() == 0);
    }

    @Test
    public void EnemyHSPoint() {
        Assertions.assertEquals(boss.getHighscorePoints(), ConfigMgr.bossHSPoints);
        Assertions.assertEquals(infantry.getHighscorePoints(), ConfigMgr.infHSPoints);
    }

    @Test
    public void EnemyDmg() {
        Assertions.assertTrue(boss.isAlive());
        boss.gainDamage(500.f);
        Assertions.assertFalse(boss.isAlive());
    }

    /**
     * Tests if Enemy.collision() works properly
     */
    @Test
    public void bulletCollision() {
        Assertions.assertTrue(boss.collides(new Vector3(2.8f, 2, 3.2f), 1.0f));
        Assertions.assertFalse(boss.collides(new Vector3(1.8f, 2, 3.2f), 1.0f));


    }

    @Test
    public void spiceTest() {
        Assertions.assertEquals(boss.getStoredSpice(), ConfigMgr.bossStoredSpice);
        Assertions.assertEquals(infantry.getStoredSpice(), ConfigMgr.infStoredSpice);
    }

}
