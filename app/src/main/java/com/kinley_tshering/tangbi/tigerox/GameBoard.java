package com.kinley_tshering.tangbi.tigerox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Kinley Tshering on 3/24/17.
 */

public class GameBoard extends View {

    Paint linePaint, textPaint, possiblePaint;
    private Canvas c;
    private ArrayList<Position> positions;
    private LinkedList<Integer> neighbours[];
    private ArrayList<Integer> possibleMoves;
    Bitmap tigerPiece, oxPiece;
    private boolean initialized = false;
    static final int OXEN = 2, TIGER = 1, SIZE = 70;
    private int turn;

    public GameBoard(Context context, AttributeSet a) {
        super(context, a);
        this.linkPositions();
        possibleMoves = new ArrayList<>();
        this.turn = 1;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(4f);
        linePaint.setTextSize(30);

        possiblePaint = new Paint();
        possiblePaint.setColor(Color.WHITE);
        possiblePaint.setStyle(Paint.Style.STROKE);
        possiblePaint.setStrokeWidth(5);
        possiblePaint.setPathEffect(new DashPathEffect(new float[]{5,20}, 0));

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);

        oxPiece = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ox_piece), GameBoard.SIZE, GameBoard.SIZE, false);
        tigerPiece = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.tiger_piece), GameBoard.SIZE, GameBoard.SIZE, false);

        this.setOnTouchListener(new GameThread(this, neighbours));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!initialized) {
            this.initializeBoard();
            this.initializeTiger();
            this.initializeOxens();
            initialized = true;
        }

        @SuppressLint("DrawAllocation") ArrayList<Position> occupiedPositions = new ArrayList<>();

        for (int i = 0; i < positions.size(); i++){

            Position position = positions.get(i);

            for (int j: neighbours[i]) {
                canvas.drawLine(position.getX(), position.getY(), positions.get(j).getX(), positions.get(j).getY(), linePaint);
            }
            //canvas.drawText("" + position.getIndex(), position.getX() - 15, position.getY() + 15, textPaint);
            if (position.getOccupiedBy() != 0) {
                occupiedPositions.add(position);
            }
        }

        if (turn == GameBoard.OXEN) {
            canvas.drawBitmap(oxPiece, 0, this.getHeight() / 12 - GameBoard.SIZE / 2, textPaint);
            canvas.drawText("\'s Turn", GameBoard.SIZE, this.getHeight() / 12 + 20, textPaint);
        }

        else if (turn == GameBoard.TIGER) {
            canvas.drawBitmap(tigerPiece, 0, this.getHeight() / 12 - GameBoard.SIZE / 2, textPaint);
            canvas.drawText("\'s Turn", GameBoard.SIZE, this.getHeight() / 12 + 20, textPaint);
        }

        //DRAW the OXENS and TIGERS
        for (Position a: occupiedPositions) {
            if (a.getOccupiedBy() == GameBoard.OXEN) {
                canvas.drawBitmap(oxPiece, a.getX() - GameBoard.SIZE / 2, a.getY() - GameBoard.SIZE / 2, textPaint);
                if (a.getNumber() > 1) {
                    canvas.drawText("" + a.getNumber(), a.getX() - 15, a.getY() + 15, textPaint);
                }
            }
            else if (a.getOccupiedBy() == GameBoard.TIGER) {
                canvas.drawBitmap(tigerPiece, a.getX() - GameBoard.SIZE / 2, a.getY() - GameBoard.SIZE / 2, textPaint);
            }
        }

        //DRAW possible moves
        if (!possibleMoves.isEmpty()) {
            for (int i: possibleMoves) {
                if (positions.get(i).getOccupiedBy() == 0) {
                    canvas.drawCircle(positions.get(i).getX(), positions.get(i).getY(), GameBoard.SIZE / 2, possiblePaint);
                }
            }
            possibleMoves.clear();
        }
    }

    /**
     * returns an arraylist of board positions
     * @return positions arraylist
     */
    public ArrayList<Position> getPositions() {
        return positions;
    }

    /**
     * Initialize the board positions to an arraylist
     */
    private void initializeBoard() {

        positions = new ArrayList<Position>(37);
        float width = (this.getWidth() - GameBoard.SIZE)/4;
        float height = (this.getHeight() - GameBoard.SIZE)/6;

        positions.add(new Position(0, width, 0));
        positions.add(new Position(1, width * 2, 0));
        positions.add(new Position(2, width * 3, 0));
        positions.add(new Position(3, width * 1.5f, height * 0.5f));
        positions.add(new Position(4, width * 2, height * 0.5f));
        positions.add(new Position(5, width * 2.5f, height * 0.5f));

        int pos = 6;
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                positions.add(new Position(pos, width * j, height * i));
                pos++;
            }
        }

        positions.add(new Position(31, width * 1.5f, height * 5.5f));
        positions.add(new Position(32, width * 2, height * 5.5f));
        positions.add(new Position(33, width * 2.5f, height * 5.5f));

        positions.add(new Position(34, width, height * 6));
        positions.add(new Position(35, width * 2, height * 6));
        positions.add(new Position(36, width * 3, height * 6));
    }

    /**
     * place the two tigers at their start position
     */
    private void initializeTiger() {
        positions.get(8).setOccupancy(GameBoard.TIGER,1);
        positions.get(28).setOccupancy(GameBoard.TIGER,1);
    }

    /**
     * Place the Oxes at their start position
     */
    private void initializeOxens() {
        positions.get(12).setOccupancy(GameBoard.OXEN, 6);
        positions.get(14).setOccupancy(GameBoard.OXEN, 6);
        positions.get(22).setOccupancy(GameBoard.OXEN, 6);
        positions.get(24).setOccupancy(GameBoard.OXEN, 6);
    }

    /**
     * Create a adjacency list (an array of LinkedList)
     * Describes how the positions are connected
     */
    private void linkPositions() {
        neighbours = new LinkedList[37];

        for (int i = 0; i < 37; i++) {
            neighbours[i] = new LinkedList<Integer>();
        }

        neighbours[0].add(1);
        neighbours[0].add(3);

        neighbours[1].add(0);
        neighbours[1].add(2);
        neighbours[1].add(4);

        neighbours[2].add(1);
        neighbours[2].add(5);

        neighbours[3].add(0);
        neighbours[3].add(4);
        neighbours[3].add(8);

        neighbours[4].add(1);
        neighbours[4].add(3);
        neighbours[4].add(5);
        neighbours[4].add(8);

        neighbours[5].add(2);
        neighbours[5].add(4);
        neighbours[5].add(8);

        neighbours[6].add(7);
        neighbours[6].add(11);
        neighbours[6].add(12);

        neighbours[7].add(6);
        neighbours[7].add(8);
        neighbours[7].add(12);

        neighbours[8].add(3);
        neighbours[8].add(4);
        neighbours[8].add(5);
        neighbours[8].add(7);
        neighbours[8].add(9);
        neighbours[8].add(12);
        neighbours[8].add(13);
        neighbours[8].add(14);

        neighbours[9].add(8);
        neighbours[9].add(10);
        neighbours[9].add(14);

        neighbours[10].add(9);
        neighbours[10].add(14);
        neighbours[10].add(15);

        neighbours[11].add(6);
        neighbours[11].add(12);
        neighbours[11].add(16);

        neighbours[12].add(6);
        neighbours[12].add(7);
        neighbours[12].add(8);
        neighbours[12].add(11);
        neighbours[12].add(13);
        neighbours[12].add(16);
        neighbours[12].add(17);
        neighbours[12].add(18);

        neighbours[13].add(8);
        neighbours[13].add(12);
        neighbours[13].add(14);
        neighbours[13].add(18);

        neighbours[14].add(8);
        neighbours[14].add(9);
        neighbours[14].add(10);
        neighbours[14].add(13);
        neighbours[14].add(15);
        neighbours[14].add(18);
        neighbours[14].add(19);
        neighbours[14].add(20);

        neighbours[15].add(10);
        neighbours[15].add(14);
        neighbours[15].add(20);

        neighbours[16].add(11);
        neighbours[16].add(12);
        neighbours[16].add(17);
        neighbours[16].add(21);
        neighbours[16].add(22);

        neighbours[17].add(12);
        neighbours[17].add(16);
        neighbours[17].add(18);
        neighbours[17].add(22);

        neighbours[18].add(12);
        neighbours[18].add(13);
        neighbours[18].add(14);
        neighbours[18].add(17);
        neighbours[18].add(19);
        neighbours[18].add(22);
        neighbours[18].add(23);
        neighbours[18].add(24);

        neighbours[19].add(14);
        neighbours[19].add(18);
        neighbours[19].add(20);
        neighbours[19].add(24);

        neighbours[20].add(14);
        neighbours[20].add(15);
        neighbours[20].add(19);
        neighbours[20].add(24);
        neighbours[20].add(25);

        neighbours[21].add(16);
        neighbours[21].add(22);
        neighbours[21].add(26);

        neighbours[22].add(16);
        neighbours[22].add(17);
        neighbours[22].add(18);
        neighbours[22].add(21);
        neighbours[22].add(23);
        neighbours[22].add(26);
        neighbours[22].add(27);
        neighbours[22].add(28);

        neighbours[23].add(18);
        neighbours[23].add(22);
        neighbours[23].add(24);
        neighbours[23].add(28);

        neighbours[24].add(18);
        neighbours[24].add(19);
        neighbours[24].add(20);
        neighbours[24].add(23);
        neighbours[24].add(25);
        neighbours[24].add(28);
        neighbours[24].add(29);
        neighbours[24].add(30);

        neighbours[25].add(20);
        neighbours[25].add(24);
        neighbours[25].add(30);

        neighbours[26].add(21);
        neighbours[26].add(22);
        neighbours[26].add(27);

        neighbours[27].add(22);
        neighbours[27].add(26);
        neighbours[27].add(28);

        neighbours[28].add(22);
        neighbours[28].add(23);
        neighbours[28].add(24);
        neighbours[28].add(27);
        neighbours[28].add(29);
        neighbours[28].add(31);
        neighbours[28].add(32);
        neighbours[28].add(33);

        neighbours[29].add(24);
        neighbours[29].add(28);
        neighbours[29].add(30);

        neighbours[30].add(24);
        neighbours[30].add(25);
        neighbours[30].add(29);

        neighbours[31].add(28);
        neighbours[31].add(32);
        neighbours[31].add(34);

        neighbours[32].add(28);
        neighbours[32].add(31);
        neighbours[32].add(33);
        neighbours[32].add(35);

        neighbours[33].add(28);
        neighbours[33].add(32);
        neighbours[33].add(36);

        neighbours[34].add(31);
        neighbours[34].add(35);

        neighbours[35].add(32);
        neighbours[35].add(34);
        neighbours[35].add(36);

        neighbours[36].add(33);
        neighbours[36].add(35);
    }

    /**
     * Set whose turn it is so that the onDraw can draw on the canvas
     * @param i OX or TIGER
     */
    public void setTurn(int i) {
        turn = i;
    }


    /**
     * Set the possible moves of currently selected position
     * @param input arraylist of possible moves
     */
    public void setPossibleMoves(ArrayList<Integer> input) {
        possibleMoves = input;
    }

}
