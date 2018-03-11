package com.david916.tetris.tetris;

/**
 * Created by david on 2017/12/2.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class TetrisView extends View {
    boolean flag;
    public static int beginPoint = 10;
    public static int max_x, max_y;
    public static float beginX;
    public int dropSpeed = 300;
    public int currentSpeed = 300;
    public boolean isRun = true;
    public boolean canMove = false;
    public Thread dropThread ;
    private int[] map = new int[100];
    private MainActivity father;
    private TetrisBlock fallingBlock;
    private Thread thread = new Thread();
    private float x1, y1, x2, y2;
    private ArrayList<TetrisBlock> blocks = new ArrayList<>();
    private float h,w;

    public void clear(){
        isRun = false;
        blocks.clear();
        thread = new Thread();
        fallingBlock = null;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP ){
            if(canMove == false)
                return false;

            x2 = event.getX();
            y2 = event.getY();

            float tx = fallingBlock.getX();
            float ty = fallingBlock.getY();

            if(x1 - x2 > BlockUnit.UNITSIZE){
                fallingBlock.move(-1);
                father.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TetrisView.this.invalidate();
                    }
                });
            }else if(x2 - x1 > BlockUnit.UNITSIZE){
                fallingBlock.move(1);
                father.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TetrisView.this.invalidate();
                    }
                });
            }else if(y1 - y2 > BlockUnit.UNITSIZE){
                fallingBlock.rotate();
                father.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TetrisView.this.invalidate();
                    }
                });
            }
        }
        return true;
    }
    public void init(){
        dropSpeed = 300;
        currentSpeed = 300;
        Arrays.fill(map, 0);
        flag = true;
        isRun = true;

        dropThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRun) {
                    try {
                        Thread.sleep(3000);
                        h = getHeight();
                        w = getWidth();
                        beginX  = (int)((w -  beginPoint)/(BlockUnit.UNITSIZE*2)) * BlockUnit.UNITSIZE + beginPoint;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(flag) {
                        father.nextBlockView.createNextBlock();

                        father.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                father.nextBlockView.invalidate();
                            }
                        });
                        flag = false;
                    }
                    if(thread.getState() == Thread.State.TERMINATED || thread.getState() == Thread.State.NEW) {
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                h = getHeight();
                                w = getWidth();

                                for(TetrisBlock b:blocks){
                                    if(b.getY()<=BlockUnit.UNITSIZE)
                                        isRun = false;
                                }

                                fallingBlock = father.nextBlockView.nextBlock;

                                father.nextBlockView.createNextBlock();

                                father.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        father.nextBlockView.invalidate();
                                    }
                                });

                                fallingBlock.setBlocks(blocks);

                                float ty;

                                int end = (int) ((h - BlockUnit.UNITSIZE - beginPoint) / BlockUnit.UNITSIZE);

                                float dropCount = fallingBlock.getY();
                                canMove = true;

                                while (dropCount-fallingBlock.getY()<=2 * BlockUnit.UNITSIZE) {
                                    try {
                                        Thread.sleep(currentSpeed);
                                        ty = fallingBlock.getY();
                                        ty = ty + BlockUnit.UNITSIZE;
                                        dropCount += BlockUnit.UNITSIZE;
                                        fallingBlock.setY(ty);

                                        father.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TetrisView.this.invalidate();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                canMove = false;

                                blocks.add(fallingBlock);

                                TetrisBlock temp = fallingBlock;

                                for (BlockUnit u : temp.getUnits()) {
                                    int index = (int) ((u.getY() - beginPoint) / BlockUnit.UNITSIZE);
                                    if(index != -1)
                                        map[index]++;
                                }

                                int full = (int) ((w - BlockUnit.UNITSIZE - beginPoint) / BlockUnit.UNITSIZE) + 1;

                                for (int i = 0; i <= end; i++) {
                                    if (map[i] >= full) {

                                        father.scoreValue += 100;
                                        if(father.scoreValue > 1000) {
                                            father.speedValue += 1;
                                            father.levelValue += 1;
                                        }
                                        if(father.scoreValue>father.maxScoreValue){
                                            father.maxScoreValue = father.scoreValue;
                                        }

                                        map[i] = 0;
                                        for (int j = i; j > 0; j--)
                                            map[j] = map[j - 1];
                                        map[0] = 0;

                                        for (TetrisBlock b : blocks)
                                            b.remove(i);
                                        for (int j = blocks.size()-1; j>=0; j--) {
                                            if (blocks.get(j).getUnits().isEmpty()) {
                                                blocks.remove(j);
                                                continue;
                                            }
                                            for (BlockUnit u : blocks.get(j).getUnits()) {
                                                if ((int) ((u.getY() - beginPoint) / BlockUnit.UNITSIZE) < i)
                                                    u.setY(u.getY() + BlockUnit.UNITSIZE);
                                            }
                                        }



                                        father.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                father.score.setText(father.scoreString + father.scoreValue);
                                                father.speed.setText(father.speedString + father.speedValue);
                                                father.level.setText(father.levelString + father.levelValue);
                                                TetrisView.this.invalidate();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        thread.start();
                    }

                }
                if(isRun == false){
                    father.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(father);
                            dialog.setMessage("Game Over!");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent(father,StartPage.class);
                                    father.startActivity(intent);
                                }
                            });
                            dialog.show();

                        }
                    });
                }
            }
        });
        dropThread.start();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        max_x = getWidth();
        max_y = getHeight();
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF rel;
        BlockUnit.UNITSIZE = max_x/10;
        float size = BlockUnit.UNITSIZE;

        int color[] = {0,Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.GRAY};

        if(!blocks.isEmpty()){
            for(TetrisBlock block:blocks){
                paint.setColor(color[block.getColor()]);
                for(BlockUnit u:block.getUnits()){
                    float tx = u.getX();
                    float ty = u.getY();
                    rel = new RectF(tx, ty, tx + size, ty + size);
                    canvas.drawRoundRect(rel, 0, 0, paint);
                }
            }
        }
        if(fallingBlock!=null) {
            paint.setColor(color[fallingBlock.getColor()]);
            for (BlockUnit u : fallingBlock.getUnits()) {
                float tx = u.getX();
                float ty = u.getY();
                rel = new RectF(tx, ty, tx + size, ty + size);
                canvas.drawRoundRect(rel, 0, 0, paint);
            }
        }
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        for(int i=beginPoint; i<max_x-max_x/10; i+= max_x/10){
            for(int j=beginPoint; j<max_y-max_x/10; j+= max_x/10) {
                rel = new RectF(i, j, i + max_x/10, j + max_x/10);
                canvas.drawRoundRect(rel, 0, 0, paint);
            }
        }
    }
    public TetrisBlock getFallingBlock() {
        return fallingBlock;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public void setFallingBlock(TetrisBlock fallingBlock) {
        this.fallingBlock = fallingBlock;
    }

    public Activity getFather() {
        return father;
    }

    public void setFather(MainActivity father) {
        this.father = father;
    }

    public TetrisView(Context context) {
        super(context);
    }

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
