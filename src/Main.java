import SnakeGame.Enum.Direction;
import SnakeGame.GameLoop;
import SnakeGame.Snake;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private GameLoop snakeBoard;

    public Main(){initMain();}

    public void initMain(){
        setResizable(false);
        ArrayList<Snake> snakes = new ArrayList<>();
        snakes.add(new Snake(20, 20, Direction.Up, Color.yellow));

        snakeBoard = new GameLoop(25, 25, 1, snakes);
        add(snakeBoard);
        snakeBoard.start();

        setTitle("Snake [FIA] Project");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Frame game = new Main();
                game.setVisible(true);
            }
        });
    }

}
