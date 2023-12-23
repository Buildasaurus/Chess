package chess;

public class Point
{
    int y;
    int x;

    public Point(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    public Point add(Point other)
    {
        return new Point(x + other.x, y + other.y);
    }

    public Point add(int scalar)
    {
        return new Point(x + scalar, y + scalar);
    }

    public Point add(int _x, int _y)
    {
        return new Point(x + _x, y + _y);
    }

    public Point multiply(int scalar)
    {
        return new Point(x * scalar, y * scalar);
    }

    @Override
    public String toString()
    {
        return x + ", " + y;
    }

    public boolean equals(Point other)
    {
        return other.x == x && other.y == y;
    }

    /**
     * Returns the largest distance between two squares, on the x, and y coordinate
     *
     * @param other the other point to compare distance to.
     * @return the largest distance between two points
     */
    public int distance(Point other)
    {
        return Math.max(Math.abs(other.x - x), Math.abs(other.y - y));
    }
}
