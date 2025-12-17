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
    private static Map2D map;
    private static Color[] colors = {
            StdDraw.WHITE,
            StdDraw.BLACK,
            StdDraw.RED,
            StdDraw.YELLOW,
            StdDraw.GREEN
    };

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
        saveMap(map, mapFile);
    }

    /// ///////////// Private functions ///////////////

    public static void drawGrid()
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
            StdDraw.line(0,i,map.getWidth(),i);
        }
        for (int i = 0; i < map.getWidth(); i++)
        {
            StdDraw.line(i,0,i,map.getHeight());
        }

        // ==== Cells ====
        //StdDraw.setPenRadius(1);
        for (int y = 0; y < map.getHeight(); y++)
        {
            for (int x = 0; x < map.getWidth(); x++)
            {
                StdDraw.setPenColor(colors[map.getPixel(x, y)]);
                StdDraw.filledCircle(x, y, 0.2);
            }
        }
    }


}
