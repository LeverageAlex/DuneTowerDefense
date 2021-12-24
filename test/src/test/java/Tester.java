import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Enemys.BossUnit;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import io.swapastack.dunetd.Towers.CanonTower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class Tester {
    static GameScreen screen;
    CanonTower canon = new CanonTower(null) {
        @Override
        public boolean isInRange(Vector3 pos) {
            return true;
        }
        @Override
        public boolean rotateTowardsVectorSmooth(Vector3 pointToRotate) {
            return true;
        }
    };

    BossUnit boss = new BossUnit() {
        @Override
        public Vector3 getCoords() {
            return new Vector3(2.8f, 2, 3.2f);
        }
    };

    @Test
    public void HelloWorld()  {
        System.out.println("Hello World!");
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
    public void EnemyFireTest() {

        ArrayList<Enemy> arr = new ArrayList<>();
        arr.add(boss);

        Assertions.assertTrue(canon.fire(arr) != null);

    }
}
