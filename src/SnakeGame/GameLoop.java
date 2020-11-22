package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

enum BoardPossibleValue {Empty, Food, SnakeHead, SnakeBody}

public class GameLoop extends JPanel implements Runnable, KeyListener {
    enum GameStatus {Running, End, Pause, Error}

    private int height;
    private int width;
    private int squareSize = 20;
    private int foodQuantity;
    private int foodOnBoard;

    private BoardPossibleValue[][] board;
    private GameStatus status;
    private ArrayList<Snake> snakes;

    private static final long updateTime = 200; //In millisecondi

    public GameLoop(int height, int width, int foodQuantity, ArrayList<Snake> snakes) {
        setPreferredSize(new Dimension(width * squareSize, height * squareSize));
//        setFocusable(true);
//        addKeyListener(this);

        this.width = width;
        this.height = height;
        this.foodQuantity = foodQuantity;
        this.snakes = snakes;

        board = new BoardPossibleValue[height][width];
        status = GameStatus.Running;
        foodOnBoard = 0;
    }

    public void start() {
        Thread t1 = new Thread(this);
        t1.start();
    }

    public void run() {
        long currentTime, lastTime = System.currentTimeMillis();
        while (status != GameStatus.End) {
            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= updateTime) {
                snakes.forEach(Snake::move);

                snakes.forEach(s -> {
                    Point head = s.getHead();
                    if (!s.isAlive() || isOutOfTheMap(head) || isSuicide(head))
                        s.kill();
                    else if (isFood(head)) {
                        s.eat();
                        foodOnBoard--;
                    }
                });

                lastTime = currentTime;
                this.repaint();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            switch (status) {
                case Running -> drawBoard(g);
                case End -> drawCenteredText(g, "FINE", Color.red);
                case Pause -> drawCenteredText(g, "PAUSA", Color.white);
                case Error -> drawCenteredText(g, "ERRORE!", Color.orange);
            }
        } catch (SnakeException e) {
            status = GameStatus.Error;
        }
    }

    private void drawBoard(Graphics g) throws SnakeException {
//        System.out.println(snakes.get(0).getCoords());
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getSize().width, getSize().height);

        setSnakes();
        if (foodOnBoard < foodQuantity)
            setFood();
        for (int i = 0; i < height; i++) {
            for (int y = 0; y < width; y++) {
                BoardPossibleValue cell = board[i][y];
                switch (cell) {
                    case Empty -> g.setColor(Color.white);
                    case Food -> g.setColor(Color.red);
                    case SnakeHead -> {
                        g.setColor(Color.black);
                        g.fillRect((i * squareSize), (y * squareSize), squareSize + 2, squareSize + 2);
                        g.setColor(getSnakeFrom(new Point(i, y)).getColor());
                    }
                    case SnakeBody -> g.setColor(getSnakeFrom(new Point(i, y)).getColor());
                }
                g.fillRect((i * squareSize) + 1, (y * squareSize) + 1, squareSize - 1, squareSize - 1);
            }
        }
    }

    private void setSnakes() {
        initBoard(); //Pulisco la board
        snakes.removeIf(s -> !s.isAlive()); // Rimuovo gli snakes deceduti
        if (!snakes.isEmpty()) {
            for (Snake s : snakes) {
                boolean tmp = true;
                for (Point p : s.getCoords())
                    if (tmp) {
                        board[p.x][p.y] = BoardPossibleValue.SnakeHead;
                        tmp = false;
                    } else {
                        board[p.x][p.y] = BoardPossibleValue.SnakeBody;
                    }
            }
        } else {
            status = GameStatus.End;
        }
    }

    private void setFood() {
        Random r = new Random();
        int x, y;
        while (foodOnBoard < foodQuantity) {
            x = r.nextInt(width);
            y = r.nextInt(height);
            if (board[x][y] == BoardPossibleValue.Empty) {
                board[x][y] = BoardPossibleValue.Food;
                foodOnBoard++;
            }
        }
    }

    private void drawCenteredText(Graphics g, String text, Color color) {
        Graphics2D g2d = (Graphics2D) g.create();
        FontMetrics fm = g2d.getFontMetrics();
        int x = ((width * squareSize - fm.stringWidth(text)) / 2);
        int y = (((height * squareSize - fm.getHeight()) / 2) + fm.getAscent());

        g.setColor(Color.black);
        g.fillRect(0, 0, height * squareSize, width * squareSize);


        g.setColor(color);
        g.drawString(text, x, y);
    }


    //Util
    private void initBoard() {
        for (int i = 0; i < height; i++) {
            for (int y = 0; y < width; y++) {
                if (board[i][y] != BoardPossibleValue.Food)
                    board[i][y] = BoardPossibleValue.Empty;
            }
        }
    }

    private Snake getSnakeFrom(Point p) throws SnakeException {
        for (Snake s : snakes)
            if (s.getCoords().contains(p))
                return s;
        throw new SnakeException();
    }

    private boolean isOutOfTheMap(Point p) {
        if (p.x < 0 || p.y < 0 || p.x >= width || p.y >= height)
            return true;
        return false;
    }

    private boolean isSuicide(Point p) {
        if (board[p.x][p.y] == BoardPossibleValue.SnakeHead || board[p.x][p.y] == BoardPossibleValue.SnakeBody)
            return true;
        return false;
    }

    private boolean isFood(Point p) {
        if (board[p.x][p.y] == BoardPossibleValue.Food)
            return true;
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Direction newDirection = snakes.get(0).getDirection();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: {
                if (newDirection != Direction.Down)
                    newDirection = Direction.Up;
                break;
            }
            case KeyEvent.VK_LEFT: {
                if (newDirection != Direction.Right)
                    newDirection = Direction.Left;
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (newDirection != Direction.Up)
                    newDirection = Direction.Down;
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if (newDirection != Direction.Left)
                    newDirection = Direction.Right;
                break;
            }
        }
        snakes.get(0).setDirection(newDirection);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
