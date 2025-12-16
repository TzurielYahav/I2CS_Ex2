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
    void testShortestPath() {
        Pixel2D[] resArr1 = {new Index2D(0,0),new Index2D(0,1),new Index2D(0,2),new Index2D(0,3), new Index2D(1,3)
                ,new Index2D(2,3),new Index2D(3,3),new Index2D(3,2),new Index2D(3,1),new Index2D(3,0)};
        Pixel2D[] resArr2 = {new Index2D(0,0),new Index2D(0,1),new Index2D(1,1),new Index2D(1,2), new Index2D(2,2)
                ,new Index2D(2,3),new Index2D(3,3)};
        Pixel2D[] resArr3 = null;
        Pixel2D[] resArr4 = {new Index2D(0,0),new Index2D(3,0),new Index2D(2,0)};

        _m0 = new Map(_map_4x4_1);
        _m1 = new Map(_map_4x4_2);
        _m2 = new Map(_map_4x4_3);

        System.out.println(Arrays.toString(_m0.shortestPath(new Index2D(0, 0), new Index2D(3, 0), 1, false)));
        assertTrue(isEqualArray(resArr1, _m0.shortestPath(new Index2D(0, 0), new Index2D(3, 0), 1, false)));
        assertTrue(isEqualArray(resArr2, _m1.shortestPath(new Index2D(0, 0), new Index2D(3, 3), 1, false)));
        assertTrue(isEqualArray(resArr3, _m2.shortestPath(new Index2D(0, 0), new Index2D(0, 3), 1, false)));
        assertTrue(isEqualArray(resArr4, _m2.shortestPath(new Index2D(0, 0), new Index2D(0, 3), 1, true)));
    }

    @Test
    void testAllDistance() {
        int[][] resArr1 = {{0,-1,10,9}, {1,-1,9,8}, {2,-1,-1,7}, {3,4,5,6}};
        int[][] resArr2 = {{0,1,2,3}, {1,2,3,4}, {2,3,4,5}, {3,4,5,6}};
        int[][] resArr3 = {{0,-1,-2,-2}, {1,-1,-2,-2}, {2,-1,-1,-1}, {3,4,5,6}};
        int[][] resArr4 = {{0, -1, 2, 1}, {1, -1, 3, 2}, {2, -1, -1, -1}, {1, 2, 3, 2}};

        _m0 = new Map(_map_4x4_1);
        _m1 = new Map(_map_4x4_2);
        _m2 = new Map(_map_4x4_3);

        Map2D res1 = new Map(resArr1);
        Map2D res2 = new Map(resArr2);
        Map2D res3 = new Map(resArr3);
        Map2D res4 = new Map(resArr4);

        assertEquals(res1, _m0.allDistance(new Index2D(0, 0), 1, false));
        assertEquals(res2, _m1.allDistance(new Index2D(0, 0), 1, false));
        assertEquals(res3, _m2.allDistance(new Index2D(0, 0), 1, false));
        assertEquals(res4, _m2.allDistance(new Index2D(0, 0), 1, true));
    }

    boolean isEqualArray(Pixel2D[] arr1, Pixel2D[] arr2)
    {
        if(arr1 == null || arr2 == null)
        {
            if (arr1 == null && arr2 == null)
            {
                return true;
            }
            return false;
        }
        if (arr1.length != arr2.length)
        {
            return false;
        }
        for (int i = 0; i < arr1.length; i++)
        {
            if (!arr1[i].equals(arr2[i]))
            {
                return false;
            }
        }
        return true;
    }
}