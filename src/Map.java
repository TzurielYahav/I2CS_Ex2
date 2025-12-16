import java.io.Serializable;
import java.util.*;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable
{
    private int[][] _map;

	/**
	 * Constructs a w * h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
	 * Constructs a square map (size*size) with values of 0.
	 * @param size
	 */
	public Map(int size) {
        this(size,size, 0);
    }
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

    @Override
	public void init(int w, int h, int v)
    {
        if (w <= 0 || h <= 0)
            throw new IllegalArgumentException("Invalid width or height parameter");
        _map = new int[w][h];
        for (int i = 0; i < w * h; i++)
        {
            _map[i % h][i / h] = v;
        }
	}

	@Override
	public void init(int[][] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i].length != arr[0].length)
                throw new IllegalArgumentException("Invalid array length");
        }
        _map = arr.clone();
	}

	@Override
	public int[][] getMap()
    {
		return _map.clone();
	}

	@Override
	public int getWidth()
    {
        return _map.length;
    }

	@Override
	public int getHeight()
    {
        return _map[0].length;
    }

	@Override
	public int getPixel(int x, int y)
    {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            throw new IllegalArgumentException("x or y is not inside the map");
        return _map[x][y];
    }

	@Override
	public int getPixel(Pixel2D p)
    {
        if (!isInside(p))
            throw new IllegalArgumentException("Pixel2D is not inside the map");
        return _map[p.getX()][p.getY()];
	}

	@Override
	public void setPixel(int x, int y, int v)
    {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            throw new IllegalArgumentException("x or y is not inside the map");
        _map[x][y] = v;
    }

	@Override
	public void setPixel(Pixel2D p, int v)
    {
        if (!isInside(p))
            throw new IllegalArgumentException("Pixel2D is not inside the map");
        _map[p.getX()][p.getY()] = v;
	}

    @Override
    public boolean isInside(Pixel2D p)
    {
        if (p == null)
            throw new NullPointerException("Pixel2D p is null");
        if (p.getX() >= 0 && p.getY() >= 0)
            return p.getX() < _map.length && p.getY() < _map[0].length;
        return false;
    }

    @Override
    public boolean sameDimensions(Map2D p)
    {
        if (p == null)
            throw new NullPointerException("Map2D p is null");
        int[][] otherMap = p.getMap();
        return _map.length == otherMap.length && _map[0].length == otherMap[0].length;
    }

    @Override
    public void addMap2D(Map2D p)
    {
        if (p == null)
            throw new NullPointerException("Map2D p is null");
        if (sameDimensions(p))
        {
            int[][] otherMap = p.getMap();
            int w = _map.length, h = _map[0].length;
            for (int i = 0; i < w * h; i++)
            {
                _map[i % h][i / h] += otherMap[i % h][i / h];
            }
        }
    }

    @Override
    public void mul(double scalar)
    {
        int w = _map.length, h = _map[0].length;
        for (int i = 0; i < w * h; i++)
        {
            _map[i % h][i / h] = (int) ((double)_map[i % h][i / h] * scalar);
        }
    }

    @Override
    public void rescale(double sx, double sy)
    {
        int[][] newMap = new int[(int) ((double)_map.length * sx)][(int) ((double)_map[0].length * sy)];
        int w = newMap.length, h = newMap[0].length;
        for (int i = 0; i < w * h; i++)
        {
            int x = i % h, y = i / h;
            if (x < _map.length && y < _map[0].length)
            {
                newMap[x][y] = _map[x][y];
            }
        }
    }

    @Override
    public void drawCircle(Pixel2D center, double rad, int color)
    {
        if (center == null)
            throw new NullPointerException("Pixel2D center is null");
        if (rad <= 0)
            throw new IllegalArgumentException("rad is out of range");
        int w = _map.length, h = _map[0].length;
        int startX = (int) ((double)center.getX() - rad);
        int startY = (int) ((double)center.getY() - rad);
        for (int i = 0; i < rad; i++)
        {
            for (int j = 0; j < rad; j++)
            {
                if (isInside(new Index2D(startX + i, startY + j)))
                    if (center.distance2D(new Index2D(startX + i, startY + j)) <= rad)
                        _map[startX + i][startY + j] = color;
            }
        }
    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color)
    {
        if (!isInside(p1) || !isInside(p2))
            throw new RuntimeException("p1 or p2 is out of range");
        if (p1.equals(p2))
        {
            _map[p1.getX()][p1.getY()] = color;
            return;
        }
        int dx = Math.abs(p2.getX() - p1.getX());
        int dy = Math.abs(p2.getY() - p1.getY());
        if (dx >= dy)
        {
            if (p1.getX() <= p2.getX())
                DrawLineX(p1.getX(), p1.getY(), p2.getX(), p2.getY(), color);
            else
                DrawLineX(p2.getX(), p2.getY(), p1.getX(), p1.getY(), color);
        }
        else
        {
            if (p1.getX() <= p2.getX())
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
        int x1 = Math.min(p1.getX(), p2.getX());
        int x2 = Math.max(p1.getX(), p2.getX());
        int y1 = Math.min(p1.getY(), p2.getY());
        int y2 = Math.max(p1.getY(), p2.getY());
        for (int i = x1; i <= x2; i++)
        {
            for (int j = y1; j <= y2; j++)
            {
                _map[i][j] = color;
            }
        }
    }

    @Override
    public boolean equals(Object ob)
    {
        if (ob instanceof Map)
        {
            int[][] otherMap = ((Map) ob).getMap();
            if (_map.length == otherMap.length &&  _map[0].length == otherMap[0].length)
            {
                int w = _map.length, h = _map[0].length;
                for (int i = 0; i < w * h; i++)
                {
                    if (_map[i % h][i / h] !=  otherMap[i % h][i / h])
                        return false;
                }
                return true;
            }
        }
        return false;
    }

	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v,  boolean cyclic)
    {
        if (!isInside(xy))
            throw new RuntimeException("xy is out of range");
        int ans = 0;
        int oldColor = _map[xy.getX()][xy.getY()];
        if (oldColor != new_v)
        {
            ans = fillPixels(xy, oldColor, new_v, cyclic);
        }
		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic)
    {
        if (!isInside(p1) || !isInside(p2))
            throw new RuntimeException("p1 or p2 is out of range");
        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor)
            throw new RuntimeException("p1 or p2 is unreachable");
        if (p1.equals(p2))
            return new Pixel2D[]{p1, p2};

        boolean[][] exploreMap = new boolean[_map.length][_map[0].length];

        ArrayList<Pixel2D> exploreQueue = new ArrayList<Pixel2D>();
        ArrayList<Pixel2D> parentQueue = new ArrayList<Pixel2D>();

        exploreQueue.add(p1);
        parentQueue.add(p1);

        int currentPixel = 0;
        boolean found = false;
        while (!found && currentPixel < exploreQueue.size())
        {
            ArrayList<Pixel2D> children = shortestPathGetChildren(exploreQueue.get(currentPixel), p2, exploreMap, obsColor, cyclic);
            for (Pixel2D child : children)
            {
                exploreQueue.add(child);
                parentQueue.add(exploreQueue.get(currentPixel));
                if (child.equals(p2))
                {
                    found = true;
                    break;
                }
            }
            currentPixel++;
        }
        if (!found)
        {
            return null;
        }

        ArrayList<Pixel2D> ansQueue = new ArrayList<Pixel2D>();
        ansQueue.add(exploreQueue.getLast());
        ansQueue.add(parentQueue.getLast());
        for (int i = exploreQueue.size() - 1; i > 0; i--)
        {
            if (exploreQueue.get(i).equals(ansQueue.getLast()))
            {
                ansQueue.add(exploreQueue.get(i));
                ansQueue.add(parentQueue.get(i));
            }
        }
        Pixel2D[] ans = new Pixel2D[ansQueue.size()];
        for (int i = 0; i < ans.length; i++)
        {
            ans[i] = ansQueue.getLast();
            ansQueue.removeLast();
        }
        return ans;
	}

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic)
    {
        if (!isInside(start))
            throw new RuntimeException("start is out of range");
        if (getPixel(start) == obsColor)
            throw new RuntimeException("start is unreachable");

        int[][] exploreMap = new int[_map.length][_map[0].length];
        for (int x = 0; x < _map.length; x++)
        {
            for (int y = 0; y < _map[0].length; y++)
            {
                if (_map[x][y] == obsColor)
                    exploreMap[x][y] = -1; // obstacle color
                else
                    exploreMap[x][y] = -2; // Not checked color
            }
        }

        ArrayList<Pixel2D> exploreQueue = new ArrayList<Pixel2D>();
        exploreMap[start.getX()][start.getY()] =  0;
        exploreQueue.add(start);
        while (!exploreQueue.isEmpty())
        {
            exploreQueue.addAll(allDistancesRec(exploreQueue.getFirst(), exploreMap, cyclic));
            exploreQueue.removeFirst();
        }
        return new Map(exploreMap);
    }

	////////////////////// Private Methods ///////////////////////

    private void DrawLineX(int x1, int y1, int x2, int y2, int color)
    {
        double a = (double)(y2 - y1) / (x2 - x1);   // compute the A element
        double b = y1 - x1 * a;                     // compute the B element
        for (int x = x1; x <= x2; x++)
        {
            double fx = a * x + b;
            _map[x][(int) fx] = color;
        }
    }

    private void DrawLineY(int x1, int y1, int x2, int y2, int color)
    {
        double a = (double)(y2 - y1) / (x2 - x1);   // compute the A element
        double b = y1 - x1 * a;                     // compute the B element
        for (int y = y1; y <= y2; y++)
        {
            double gy = (y + b) / a;
            _map[(int) gy][y] = color;
        }
    }

    private int fillPixels(Pixel2D p1, int oldColor, int color, boolean cyclic)
    {
        int ans = 0;

        Index2D currentP = isInBounds(p1.getX(), p1.getY(), cyclic);
        if (isInside(currentP) && getPixel(currentP) == oldColor)
        {
            ans = 1;
            setPixel(currentP, color);
            ans += fillPixels(new Index2D(currentP.getX() + 1, currentP.getY()), oldColor, color, cyclic);
            ans += fillPixels(new Index2D(currentP.getX(), currentP.getY() + 1), oldColor, color, cyclic);
            ans += fillPixels(new Index2D(currentP.getX() - 1, currentP.getY()), oldColor, color, cyclic);
            ans += fillPixels(new Index2D(currentP.getX(), currentP.getY() - 1), oldColor, color, cyclic);
        }
        return ans;
    }

    private Index2D isInBounds(int x, int y, boolean cyclic)
    {
        if (x >= _map.length)
        {
            if (cyclic)
                x = 0;
            else
                x = -1;
        }
        else if (x < 0)
        {
            if (cyclic)
                x = _map.length - 1;
            else
                x = -1;
        }

        if (y >= _map[0].length)
        {
            if (cyclic)
                y = 0;
            else
                y = -1;
        }
        else if (y < 0)
        {
            if (cyclic)
                y = _map[0].length - 1;
            else
                y = -1;
        }
        return new Index2D(x, y);
    }

    private ArrayList<Pixel2D> shortestPathGetChildren(Pixel2D p1, Pixel2D p2, boolean[][] exploreMap, int obsColor, boolean cyclic)
    {
        int x = p1.getX();
        int y = p1.getY();

        ArrayList<Pixel2D> children = new ArrayList<Pixel2D>();
        exploreMap[x][y] = true;

        Index2D up = isInBounds(x, y + 1, cyclic);
        Index2D down = isInBounds(x, y - 1, cyclic);
        Index2D left = isInBounds(x - 1, y, cyclic);
        Index2D right = isInBounds(x + 1, y, cyclic);
        if (isInside(up) && getPixel(up) != obsColor && !exploreMap[x][y + 1])
        {
            children.add(up);
        }
        if (isInside(down) && getPixel(down) != obsColor && !exploreMap[x][y - 1])
        {
            children.add(down);
        }
        if (isInside(left) && getPixel(left) != obsColor && !exploreMap[x - 1][y])
        {
            children.add(left);
        }
        if (isInside(right) && getPixel(right) != obsColor && !exploreMap[x + 1][y])
        {
            children.add(right);
        }
        return children;
    }

    private ArrayList<Pixel2D> allDistancesRec(Pixel2D start, int[][] exploreMap, boolean cyclic)
    {
        int distance = exploreMap[start.getX()][start.getY()];
        ArrayList<Pixel2D> children = new ArrayList<Pixel2D>();

        Index2D[] childrenCoords = new Index2D[4];
        childrenCoords[0] = isInBounds(start.getX(), start.getY() + 1, cyclic);
        childrenCoords[1] = isInBounds(start.getX(), start.getY() - 1, cyclic);
        childrenCoords[2] = isInBounds(start.getX() - 1, start.getY(), cyclic);
        childrenCoords[3] = isInBounds(start.getX() + 1, start.getY(), cyclic);

        for (int i = 0; i < 4; i++)
        {
            if (isInside(childrenCoords[i]) && exploreMap[childrenCoords[i].getX()][childrenCoords[i].getY()] == -2)
            {
                children.add(childrenCoords[i]);
                exploreMap[childrenCoords[i].getX()][childrenCoords[i].getY()] = distance + 1;
            }
        }
        return children;
    }
}
