package com.example.pc.star;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Administrator on 2016/7/16.
 */
public class DrawView extends View {
    static float ballsize=0,starsize,width=1980,height=1080;
    static float x,y,rotate,xv=0,yv=0,h,other[],otherx[],othery[];
    int num=3;
    Bitmap star,ball,otherball;
    /**
     *
     * @param context
     */
    public DrawView(Context context) {
        super(context);
        other=new float[num];
        otherx=new float[num];
        othery=new float[num];
        other[0]=(float) Math.PI/6;
        for(int i=1;i<num;i++){
            other[i]=other[0]+2*i*(float)Math.PI/3;
        }
        star= BitmapFactory.decodeResource(getResources(), R.mipmap.star);
        ball= BitmapFactory.decodeResource(getResources(), R.mipmap.ball);
        otherball= BitmapFactory.decodeResource(getResources(), R.mipmap.other);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //创建画笔 ;

        Paint p = new Paint() ;
        //绘制一个小球 ；
        drawball(canvas,width/2,height/2,starsize,star);
        drawball(canvas,x,y,ballsize,ball);
        for(int i=0;i<num;i++){
            otherx[i]=width/2+h*(float) Math.cos(other[i]);
            othery[i]=height/2+h*(float) Math.sin(other[i]);
            drawball(canvas,otherx[i],othery[i],ballsize,otherball);
        }
    }
    public void drawball(Canvas canvas,float ballx,float bally,float ballsize,Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (int)ballsize*2;
        int newHeight = (int)ballsize*2;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap0 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(bitmap0,ballx-ballsize,bally-ballsize,null);
    }

}

