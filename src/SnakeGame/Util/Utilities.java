package SnakeGame.Util;

import java.awt.*;

public class Utilities {
    public static int distanzaManhattan(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
    public static double probability(int f1, int f2, double temp) {
        double delta = f2 - f1;
        if (delta > 0) return 1;
        return Math.exp((f1 - f2) / temp);
    }
}
