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
    private static final int FLOOR_VALUE = 0;
    private static final int PLAYER_VALUE = 1;
    private static final int ENEMY_VALUE = 2;
    private static final int OBSTACLE_VALUE = 3;
    private static final int FRUIT_VALUE = 4;
    private static int enemyFrameTimerMax = 4;
    private static Map2D map;
    private static final Color[] COLORS = {
            StdDraw.WHITE,
            StdDraw.BLUE,
            StdDraw.RED,
            StdDraw.BLACK,
            StdDraw.YELLOW
    };
    private static Pixel2D playerPos = new Index2D(0,0);
    private static Pixel2D enemyPos = new Index2D(0,0);
    private static Pixel2D targetPos = new Index2D(0,0);
    private static int enemyTimer = 0;
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
        drawCharacter(playerPos, playerPos, PLAYER_VALUE);
        drawCharacter(enemyPos, enemyPos, ENEMY_VALUE);
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
        if (enemyTimer == enemyFrameTimerMax)
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
            Pixel2D[] waypointsArr = map.shortestPath(enemyPos, playerPos, OBSTACLE_VALUE, false);
            if (waypointsArr == null)
            {
                return;
            }
            compareWaypoints(waypointsArr);
            targetPos = new Index2D(playerPos.getX(), playerPos.getY());
        }
        Pixel2D waypoint = waypoints.getFirst();
        drawCharacter(enemyPos, waypoint, ENEMY_VALUE);
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
        if (map.getPixel(newPlayerX, newPlayerY) != OBSTACLE_VALUE && !playerPos.equals(newPlayerPos))
        {
            drawCharacter(playerPos, newPlayerPos, PLAYER_VALUE);
            playerPos = newPlayerPos;
        }
    }

    private static void drawCharacter(Pixel2D oldPos, Pixel2D newPos, int color)
    {
        map.setPixel(oldPos, FLOOR_VALUE);
        drawCell(oldPos);
        map.setPixel(newPos, color);
        drawCell(newPos);
    }

    private static void drawCell(Pixel2D pos)
    {
        StdDraw.setPenColor(COLORS[map.getPixel(pos)]);
        if (map.getPixel(pos) == FLOOR_VALUE)
            StdDraw.filledRectangle(pos.getX() + 0.5, pos.getY() + 0.5, 0.5, 0.5);
        else if (map.getPixel(pos) == PLAYER_VALUE)
            StdDraw.filledCircle(pos.getX() + 0.5, pos.getY() + 0.5, 0.4);
        else if (map.getPixel(pos) == ENEMY_VALUE)
            StdDraw.filledCircle(pos.getX() + 0.5, pos.getY() + 0.5, 0.4);
        else if (map.getPixel(pos) == OBSTACLE_VALUE)
            StdDraw.filledRectangle(pos.getX() + 0.5, pos.getY() + 0.5, 0.5, 0.5);
        else if (map.getPixel(pos) == FRUIT_VALUE)
            StdDraw.filledCircle(pos.getX() + 0.5, pos.getY() + 0.5, 0.2);
    }

    private static void drawGrid()
    {
        for (int y = 0; y < map.getHeight(); y++)
        {
            for (int x = 0; x < map.getWidth(); x++)
            {
                drawCell(new Index2D(x, y));
            }
        }
    }

    private static void changeLevel(int level)
    {
        if (level == 1)
        {
            enemyFrameTimerMax = 4;
            int[][] mapArr = new int[10][10];
            map.init(mapArr);
            playerPos = new Index2D(0, 0);
            enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
            map.drawRect(new Index2D(3,3), new Index2D(6,6), OBSTACLE_VALUE);
        }
        if (level == 2)
        {
            enemyFrameTimerMax = 2;
            int[][] mapArr = new int[14][26];
            map.init(mapArr);
            playerPos = new Index2D(0, 0);
            enemyPos = new Index2D(map.getWidth() / 2, map.getHeight() - 1);
            map.drawRect(new Index2D(0,10), new Index2D(4,13), OBSTACLE_VALUE);
            map.drawRect(new Index2D(25,10), new Index2D(21,13), OBSTACLE_VALUE);
            map.drawRect(new Index2D(1,1), new Index2D(10,2), OBSTACLE_VALUE);
            map.drawRect(new Index2D(15,1), new Index2D(24,2), OBSTACLE_VALUE);
            map.drawRect(new Index2D(6,3), new Index2D(7,5), OBSTACLE_VALUE);
            map.drawRect(new Index2D(18,3), new Index2D(19,5), OBSTACLE_VALUE);
            map.drawRect(new Index2D(12,1), new Index2D(13,3), OBSTACLE_VALUE);
            map.drawRect(new Index2D(9,4), new Index2D(16,5), OBSTACLE_VALUE);
            map.drawRect(new Index2D(0,4), new Index2D(1,5), OBSTACLE_VALUE);
            map.drawRect(new Index2D(24,4), new Index2D(25,5), OBSTACLE_VALUE);
            map.drawRect(new Index2D(3,4), new Index2D(4,8), OBSTACLE_VALUE);
            map.drawRect(new Index2D(21,4), new Index2D(22,8), OBSTACLE_VALUE);
            map.drawRect(new Index2D(1,7), new Index2D(4,8), OBSTACLE_VALUE);
            map.drawRect(new Index2D(21,7), new Index2D(24,8), OBSTACLE_VALUE);
            map.drawRect(new Index2D(6,7), new Index2D(10,8), OBSTACLE_VALUE);
            map.drawRect(new Index2D(15,7), new Index2D(19,8), OBSTACLE_VALUE);
            map.drawRect(new Index2D(12,7), new Index2D(13,9), OBSTACLE_VALUE);
            map.drawRect(new Index2D(9,10), new Index2D(16,11), OBSTACLE_VALUE);
            map.drawRect(new Index2D(6,10), new Index2D(7,13), OBSTACLE_VALUE);
            map.drawRect(new Index2D(18,10), new Index2D(19,13), OBSTACLE_VALUE);
            map.fill(playerPos, FRUIT_VALUE, false);
        }
        waypoints.clear();
        drawMap();
    }
}
