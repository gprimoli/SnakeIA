package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Snake;
import SnakeGame.SnakeBoard;
import SnakeGame.Util.UtilBase;
import SnakeGame.Util.Utilities;

import java.awt.*;
import java.util.LinkedList;

public class IA extends SnakeBoard {
    static final Direction[] avaiableDirection = new Direction[]{Direction.Up, Direction.Left, Direction.Right};
    boolean ready;

    public IA(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
        reset();
        ready = false;
    }

    public UtilBase simulate(LinkedList<Direction> moves) {
        Snake s = new Snake(getSnake());
        s.setRelativeMoves(moves);
        while (!s.isMovesFinisched()) {
            s.move();
            if (s.checkSelfCollision() || outOfTheMap(s) || checkCollition(s)) {
                s.kill();
                break;
            }
        }
        return new UtilBase(s.getHead(), s.isAlive(), moves);
    }

    public int getDistanceFromApple(Snake s){
        int distance = Integer.MAX_VALUE;
        for (Point p : getFood()) {
            int x = Utilities.distanzaManhattan(s.getHead(), p);
            if (x < distance)
                distance = x;
        }
        return distance;
    }

    public int getDistanceFromApple(Point head){
        int distance = Integer.MAX_VALUE;
        for (Point p : getFood()) {
            int x = Utilities.distanzaManhattan(head, p);
            if (x < distance)
                distance = x;
        }
        return distance;
    }
}
