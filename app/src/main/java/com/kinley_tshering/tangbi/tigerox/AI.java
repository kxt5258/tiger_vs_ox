package com.kinley_tshering.tangbi.tigerox;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Kinley Tshering on 4/6/17.
 */

public class AI {
    GameThread thread;
    private int player;

    AI(GameThread _thread) {
        thread = _thread;
    }

    /**
     *
     * @param _currentState current game state
     * @param _player current player
     * @return the best action (move) to make
     */
    Action bestAction(GameState _currentState, int _player) {
        player = _player;
        return this.minimax(_currentState.getBoard(), 4, _player);
    }

    /**
     *
     * @param currentState current game state
     * @param depth search depth
     * @param _agentplayer player taking turn
     * @return best action
     */
    private Action minimax(final ArrayList currentState, int depth, int _agentplayer) {
        Action action = new Action();
        Move final_move = new Move(null, null, null);
        int score;

        ArrayList<Move> moveList = getLegalActions(currentState, _agentplayer);

        if (depth < 0 || moveList.size() == 0) {
             score = evaluateScore(currentState, _agentplayer);
            //Log.e("inside", _agentplayer + " ::::: " + score);
            return new Action(final_move, score);
        }
        else if (_agentplayer == player) {
            score = Integer.MIN_VALUE;
            for (Move move: moveList) {
                int newScore = minimax(this.generateSuccessor(currentState, move).getBoard(), depth - 1, (_agentplayer + 1) % 2).getScore();
                if (score < newScore) {
                    score = newScore;
                    final_move = move;
                }
            }
        }
        else {
            score = Integer.MAX_VALUE;
            for (Move move: moveList) {
                int newScore = minimax(this.generateSuccessor(currentState, move).getBoard(), depth - 1, (_agentplayer + 1) % 2).getScore();
                if (score > newScore) {
                    score = newScore;
                    final_move = move;
                }
            }
        }

        action.setMove(final_move);
        action.setScore(score);

        return action;
    }

    /**
     *
     * @param current current game state
     * @param move move to make
     * @return the game state after the move
     */
    private GameState generateSuccessor(final ArrayList<Position> current, Move move) {
        ArrayList<Position> currentBoard = new ArrayList(current);
        Position _from = new Position(move.getFrom());
        Position _to = new Position(move.getTo());


        int result = 0, numOx = 0, numTiger = 0;

        _from.setOccupancy(_from.getOccupiedBy(), -1);
        _to.setOccupancy(_from.getOccupiedBy(), 1);

        currentBoard.set(_from.getIndex(), _from);
        currentBoard.set(_to.getIndex(), _to);

        if (move.getMiddle() != null) {
            Position _middle = new Position(move.getMiddle());
            _middle.setOccupancy(_middle.getOccupiedBy(), -1);
            currentBoard.set(_middle.getIndex(), _middle);
        }

        for (Position position: currentBoard) {
            if (position.getOccupiedBy() == GameBoard.TIGER) {
                numOx++;
            }
            else if (position.getOccupiedBy() == GameBoard.OXEN) {
                numTiger++;
            }
        }

        if (numOx < 6) {
            result = GameBoard.TIGER;
        }
        else if (numTiger == 0) {
            result = GameBoard.OXEN;
        }
        else {
            result = GameBoard.EMPTY;
        }
        return new GameState(currentBoard, result);
    }

    /**
     * returns the entire lists of valid moves from the current game state
     * @param turn current player
     * @return an arraylist of valid moves
     */
    private ArrayList getLegalActions(ArrayList _currentBoard, int turn) {
        ArrayList<Move> possibleMoves = new ArrayList();
        for (Position position: (ArrayList<Position>)_currentBoard) {
            if (position.getOccupiedBy() == turn) {
                for (Move a: thread.getPossibleMoves(position)) {
                    if (a.getMiddle() == null) {
                        if (a.getTo().getOccupiedBy() == GameBoard.EMPTY) {
                            possibleMoves.add(a);
                        }
                    }
                    else if (a.getTo().getOccupiedBy() == GameBoard.EMPTY) {
                        possibleMoves.add(a);
                    }
                }
            }
        }
        return possibleMoves;
    }

    /**
     * The evaluation function which will give weight to the game states
     * This function determines the best action the AI will take
     * @return the score of the current game state
     */
    private int evaluateScore(final ArrayList gameState, int agentPlayer) {
        ArrayList<Position> newList = new ArrayList(gameState);
        int score = 0;
        int i = (int) (Math.random() * 500 + 1);
        score += i;

        int numOx = 0, numTigers = 0, stacked = 0;

        if (gameState.size() > 0) {
            for (Position position : newList) {
                if (position.getNumber() > 0) {
                    if (position.getOccupiedBy() == GameBoard.TIGER) {
                        numTigers++;
                    }
                    else {
                        numOx += position.getNumber();
                        if (position.getNumber() > 1) {
                            stacked++;
                        }
                    }
                }
            }
            //THIS EVALUATION FUNCTION NEEDS WORK
            score = score + numTigers * 500 + (int) (10000 / numOx + 1) - stacked * 500;
        }

        if (agentPlayer == 0) {
            return score;
        }
        else {
            return -score;
        }
    }
}
