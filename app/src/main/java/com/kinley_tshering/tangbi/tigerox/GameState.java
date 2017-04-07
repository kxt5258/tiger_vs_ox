package com.kinley_tshering.tangbi.tigerox;

import java.util.ArrayList;

/**
 * Created by Kinley Tshering on 4/7/17.
 */

public class GameState {
    private ArrayList<Position> board;
    //result 1=Tiger won, 2=Ox won, 0=Playing
    private int result;

    GameState(ArrayList _board, int _result) {
        board = _board;
        result = _result;
    }

    public ArrayList<Position> getBoard() {
        return board;
    }

    public int getResult() {
        return result;
    }
}
