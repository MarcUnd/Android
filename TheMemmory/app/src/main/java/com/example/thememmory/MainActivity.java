package com.example.thememmory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    Playground field;
    int[] pics;
    boolean waiting = false;
    Position previousCard;
    ImageButton[][] buttons = new ImageButton[5][6];
    int anzPlayer = 2;
    boolean resetPossible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateGrid(5,6);
        field = new Playground(5,6, anzPlayer);
        pics = getPicsArray();
    }

    public static int[] getPicsArray() {
        int[] c = new int[20];

        c[0] = R.drawable.i000;
        c[1] = R.drawable.i001;
        c[2] = R.drawable.i002;
        c[3] = R.drawable.i003;
        c[4] = R.drawable.i004;
        c[5] = R.drawable.i005;
        c[6] = R.drawable.i006;
        c[7] = R.drawable.i007;
        c[8] = R.drawable.i008;
        c[9] = R.drawable.i009;
        c[10] = R.drawable.i010;
        c[11] = R.drawable.i011;
        c[12] = R.drawable.i012;
        c[13] = R.drawable.i013;
        c[14] = R.drawable.i014;
        c[15] = R.drawable.i015;
        c[16] = R.drawable.i016;
        c[17] = R.drawable.i017;
        c[18] = R.drawable.i018;
        c[19] = R.drawable.i019;
        return c;
    }

    private void generateGrid(int nrRow, int nrCol){
        TableLayout playField = findViewById(R.id.playfield);
        for(int i = 0; i < nrRow; i++){
            playField.addView(generateAndAddRows(i,nrCol));
        }
    }

    private TableRow generateAndAddRows(int row, int nrCol)
    {
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(tableRowParams);
        for (int i = 0; i < nrCol ; i++) {
            Position p = new Position(row, i);
            tr.addView(generateButton(p));
        }
        return tr;
    }

    @Override
    public void onClick(View v) {

        Position position = (Position) v.getTag();
        Card card = field.getCard(position);
        if(card.isVisible()||waiting){
            return;
        }
        Card preCard = null;
        if(previousCard != null){
           preCard = field.getCard(previousCard);
       }
        else{
            previousCard = position;
            preCard = card;
            preCard.setVisible(true);
            buttons[position.x][position.y].setImageResource(pics[card.getValue()]);
            return;
        }

        buttons[position.x][position.y].setImageResource(pics[card.getValue()]);
        card.setVisible(true);
        if(preCard.isVisible()) {
            switch (field.play(position, previousCard)) {
                case 0:
                    closeCards(position, previousCard);
                    preCard.setVisible(false);
                    card.setVisible(false);
                    previousCard = position;
                    waiting = true;
                    break;
                case 1:
                    if(field.getWhoseTurn() == 0){
                        buttons[position.x][position.y].setBackgroundColor(Color.MAGENTA);
                        buttons[previousCard.x][previousCard.y].setBackgroundColor(Color.MAGENTA);
                        Button b = findViewById(R.id.button);
                        b.setText("Player 1 Points:" + Integer.toString(field.getScore(field.getWhoseTurn())));
                    }
                    else{
                        buttons[position.x][position.y].setBackgroundColor(Color.LTGRAY);
                        buttons[previousCard.x][previousCard.y].setBackgroundColor(Color.LTGRAY);
                        Button b = findViewById(R.id.button2);
                        b.setText("Player 2 Points:" + Integer.toString(field.getScore(field.getWhoseTurn())));
                    }
                    previousCard = null;
                    break;
                case 2:
                    resetPossible = true;
                    break;
            }

        }
        else {
            previousCard = position;
        }
    }

    private void closeCards(Position pos1, Position pos2)
    {
        class CloseTask extends TimerTask
        {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    buttons[pos1.x][pos1.y].setImageResource(R.drawable.b2);
                    buttons[pos2.x][pos2.y].setImageResource(R.drawable.b2);
                    waiting = false;
                });
            }
        }

        Timer timer = new Timer();
        timer.schedule(new CloseTask(),1000);

    }


    private  ImageButton  generateButton(Position pos)
    {
        ImageButton b = new ImageButton(this);
        b.setImageResource(R.drawable.b2);
        b.setTag(pos);
        b.setOnClickListener(this);
        buttons[pos.x][pos.y] = b;
        return b;
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    public void resetGame(View view) {
        reload();
    }

}