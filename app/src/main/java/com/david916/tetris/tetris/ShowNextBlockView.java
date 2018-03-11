package com.david916.tetris.tetris;

/**
 * Created by david on 2017/12/2.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ShowNextBlockView extends View {
    public TetrisBlock nextBlock = null;

    public TetrisBlock createNextBlock(){
        nextBlock = new TetrisBlock(TetrisView.beginX,TetrisView.beginPoint);
        return  nextBlock;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF rel;


        float size = BlockUnit.UNITSIZE;
        int color[] = {0,Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.GRAY};
        if(nextBlock!=null) {
            paint.setColor(color[nextBlock.getColor()]);
            for (BlockUnit u : nextBlock.getUnits()) {

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(color[nextBlock.getColor()]);

                float tx = (float)(u.getX() - TetrisView.beginX + BlockUnit.UNITSIZE * 1.5)/2;
                float ty = (u.getY() + BlockUnit.UNITSIZE)/2  ;

                rel = new RectF(tx, ty, tx + size/2, ty + size/2);
                canvas.drawRoundRect(rel, 0, 0, paint);

                paint.setColor(Color.LTGRAY);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                canvas.drawRoundRect(rel, 0, 0, paint);
            }
        }
    }
    public ShowNextBlockView(Context context) {
        super(context);
    }
    public ShowNextBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

