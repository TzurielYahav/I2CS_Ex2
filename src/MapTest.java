import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private int[][] _map_4x4_1 = {{0,1,0,0}, {0,1,0,0}, {0,1,1,0}, {0,0,0,0}};
    private int[][] _map_4x4_2 = {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
    private int[][] _map_4x4_3 = {{0,1,0,0}, {3,1,0,0}, {2,1,1,1}, {2,4,10,6}};
    private Map2D _m0, _m1, _m2, _m3_3;

    @BeforeEach
    public void setup()
    {
        _m3_3 = new Map(_map_3_3);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }

    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0,_m1);
    }

    @Test
    void testAllDistance() {
        int[][] resArr1 = {{0,-1,10,9}, {1,-1,9,8}, {2,-1,-1,7}, {3,4,5,6}};
        int[][] resArr2 = {{0,1,2,3}, {1,2,3,4}, {2,3,4,5}, {3,4,5,6}};
        int[][] resArr3 = {{0,-1,-2,-2}, {1,-1,-2,-2}, {2,-1,-1,-1}, {3,4,5,6}};
        int[][] resArr4 = {{0,-1,2,1}, {1,-1,3,2}, {2,-1,-1,-1}, {3,4,5,6}};

        _m0 = new Map(_map_4x4_1);
        _m1 = new Map(_map_4x4_2);
        _m2 = new Map(_map_4x4_3);
        System.out.println(Arrays.deepToString(_m0.allDistance(new Index2D(0, 0), 1, false).getMap()));
        assertEquals(_m0.allDistance(new Index2D(0,0), 1, false).getMap(),resArr1);
        assertEquals(_m1.allDistance(new Index2D(0,0), 1, false).getMap(),resArr2);
        assertEquals(_m2.allDistance(new Index2D(0,0), 1, false).getMap(),resArr3);
        assertEquals(_m2.allDistance(new Index2D(0,0), 1, false).getMap(),resArr3);
    }
}