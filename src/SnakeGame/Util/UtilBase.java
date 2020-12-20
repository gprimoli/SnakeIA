package SnakeGame.Util;

import SnakeGame.Enum.Direction;

import java.awt.*;
import java.util.LinkedList;

public class UtilBase {
    private Point head;
    private LinkedList<Direction> moves;
    private boolean alive;

    public UtilBase(Point head, boolean alive) {
        this.head = head;
        this.moves = new LinkedList<>();
        this.alive = alive;
    }

    public UtilBase(Point head, boolean alive, LinkedList<Direction> moves) {
        this.head = head;
        this.moves = new LinkedList<>();
        this.moves.addAll(moves);
        this.alive = alive;
    }

    public UtilBase(UtilBase c) {
        this.head = c.head;
        this.moves = new LinkedList<>();
        this.moves.addAll(c.moves);
        this.alive = c.alive;
    }

    public LinkedList<Direction> fakeAdd(Direction d){
        LinkedList<Direction> fake = new LinkedList<>();
        fake.addAll(this.moves);
        fake.add(d);
        return fake;
    }

    public LinkedList<Direction> getMoves() {
        return moves;
    }

    public Point getHead() {
        return head;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtilBase)) return false;
        UtilBase utilBase = (UtilBase) o;
        return head.equals(utilBase.head);
    }

}
