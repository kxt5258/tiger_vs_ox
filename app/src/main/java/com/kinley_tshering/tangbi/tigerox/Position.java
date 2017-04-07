package com.kinley_tshering.tangbi.tigerox;

/**
 * Created by Kinley Tshering on 3/25/17.
 */

class Position {
    private float x, y;
    //occupiedBy -1 = empty, 0 = tiger, 1 = oxen
    private int index, occupiedBy, number;

    Position(int i, float _x, float _y) {
        index = i;
        x = _x;
        y = _y;
        occupiedBy = GameBoard.EMPTY;
        number = 0;
    }

    Position(Position position) {
        index = position.getIndex();
        x = position.getX();
        y = position.getY();
        occupiedBy = position.getOccupiedBy();
        number = position.getNumber();
    }

    /**
     * what is occupying the position
     * @return OX (1) or TIGER (0)
     */
    int getOccupiedBy() {
        return occupiedBy;
    }

    /**
     * return the number of pieces on the position
     * @return number of pieces
     */
    int getNumber() {
        return number;
    }

    /**
     * Set which piece is occupying the position (OX or TIGER)
     * @param _occupiedBy OX or TIGER
     * @param _number number of OX/TIGER
     */
    void setOccupancy(int _occupiedBy, int _number) {
        occupiedBy = _occupiedBy;
        number += _number;

        if (number == 0) {
            occupiedBy = GameBoard.EMPTY;
        }
    }

    /**
     * Return the X coordinate of the position
     * @return x + margin
     */
    float getX() {
        return x + GameBoard.SIZE / 2;
    }

    /**
     * Return the Y coordinate of the position
     * @return y + margin
     */
    float getY() {
        return y + GameBoard.SIZE / 2;
    }

    /**
     * Return the index of the position
     * @return index
     */
    int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "" + index + "/" + x + "/" + y + "/" + occupiedBy + " / " + number;
    }
}
