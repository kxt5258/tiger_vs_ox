package com.kinley_tshering.tangbi.tigerox;

/**
 * Created by Kinley Tshering on 4/7/17.
 */

public class Move {
    private Position from;
    private Position to;
    private Position middle;

    Move(Position _from, Position _to, Position _middle) {
        from = _from;
        to = _to;
        middle = _middle;
    }

    public Position getFrom() {
        return from;
    }

    public Position getMiddle() {
        return middle;
    }

    public Position getTo() {
        return to;
    }

    @Override
    public String toString() {
        String string = this.getFrom().getIndex() + " ==> ";
        if (this.getMiddle() != null) {
            string = string.concat(this.getMiddle().getIndex() + " ==> ");
        }
        string = string.concat(""+this.getTo().getIndex());
        return string;
    }
}
