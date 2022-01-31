package com.example.myapplication;

import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity3 extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private Button button;
    private TextView textView;
    private String name;
    private int del = 1500;
    private boolean isImage = false;
    private boolean debugMode = false;
    Bitmap ImageFile = null;
    Bitmap ImageFile1 = null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        button = (Button)findViewById(R.id.button3);
        textView = (TextView)findViewById(R.id.fileText);

        //String path = getIntent().getExtras().getString("path");
        name = getIntent().getExtras().getString("name");
        byte[]file = getIntent().getExtras().getByteArray("file");
        del = Integer.parseInt(getIntent().getExtras().getString("delay"));
        iterations = Integer.parseInt(getIntent().getExtras().getString("iterations"));
        isImage = getIntent().getExtras().getBoolean("image");
        debugMode = getIntent().getExtras().getBoolean("debug");

        sz1 = Integer.parseInt(getIntent().getExtras().getString("w"));
        sz2 = Integer.parseInt(getIntent().getExtras().getString("h"));
        mx = sz1*(sz2-2);
        //path = path.substring(path.indexOf(":")+1);
//        Toast.makeText(getApplicationContext(), path,
//                Toast.LENGTH_SHORT).show();
        String text = isImage ? byteToStringImage(file, name) : byteToString(file);
        System.out.println(text.length());
        System.out.println(text);
//        Toast.makeText(getApplicationContext(), text,
//                Toast.LENGTH_SHORT).show();

        showText(name, text, del);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }
    int sz1 = 16;
    int sz2 = 21;
    int mx = sz1*(sz2-2);
    int par = 0;
    private void showTextSmall(String s)
    {
        par = 1 - par;
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = Bitmap.createBitmap(sz1+2, sz2 + 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paintBg = new Paint();
        paintBg.setColor(Color.WHITE);
        canvas.drawRect(0, 0, sz1 + 3, sz2 + 3, paintBg);
        StringBuilder a = new StringBuilder();
        for(int i=0;i<sz1;i++)
            if((i+par)%2==0)
                a.append('2');
            else
                a.append('3');
        while(s.length()<mx)
            s = s + "0";
        s = a + s + a;
        for(int i=0;i<s.length();i++)
        {
            int x = i%sz1;
            int y = i/sz1;
            Paint col = paintBg;
            if(s.charAt(i)=='0')
                col.setColor(Color.WHITE);
            else if(s.charAt(i)=='1')
                col.setColor(Color.BLACK);
            else if(s.charAt(i)=='2')
                col.setColor(Color.WHITE);
            else if(s.charAt(i)=='3')
                col.setColor(Color.BLACK);
            canvas.drawRect(x+1, y+1, x+2, y+2, col);
        }
        paintBg.setColor(Color.RED);
        canvas.drawRect(0, 0, 1, 1, paintBg);
        canvas.drawRect(sz1+1, 0, sz1 + 2, 1, paintBg);
        canvas.drawRect(0, sz2+1, 1, sz2+2, paintBg);
        canvas.drawRect(sz1+1, sz2+1, sz1 + 2, sz2+2, paintBg);
        iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1080, 1350, false));
        if(debugMode)
            iv.setImageBitmap(Bitmap.createScaledBitmap(ImageFile1, 1080, 1350, false));
    }
    private void showText(String name, String s, int ms)
    {

        final int cntBeg = Math.max(4, 4000/ms);
        textView.setText(name);
        List<String>T = new ArrayList<>();
        {
            StringBuilder beg = new StringBuilder();
            StringBuilder beg1 = new StringBuilder();
            for(int j=0;j<sz2-2;j++)
            {
                for(int i=0;i<sz1;i++)
                {
                    if((i+j)%2==0)
                        beg.append('2');
                    else
                        beg.append('3');
                }
            }
            for(int j=0;j<sz2-2;j++)
            {
                for(int i=0;i<sz1;i++)
                {
                    if((i+j)%2==1)
                        beg1.append('2');
                    else
                        beg1.append('3');
                }
            }
            for(int k=0;k<cntBeg;k++){
                T.add(beg.toString());
                T.add(beg1.toString());
            }
        }
        for(int i=0;i<s.length();i += mx)
        {
            int j=i + mx;
            j=Math.min(j,s.length());
            T.add(s.substring(i,j));
        }
        {
            int c = s.length();
            StringBuilder C = new StringBuilder();
            while(c>0)
            {
                if(c % 2 == 0)
                    C.append('0');
                else
                    C.append('1');
                c/=2;
            }
            T.add(cntBeg * 2, C.toString());
            System.out.println(C);
            System.out.println(s.length());
        }
        textView.post(new Runnable() {
            Integer it = 0;
            @Override
            public void run() {
                if(it < T.size())
                {
                    textView.setText(name + "     " + (it + 1) + " / " + T.size());
                    showTextSmall(T.get(it));
                }
                it++;
                if(it < T.size())
                {
                    textView.postDelayed(this, del);
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String byteToString(byte[] fileContent)
    {
        if(fileContent == null)
            return "";
        StringBuilder text = new StringBuilder();
        for(byte i : fileContent)
        {
            int x = i;
            x += 256;
            x %= 256;
            for(int t=0;t<8;t++)
            {
                if(x % 2 == 0)
                    text.append('0');
                else
                    text.append('1');
                x /= 2;
            }
//            int  a = x/26;
//            int b = x%26;
//            char A = (char) ('a' + a);
//            char B = (char) ('a' + b);
//            text.append(A);
//            text.append(B);
        }
        return text.toString();
    }
    private int dotP(int x1, int y1, int x2, int y2)
    {
        return x1 * x2 + y1 * y2;
    }
    private int iterations = 1000;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String byteToStringImage(byte[] fileContent, String name)
    {
        if(fileContent == null)
            return "";
        ImageFile = BitmapFactory.decodeByteArray(fileContent, 0, fileContent.length);
        int[] pixels = new int[ImageFile.getWidth() * ImageFile.getHeight()];
        int[][][] rgb = new int[ImageFile.getHeight()][ImageFile.getWidth()][3];
        int[][][] rgb1 = new int[ImageFile.getHeight()][ImageFile.getWidth()][3];
        ImageFile.getPixels(pixels, 0, ImageFile.getWidth(), 0, 0, ImageFile.getWidth(), ImageFile.getHeight());
        for (int x = 0; x < ImageFile.getHeight(); x++)
            for (int y = 0; y < ImageFile.getWidth(); y++){
                //Color c = Color.valueOf(pixels[x * ImageFile.getWidth() + y]);
                int ci = pixels[x * ImageFile.getWidth() + y];
                rgb[x][y][0] = Color.red(ci);
                rgb[x][y][1] = Color.green(ci);
                rgb[x][y][2] = Color.blue(ci);
                rgb1[x][y][0] = 128;
                rgb1[x][y][1] = 128;
                rgb1[x][y][2] = 128;
            }
        List<Boolean>S = new ArrayList<>();
        int w = ImageFile.getWidth();
        int h = ImageFile.getHeight();
        int s = w;
        for(int t=0;t<30;t++)
        {
            if(t == 15)
                s = h;
            if(s % 2 == 1)
                S.add(true);
            else
                S.add(false);
            s /= 2;
        }
        Random rand = new Random();
        int testc = 0;
        int logwh = 1;
        {
            int sz = 2;
            while(sz < w * h)
            {
                sz *= 2;
                logwh++;
            }
        }
        long bst=-1;
        int rr1=0, rr2=0;
        for(int tim = 0; tim < iterations; tim++)
        {
            int r1 = rand.nextInt(w*h);
            int r2 = rand.nextInt(w*h);
            long good = 0;
            {
                int x1 = r1 % h;
                int y1 = r1 / h;
                int x2 = r2 % h;
                int y2 = r2 / h;
                int d=(int)Math.floor(Math.sqrt((x1-x2)*(x1-x2)+ (y2-y1)*(y2-y1)))+1;
                if(d < 2)
                {
                    tim--;
                    continue;
                }
                float prop = (float)(d) / (float)(h+w);
                if(tim < iterations/20)
                {
                    if(prop < 0.15 || prop > 0.4)
                    {
                        tim--;
                        continue;
                    }
                }
                else if (tim < iterations/20*2)
                {
                    if(prop > 0.20 || prop < 0.07)
                    {
                        tim--;
                        continue;
                    }
                }
                else if (tim < iterations/20*10)
                {
                    if(d > 25)
                    {
                        tim--;
                        continue;
                    }
                }
                else{
                    if(d > 17)
                    {
                        tim--;
                        continue;
                    }
                }
                int x3 = ((x1 + x2) + (y2 - y1)) / 2;
                int y3 = ((y1 + y2) + (x1 - x2)) / 2;
                int x4 = ((x1 + x2) - (y2 - y1)) / 2;
                int y4 = ((y1 + y2) - (x1 - x2)) / 2;
                int xmn = Math.min(h - 1, Math.max(0, Math.min(Math.min(x1, x2) , Math.min(x3, x4))));
                int ymn = Math.min(w - 1, Math.max(0, Math.min(Math.min(y1, y2) , Math.min(y3, y4))));
                int xmx = Math.min(h - 1, Math.max(0, Math.max(Math.max(x1, x2) , Math.max(x3, x4))));
                int ymx = Math.min(w - 1, Math.max(0, Math.max(Math.max(y1, y2) , Math.max(y3, y4))));
                //int cnt = 0;
                int r = 0;
                int g = 0;
                int b = 0;
                for(int x = xmn; x <= xmx; x++)
                {
                    for(int y = ymn; y <= ymx; y++)
                    {
                        boolean ok=true;
                        if(dotP(x3-x1, y3-y1, x-x1, y-y1) < 0)
                            ok = false;
                        if(dotP(x4-x1, y4-y1, x-x1, y-y1) < 0)
                            ok = false;
                        if(dotP(x3-x2, y3-y2, x-x2, y-y2) < 0)
                            ok = false;
                        if(dotP(x4-x2, y4-y2, x-x2, y-y2) < 0)
                            ok = false;
                        if(ok)
                        {
                            //cnt++;
                            r += rgb[x][y][0] - rgb1[x][y][0];
                            g += rgb[x][y][1] - rgb1[x][y][1];
                            b += rgb[x][y][2] - rgb1[x][y][2];
                        }
                    }
                }
                good = Math.abs(r) + Math.abs(g) + Math.abs(b);
            }
            testc++;
            if(good > bst)
            {
                bst = good;
                rr1 = r1;
                rr2 = r2;
            }
            if(testc < 60)
            {
                tim--;
                continue;
            }
            r1 = rr1;
            r2 = rr2;
            int x1 = r1 % h;
            int y1 = r1 / h;
            int x2 = r2 % h;
            int y2 = r2 / h;
            int x3 = ((x1 + x2) + (y2 - y1)) / 2;
            int y3 = ((y1 + y2) + (x1 - x2)) / 2;
            int x4 = ((x1 + x2) - (y2 - y1)) / 2;
            int y4 = ((y1 + y2) - (x1 - x2)) / 2;
            int xmn = Math.min(h - 1, Math.max(0, Math.min(Math.min(x1, x2) , Math.min(x3, x4))));
            int ymn = Math.min(w - 1, Math.max(0, Math.min(Math.min(y1, y2) , Math.min(y3, y4))));
            int xmx = Math.min(h - 1, Math.max(0, Math.max(Math.max(x1, x2) , Math.max(x3, x4))));
            int ymx = Math.min(w - 1, Math.max(0, Math.max(Math.max(y1, y2) , Math.max(y3, y4))));
            int cnt = 0;
            int r = 0;
            int g = 0;
            int b = 0;
            for(int x = xmn; x <= xmx; x++)
            {
                for(int y = ymn; y <= ymx; y++)
                {
                    boolean ok=true;
                    if(dotP(x3-x1, y3-y1, x-x1, y-y1) < 0)
                        ok = false;
                    if(dotP(x4-x1, y4-y1, x-x1, y-y1) < 0)
                        ok = false;
                    if(dotP(x3-x2, y3-y2, x-x2, y-y2) < 0)
                        ok = false;
                    if(dotP(x4-x2, y4-y2, x-x2, y-y2) < 0)
                        ok = false;
                    if(ok)
                    {
                        cnt++;
                        r += rgb[x][y][0] - rgb1[x][y][0];
                        g += rgb[x][y][1] - rgb1[x][y][1];
                        b += rgb[x][y][2] - rgb1[x][y][2];
                    }
                }
            }
            r/=cnt;
            g/=cnt;
            b/=cnt;
            r = Math.min(63, Math.max(r, -64));
            g = Math.min(63, Math.max(g, -64));
            b = Math.min(63, Math.max(b, -64));
            int it = 0;
            int lg = logwh;
            for (int vals : new int[]{r1, r2, r + 64,g + 64,b + 64})
            {
                it++;
                if(it == 3)
                    lg = 7;
                for(int t=0;t<lg;t++)
                {
                    if(vals%2==1)
                        S.add(true);
                    else
                        S.add(false);
                    vals/=2;
                }
            }

            bst = -1;
            testc = 0;
            for(int x = xmn; x <= xmx; x++)
            {
                for(int y = ymn; y <= ymx; y++)
                {
                    boolean ok=true;
                    if(dotP(x3-x1, y3-y1, x-x1, y-y1) < 0)
                        ok = false;
                    if(dotP(x4-x1, y4-y1, x-x1, y-y1) < 0)
                        ok = false;
                    if(dotP(x3-x2, y3-y2, x-x2, y-y2) < 0)
                        ok = false;
                    if(dotP(x4-x2, y4-y2, x-x2, y-y2) < 0)
                        ok = false;
                    if(ok)
                    {
                        rgb1[x][y][0] += r;
                        rgb1[x][y][1] += g;
                        rgb1[x][y][2] += b;
                        rgb1[x][y][0] = Math.min(rgb1[x][y][0], 255);
                        rgb1[x][y][1] = Math.min(rgb1[x][y][1], 255);
                        rgb1[x][y][2] = Math.min(rgb1[x][y][2], 255);
                        rgb1[x][y][0] = Math.max(rgb1[x][y][0], 0);
                        rgb1[x][y][1] = Math.max(rgb1[x][y][1], 0);
                        rgb1[x][y][2] = Math.max(rgb1[x][y][2], 0);
                    }
                }
            }
        }

        for (int x = 0; x < ImageFile.getHeight(); x++)
            for (int y = 0; y < ImageFile.getWidth(); y++)
                pixels[x * ImageFile.getWidth() + y] = Color.rgb(rgb1[x][y][0], rgb1[x][y][1], rgb1[x][y][2]);
        ImageFile1 = ImageFile.copy(Bitmap.Config.ARGB_8888, true);
        ImageFile1.setPixels(pixels, 0, ImageFile1.getWidth(), 0, 0, ImageFile1.getWidth(), ImageFile1.getHeight());
        StringBuilder text = new StringBuilder();
        for(boolean i : S)
            if(i)
                text.append('1');
            else
                text.append('0');
        return text.toString();
    }
    public void goBack()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}