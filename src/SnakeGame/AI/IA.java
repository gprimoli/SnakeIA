package SnakeGame.AI;

import SnakeGame.Enum.Direction;
import SnakeGame.Snake;
import SnakeGame.SnakeBoard;
import SnakeGame.Util.UtilBase;

import java.awt.*;
import java.util.LinkedList;

public class IA extends SnakeBoard {
    Snake ia;
    boolean ready;

    public IA(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
        ia = new Snake(2, 2, Direction.Up, Color.BLUE);
        addSnake(ia);
        ready = false;
    }

    public UtilBase simulate(LinkedList<Direction> moves) {
        Snake s = new Snake(ia);
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
}
