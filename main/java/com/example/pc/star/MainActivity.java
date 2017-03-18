package com.example.pc.star;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{
    static int best=0,score=0;
    static float rotate,xv=0,yv=0,a,dath;
    DrawView draw;
    boolean live=true,start=false,canadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout main = (LinearLayout)findViewById(R.id.root) ;
        draw = new DrawView(this) ;
        SharedPreferences best0 = getSharedPreferences("best",
                Activity.MODE_PRIVATE);
        int history = best0.getInt("best", 0);
        TextView besttext = (TextView)findViewById(R.id.best);
        besttext.setText("最高分："+history);
        best=history;
        final RelativeLayout re = (RelativeLayout) findViewById(R.id.re) ;
        main.post(new Runnable() {
            @Override
            public void run() {
                draw.width=(float) re.getWidth();
                draw.height=(float)re.getHeight();
                draw.ballsize=draw.height/80;
                draw.starsize=draw.width/5;
                draw.x=draw.width/2;
                draw.y=draw.height/2+5*draw.width/15;
                draw.h=draw.width/4;
                dath=draw.width/2000;
                xv=0;yv=0;
            }
        });

        draw.setOnTouchListener(new View.OnTouchListener()
        {


            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    yv=-draw.width/600;
                    xv=draw.width/1200;
                    start=true;
                }
                return true;

            }

        }) ;
        main.addView(draw) ;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(live){
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rotate=(float) Math.atan((draw.y-draw.height/2)/(draw.x-draw.width/2));
                    if(draw.x-draw.width/2<0)rotate+=Math.PI;
                    draw.xv=-(float)(xv*Math.sin(rotate))-(float)(yv*Math.cos(rotate));
                    draw.yv=(float)(xv*Math.cos(rotate))-(float)(yv*Math.sin(rotate));
                    draw.x+=draw.xv;
                    draw.y+=draw.yv;
                    if((draw.x-draw.width/2)*(draw.x-draw.width/2)+(draw.y-draw.height/2)*(draw.y-draw.height/2)>
                            (draw.width/2-draw.ballsize)*(draw.width/2-draw.ballsize)){
                        draw.x-=(float)(yv*Math.cos(rotate));
                        draw.y-=(float)(yv*Math.sin(rotate));
                        yv=0;
                    }
                    if(start)yv+=draw.width/50000;
                    draw.h+=dath;
                    if(draw.h>draw.width/2-2*draw.ballsize)dath=-draw.width/2000;
                    if(draw.h<draw.width/5+2*draw.ballsize)dath=draw.width/2000;
                    if(draw.x<draw.width/2)canadd=true;
                    if(draw.x>draw.width/2&&canadd){
                        canadd=false;
                        score++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView currentscore = (TextView)findViewById(R.id.score);
                                currentscore.setText("得分："+score);
                            }
                        });
                    }
                    draw.postInvalidate();
                    if(start&&(draw.x-draw.width/2)*(draw.x-draw.width/2)+(draw.y-draw.height/2)*(draw.y-draw.height/2)<
                            (draw.width/5+draw.ballsize)*(draw.width/5+draw.ballsize)){
                        live=false;
                        start=false;
                        main.post(MainActivity.this);
                    }
                    for(int i = 0;i<draw.num;i++){
                        if(start&&(draw.x-draw.otherx[i])*(draw.x-draw.otherx[i])+(draw.y-draw.othery[i])*(draw.y-draw.othery[i])<
                                4*draw.ballsize*draw.ballsize){
                            live=false;
                            start=false;
                            main.post(MainActivity.this);
                        }
                    }
                }
            }
        }).start();
    }
    @Override
    public void run() {
        if(best<score)best=score;
        SharedPreferences best0 = getSharedPreferences("best", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = best0.edit();
        editor.putInt("best", best);
        editor.commit();
        TextView bestscore = (TextView)findViewById(R.id.best2);
        bestscore.setVisibility(View.VISIBLE);
        bestscore.setText("最高分："+best);
        TextView currentscore = (TextView)findViewById(R.id.score2);
        currentscore.setVisibility(View.VISIBLE);
        currentscore.setText("得分："+score);
        Button button=(Button)findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
        TextView rule = (TextView)findViewById(R.id.rule);
        rule.setVisibility(View.VISIBLE);
    }
    public void replay(View view){
        score=0;
        live=true;
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}
