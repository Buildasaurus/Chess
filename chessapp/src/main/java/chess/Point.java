package chess;

public class Point {
    int y;
    int x;
    public Point(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    public Point add(Point other)
    {
        return new Point(x+other.x, y+other.y);
    }

    public Point multiply(int scalar)
    {
        return new Point(x*scalar, y*scalar);
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
}
