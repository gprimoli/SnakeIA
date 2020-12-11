package SnakeGame;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;

import java.awt.*;
import java.util.*;

public class IATest extends SnakeBoard {
    private Snake ia;
    private boolean ready;

    class Util {
        private Point head;
        private LinkedList<Direction> moves;
        private boolean alive;

        public Util(Point head, boolean alive) {
            this.head = head;
            this.moves = new LinkedList<>();
            this.alive = alive;
        }

        public Util(Point head, boolean alive, LinkedList<Direction> moves) {
            this.head = head;
            this.moves = new LinkedList<>();
            this.moves.addAll(moves);
            this.alive = alive;
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
            if (!(o instanceof Util)) return false;
            Util util = (Util) o;
            return head.equals(util.head);
        }
    }

    public IATest(int height, int width, int maxFoodOnBoard) {
        super(height, width, maxFoodOnBoard);
        ia = new Snake(2, 2, Direction.Up, Color.BLUE);
        addSnake(ia);
        ready = false;
    }

    private int distanzaManhattan(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    private Util simulate(LinkedList<Direction> moves) {
        Snake s = new Snake(ia);
        s.setRelativeMoves(moves);
        while (!s.isMovesFinisched()) {
            s.move();
            if (s.checkSelfCollision() || outOfTheMap(s) || checkCollition(s)) {
                s.kill();
                break;
            }
        }
        return new Util(s.getHead(), s.isAlive(), moves);
    }


    @Override
    public GameStatus update() {
        if (ia.isMovesFinisched()) {
            ia.setRelativeMoves(getMossa());
            ready = true;
        }
        if (ready)
            return super.update();
        return GameStatus.Waiting;
    }

    public LinkedList<Direction> getMossa() {
        ready = false;
        System.out.println("calcolo capo!");
        Direction[] avaiableDirection = new Direction[]{Direction.Up, Direction.Left, Direction.Right};

        LinkedList<Util> frontiera = new LinkedList<>();
        HashSet<Point> esplorati = new HashSet<>();
        frontiera.add(new Util(ia.getHead(), ia.isAlive()));

        while (!frontiera.isEmpty()) {
            Util padre = frontiera.removeFirst();
            if(isFood(padre.getHead()))
                return padre.getMoves();
            esplorati.add(padre.getHead());

            for (Direction d : avaiableDirection) {
                Util figlio = simulate(padre.fakeAdd(d));

                if (!esplorati.contains(figlio.getHead()) && !frontiera.contains(figlio)) {
                    if (figlio.isAlive() && isFood(figlio.getHead())) {
                        System.out.println("Ho trovato una mossa!");
                        System.out.println(figlio.getMoves());
                        return figlio.getMoves();
                    } else {
                        frontiera.add(figlio);
                    }
                }
            }
        }
        System.out.println("Nessuna mossa trovata");
        return new LinkedList<>();
    }
}
