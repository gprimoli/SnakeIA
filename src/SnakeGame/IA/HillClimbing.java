package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.UtilEuristic;

import java.util.LinkedList;
import java.util.Random;

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


    public UtilEuristic simulateOneDirectoin(Direction d) {
        Snake s = new Snake(getSnake());
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
            UtilEuristic vicino = simulateOneDirectoin(d);
            if(vicino.isAlive() && vicino.compareTo(corrente) < 0)
                corrente = vicino;

        }
        if (corrente.getMoves().size() == 0){
            int avaiableDirectionSize = avaiableDirection.length;
            Random r = new Random();
            corrente.getMoves().add(avaiableDirection[r.nextInt(avaiableDirectionSize)]);
        }
        ready = true;

        lunghezzaFinale = ia.getCoords().size();

        return corrente.getMoves().getFirst();
    }
}
