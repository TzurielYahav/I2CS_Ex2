import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static Map2D map;
    private static Color[] colors = {
            StdDraw.WHITE,
            StdDraw.BLACK,
            StdDraw.RED,
            StdDraw.YELLOW,
            StdDraw.GREEN
    };
    private static int playerX = 0;
    private static int playerY = 0;
    private static double enemyX = 0;
    private static double enemyY = 0;
    private static double targetX = 0;
    private static double targetY = 0;
    private static ArrayList<Pixel2D> waypoints;


    public static void drawMap()
    {
        double maxX = map.getWidth(), maxY = map.getHeight();
//        int samples = 16;
        StdDraw.setXscale(0,maxX);
        StdDraw.setYscale(0,maxY);
        StdDraw.clear();


        drawGrid();
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
        mapArr[1][1] = 3;
        map.init(mapArr);
        drawMap();
        gameLoop();
        saveMap(map, mapFile);
    }

    /// ///////////// Private functions ///////////////

    private static void gameLoop()
    {
        long MS_PER_FRAME = 200;
        boolean isGameRunning = true;
        playerX = 0;
        playerY = 0;
        enemyX = map.getWidth() - 1;
        enemyY  = map.getHeight() - 1;

        long previousTime = System.currentTimeMillis();
        long lag = 0L;
        while (isGameRunning) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - previousTime;
            previousTime = currentTime;
            lag += elapsedTime;

            if (StdDraw.isKeyPressed('q'))
            {

                isGameRunning = false;
            }
//            processInput();

            while (lag >= MS_PER_FRAME) {
                update();
                lag -= MS_PER_FRAME;
            }
        }
    }

    private static void update()
    {
        updatePlayer();
        updateEnemy();
    }

    private static void drawPlayer(int x, int y)
    {
        map.setPixel(playerX, playerY, 0);
        drawCell(playerX, playerY);
        map.setPixel(x, y, 1);
        drawCell(x, y);
        playerX = x;
        playerY = y;
    }

    private static void drawEnemy(double x, double y)
    {
        map.setPixel((int)enemyX, (int)enemyY, 0);
        drawCell((int)enemyX, (int)enemyY);
        map.setPixel((int)x, (int)y, 2);
        drawCell((int)x, (int)y);
        enemyX = x;
        enemyY = y;
    }

    private static void updateEnemy()
    {
        if (waypoints == null)
        {
            waypoints = new ArrayList<>();
        }
        if (targetX != playerX || targetY != playerY)
        {
            Pixel2D[] waypointsArr = map.shortestPath(new Index2D((int)enemyX, (int)enemyY), new Index2D(playerX, playerY), 3, false);
            compareWaypoints(waypointsArr);
            targetX = playerX;
            targetY = playerY;
        }
        Pixel2D waypoint = waypoints.getFirst();
        waypoints.removeFirst();
        double newEnemyX = (enemyX + ((double) waypoint.getX() - enemyX) * 0.1);
        double newEnemyY = (enemyY + ((double) waypoint.getY() - enemyY) * 0.1);

        if (newEnemyX != enemyX || newEnemyY != enemyY)
        {
            drawEnemy(newEnemyX, newEnemyY);
        }
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
    }

    private static void updatePlayer()
    {
        int newPlayerX = playerX;
        int newPlayerY = playerY;

        while (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();

            if (key == 'w') newPlayerY += 1;
            if (key == 's') newPlayerY -= 1;
            if (key == 'a') newPlayerX -= 1;
            if (key == 'd') newPlayerX += 1;
        }
        if (newPlayerX >= map.getWidth())
                newPlayerX = map.getWidth() - 1;
        if (newPlayerX < 0)
                newPlayerX = 0;
        if (newPlayerY >= map.getHeight())
                newPlayerY = map.getHeight() - 1;
        if (newPlayerY < 0)
                newPlayerY = 0;

//        if (StdDraw.isKeyPressed('w'))
//        {
//            newPlayerX += 1;
//            if (newPlayerX >= map.getWidth())
//                newPlayerX = map.getWidth() - 1;
//        }
//        else if (StdDraw.isKeyPressed('s'))
//        {
//            newPlayerX -= 1;
//            if (newPlayerX < 0)
//                newPlayerX = 0;
//
//        }
//        else if (StdDraw.isKeyPressed('a'))
//        {
//            newPlayerY += 1;
//            if (newPlayerY >= map.getHeight())
//                newPlayerY = map.getHeight() - 1;
//
//        }
//        else if (StdDraw.isKeyPressed('d'))
//        {
//            newPlayerY -= 1;
//            if (newPlayerY < 0)
//                newPlayerY = 0;
//        }

        if (newPlayerX != playerX || newPlayerY != playerY)
        {
            drawPlayer(newPlayerX, newPlayerY);
        }
    }

    private static void drawCell(int x, int y)
    {
        StdDraw.setPenColor(colors[map.getPixel(x, y)]);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.2);
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
                StdDraw.setPenColor(colors[map.getPixel(x, y)]);
                StdDraw.filledCircle(x + 0.5, y + 0.5, 0.2);
            }
        }
    }


}
