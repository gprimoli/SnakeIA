package SnakeGame;

import SnakeGame.Enum.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private static int idCounter = 0;
    private final int id;
    private boolean alive;
    private List<Point> coords;
    private Direction direction;
    private Color color;

    public Snake(int x, int y, Direction direction, Color color) {
        this.id = ++idCounter;
        this.alive = true;
        this.coords = new ArrayList<>();
        this.direction = direction;
        this.color = color;

        coords.add(new Point(x, y));
        Point p = new Point(x, y);
        switch (direction) {
            case Up -> p.translate(0, +1);
            case Down -> p.translate(0, -1);
            case Left -> p.translate(+1, 0);
            case Right -> p.translate(-1, 0);
        }
        coords.add(p);
    }


    //action
    public void move() {
        Point newHead = new Point(getHead());
        coords.remove(getLenght() - 1);
        switch (direction) {
            case Up -> newHead.translate(0, -1);
            case Down -> newHead.translate(0, +1);
            case Left -> newHead.translate(-1, 0);
            case Right -> newHead.translate(+1, 0);
        }
        coords.add(0, newHead);
        if (checkSelfCollision())
            kill();
    }

    public synchronized void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean checkSelfCollision() {
        Point head = getHead();
        for (int i = 1; i < coords.size(); i++)
            if (head.equals(coords.get(i)))
                return true;
        return false;
    }

    public void eat() {
        coords.add(new Point(getTail()));
    }

    public void kill() {
        alive = false;
    }


    //Public Getters
    public int getId() {
        return id;
    }

    public List<Point> getCoords() {
        return coords;
    }

    public Direction getDirection() {
        return direction;
    }

    public Color getColor() {
        return color;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getLenght() {
        return coords.size();
    }

    public Point getHead() {
        return coords.get(0);
    }

    //Private Getters
    private Point getTail() {
        return new Point(coords.get(getLenght() - 1).x, coords.get(getLenght() - 1).y);
    }


    //Debug Only
    @Override
    public String toString() {
        return "Snake{" +
                "id=" + id +
                ", alive=" + alive +
                ", coords=" + coords +
                ", direction=" + direction +
                ", color=" + color +
                '}';
    }
}