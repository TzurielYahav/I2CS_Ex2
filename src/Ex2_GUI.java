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
    private static final int ENEMY_FRAME_MOVEMENT_DELAY = 2;
    private static Map2D map;
    private static final Color[] COLORS = {
            StdDraw.DARK_GRAY,
            StdDraw.BLUE,
            StdDraw.RED,
            StdDraw.MAGENTA,
            StdDraw.YELLOW
    };
    private static Pixel2D playerPos = new Index2D(0,0);
    private static Pixel2D enemyPos = new Index2D(0,0);
    private static Pixel2D targetPos = new Index2D(0,0);
    private static int enemyDelayTimer = 0;
    private static ArrayList<Pixel2D> waypoints = new ArrayList<>();
    private static boolean isGameRunning = false;
    private static boolean cyclic = false;


    public static void drawMap()
    {
        double maxX = map.getWidth(), maxY = map.getHeight();
        StdDraw.setXscale(0,maxX);
        StdDraw.setYscale(0,maxY + (maxY / 10));
        StdDraw.clear();

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(maxX / 2, maxY + (maxY / 20), "'WASD' for movement  |  Numbers to switch levels  |  'Q' to stop and save");
        drawGrid();
        drawCharacter(playerPos, playerPos, PLAYER_VALUE);
        drawCharacter(enemyPos, enemyPos, ENEMY_VALUE);
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
            if (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                cyclic = data.equals("true");
            }
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
            myWriter.write(cyclic + "\n");
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
            int[][] mapArr = new int[10][10];
            map.init(mapArr);
            saveMap(map, mapFile);
        }
        gameLoop();
        saveMap(map, mapFile);
    }

    /// ///////////// Private functions ///////////////

    private static void gameInit()
    {
        isGameRunning = true;
        enemyDelayTimer = 0;
        playerPos = new Index2D(0, 0);
        enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
        targetPos = playerPos;
        for (int i = 0; i < map.getWidth(); i++)
        {
            for (int j = 0; j < map.getHeight(); j++)
            {
                if (map.getPixel(i, j) == PLAYER_VALUE)
                    playerPos = new Index2D(i, j);
                else if (map.getPixel(i, j) == ENEMY_VALUE)
                    enemyPos = new Index2D(i, j);
            }
        }
        drawMap();
    }

    private static void gameLoop()
    {
        gameInit();

        long MS_PER_FRAME = 200;
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
        if (enemyDelayTimer == ENEMY_FRAME_MOVEMENT_DELAY)
            enemyDelayTimer = 0;
        if (enemyDelayTimer == 0)
            updateEnemy();
        enemyDelayTimer++;
    }

    private static void updateEnemy()
    {
        if (enemyPos.equals(playerPos))
        {
            gameOver();
            return;
        }
        if (!targetPos.equals(playerPos) || waypoints.isEmpty())
        {
            Pixel2D[] waypointsArr = map.shortestPath(enemyPos, playerPos, OBSTACLE_VALUE, cyclic);
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
        // ======== X ========
        if (newPlayerX >= map.getWidth())
            if (cyclic)
                newPlayerX = 0;
            else
                newPlayerX = map.getWidth() - 1;
        if (newPlayerX < 0)
            if (cyclic)
                newPlayerX = map.getWidth() - 1;
            else
                newPlayerX = 0;
        // ======== Y ========
        if (newPlayerY >= map.getHeight())
            if (cyclic)
                newPlayerY = 0;
            else
                newPlayerY = map.getHeight() - 1;
        if (newPlayerY < 0)
            if (cyclic)
                newPlayerY = map.getHeight() - 1;
            else
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
        StdDraw.setPenColor(COLORS[FLOOR_VALUE]);
        StdDraw.filledRectangle(pos.getX() + 0.5, pos.getY() + 0.5, 0.5, 0.5);

        StdDraw.setPenColor(COLORS[map.getPixel(pos)]);
        if (map.getPixel(pos) == PLAYER_VALUE)
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
            int[][] mapArr = new int[10][10];
            map.init(mapArr);
            playerPos = new Index2D(0, 0);
            enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
            cyclic = false;
        }
        else if (level == 2)
        {
            int[][] mapArr = new int[10][10];
            map.init(mapArr);
            playerPos = new Index2D(0, 0);
            enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
            cyclic = false;
            map.drawRect(new Index2D(3,3), new Index2D(6,6), OBSTACLE_VALUE);
        }
        else if (level == 3)
        {
            int[][] mapArr = new int[14][26];
            map.init(mapArr);
            playerPos = new Index2D(0, 0);
            enemyPos = new Index2D(map.getWidth() / 2, map.getHeight() - 1);
            cyclic = false;
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
            map.fill(playerPos, FRUIT_VALUE, cyclic);
        }
        else  if (level == 4)
        {
            int[][] mapArr = new int[10][10];
            map.init(mapArr);
            playerPos = new Index2D(0, 0);
            enemyPos = new Index2D(map.getWidth() - 3, map.getHeight() - 3);
            cyclic = true;
            map.drawLine(new Index2D(4,0), new Index2D(4,9), OBSTACLE_VALUE);
            map.drawLine(new Index2D(0,4), new Index2D(9,4), OBSTACLE_VALUE);
        }
        waypoints.clear();
        drawMap();
    }

    private static void gameOver()
    {
        double centerX = (double) map.getWidth() / 2;
        double centerY = (double) (map.getHeight() + (map.getHeight() / 10)) / 2;
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(centerX, centerY, centerX / 2, centerY / 2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.rectangle(centerX, centerY, centerX / 2, centerY / 2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(centerX, centerY, "Game Over");
        isGameRunning = false;
        int[][] mapArr = new int[10][10];
        map.init(mapArr);
        playerPos = new Index2D(0, 0);
        enemyPos = new Index2D(map.getWidth() - 1, map.getHeight() - 1);
    }
}
