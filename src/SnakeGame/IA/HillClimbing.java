package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.UtilBase;
import SnakeGame.Util.UtilEuristic;

import java.util.LinkedList;

public class HillClimbing extends IA{
    public HillClimbing(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
    }

    public GameStatus update() {
        Snake ia = getSnake();

        ia.setRelativeDirection(getMossa());
        if (ready)
            return super.update();
        return GameStatus.Waiting;
    }


    public UtilBase simulateOneDirectoin(Direction d) {
        Snake ia = getSnake();
        Snake s = new Snake(ia);
        s.setRelativeDirection(d);
        s.move();
        if (s.checkSelfCollision() || outOfTheMap(s) || checkCollition(s))
            s.kill();
        LinkedList<Direction> moves = new LinkedList<>();
        moves.add(d);
        return new UtilEuristic(s.getHead(), s.isAlive(), moves, getDistanceFromApple(s));
    }

    private Direction getMossa(){
        ready = false;
        Snake ia = getSnake();

        UtilEuristic corrente = new UtilEuristic(ia.getHead(), ia.isAlive(), getDistanceFromApple(ia));
        for(Direction d : avaiableDirection){
            UtilEuristic vicino = (UtilEuristic) simulateOneDirectoin(d);
            if(vicino.isAlive() && vicino.compareTo(corrente) < 0)
                corrente = vicino;
        }
        ready = true;
        return corrente.getMoves().getFirst();
    }
}
