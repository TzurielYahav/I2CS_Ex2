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
    private final int[][] _map_3x3_1 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private final int[][] _map_4x4_1 = {{0,1,0,0}, {0,1,0,0}, {0,1,1,0}, {0,0,0,0}};
    private final int[][] _map_4x4_2 = {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
    private final int[][] _map_4x4_3 = {{0,1,0,0}, {3,1,0,0}, {2,1,1,1}, {2,4,10,6}};
    private Map2D _m0, _m1, _m2, _m3, _m3x3;

    @BeforeEach
    public void setup()
    {
        _m0 = new Map(_map_4x4_1);
        _m1 = new Map(_map_4x4_2);
        _m2 = new Map(_map_4x4_3);
        _m3 = new Map(4);
        _m3x3 = new Map(_map_3x3_1);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testInit()
    {
        Map2D m500 = new Map(4);
        int[][] bigArr = new int [500][500];
        m500.init(bigArr);
        assertEquals(bigArr[0].length, m500.getWidth());
        assertEquals(bigArr.length, m500.getHeight());

        m500.setPixel(0, 0, 1);
        m500.setPixel(new Index2D(3,2), 1);
        assertEquals(m500.getPixel(3, 2), m500.getPixel(0, 0));

        _m0.init(_map_3x3_1);
        _m1.init(_map_3x3_1);
        assertEquals(_m0, _m1);
    }

    @Test
    void testIsInside()
    {
        Pixel2D p1 = new Index2D(-1,2);
        Pixel2D p2 = new Index2D(2,0);
        Pixel2D p3 = new Index2D(3,5);

        assertFalse(_m0.isInside(p1));
        assertTrue(_m0.isInside(p2));
        assertFalse(_m0.isInside(p3));
        assertThrowsExactly(NullPointerException.class, () -> _m0.isInside(null));
    }

    @Test
    void testSameDimensions()
    {
        Pixel2D p1 = new Index2D(-1,2);
        Pixel2D p2 = new Index2D(2,0);
        Pixel2D p3 = new Index2D(3,5);

        assertTrue(_m0.sameDimensions(_m1));
        assertTrue(_m1.sameDimensions(_m0));
        assertFalse(_m0.sameDimensions(_m3x3));
        assertThrowsExactly(NullPointerException.class, () -> _m0.sameDimensions(null));
    }

    @Test
    void testAddMap2D()
    {
        int[][] arr1 = {{1,1,1,1}, {1,1,1,1}, {1,1,1,1}, {1,1,1,1}};

        _m2.init(arr1);
        _m3.addMap2D(_m2);

        assertEquals(_m2, _m3);
        assertThrowsExactly(NullPointerException.class, () -> _m0.addMap2D(null));
    }

    @Test
    void testMul()
    {
        int[][] arr1 = {{1,1,1,1}, {1,1,1,1}, {1,1,1,1}, {1,1,1,1}};

        _m0.init(arr1);
        _m1.init(arr1);
        _m2.init(arr1);
        _m0.mul(5.5);
        _m1.mul(0.5);
        _m2.mul(5);

        assertEquals(_m0, _m2);
        assertEquals(_m1, _m3);
    }

    @Test
    void testRescale()
    {
        int[][] arr1 = {{0,1,2,3}, {1,2,3,4}, {2,3,4,5}, {3,4,5,6}};
        int[][] resArr1 = {{0,2}, {2,4}};
        int[][] resArr2 = {{0, 0, 1, 1, 2, 2, 3, 3}, {1, 1, 2, 2, 3, 3, 4, 4}, {2, 2, 3, 3, 4, 4, 5, 5}, {3, 3, 4, 4, 5, 5, 6, 6}};
        int[][] resArr3 = {{0, 0, 1, 2, 2, 3}, {0, 0, 1, 2, 2, 3}, {1, 1, 2, 3, 3, 4}, {2, 2, 3, 4, 4, 5}, {2, 2, 3, 4, 4, 5}, {3, 3, 4, 5, 5, 6}};

        _m0.init(arr1);
        _m1.init(arr1);
        _m2.init(arr1);
        _m0.rescale(0.5,0.5);
        _m1.rescale(2,1);
        _m2.rescale(1.5,1.5);

        assertEquals(_m0, new Map(resArr1));
        assertEquals(_m1, new Map(resArr2));
        assertEquals(_m2, new Map(resArr3));
        assertThrowsExactly(IllegalArgumentException.class, () -> _m2.rescale(2,0));
    }

    @Test
    void testDraw()
    {
        // Circle
        int[][] resArr1 = {{2,2,2,0}, {2,2,0,0}, {2,1,1,0}, {0,0,0,0}};
        int[][] resArr2 = {{2,2,2,0}, {2,2,0,0}, {2,1,1,3}, {0,0,3,3}};
        // Line
        int[][] resArr3 = {{4,4,4,4}, {2,2,0,0}, {2,1,1,3}, {0,0,3,3}};
        int[][] resArr4 = {{5,4,4,4}, {5,2,0,0}, {5,1,1,3}, {5,0,3,3}};
        int[][] resArr5 = {{5,4,4,6}, {5,2,6,0}, {5,6,1,3}, {6,0,3,3}};
        // Rect
        int[][] resArr6 = {{7,7,7,7}, {5,2,6,0}, {5,6,1,3}, {6,0,3,3}};
        int[][] resArr7 = {{8,7,7,7}, {8,2,6,0}, {8,6,1,3}, {8,0,3,3}};
        int[][] resArr8 = {{9,9,9,9}, {9,9,9,9}, {9,9,9,9}, {9,9,9,9}};

        _m0.drawCircle(new Index2D(0,0), 2, 2);
        assertEquals(_m0,new Map(resArr1));
        _m0.drawCircle(new Index2D(4,4), 2.5, 3);
        assertEquals(_m0,new Map(resArr2));

        _m0.drawLine(new Index2D(0,0), new Index2D(3,0), 4);
        assertEquals(_m0,new Map(resArr3));
        _m0.drawLine(new Index2D(0,3), new Index2D(0,0), 5);
        assertEquals(_m0,new Map(resArr4));
        _m0.drawLine(new Index2D(0,3), new Index2D(3,0), 6);
        assertEquals(_m0,new Map(resArr5));
        assertThrowsExactly(RuntimeException.class, () -> _m0.drawLine(new Index2D(4,4), new Index2D(0,0), 7));

        _m0.drawRect(new Index2D(0,0), new Index2D(3,0), 7);
        assertEquals(_m0,new Map(resArr6));
        _m0.drawRect(new Index2D(0,3), new Index2D(0,0), 8);
        assertEquals(_m0,new Map(resArr7));
        _m0.drawRect(new Index2D(0,3), new Index2D(3,0), 9);
        assertEquals(_m0,new Map(resArr8));
        assertThrowsExactly(RuntimeException.class, () -> _m0.drawRect(new Index2D(4,4), new Index2D(0,0), 7));
    }

    @Test
    void testEquals()
    {
        assertEquals(_m3,_m1);
        assertNotEquals(_m0,_m1);
        assertNotEquals(null, _m0);
    }

    @Test
    void testFill()
    {
        int[][] arr1 = {{1,5,1,1}, {1,1,0,0}, {1,1,1,0}, {0,1,3,0}};

        int[][] resArr1 = {{2,5,1,1}, {2,2,0,0}, {2,2,2,0}, {0,2,3,0}};
        int[][] resArr2 = {{2,5,2,2}, {2,2,0,0}, {2,2,2,0}, {0,2,3,0}};

        _m1.init(arr1);
        _m2.init(arr1);
        _m3.fill(new Index2D(0,0),2, false);
        _m1.fill(new Index2D(0,0),2, false);
        _m2.fill(new Index2D(0,0),2, true);

        assertEquals(_m1,new Map(resArr1));
        assertEquals(_m2,new Map(resArr2));
    }

    @Test
    void testShortestPath()
    {
        Pixel2D[] resArr1 = {new Index2D(0,0),new Index2D(0,1),new Index2D(0,2),new Index2D(0,3), new Index2D(1,3)
                ,new Index2D(2,3),new Index2D(3,3),new Index2D(3,2),new Index2D(3,1),new Index2D(3,0)};
        Pixel2D[] resArr2 = {new Index2D(3,0),new Index2D(3,1),new Index2D(3,2),new Index2D(3,3), new Index2D(2,3)
                ,new Index2D(1,3),new Index2D(0,3)};
        Pixel2D[] resArr3 = null;
        Pixel2D[] resArr4 = {new Index2D(0,0),new Index2D(3,0)};

        assertTrue(isEqualArray(resArr1, _m0.shortestPath(new Index2D(0, 0), new Index2D(3, 0), 1, false)));
        assertTrue(isEqualArray(resArr2, _m1.shortestPath(new Index2D(3, 0), new Index2D(0, 3), 1, false)));
        assertTrue(isEqualArray(resArr3, _m2.shortestPath(new Index2D(0, 0), new Index2D(3, 0), 1, false)));
        assertTrue(isEqualArray(resArr4, _m2.shortestPath(new Index2D(0, 0), new Index2D(3, 0), 1, true)));
    }

    @Test
    void testPacman()
    {
        int[][] mapArr = new int[29][26];
        _m0.init(mapArr);
        _m0.drawRect(new Index2D(0,10), new Index2D(4,20), 3);
        _m0.drawRect(new Index2D(25,10), new Index2D(21,20), 3);
        _m0.drawRect(new Index2D(1,1), new Index2D(10,2), 3);
        _m0.drawRect(new Index2D(15,1), new Index2D(24,2), 3);
        _m0.drawRect(new Index2D(6,3), new Index2D(7,5), 3);
        _m0.drawRect(new Index2D(18,3), new Index2D(19,5), 3);
        _m0.drawRect(new Index2D(12,1), new Index2D(13,3), 3);
        _m0.drawRect(new Index2D(9,4), new Index2D(16,5), 3);

        System.out.println(Arrays.toString(_m0.shortestPath(new Index2D(_m0.getWidth() - 1, _m0.getHeight() - 1), new Index2D(0, 0), 3, false)));
        assertNotNull(_m0.shortestPath(new Index2D(0, 0), new Index2D(3, 0), 1, false));
    }

    @Test
    void testAllDistance()
    {
        int[][] resArr1 = {{0,-1,10,9}, {1,-1,9,8}, {2,-1,-1,7}, {3,4,5,6}};
        int[][] resArr2 = {{0,1,2,3}, {1,2,3,4}, {2,3,4,5}, {3,4,5,6}};
        int[][] resArr3 = {{0,-1,-1,-1}, {1,-1,-1,-1}, {2,-1,-1,-1}, {3,4,5,6}};
        int[][] resArr4 = {{0, -1, 2, 1}, {1, -1, 3, 2}, {2, -1, -1, -1}, {1, 2, 3, 2}};

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
            return arr1 == null && arr2 == null;
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