package SnakeGame.Util;

import java.awt.*;

public class GeometricalDistance {
    public static int distanzaManhattan(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}
