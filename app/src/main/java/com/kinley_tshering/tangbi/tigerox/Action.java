package com.kinley_tshering.tangbi.tigerox;

/**
 * Created by Kinley Tshering on 4/7/17.
 */

public class Action {
    private Move move;
    private int score;

    Action() {
    }

    Action(Move _move, int _score) {
        move = _move;
        score = _score;
    }

    public Move getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return move.getFrom().getOccupiedBy() + " : " + move.getFrom().getIndex() + " ==> " + move.getTo().getIndex();
    }
}
