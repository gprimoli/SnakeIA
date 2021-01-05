
package SnakeGame.IA;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;
import SnakeGame.Snake;
import SnakeGame.Util.UtilEuristic;

import java.awt.*;
import java.util.*;

public class BestFirstAstar extends IA {
    public BestFirstAstar(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
    }

    public GameStatus update() {
        Snake ia = getSnake();

        if (ia.isMovesFinisched()) {
            ia.setRelativeMoves(getMossa());
            ready = true;
        }
        if (ready)
            return super.update();
        return GameStatus.Waiting;
    }

    public UtilEuristic simulate(LinkedList<Direction> moves) {
        Snake s = new Snake(getSnake());
        s.setRelativeMoves(moves);
        while (!s.isMovesFinisched()) {
            s.move();
            if (s.checkSelfCollision() || outOfTheMap(s) || checkCollition(s)) {
                s.kill();
                break;
            }
        }
        return new UtilEuristic(s.getHead(), s.isAlive(), moves, getDistanceFromApple(s));
    }



    private LinkedList<Direction> getMossa() {
        ready = false;
        Snake ia = getSnake();


        PriorityQueue<UtilEuristic> frontiera = new PriorityQueue<>();
        HashSet<Point> esplorati = new HashSet<>();
        frontiera.add(new UtilEuristic(ia.getHead(), ia.isAlive(), 0));

        long timeStart = System.currentTimeMillis();
        while (!frontiera.isEmpty()) {
            nodiEsplorati++;
            UtilEuristic padre = frontiera.poll();
            if (padre.isAlive() && isFood(padre.getHead())) {
                long timeEnd = System.currentTimeMillis();
                tempo += (timeEnd - timeStart);
                nodiAllaSolzuione += padre.getMoves().size();
                System.out.println(nodiEsplorati + " " + nodiAllaSolzuione + " "+ tempo);

//                System.out.println("nodi esplorati: " + nodiEsplorati);
//                String out = "Tempo Necessario per la ricerca: "
//                        + (timeEnd - timeStart) +
//                        " millisecondi\n"
//                        + padre.getMoves().size() +
//                        " mosse necessarie per arrivare ad una soluzione" + "\n";
//                System.out.println(out);
                return padre.getMoves();
            }
            esplorati.add(padre.getHead());

            for (Direction d : avaiableDirection) {
                UtilEuristic figlio = simulate(padre.fakeAdd(d));

                if (!esplorati.contains(figlio.getHead()) && !frontiera.contains(figlio))
                    frontiera.add(figlio);
                else
                    for (UtilEuristic u : frontiera)
                        if (u.equals(figlio) && u.compareTo(figlio) > 0) {
                            frontiera.remove(u);
                            frontiera.add(figlio);
                            break;
                        }
            }
        }

        lunghezzaFinale = ia.getCoords().size();
        System.out.println("Nessuna mossa trovata");
        return new LinkedList<>();
    }
}
