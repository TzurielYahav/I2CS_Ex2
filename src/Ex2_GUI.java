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

    public static void drawMap()
    {
        double maxX = map.getWidth(), maxY = map.getHeight();
//        int samples = 16;
        StdDraw.setXscale(0,max);
        StdDraw.setYscale(0,max);
        StdDraw.clear();

        StdDraw.setPenColor(StdDraw.GRAY);
        drawGrid(min,max);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        drawArea(po1,po2, xx[0], 10, samples);
        StdDraw.setPenColor(StdDraw.BLUE);
        drawPoly(po1, min, max,n);
        drawInfo(po2,0,8);
        StdDraw.setPenColor(StdDraw.GREEN);
        drawPoly(po2, min, max,n);
        drawInfo(po1,0,7);
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
        drawMap();
        saveMap(map, mapFile);
    }

    /// ///////////// Private functions ///////////////

    public static void drawGrid(double min, double max)
    {
        StdDraw.setXscale(min, max);
        StdDraw.line(0,min,0,max);
        StdDraw.line(min,0,max,0);
    }


}
