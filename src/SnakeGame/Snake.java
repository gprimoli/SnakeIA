package SnakeGame;

import SnakeGame.Enum.Direction;

import java.awt.*;
import java.util.*;

public class Snake {
    private static int idCounter = 0;
    private final int id;
    private final LinkedList<Point> coords;
    private final Color color;
    private boolean alive;
    private Direction direction;

    public Snake(int x, int y, Direction direction, Color color) {
        this.id = ++idCounter;
        this.alive = true;
        this.coords = new LinkedList<>();
        this.direction = direction;
        this.color = color;

        coords.add(new Point(x, y));
        coords.add(new Point(x, y));

        assert coords.peekLast() != null;
        switch (direction) {
            case Up -> coords.peekLast().translate(0, -1);
            case Down -> coords.peekLast().translate(0, +1);
            case Left -> coords.peekLast().translate(-1, 0);
            case Right -> coords.peekLast().translate(+1, 0);
        }
    }

    //action
    public LinkedList<Point> move() {
        //Bug quando lo snake è composto da solo due punti
        //Se si cambia velocemnte posizione puoi andare nella direzione opposta...
        assert coords.peekFirst() != null;
        coords.addFirst(new Point(coords.peekFirst()));
        coords.pollLast();
        switch (direction) {
            case Up -> coords.getFirst().translate(0, -1);
            case Down -> coords.getFirst().translate(0, +1);
            case Left -> coords.getFirst().translate(-1, 0);
            case Right -> coords.getFirst().translate(+1, 0);
        }
        return coords;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean checkSelfCollision() {
        assert coords.peekFirst() != null;
        //La testa si sovrappone ad una parte del corpo
        //Nel caso in cui lo snake mangia una mela gli ultimi due punti del suo corpo sono sovrapposti
        //Ma con questa funzione contiamo solo le frequenze del primo punto!
        return Collections.frequency(coords, coords.peekFirst()) > 1;
    }

    public void eat() {
        assert coords.peekLast() != null;
        coords.addLast(new Point(coords.peekLast()));
    }

    public void kill() {
        alive = false;
    }

    //Public Getters
    public int getId() {
        return id;
    }

    public LinkedList<Point> getCoords() {
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snake)) return false;
        Snake that = (Snake) o;
        return that.getId() == this.getId();
    }


    //Util
    public Point getHead() {
        return this.coords.getFirst();
    }

    public boolean contain(Point point) {
        for (Point p : coords) {
            if (p.equals(point))
                return true;
        }
        return false;
    }

    //Debug Only
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