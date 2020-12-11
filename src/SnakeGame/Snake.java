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
    private LinkedList<Direction> relativeMoves;


    public Snake(int x, int y, Direction direction, Color color) {
        this.id = ++idCounter;
        this.alive = true;
        this.direction = direction;
        this.color = color;
        this.relativeMoves = new LinkedList<>();

        this.coords = new LinkedList<>();
        coords.add(new Point(x, y));
        coords.add(new Point(x, y));

        assert coords.peekLast() != null;
        switch (direction) {
            case Up -> coords.peekLast().translate(0, +1);
            case Down -> coords.peekLast().translate(0, -1);
            case Left -> coords.peekLast().translate(+1, 0);
            case Right -> coords.peekLast().translate(-1, 0);
        }
    }

    public Snake(Snake snake) {
        this.id = snake.id;
        this.alive = snake.alive;
        this.direction = snake.direction;
        this.color = snake.color;
        this.relativeMoves = new LinkedList<>();

        this.coords = new LinkedList<>();
        coords.addAll(snake.getCoords());
    }





    //action
    public LinkedList<Point> move() {
        //Bug quando lo snake è composto da solo due punti
        //Se si cambia velocemnte posizione puoi andare nella direzione opposta...
        assert coords.peekFirst() != null;
        coords.addFirst(new Point(coords.peekFirst()));
        coords.pollLast();
        if(relativeMoves.size() != 0)
            setRelativeDirection(relativeMoves.poll());
        switch (direction) {
            case Up -> coords.getFirst().translate(0, -1);
            case Down -> coords.getFirst().translate(0, +1);
            case Left -> coords.getFirst().translate(-1, 0);
            case Right -> coords.getFirst().translate(+1, 0);
        }
        return coords;
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
    public LinkedList<Point> getCoords() {
        return coords;
    }

    public Color getColor() {
        return color;
    }

    public boolean isMovesFinisched(){
        return relativeMoves.size() == 0;
    }

    public Direction getCurrentDirection() {
        return direction;
    }

    public boolean isAlive() {
        return alive;
    }

    public Point getHead() {
        return this.coords.getFirst();
    }





    //Public Setter
    public void setRelativeMoves(LinkedList<Direction> relativeMoves) {
        this.relativeMoves.addAll(relativeMoves);
    }

    public void setAbsoluteDirection(Direction d) {
        if (!direction.equals(d) && !direction.equals(d.getOpposite()))
            this.direction = d;
    }

    public void setRelativeDirection(Direction d) {
        switch (d) {
            case Left -> {
                switch (direction) {
                    case Up -> setAbsoluteDirection(Direction.Left);
                    case Down -> setAbsoluteDirection(Direction.Right);
                    case Left -> setAbsoluteDirection(Direction.Down);
                    case Right -> setAbsoluteDirection(Direction.Up);
                }
            }
            case Right -> {
                switch (direction) {
                    case Up -> setAbsoluteDirection(Direction.Right);
                    case Down -> setAbsoluteDirection(Direction.Left);
                    case Left -> setAbsoluteDirection(Direction.Up);
                    case Right -> setAbsoluteDirection(Direction.Down);
                }
            }
        }
    }





    //Util
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Snake)) return false;
        Snake that = (Snake) o;
        return that.id == this.id;
    }

    public boolean contain(Point point) {
        for (Point p : coords) {
            if (p.equals(point))
                return true;
        }
        return false;
    }
}