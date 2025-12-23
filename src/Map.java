import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 */
public class Map implements Map2D, Serializable
{
    private int[][] _map;

	/**
	 * Constructs a w * h 2D raster map with an init value v.
	 * @param w The width of the map
	 * @param h The height of the map
	 * @param v The initial value to put in all cells
	 */
	public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
	 * Constructs a square map (size*size) with values of 0.
	 * @param size The size of the square map
	 */
	public Map(int size) {
        this(size,size, 0);
    }
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data The 2D int array of data to construct the map from
	 */
	public Map(int[][] data) {
		init(data);
	}

    /**
     * Creates a new 2D int array and puts v as the initial value of each cell.
     *
     * @param w the width of the underlying 2D array.
     * @param h the height of the underlying 2D array.
     * @param v the init value of all the entries in the 2D array.
     */
    @Override
	public void init(int w, int h, int v)
    {
        if (w <= 0 || h <= 0)
            throw new IllegalArgumentException("Invalid width or height parameter");
        _map = new int[h][w];
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                _map[j][i] = v;
            }
        }
	}

    /**
     * Creates a new 2D int array with values from a given 2D array.
     *
     * @param arr a 2D int array.
     */
	@Override
	public void init(int[][] arr)
    {
        _map = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i].length != arr[0].length)
                throw new IllegalArgumentException("Invalid array length");
            _map[i] = arr[i].clone();
        }
	}

	@Override
	public int[][] getMap()
    {
        int[][] ans = new int[_map.length][_map[0].length];
        for (int i = 0; i < ans.length; i++)
        {
            ans[i] = _map[i].clone();
        }
		return ans;
	}

	@Override
	public int getWidth()
    {
        return _map[0].length;
    }

	@Override
	public int getHeight()
    {
        return _map.length;
    }

	@Override
	public int getPixel(int x, int y)
    {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            throw new IllegalArgumentException("x or y is not inside the map");
        return _map[y][x];
    }

	@Override
	public int getPixel(Pixel2D p)
    {
        if (!isInside(p))
            throw new IllegalArgumentException("Pixel2D is not inside the map");
        return _map[p.getY()][p.getX()];
	}

	@Override
	public void setPixel(int x, int y, int v)
    {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            throw new IllegalArgumentException("x or y is not inside the map");
        _map[y][x] = v;
    }

	@Override
	public void setPixel(Pixel2D p, int v)
    {
        if (!isInside(p))
            throw new IllegalArgumentException("Pixel2D is not inside the map");
        _map[p.getY()][p.getX()] = v;
	}

    @Override
    public boolean isInside(Pixel2D p)
    {
        if (p == null)
            throw new NullPointerException("Pixel2D p is null");
        if (p.getX() >= 0 && p.getY() >= 0)
            return p.getX() < _map[0].length && p.getY() < _map.length;
        return false;
    }

    @Override
    public boolean sameDimensions(Map2D p)
    {
        if (p == null)
            throw new NullPointerException("Map2D p is null");
        return getHeight() == p.getHeight() && getWidth() == p.getWidth();
    }

    @Override
    public void addMap2D(Map2D p)
    {
        if (p == null)
            throw new NullPointerException("Map2D p is null");
        if (sameDimensions(p))
        {
            int[][] otherMap = p.getMap();
            for (int i = 0; i < _map[0].length; i++)
            {
                for (int j = 0; j < _map.length; j++)
                {
                    _map[j][i] += otherMap[j][i];
                }
            }
        }
    }

    @Override
    public void mul(double scalar)
    {
        for (int i = 0; i < _map[0].length; i++)
        {
            for (int j = 0; j < _map.length; j++)
            {
                _map[j][i] = (int) ((double)_map[j][i] * scalar);
            }
        }
    }

    /**
     * Creates a new map with size of width * sx , height * sy
     * and puts in each cell of the new map the value of newMap(x,y) -> oldMap(x / sx, y / sy)
     * @param sx The x scale to multiply the map width by
     * @param sy The y scale to multiply the map height by
     */
    @Override
    public void rescale(double sx, double sy)
    {
        if (sx <= 0 || sy <= 0)
            throw new IllegalArgumentException("sx or sy is not a valid scale");
        int[][] newMap = new int[(int) ((double)_map.length * sy)][(int) ((double)_map[0].length * sx)];
        for (int x = 0; x < newMap[0].length; x++)
        {
            for (int y = 0; y < newMap.length; y++)
            {
                newMap[y][x] = _map[(int) (y / sy)][(int) (x / sx)];    // newMap(x,y) -> oldMap(x / sx, y / sy)
            }
        }
        _map = newMap;
    }

    /**
     * Loops over all the points in a square with size 2*radius around the center
     * and checks for each point if it's in the map and if its distance from the center is smaller than the radius
     * if true it sets the point to color
     * @param center The center of the circle
     * @param rad The radius of the circle (must be bigger than 0)
     * @param color - the new color to draw the circle.
     */
    @Override
    public void drawCircle(Pixel2D center, double rad, int color)
    {
        if (center == null)
            throw new NullPointerException("Pixel2D center is null");
        if (rad <= 0)
            throw new IllegalArgumentException("rad is out of range");
        int startX = (int) ((double)center.getX() - rad);                   // the x pos to start looking for points in the circle from
        int startY = (int) ((double)center.getY() - rad);                   // the y pos to start looking for points in the circle from
        for (int i = 0; i <= 2 * rad; i++)
        {
            for (int j = 0; j <= 2 * rad; j++)
            {
                if (isInside(new Index2D(startX + i, startY + j)))   // check if the current pos is inside the map
                    if (center.distance2D(new Index2D(startX + i, startY + j)) <= rad)  // check if the current pos is inside the circle
                        _map[startY + i][startX + j] = color;
            }
        }
    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color)
    {
        if (!isInside(p1) || !isInside(p2))
            throw new RuntimeException("p1 or p2 is out of range");
        if (p1.equals(p2))                          // if p1 and p1 is the same point draw only this point
        {
            _map[p1.getY()][p1.getX()] = color;
            return;
        }
        int dx = Math.abs(p2.getX() - p1.getX());   // the delta between p1.x and p2.x
        int dy = Math.abs(p2.getY() - p1.getY());   // the delta between p1.y and p2.y
        if (dx >= dy)                               // check if the line is more horizontal than vertical
        {
            if (p1.getX() <= p2.getX())             // check which point has the bigger x and start drawing from it
                DrawLineX(p1.getX(), p1.getY(), p2.getX(), p2.getY(), color);
            else
                DrawLineX(p2.getX(), p2.getY(), p1.getX(), p1.getY(), color);
        }
        else
        {
            if (p1.getY() <= p2.getY())             // check which point has the bigger y and start drawing from it
                DrawLineY(p1.getX(), p1.getY(), p2.getX(), p2.getY(), color);
            else
                DrawLineY(p2.getX(), p2.getY(), p1.getX(), p1.getY(), color);
        }
    }

    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color)
    {
        if (!isInside(p1) || !isInside(p2))
            throw new RuntimeException("p1 or p2 is out of range");
        int x1 = Math.min(p1.getX(), p2.getX());    // the x coord of the first corner
        int x2 = Math.max(p1.getX(), p2.getX());    // the x coord of the last corner
        int y1 = Math.min(p1.getY(), p2.getY());    // the y coord of the first corner
        int y2 = Math.max(p1.getY(), p2.getY());    // the y coord of the last corner
        for (int i = x1; i <= x2; i++)
        {
            for (int j = y1; j <= y2; j++)
            {
                _map[j][i] = color;
            }
        }
    }

    @Override
    public boolean equals(Object ob)
    {
        if (ob instanceof Map)
        {
            if (getHeight() == ((Map) ob).getHeight() && getWidth() == ((Map) ob).getWidth())
            {
                int[][] otherMap = ((Map) ob).getMap();
                for (int i = 0; i < _map[0].length; i++)
                {
                    for (int j = 0; j < _map.length; j++)
                    {
                        if (_map[j][i] != otherMap[j][i])
                            return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     *
     *
     * @param xy The pos to start filling from
     * @param new_v The new value to fill
     * @param cyclic If the map is cyclic
     */
	@Override
	public int fill(Pixel2D xy, int new_v,  boolean cyclic)
    {
        if (!isInside(xy))
            throw new RuntimeException("xy is out of range");
        int ans = 0;
        int oldColor = _map[xy.getY()][xy.getX()];  // the old color to search for when filling
        if (oldColor != new_v)                      // if old color == new color we have nothing to do
        {
            ans = fillPixels(xy, oldColor, new_v, cyclic);  // calls a recursive function that fills the tiles needed
        }
		return ans;
	}

    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     * Searches for the shortest from p1 to p2,
     * uses a map of all the already explored cells, a queue of all the explored and to be explored cells and a queue of the parent of each explored cell
     * loops through the explored queue, for each cell in ExQ search for any valid neighbor and add it to the ExQ, and it's parent to the PrQ.
     * if found the target cell, build a queue from the parents of the shortest path starting from the target and back to the start cell,
     * reverse it and convert to an array
     *
     * @param p1 The starting cell
     * @param p2 The target cell
     * @param obsColor The obstacle color to avoid
     * @param cyclic If the map is cyclic
     */
	@Override
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic)
    {
        if (!isInside(p1) || !isInside(p2))
            throw new RuntimeException("p1 or p2 is out of range");
        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor)
            throw new RuntimeException("p1 or p2 is unreachable");
        if (p1.equals(p2))
            return new Pixel2D[]{p1, p2};

        boolean[][] exploreMap = new boolean[_map.length][_map[0].length];  // a map representing all the tiles that were explored

        ArrayList<Pixel2D> exploreQueue = new ArrayList<Pixel2D>();         // the queue of explored tiles and to be explored tiles
        ArrayList<Pixel2D> parentQueue = new ArrayList<Pixel2D>();          // the queue holding the parent for each explored tile (parentQ[i] == parent(exploreQ[i]))

        exploreQueue.add(p1);                                               // add the starting tile to the ExQ
        parentQueue.add(p1);                                                // add the starting tile as the parent of itself
        exploreMap[p1.getY()][p1.getX()] = true;                            // add the starting tile to the explored tiles map

        int currentPixel = 0;                                               // the current index in the explore queue
        boolean found = false;                                              // if we found the target pixel (p2)
        while (!found && currentPixel < exploreQueue.size())                // while we didn't find && there are still elements to explore in the explore queue
        {
            Pixel2D p = exploreQueue.get(currentPixel);                     // get the current cell to explore
            Index2D[] childrenCoords = new Index2D[4];
            childrenCoords[0] = isInBounds(p.getX(), p.getY() + 1, cyclic); // the UP neighbor of the current cell
            childrenCoords[1] = isInBounds(p.getX(), p.getY() - 1, cyclic); // the DOWN neighbor of the current cell
            childrenCoords[2] = isInBounds(p.getX() - 1, p.getY(), cyclic); // the LEFT neighbor of the current cell
            childrenCoords[3] = isInBounds(p.getX() + 1, p.getY(), cyclic); // the RIGHT neighbor of the current cell

            for (int i = 0; i < 4; i++)                                     // for each neighbor cell
            {
                if (isInside(childrenCoords[i]) && getPixel(childrenCoords[i]) != obsColor && !exploreMap[childrenCoords[i].getY()][childrenCoords[i].getX()])
                {                                                           // check if the cell is (inside && not obstacle && not explored yet)
                    exploreMap[childrenCoords[i].getY()][childrenCoords[i].getX()] = true;  // add the cell to the explored cells map
                    exploreQueue.add(childrenCoords[i]);                                    // add the cell to the explore queue
                    parentQueue.add(p);                                                     // add the current cell (p) as the parent of this cell (neighbor)
                    if (childrenCoords[i].equals(p2))                                       // check if this cell is the target (p2)
                    {
                        found = true;
                        break;
                    }
                }
            }
            currentPixel++;                                                                 // move to the next cell in the explore queue
        }
        if (!found)
            return null;

        // ======== Build the waypoint array ========
        ArrayList<Pixel2D> ansQueue = new ArrayList<Pixel2D>();
        ansQueue.add(exploreQueue.getLast());
        ansQueue.add(parentQueue.getLast());
        for (int i = exploreQueue.size() - 1; i > 0; i--)                                   // a loop that goes back though the explore list and finds the parent of each cell in the shortest path
        {
            if (exploreQueue.get(i).equals(ansQueue.getLast()))                             // for each cell in answer queue starting from the target find its parent
            {
                ansQueue.add(parentQueue.get(i));                                           // add the parent to the answer queue
            }
        }
        Pixel2D[] ans = new Pixel2D[ansQueue.size()];                                       // convert to array
        for (int i = 0; i < ans.length; i++)
        {
            ans[i] = ansQueue.getLast();
            ansQueue.removeLast();
        }
        return ans;
	}

    /**
     * Creates a new map for the distances, starts from the start cell and loops through the explored queue,
     * for each cell in ExQ put its distance from start in the new map and search for any valid neighbor.
     * remove the current cell and add the neighbors to the ExQ and move to the next cell until ExQ is empty.
     *
     * @param start the source (starting) point
     * @param obsColor the color representing obstacles
     * @param cyclic If the map is cyclic
     * @return A map containing the distance of each tile from the start pos
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic)
    {
        if (!isInside(start))
            throw new RuntimeException("start is out of range");
        if (getPixel(start) == obsColor)
            throw new RuntimeException("start is unreachable");

        int[][] exploreMap = new int[_map.length][_map[0].length];          // the map of distances
        for (int x = 0; x < _map[0].length; x++)
        {
            for (int y = 0; y < _map.length; y++)
            {
                if (_map[y][x] == obsColor)
                    exploreMap[y][x] = -1;  // obstacle color
                else
                    exploreMap[y][x] = -2;  // Not checked color
            }
        }
        ArrayList<Pixel2D> exploreQueue = new ArrayList<Pixel2D>();         // a queue of the to be explored cells
        exploreMap[start.getY()][start.getX()] = 0;                         // the distance of the start point
        exploreQueue.add(start);
        while (!exploreQueue.isEmpty())                                     // while there are still cells to explore
        {
            Pixel2D current = exploreQueue.removeFirst();                   // get the next cell to explore
            int distance = exploreMap[current.getY()][current.getX()];

            Index2D[] childrenCoords = new Index2D[4];
            childrenCoords[0] = isInBounds(current.getX(), current.getY() + 1, cyclic); // the UP neighbor of the current cell
            childrenCoords[1] = isInBounds(current.getX(), current.getY() - 1, cyclic); // the DOWN neighbor of the current cell
            childrenCoords[2] = isInBounds(current.getX() - 1, current.getY(), cyclic); // the LEFT neighbor of the current cell
            childrenCoords[3] = isInBounds(current.getX() + 1, current.getY(), cyclic); // the RIGHT neighbor of the current cell

            for (int i = 0; i < 4; i++)
            {
                if (isInside(childrenCoords[i]) && exploreMap[childrenCoords[i].getY()][childrenCoords[i].getX()] == -2)
                {                                                           // check if (is inside && is a not explored cell)
                    exploreQueue.add(childrenCoords[i]);                    // add the neighbor to the explore queue
                    exploreMap[childrenCoords[i].getY()][childrenCoords[i].getX()] = distance + 1;  // mark the explore map with the distance of this neighbor
                }
            }
        }
        for (int x = 0; x < exploreMap[0].length; x++)
        {
            for (int y = 0; y < exploreMap.length; y++)
            {
                if (exploreMap[y][x] == -2)
                    exploreMap[y][x] = -1;  // unreachable
            }
        }
        return new Map(exploreMap);
    }

	////////////////////// Private Methods ///////////////////////

    private void DrawLineX(int x1, int y1, int x2, int y2, int color)
    {
        double a = (double)(y2 - y1) / (x2 - x1);   // compute the A element
        double b = y1 - x1 * a;                     // compute the B element
        for (int x = x1; x <= x2; x++)              // loop through all the x values between x1 and x2
        {
            double fx = a * x + b;                  // compute the y value for the current x
            _map[(int) fx][x] = color;
        }
    }

    private void DrawLineY(int x1, int y1, int x2, int y2, int color)
    {
        double a = (double)(y2 - y1) / (x2 - x1);       // compute the A element
        double b = y1 - x1 * a;                         // compute the B element
        for (int y = y1; y <= y2; y++)                  // loop through all the y values between y1 and y2
        {
            double gy = (x1 == x2) ? x1 : (y + b) / a;  // compute the x value for the current y
            _map[y][(int) gy] = color;
        }
    }

    /**
     * recursively checks if the current cell is valid to fill,
     * if true fills it and moves to check its neighbors
     * @param p1 The pos of the cell to start from
     * @param oldColor The old color to search for
     * @param color The new color to fill
     * @param cyclic If the map is cyclic
     * @return The number of cells filled
     */
    private int fillPixels(Pixel2D p1, int oldColor, int color, boolean cyclic)
    {
        int ans = 0;

        Index2D currentP = isInBounds(p1.getX(), p1.getY(), cyclic);    // checks if p1 is in the map (if not and is cyclic then moves the point to the other side)
        if (isInside(currentP) && getPixel(currentP) == oldColor)       // checks if the new corrected point is inside the map and if the color is oldColor
        {
            ans = 1;                                                    // add this cell to the number of cells filled
            setPixel(currentP, color);                                  // fill this cell
            ans += fillPixels(new Index2D(currentP.getX() + 1, currentP.getY()), oldColor, color, cyclic);  // move to the right neighbor
            ans += fillPixels(new Index2D(currentP.getX(), currentP.getY() + 1), oldColor, color, cyclic);  // move to the up neighbor
            ans += fillPixels(new Index2D(currentP.getX() - 1, currentP.getY()), oldColor, color, cyclic);  // move to the left neighbor
            ans += fillPixels(new Index2D(currentP.getX(), currentP.getY() - 1), oldColor, color, cyclic);  // move to the down neighbor
        }
        return ans;
    }

    /**
     * Checks if the x,y pos is in the map, if not and is cyclic moves it to the other side
     * @param x The x pos
     * @param y The y pos
     * @param cyclic If the map is cyclic
     * @return The corrected pos or (-1,y) | (x,-1) for the part that is out of bounds
     */
    private Index2D isInBounds(int x, int y, boolean cyclic)
    {
        // ======= X =======
        if (x >= _map[0].length)
        {
            if (cyclic)
                x = 0;
            else
                x = -1;
        }
        else if (x < 0)
        {
            if (cyclic)
                x = _map[0].length - 1;
            else
                x = -1;
        }
        // ======= Y =======
        if (y >= _map.length)
        {
            if (cyclic)
                y = 0;
            else
                y = -1;
        }
        else if (y < 0)
        {
            if (cyclic)
                y = _map.length - 1;
            else
                y = -1;
        }
        return new Index2D(x, y);
    }

}
