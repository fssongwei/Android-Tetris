package com.david916.tetris.tetris;

/**
 * Created by david on 2017/12/2.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    public Button left, right, rotate, speedUp;
    public TextView score, level, speed;
    public int scoreValue,maxScoreValue,levelValue,speedValue;
    public String scoreString = "Score: ",levelString = "Level: ",speedString = "Speed: ";
    public TetrisView view;
    public ShowNextBlockView nextBlockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TetrisView)findViewById(R.id.tetrisView);
        left = (Button)findViewById(R.id.left);
        right = (Button)findViewById(R.id.right);
        rotate = (Button)findViewById(R.id.rotate);
        speedUp = (Button)findViewById(R.id.speedUp);
        nextBlockView = (ShowNextBlockView)findViewById(R.id.nextBlockView);
        nextBlockView.invalidate();
        score = (TextView)findViewById(R.id.score);
        level = (TextView)findViewById(R.id.level);
        speed = (TextView)findViewById(R.id.speed);
        scoreValue = maxScoreValue =0;
        levelValue = speedValue = 1;
        score.setText(scoreString + scoreValue);
        level.setText(levelString + levelValue);
        speed.setText(speedString + speedValue);

        view.init();

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.canMove)
                    view.getFallingBlock().move(-1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.invalidate();
                    }
                });
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.canMove)
                    view.getFallingBlock().move(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.invalidate();
                    }
                });
            }
        });
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.canMove == false)
                    return;
                TetrisBlock copyOfFallingBlock = view.getFallingBlock().clone();
                copyOfFallingBlock.rotate();
                if (copyOfFallingBlock.canRotate()) {
                    TetrisBlock fallinBlock = view.getFallingBlock();
                    fallinBlock.rotate();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.invalidate();
                    }
                });
            }
        });
        speedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.canMove) {
                    view.getFallingBlock().setY(view.getFallingBlock().getY() + BlockUnit.UNITSIZE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.invalidate();
                        }
                    });
                }
            }
        });
        view.setFather(this);
        view.invalidate();
    }


    public void show_dialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Game Over!");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MainActivity.this,StartPage.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

}