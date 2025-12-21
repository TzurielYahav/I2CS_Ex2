import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Intro2CS_2026A
 * This class represents a Graphical User Interface (GUI) for Map2D.
 * The class has save and load functions, and a GUI draw function.
 * You should implement this class, it is recommender to use the StdDraw class, as in:
 * https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
 *
 *
 */
public class Ex2_GUI
{
    private static final int PLAYER = 1;
    private static final int ENEMY = 2;
    private static final int OBSTACLE = 3;
    private static final int ENEMY_FRAME_TIMER_MAX = 4;
    private static Map2D map;
    private static final Color[] COLORS = {
            StdDraw.WHITE,
            StdDraw.BLUE,
            StdDraw.RED,
            StdDraw.BLACK,
            StdDraw.YELLOW
    };
//    private static int playerX = 0;
//    private static int playerY = 0;
//    private static int enemyX = 0;
//    private static int enemyY = 0;
    private static Pixel2D playerPos = new Index2D(0,0);
    private static Pixel2D enemyPos = new Index2D(0,0);
    private static Pixel2D targetPos = new Index2D(0,0);
    private static int enemyTimer = 0;
//    private static int targetX = 0;
//    private static int targetY = 0;
    private static ArrayList<Pixel2D> waypoints = new ArrayList<>();
    private static boolean isGameRunning = false;



    public static void drawMap()
    {
        double maxX = map.getWidth(), maxY = map.getHeight();
//        int samples = 16;
        StdDraw.setXscale(0,maxX);
        StdDraw.setYscale(0,maxY);
        StdDraw.clear();


        drawGrid();
        drawCharacter(playerPos, playerPos, PLAYER);
        drawCharacter(enemyPos, enemyPos, ENEMY);
//        StdDraw.setPenRadius(0.005);
//        StdDraw.setPenColor(StdDraw.BLACK);
//        drawArea(po1,po2, xx[0], 10, samples);
//        StdDraw.setPenColor(StdDraw.BLUE);
//        drawPoly(po1, min, max,n);
//        drawInfo(po2,0,8);
//        StdDraw.setPenColor(StdDraw.GREEN);
//        drawPoly(po2, min, max,n);
//        drawInfo(po1,0,7);
    }

    /**
     * @param mapFileName
     * @return
     */
    public static Map2D loadMap(String mapFileName) throws FileNotFoundException
    {
        Map2D ans = new Map(1);
        try
        {
            File myFile = new File(mapFileName);
            Scanner myReader = new Scanner(myFile);
            ArrayList<String[]> rowList = new ArrayList<>();
            while (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                String[] splitString =  data.split(",");
                rowList.add(splitString);
            }
            int[][] mapArr = new int[rowList.size()][rowList.getFirst().length];
            for (int i = 0; i < rowList.size(); i++)
            {
                for (int j = 0; j < rowList.get(i).length; j++)
                {
                    mapArr[i][j] = Integer.parseInt(rowList.get(i)[j]);
                }
            }
            ans.init(mapArr);
            myReader.close();
        }
        catch(Exception e)
        {
            throw new FileNotFoundException(mapFileName + " not found");
        }
        return ans;
    }

    /**
     *
     * @param map
     * @param mapFileName
     */
    public static void saveMap(Map2D map, String mapFileName)
    {
        int[][] mapArr = map.getMap();
        try
        {
            FileWriter myWriter = new FileWriter(mapFileName);
            for (int i = 0; i < mapArr.length; i++) {
                for (int j = 0; j < mapArr[i].length; j++) {
                    myWriter.write(mapArr[i][j] + ",");
                }
                myWriter.write("\n");
            }
            myWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] a)
    {
        String mapFile = "map.txt";
        map = new Map(1);
        try
        {
            map = loadMap(mapFile);
        }
        catch(Exception e)
        {
            saveMap(map, mapFile);
        }
        int[][] mapArr = new int[10][10];
        map.init(mapArr);
        gameLoop();
        saveMap(map, mapFile);
    }

    /// ///////////// Private functions ///////////////

    private static void gameLoop()
    {
        long MS_PER_FRAME = 200;
        isGameRunning = true;
        playerPos = new Index2D(0, 0);
        enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
        targetPos = new Index2D(0, 0);
        enemyTimer = 0;
        drawMap();

        long previousTime = System.currentTimeMillis();
        long lag = 0L;
        while (isGameRunning) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - previousTime;
            previousTime = currentTime;
            lag += elapsedTime;

            while (lag >= MS_PER_FRAME) {
                update();
                lag -= MS_PER_FRAME;
            }
        }
    }

    private static void update()
    {
        processInput();
        if (enemyTimer == ENEMY_FRAME_TIMER_MAX)
            enemyTimer = 0;
        if (enemyTimer == 0)
            updateEnemy();
        enemyTimer++;
    }

    private static void updateEnemy()
    {
        if (enemyPos.equals(playerPos))
            return;
        if (!targetPos.equals(playerPos) || waypoints.isEmpty())
        {
            Pixel2D[] waypointsArr = map.shortestPath(enemyPos, playerPos, OBSTACLE, false);
            if (waypointsArr == null)
            {
                return;
            }
            compareWaypoints(waypointsArr);
            targetPos = new Index2D(playerPos.getX(), playerPos.getY());
        }
        Pixel2D waypoint = waypoints.getFirst();
        drawCharacter(enemyPos, waypoint, ENEMY);
        enemyPos = waypoint;
        waypoints.removeFirst();
    }

    private static void compareWaypoints(Pixel2D[] waypointsArr)
    {

        for (int i = 1; i < waypointsArr.length; i++)
        {
            if (i - 1 < waypoints.size())
            {
                if (!waypointsArr[i].equals(waypoints.get(i - 1)))
                {
                    waypoints.set(i - 1, waypointsArr[i]);
                }
            }
            else
            {
                waypoints.add(waypointsArr[i]);
            }
        }
        if (waypointsArr.length < waypoints.size())
        {
            for (int i = waypoints.size(); i > waypointsArr.length; i--)
            {
                waypoints.removeLast();
            }
        }
    }

    private static void processInput()
    {
        int newPlayerX = playerPos.getX();
        int newPlayerY = playerPos.getY();
        while (StdDraw.hasNextKeyTyped())
        {
            char key = StdDraw.nextKeyTyped();
            if (49 <= (int)key && (int)key <= 57)
            {
                changeLevel((int)key - 48);
                return;
            }
            if (key == 'q') isGameRunning = false;

            if (newPlayerX == playerPos.getX() && newPlayerY == playerPos.getY())
            {
                if (key == 'w') newPlayerY += 1;
                else if (key == 's') newPlayerY -= 1;
                else if (key == 'a') newPlayerX -= 1;
                else if (key == 'd') newPlayerX += 1;
            }
        }
        if (newPlayerX != playerPos.getX() || newPlayerY != playerPos.getY())
        {
            updatePlayer(newPlayerX, newPlayerY);
        }
    }

    private static void updatePlayer(int newPlayerX, int newPlayerY)
    {
        if (newPlayerX >= map.getWidth())
                newPlayerX = map.getWidth() - 1;
        if (newPlayerX < 0)
                newPlayerX = 0;
        if (newPlayerY >= map.getHeight())
                newPlayerY = map.getHeight() - 1;
        if (newPlayerY < 0)
                newPlayerY = 0;
        Pixel2D newPlayerPos = new Index2D(newPlayerX, newPlayerY);
        if (map.getPixel(newPlayerX, newPlayerY) != OBSTACLE && !playerPos.equals(newPlayerPos))
        {
            drawCharacter(playerPos, newPlayerPos, PLAYER);
            playerPos = newPlayerPos;
        }
    }

    private static void drawCharacter(Pixel2D oldPos, Pixel2D newPos, int color)
    {
        map.setPixel(oldPos, 0);
        drawCell(oldPos);
        map.setPixel(newPos, color);
        drawCell(newPos);
    }

    private static void drawCell(Pixel2D pos)
    {
        StdDraw.setPenColor(COLORS[map.getPixel(pos)]);
        if (StdDraw.getPenColor() == StdDraw.WHITE)
            StdDraw.filledRectangle(pos.getX() + 0.5, pos.getY() + 0.5, 0.5, 0.5);
        else
            StdDraw.filledCircle(pos.getX() + 0.5, pos.getY() + 0.5, 0.5);
    }

    private static void drawGrid()
    {

//        StdDraw.setPenColor(StdDraw.BLACK);
//        drawArea(po1,po2, xx[0], 10, samples);
//        StdDraw.setPenColor(StdDraw.BLUE);
//        drawPoly(po1, min, max,n);
//        drawInfo(po2,0,8);
//        StdDraw.setPenColor(StdDraw.GREEN);
//        drawPoly(po2, min, max,n);
//        drawInfo(po1,0,7);

        // ==== Grid ====
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.GRAY);
        for (int i = 0; i < map.getHeight(); i++)
        {
            StdDraw.line(0,i + 0.5,map.getWidth(),i + 0.5);
        }
        for (int i = 0; i < map.getWidth(); i++)
        {
            StdDraw.line(i + 0.5,0,i + 0.5,map.getHeight());
        }

        // ==== Cells ====
        //StdDraw.setPenRadius(1);
        for (int y = 0; y < map.getHeight(); y++)
        {
            for (int x = 0; x < map.getWidth(); x++)
            {
                StdDraw.setPenColor(COLORS[map.getPixel(x, y)]);
                StdDraw.filledCircle(x + 0.5, y + 0.5, 0.5);
            }
        }
    }

    private static void changeLevel(int level)
    {

        if (level == 1)
        {
            int[][] mapArr = new int[10][10];
            map.init(mapArr);
            map.drawRect(new Index2D(3,3), new Index2D(6,6), OBSTACLE);
        }
        if (level == 2)
        {
            int[][] mapArr = new int[29][26];
            map.init(mapArr);
            map.drawRect(new Index2D(0,10), new Index2D(4,20), OBSTACLE);
            map.drawRect(new Index2D(25,10), new Index2D(21,20), OBSTACLE);
            map.drawRect(new Index2D(1,1), new Index2D(10,2), OBSTACLE);
            map.drawRect(new Index2D(15,1), new Index2D(24,2), OBSTACLE);
            map.drawRect(new Index2D(6,3), new Index2D(7,5), OBSTACLE);
            map.drawRect(new Index2D(18,3), new Index2D(19,5), OBSTACLE);
            map.drawRect(new Index2D(12,1), new Index2D(13,3), OBSTACLE);
            map.drawRect(new Index2D(9,4), new Index2D(16,5), OBSTACLE);
        }
        playerPos = new Index2D(0, 0);
        enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
        drawMap();
    }
}
