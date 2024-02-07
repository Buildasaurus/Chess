package chess.Models.Board;

public class Point
{
    public int y;
    public int x;

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

    public Point subtract(Point other)
    {
        return new Point(x - other.x, y - other.y);
    }

    public Point subtract(int scalar)
    {
        return new Point(x - scalar, y - scalar);
    }

    public Point subtract(int _x, int _y)
    {
        return new Point(x - _x, y - _y);
    }

    public Point multiply(int scalar)
    {
        return new Point(x * scalar, y * scalar);
    }

    public Point divide(double scalar)
    {
        return new Point((int) Math.round(x / scalar), (int) Math.round(y / scalar));
    }

    // Method to check if two points are parallel
    public boolean isParallel(Point other)
    {
        // Cross product for 2D is just (x1*y2 - y1*x2)
        double crossProduct = this.x * other.y - this.y * other.x;
        return crossProduct == 0;
    }

    public Point unitVector()
    {
        return this.divide(this.length());
    }

    public double length()
    {
        return Math.sqrt(x * x + y * y);
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
