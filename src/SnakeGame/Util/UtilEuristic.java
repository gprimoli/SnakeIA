package SnakeGame.Util;

import SnakeGame.Enum.Direction;

import java.awt.*;
import java.util.LinkedList;

public class UtilEuristic extends UtilBase implements Comparable<UtilEuristic>{
    private int f_n;

    public UtilEuristic(Point head, boolean alive, int f) {
        super(head, alive);
        this.f_n = f;
    }

    public UtilEuristic(Point head, boolean alive, LinkedList<Direction> moves, int f) {
        super(head, alive, moves);
        this.f_n = f;
    }

    public int compareTo(UtilEuristic o) {
        return this.f_n - o.f_n;
    }
}
