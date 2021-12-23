package com.example.thememmory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Playground {
    private Card[][] cards;
    private int whoseTurn;
    private int[] score;
    private int x;
    private int y;
    private int anzPlayer;

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public int getScore(int i) {
        return score[i];
    }

    public Playground(int x, int y, int p){
        this.x = x;
        this.y = y;
        cards = new Card[x][y];
        this.anzPlayer = p;
        score = new int[anzPlayer];
        init();
    }
    public void init(){
        ArrayList<Integer> values = new ArrayList<Integer>();
        for(int i = 0 ; i<getNrPairs(); i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);
        Collections.shuffle(values);
        int zaehler = 0;
        for(int i = 0; i <x; i++){
            for (int j = 0; j< y; j++){
                cards[i][j] = new Card();
                cards[i][j].setValue(values.get(zaehler));
                zaehler++;
            }
        }
    }
    public int play(Position pos, Position prev){
        if(isPair(pos,prev)){
            score[whoseTurn]++;
            return 1;
        }
        if(finished()){
            return 2;
        }
        whoseTurn++;
        whoseTurn = whoseTurn % anzPlayer;
        return 0;
    }

    public boolean finished(){
        int pair = 0;
        for(int i = 0; i < score.length; i++){
            pair  += score[i];
        }
        if(pair == getNrPairs()){
            return true;
        }
        return false;
    }

    public boolean isPair(Position pos1, Position pos2){
        if(getCard(pos1).getValue() == getCard(pos2).getValue()) {
            return true;
        }
        return false;
    }
    public Card getCard(Position pos){
        return cards[pos.x][pos.y];
    }
    private int getNrPairs(){
        return (x*y)/2;
    }

    @Override
    public String toString() {
        return "Playground{" +
                "cards=" + Arrays.toString(cards) +
                ", whosOnTurn=" + whoseTurn +
                ", score=" + score +
                '}';
    }
}
