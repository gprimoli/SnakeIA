package SnakeGame;

import SnakeGame.IA.HillClimbing;
import SnakeGame.Enum.Direction;
import SnakeGame.Enum.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameLoop extends JPanel implements Runnable, KeyListener {
    public static final int squareSize = 20;
    private static final long updateTime = 10; //In millisecondi
    private final SnakeBoard board;
    private GameStatus status;


    public GameLoop(SnakeBoard ia) {
        int width = ia.getWidth();
        int height = ia.getHeight();

        setPreferredSize(new Dimension(width * squareSize, height * squareSize));
        setFocusable(true);
        addKeyListener(this);

        this.board = ia;

        status = GameStatus.Running;
    }

    public void start() {
        Thread t1 = new Thread(this);
        t1.start();
    }

    public void run() {
        long currentTime, lastTime = System.currentTimeMillis();
        while (status == GameStatus.Running) {
            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= updateTime) {
                lastTime = currentTime;
                this.status = board.update();
                this.repaint();
            } if (this.status != GameStatus.Running){
                System.out.println(board.getInfo());

                board.reset();
                this.status = GameStatus.Running;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (status) {
            case Running, Waiting -> board.drawBoard(g);
            case Win -> drawCenteredText(g, "HAI VINTO", Color.green);
            case Lose -> drawCenteredText(g, "HAI PERSO", Color.red);
            case Pause -> drawCenteredText(g, "PAUSA", Color.white);
            case Error -> drawCenteredText(g, "ERRORE!", Color.orange);
        }

    }

    private void drawCenteredText(Graphics g, String text, Color color) {
        Graphics2D g2d = (Graphics2D) g.create();
        FontMetrics fm = g2d.getFontMetrics();
        int width = board.getWidth();
        int height = board.getHeight();

        int x = ((width * squareSize - fm.stringWidth(text)) / 2);
        int y = (((height * squareSize - fm.getHeight()) / 2) + fm.getAscent());

        g.setColor(Color.black);
        g.fillRect(0, 0, width * squareSize, height * squareSize);

        g.setColor(color);
        g.drawString(text, x, y);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> board.setDirectionDebug(Direction.Up);
            case KeyEvent.VK_LEFT -> board.setDirectionDebug(Direction.Left);
            case KeyEvent.VK_DOWN -> board.setDirectionDebug(Direction.Down);
            case KeyEvent.VK_RIGHT -> board.setDirectionDebug(Direction.Right);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
