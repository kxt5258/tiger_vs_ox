package com.kinley_tshering.tangbi.tigerox;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Kinley Tshering on 3/27/17.
 */

class GameThread implements View.OnTouchListener {

    private LinkedList<Integer> [] neighbours;
    private ArrayList<Position> positions;
    private GameBoard board;
    private boolean pieceSelected, gameOver = false;
    private Position firstClick = null;
    private boolean tigerTurn = true;
    private int numTigers, numOxens;
    private boolean stacked;
    //positions inside the lair
    private List triangleList;

    GameThread(GameBoard b, LinkedList<Integer>[] link) {
        board = b;
        neighbours = link;
        numOxens = 24;
        numTigers = 2;
        stacked = true;
        triangleList = Arrays.asList(3,4,5,31,32,33);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!gameOver) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                positions = board.getPositions();

                for (int i = 0; i < positions.size(); i++) {
                    Position position = positions.get(i);
                    float dx = position.getX() - event.getX();
                    float dy = position.getY() - event.getY();

                    if (Math.sqrt(dx * dx + dy * dy) <= 30) {
                        if (stacked) {
                            this.checkStacked();
                        }
                         if (tigerTurn) {
                                if (pieceSelected && position.getOccupiedBy() == 0) {
                                    if (!(stacked && triangleList.contains(position.getIndex()))) {
                                        if (firstClick.equals(position) || !(neighbours[firstClick.getIndex()].contains(position.getIndex()))) {
                                            int interIndex = checkJumpable(firstClick, position);
                                            if (interIndex != -1 && positions.get(interIndex).getOccupiedBy() == board.OXEN) {
                                                this.movePiece(board.TIGER, firstClick, position);
                                                killPiece(board.OXEN, positions.get(interIndex));
                                                pieceSelected = false;
                                                tigerTurn = false;
                                                board.setTurn(board.OXEN);
                                                board.invalidate();
                                            }
                                        } else {
                                            this.movePiece(board.TIGER, firstClick, position);
                                            pieceSelected = false;
                                            tigerTurn = false;
                                            board.setTurn(board.OXEN);
                                            board.invalidate();
                                        }
                                    }
                                } else {
                                    if (position.getOccupiedBy() == board.TIGER) {
                                        pieceSelected = true;
                                        firstClick = position;
                                        board.setPossibleMoves(this.getPossibleMoves(position));
                                        board.invalidate();
                                    }
                                }
                            }
                            //Ox Turn
                            else {
                                if (pieceSelected && position.getOccupiedBy() == 0) {
                                    if (!(stacked && triangleList.contains(position.getIndex()))) {
                                        if (!(firstClick.equals(position)) && (neighbours[firstClick.getIndex()].contains(position.getIndex()))) {
                                            this.movePiece(board.OXEN, firstClick, position);
                                            pieceSelected = false;
                                            tigerTurn = true;
                                            board.setTurn(board.TIGER);
                                            ArrayList<Integer> tigerValue = this.checkTigerLife();
                                            if (tigerValue.size() != 0) {
                                                for (int tiger: tigerValue) {
                                                    this.killPiece(board.TIGER, positions.get(tiger));
                                                }
                                            }
                                            board.invalidate();
                                        }
                                    }
                                } else {
                                    if (position.getOccupiedBy() == board.OXEN) {
                                        pieceSelected = true;
                                        firstClick = position;
                                        board.setPossibleMoves(this.getPossibleMoves(position));
                                        board.invalidate();
                                    }
                                }

                            }

                            break;
                    }
                }
            }
            //Check GAME OVER
            checkGameOver();
        }
        return true;
    }

    /**
     * check if the game is over or not
     * @return boolean to indicate the status of game
     */
    private boolean checkGameOver() {
        String winner = "";
        if (numTigers == 0) {
            gameOver = true;
            winner = "OXES";
        }
        else if (numOxens == 5) {
            gameOver = true;
            winner = "TIGERS";
        }
        else {
            return gameOver;
        }

        board.setTurn(0);
        AlertDialog builder = new AlertDialog.Builder(board.getContext()).create();
        builder.setMessage("Congratulations! \n" + winner + " WON");
        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        TextView messageView = (TextView)builder.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

        Button button = builder.getButton(AlertDialog.BUTTON_NEUTRAL);
        button.setWidth(2000);
        button.setHeight(80);

        return gameOver;
    }

    /**
     * Remove the piece from board (play)
     * @param what which piece is it (oxen or tiger)
     * @param from the position from where it got killed
     */
    private void killPiece(int what, Position from) {
        from.setOccupancy(what, -1);
        if (what == board.OXEN) {
            numOxens--;
        }
        else if (what == board.TIGER) {
            numTigers--;
        }
    }

    /**
     * move a piece from one position to another valid position
     * @param what which piece
     * @param from from position
     * @param to to position
     */
    private void movePiece(int what, Position from, Position to) {
        from.setOccupancy(what, -1);
        to.setOccupancy(what, 1);
    }

    /**
     * check for the valid moves of tiger
     * @param a from position
     * @param b middle position
     * @param c to position
     * @return middle positions index
     */
    private boolean checkValidTigerMove(Position a, Position b, Position c) {
        //check the triangle of three points is zero (i.e striaght line)
        float area = a.getX() * (b.getY() - c.getY()) + b.getX() * (c.getY() - a.getY()) + c.getX() * (a.getY() - b.getY());

        //check the distance between the two points
        float dx = a.getX() - b.getX();
        float dy = a.getY() - b.getY();

        float fx = b.getX() - c.getX();
        float fy = b.getY() - c.getY();

        return area == 0 && ((Math.sqrt(dx * dx + dy * dy) == Math.sqrt(fx * fx + fy * fy)) || (2 * Math.sqrt(dx * dx + dy * dy) == Math.sqrt(fx * fx + fy * fy)) || (Math.sqrt(dx * dx + dy * dy) == Math.sqrt(fx * fx + fy * fy) * 2));
    }

    /**
     * Find intersection points between two points
     * @param a from position
     * @param b to position
     * @return position of valid position (intermediate)
     */
    private int checkJumpable(Position a, Position b) {
        for (int i: neighbours[a.getIndex()]) {
            for(int j: neighbours[b.getIndex()]) {
                if(i == j) {
                    if (this.checkValidTigerMove(a, positions.get(i), b)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * check of there is any valid move for tiger
     * @return position of tiger
     */
    private ArrayList<Integer> checkTigerLife() {
        int tigerChecked = 0;
        ArrayList<Integer> deadTiger = new ArrayList<Integer>();

        for (Position position: positions) {
            if (position.getOccupiedBy() == board.TIGER) {
                tigerChecked += 1;
                boolean validCell = false;
                for(int i: neighbours[position.getIndex()]) {
                    if (positions.get(i).getOccupiedBy() == 0) {
                        if (!(triangleList.contains(i) && stacked)) {
                            validCell = true;
                            break;
                        }
                    }

                    //check for jumpable moves
                    for (int j: neighbours[i]) {
                        //don't check for the tiger position with itself
                        if (j != position.getIndex()) {
                            if (checkValidTigerMove(position, positions.get(i), positions.get(j)) && positions.get(j).getOccupiedBy() == 0) {
                                if (!(triangleList.contains(i) && stacked)) {
                                    validCell = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!validCell) {
                        deadTiger.add(position.getIndex());
                }

            }

            //checked all the tigers left in the game
            if (tigerChecked == numTigers) {
                break;
            }
        }

        if (deadTiger.size() > 0) {
            //If there is apossibility of clearing the way by moving the other tiger, dont kill it
            for (int i: deadTiger) {
                for(int k: getPossibleMoves(positions.get(i))) {
                    if (positions.get(k).getOccupiedBy() == board.TIGER) {
                        deadTiger.clear();
                    }
                }
            }
        }
        return deadTiger;
    }

    /**
     * check if there are still oxes which are stacked
     * @return boolean to indicate if there are still stacks of oxes
     */
    private boolean checkStacked() {
        for (Position position: positions) {
            if (position.getNumber() > 1) {
                stacked = true;
                return true;
            }
        }
        stacked = false;
        return false;
    }

    /**
     * check for any possible moves and return them to the caller
     * @param position the current position
     * @return an arraylist of all possible moves
     */
    private ArrayList<Integer> getPossibleMoves(Position position) {
        ArrayList<Integer> possible = new ArrayList<>();
        for (int i: neighbours[position.getIndex()]) {
            if (positions.get(i).getOccupiedBy() == 0) {
                if (!(stacked && triangleList.contains(positions.get(i).getIndex()))) {
                    possible.add(i);
                }
            }

            //if the player is playing tiger and chances of a kill
            if (position.getOccupiedBy() == board.TIGER && positions.get(i).getOccupiedBy() == board.OXEN) {
                for (int j: neighbours[i]) {
                    if (positions.get(j).getOccupiedBy() != board.OXEN && j != position.getIndex()) {
                        if (this.checkValidTigerMove(position, positions.get(i), positions.get(j))) {
                            if (!(stacked && triangleList.contains(positions.get(j).getIndex()))) {
                                possible.add(j);
                            }
                        }
                    }
                }
            }
        }
        return possible;
    }

    /**
     *
     * @return the position of the move
     */
    private int getMove() {

        return 0;
    }
}
