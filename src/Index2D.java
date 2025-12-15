

public class Index2D implements Pixel2D
{
    private final int _x;
    private final int _y;


    public Index2D(int w, int h)
    {
        _x = w;
        _y = h;
    }

    public Index2D(Pixel2D other)
    {
        if (other == null)
        {
            throw new NullPointerException("Pixel2D object is null");
        }
        _x = other.getX();
        _y = other.getY();
    }
    @Override
    public int getX()
    {
        return _x;
    }

    @Override
    public int getY()
    {
        return _y;
    }

    @Override
    public double distance2D(Pixel2D p2)
    {
        if (p2 == null)
        {
            throw new NullPointerException("Pixel2D p2 is null");
        }
        return Math.sqrt(Math.pow(_x-p2.getX(), 2) + Math.pow(_y-p2.getY(), 2));
    }

    @Override
    public String toString()
    {
        return "(" + _x + ", " + _y + ")";
    }

    @Override
    public boolean equals(Object p)
    {
        if (p instanceof Index2D)
        {
            return ((Index2D) p).getX() == _x && ((Index2D) p).getY() == _y;
        }
        return false;
    }
}
