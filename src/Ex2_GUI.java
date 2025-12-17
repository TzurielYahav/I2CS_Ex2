import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
    public static void drawMap(Map2D map)
    {
        double min = 50, max=20;
        int samples = 16;
        StdDraw.setScale(min, max);
        StdDraw.clear();
    }

    /**
     * @param mapFileName
     * @return
     */
    public static Map2D loadMap(String mapFileName)
    {
        try
        {
            File myFile = new File(mapFileName);
            Scanner myReader = new Scanner(myFile);
            int i=0;
            while (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                System.out.println(i+") "+data);
                i=i+1;
            }
            myReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Map2D ans = new Map(4);

        return ans;
    }

    /**
     *
     * @param map
     * @param mapFileName
     */
    public static void saveMap(Map2D map, String mapFileName)
    {
//        int[][] mapArr = map.getMap();
//        try
//        {
//            FileWriter myWriter = new FileWriter(mapFileName);
//            myWriter.write("Text file named: "+file+"\n");
//            myWriter.write("Point "+p1.toString());
//            myWriter.close();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }

    }

    public static void main(String[] a)
    {
//        String mapFile = "map.txt";
//        Map2D map = loadMap(mapFile);
//        drawMap(map);
//
//        String str = "file_123.txt";
//        String data = "This is a multi String file,\n"
//                + "second line ...\n ";
//        save(str, data);
//        try {
//            load(str);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("OK");

    }

    /// ///////////// Private functions ///////////////
}
