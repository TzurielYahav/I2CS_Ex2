import java.io.Serializable;
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
        _map = new int[w][h];
        for (int i = 0; i < w * h; i++)
        {
            _map[i % h][i / h] = v;
        }
	}

	@Override
	public void init(int[][] arr)
    {
        _map = new int[arr.length][arr[0].length];
        int w = arr.length, h = arr[0].length;
        for (int i = 0; i < w * h; i++)
        {
            _map[i % h][i / h] = arr[i % h][i / h];
        }
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
        return _map[x][y];
    }

	@Override
	public int getPixel(Pixel2D p)
    {
        if (p == null)
            throw new NullPointerException("Pixel2D p is null");
        return _map[p.getX()][p.getY()];
	}

	@Override
	public void setPixel(int x, int y, int v)
    {
        _map[x][y] = v;
    }

	@Override
	public void setPixel(Pixel2D p, int v)
    {
        if (p == null)
            throw new NullPointerException("Pixel2D p is null");
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
                if (center.distance2D(new Index2D(startX + i, startY + i)) <= rad)
                    _map[startX + i][startY + i] = color;
            }
        }
    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color)
    {
        if (p1 == null || p2 == null)
            throw new NullPointerException("Pixel2D p1 or p2 is null");
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

    }

    @Override
    public boolean equals(Object ob)
    {
        boolean ans = false;

        return ans;
    }
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v,  boolean cyclic)
    {
		int ans = -1;

		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic)
    {
		Pixel2D[] ans = null;  // the result.

		return ans;
	}
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic)
    {
        Map2D ans = null;  // the result.

        return ans;
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
}
