package SnakeGame;

import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SnakeBoard {
    private final int height;
    private final int width;
    private final int maxFoodOnBoard;
    private final HashSet<Point> food;
    private final List<Snake> snakes;

    public SnakeBoard(int height, int width, int maxFoodOnBoard, List<Snake> snakes) {
        this.height = height;
        this.width = width;
        this.maxFoodOnBoard = maxFoodOnBoard;

        this.food = new HashSet<>(); //Non ci possono essere doppioni e non ci interessa l'ordinamento.
        this.snakes = snakes;
        setFoodOnBoard();
    }

    public void setFoodOnBoard() {
        Random r = new Random();
        int x, y;
        while (food.size() < maxFoodOnBoard) {
            x = r.nextInt(width);
            y = r.nextInt(height);
            Point p = new Point(x,y);
            for (Snake s : snakes)
                if(!s.contain(p))
                    food.add(new Point(x, y));
            //Documentazione Java8
            //Adds the specified element to this set if it is not already present.
        }
    }

    public GameStatus update() {
        AtomicReference<GameStatus> status = new AtomicReference<>(GameStatus.Running);
        AtomicInteger alive = new AtomicInteger(0);
        snakes.forEach(s -> {
            if (s.isAlive()){
                s.move();
                alive.incrementAndGet();
            }
        });
        snakes.forEach(s -> {
            if (s.isAlive() && (s.checkSelfCollision() || outOfTheMap(s) || checkCollition(s))){
                s.kill();
                alive.decrementAndGet();
            }
            else if (isFood(s.getHead())) {
                food.remove(s.getHead());
                s.eat();
                setFoodOnBoard();
                if (s.getCoords().size() == width * height)
                    status.set(GameStatus.Win);
            }
        });

        if(alive.get() == 0)
            status.set(GameStatus.Lose);
        return status.get();
    }

    public boolean outOfTheMap(Snake s) {
        Point p = s.getHead();
        return p.x < 0 || p.x > width - 1 || p.y < 0 || p.y > height - 1;
    }

    public boolean checkCollition(Snake s) {
        for (Snake b : snakes)
            if (!s.equals(b) && b.contain(s.getHead()))
                return true;
        return false;
    }

    public boolean isFood(Point p) {
        return food.contains(p);
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void drawBoard(Graphics g) {
        int squareSize = GameLoop.squareSize;
//        Solo per abbellimento, ma solo uno spreco di risorse!
//        for (int i = 0; i < height; i++) {
//            for (int y = 0; y < width; y++) {
//                g.setColor(Color.WHITE);
//                g.fillRect((i * squareSize + 1), (y * squareSize + 1), squareSize - 1, squareSize - 1);
//            }
//        }
        for (Snake s : snakes) {
            for (Point p : s.getCoords()) {
                if (p.equals(s.getHead())) {
                    g.setColor(Color.BLACK);
                    g.fillRect((p.x * squareSize - 1), (p.y * squareSize - 1), squareSize + 1, squareSize + 1);
                }
                g.setColor(s.getColor());
                g.fillRect((p.x * squareSize), (p.y * squareSize), squareSize - 1, squareSize - 1);
            }
        }

        for (Point p : food) {
            g.setColor(Color.RED);
            g.fillRect((p.x * squareSize), (p.y * squareSize), squareSize - 1, squareSize - 1);
        }
    }


    public void setDirectionDebug(Direction d) {
        Direction tmp = snakes.get(0).getDirection();
        switch (d) {
            case Down -> {
                if (tmp != Direction.Up) snakes.get(0).setDirection(d);
            }
            case Up -> {
                if (tmp != Direction.Down) snakes.get(0).setDirection(d);
            }
            case Left -> {
                if (tmp != Direction.Right) snakes.get(0).setDirection(d);
            }
            case Right -> {
                if (tmp != Direction.Left) snakes.get(0).setDirection(d);
            }
        }
    }
}