package SnakeGame.Enum;

public enum Direction {
    Up, Down, Right, Left;

    public Direction getOpposite(){
        Direction tmp;
        switch (this){
            case Down -> tmp = Up;
            case Up -> tmp = Down;
            case Right -> tmp = Left;
            case Left -> tmp = Right;
            default -> tmp = this;
        }
        return tmp;
    }
}
