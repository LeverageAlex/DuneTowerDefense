import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.Enemys.BossUnit;
import io.swapastack.dunetd.Enemys.Enemy;
import io.swapastack.dunetd.GameScreen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Tester {
    GameScreen gs;

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
}
