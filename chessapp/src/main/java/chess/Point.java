package chess;

public class Point {
    int y;
    int x;
    public Point(int _rank, int _file)
    {
        y = _rank;
        x = _file;
    }

    public Point add(Point other)
    {
        return new Point(y+other.y, x+other.x);
    }

    public Point multiply(int scalar)
    {
        return new Point(y*scalar, x*scalar);
    }
}
