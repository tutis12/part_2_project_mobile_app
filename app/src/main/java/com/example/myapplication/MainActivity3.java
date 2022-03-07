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
    private boolean circles = false;
    private boolean cosines = false;
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
        circles = getIntent().getExtras().getBoolean("circles");
        cosines = getIntent().getExtras().getBoolean("cosines");

        sz1 = Integer.parseInt(getIntent().getExtras().getString("w"));
        sz2 = Integer.parseInt(getIntent().getExtras().getString("h"));
        mx = sz1*(sz2-2);
        //path = path.substring(path.indexOf(":")+1);
//        Toast.makeText(getApplicationContext(), path,
//                Toast.LENGTH_SHORT).show();
        String text = isImage ? byteToStringImage(file, name) : byteToString(file);
        //System.out.println(text.length());
        //System.out.println(text);
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
    final double wP = 0.6;
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
    private String ImageToBytes(int w, int h, int[][][] rgb, double[][][] rgb1)
    {
        List<Boolean>S = new ArrayList<>();
        {
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
        }
        for(int k = 1; k <= Math.min(w,h); k *= 2)
        {
            int sw = w / k;
            int sh = h / k;
            if(sw * k < w)
                sw++;
            if(sh * k < h)
                sh++;
            if(k > 32)
                break;
            for(int i = 0; i < k; i++)
            {
                for(int j = 0; j < k; j++)
                {
                    int x0 = i * sh;
                    int x1 = Math.min(h - 1, (i + 1) * sh - 1);
                    int y0 = j * sw;
                    int y1 = Math.min(w - 1, (j + 1) * sw - 1);
                    final int mxfr = 6;
                    int[][][] delt = new int[mxfr][mxfr][3];
                    int[][]lg = new int[mxfr][mxfr];
                    int[][]mxval = new int[mxfr][mxfr];
                    for(int a=0;a<mxfr;a++)
                    {
                        for(int b=0;b<mxfr;b++)
                        {
                            lg[a][b] = mxfr + 2 - a - b;
                        }
                    }
                    for(int a=0;a<mxfr;a++)
                    {
                        for(int b=0;b<mxfr;b++)
                        {
                            mxval[a][b] = 1;
                            for(int cc = 1; cc < lg[a][b]; cc++)
                                mxval[a][b] *= 2;
                        }
                    }
                    boolean chang = true;
                    int mxit = 4;
                    while(chang && mxit > 0)
                    {
                        chang = false;
                        mxit--;
                        for(int c=0;c<3;c++)
                        {
                            for(int a=0;a<mxfr;a++)
                            {
                                for(int b=0;b<mxfr;b++)
                                {
                                    delt[a][b][c] = 0;
                                    if(lg[a][b] <= 0)
                                        continue;
                                    double sum = 0;
                                    double wsum = 0;
                                    for(int x = x0; x <= x1; x++)
                                    {
                                        for(int y = y0; y <= y1; y++)
                                        {
                                            double xd = Math.PI / 2 * ((double)(x-x0))/((double)(x1-x0));
                                            double yd = Math.PI / 2 * ((double)(y-y0))/((double)(y1-y0));
                                            double w1 = 1 + Math.cos(xd * a) * Math.cos(yd * b) + 0.05;
                                            double dc = rgb[x][y][c] - rgb1[x][y][c];
                                            wsum += w1;
                                            sum += w1 * dc;
                                        }
                                    }
                                    double val = sum / wsum;
                                    int d = (int)(val>0 ? Math.floor(val) : -Math.floor(-val));
                                    if(d != 0)
                                    {
                                        int dt1 = delt[a][b][c] + d;
                                        dt1 = Math.max(dt1, -mxval[a][b]);
                                        dt1 = Math.min(dt1, mxval[a][b] - 1);
                                        if(dt1 != delt[a][b][c])
                                        {
                                            chang = true;
                                            int dval = dt1 - delt[a][b][c];
                                            delt[a][b][c] = dt1;
                                            for(int x = x0; x <= x1; x++)
                                            {
                                                for(int y = y0; y <= y1; y++)
                                                {
                                                    double xd = Math.PI / 2 * ((double)(x-x0))/((double)(x1-x0));
                                                    double yd = Math.PI / 2 * ((double)(y-y0))/((double)(y1-y0));
                                                    double w1 = 1 + Math.cos(xd * a) * Math.cos(yd * b) + 0.05;
                                                    rgb1[x][y][c] += dval * w1;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for(int a=0;a<mxfr;a++)
                    {
                        for(int b=0;b<mxfr;b++)
                        {
                            for(int c=0;c<3;c++)
                            {
                                int val = delt[a][b][c] + mxval[a][b];
                                for(int t=0;t<lg[a][b];t++)
                                {
                                    if(val % 2 == 1)
                                        S.add(true);
                                    else
                                        S.add(false);
                                    val /= 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        int[] pixels = new int[ImageFile.getWidth() * ImageFile.getHeight()];
        for (int x = 0; x < ImageFile.getHeight(); x++)
            for (int y = 0; y < ImageFile.getWidth(); y++)
                pixels[x * ImageFile.getWidth() + y] = Color.rgb(
                        Math.min(255,Math.max(0,(int)Math.floor(rgb1[x][y][0]))),
                        Math.min(255,Math.max(0,(int)Math.floor(rgb1[x][y][1]))),
                        Math.min(255,Math.max(0,(int)Math.floor(rgb1[x][y][2]))));
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
        double[][][] rgb1 = new double[ImageFile.getHeight()][ImageFile.getWidth()][3];
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
        if(cosines)
            return ImageToBytes(w, h, rgb, rgb1);
        {
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
        double bst=-1;
        int rr1=0, rr2=0;
        double dopt = 180;
        for(int tim = 0; tim < iterations; tim++)
        {
            int r1 = rand.nextInt(w*h);
            int r2 = rand.nextInt(w*h);
            //System.out.println(tim + " / " + iterations);
            //if(dopt < 50)
            {
                int x1 = r1 % h;
                int y1 = r1 / h;
                int delt = (int)(Math.floor(dopt*1.5+3));
                int dx = rand.nextInt(delt * 2 + 1) - delt;
                int dy = rand.nextInt(delt * 2 + 1) - delt;
                x1 += dx;
                y1 += dy;
                if(x1<0 || x1>=h || y1<0 || y1>=w)
                {
                    tim--;
                    continue;
                }
                r2 = x1 + y1 * h;
            }
            double good = 0;
            {
                int x1 = r1 % h;
                int y1 = r1 / h;
                int x2 = r2 % h;
                int y2 = r2 / h;
                double d = (Math.sqrt((x1-x2)*(x1-x2)+ (y2-y1)*(y2-y1))) / 2;
                //double ss = d / dopt;
//                if(Math.abs(ss-1.0) > 0.25 && rand.nextFloat()<0.9)
//                {
//                    tim--;
//                    continue;
//                }
                if(d < 2)
                {
                    tim--;
                    continue;
                }
                int x3 = ((x1 + x2) + (y2 - y1)) / 2;
                int y3 = ((y1 + y2) + (x1 - x2)) / 2;
                int x4 = ((x1 + x2) - (y2 - y1)) / 2;
                int y4 = ((y1 + y2) - (x1 - x2)) / 2;
                int xmn = Math.min(h - 1, Math.max(0, Math.min(Math.min(x1, x2) , Math.min(x3, x4))));
                int ymn = Math.min(w - 1, Math.max(0, Math.min(Math.min(y1, y2) , Math.min(y3, y4))));
                int xmx = Math.min(h - 1, Math.max(0, Math.max(Math.max(x1, x2) , Math.max(x3, x4))));
                int ymx = Math.min(w - 1, Math.max(0, Math.max(Math.max(y1, y2) , Math.max(y3, y4))));
                int xm = (x1 + x2) / 2;
                int ym = (y1 + y2) / 2;
                int dx = x1 - xm;
                int dy = y1 - ym;
                int dd = dx * dx + dy * dy;
                int d1 = (int)Math.floor(Math.sqrt(dd));
                if(circles)
                {
                    xmn = Math.min(h - 1, Math.max(0, xm - d1 - 1));
                    ymn = Math.min(w - 1, Math.max(0, ym - d1 - 1));
                    xmx = Math.min(h - 1, Math.max(0, xm + d1 + 1));
                    ymx = Math.min(w - 1, Math.max(0, ym + d1 + 1));
                }
                //int cnt = 0;
                double r = 0;
                double g = 0;
                double b = 0;
                for(int x = xmn; x <= xmx; x++)
                {
                    for(int y = ymn; y <= ymx; y++)
                    {
                        boolean ok = true;
                        double ww = 1;
                        if(circles)
                        {
                            dx = x - xm;
                            dy = y - ym;
                            int dd1 = dx * dx + dy * dy;
                            ok = dd1 <= dd;
                            ww = Math.exp(-wP*(float)dd1/(float)dd);
                        }
                        else{
                            if(dotP(x3-x1, y3-y1, x-x1, y-y1) < 0)
                                ok = false;
                            if(dotP(x4-x1, y4-y1, x-x1, y-y1) < 0)
                                ok = false;
                            if(dotP(x3-x2, y3-y2, x-x2, y-y2) < 0)
                                ok = false;
                            if(dotP(x4-x2, y4-y2, x-x2, y-y2) < 0)
                                ok = false;
                        }
                        if(ok)
                        {
                            //cnt++;
                            r += ww*(rgb[x][y][0] - rgb1[x][y][0]);
                            g += ww*(rgb[x][y][1] - rgb1[x][y][1]);
                            b += ww*(rgb[x][y][2] - rgb1[x][y][2]);
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
            int maxt = 200;
            if(tim < iterations / 20)
                maxt = 20;
            if(tim < iterations / 10)
                maxt = 50;
            if(tim < iterations / 7)
                maxt = 70;
            if(testc < maxt)
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
            dopt = (Math.sqrt((x1-x2)*(x1-x2)+ (y2-y1)*(y2-y1))) / 2;
            int x3 = ((x1 + x2) + (y2 - y1)) / 2;
            int y3 = ((y1 + y2) + (x1 - x2)) / 2;
            int x4 = ((x1 + x2) - (y2 - y1)) / 2;
            int y4 = ((y1 + y2) - (x1 - x2)) / 2;
            int xmn = Math.min(h - 1, Math.max(0, Math.min(Math.min(x1, x2) , Math.min(x3, x4))));
            int ymn = Math.min(w - 1, Math.max(0, Math.min(Math.min(y1, y2) , Math.min(y3, y4))));
            int xmx = Math.min(h - 1, Math.max(0, Math.max(Math.max(x1, x2) , Math.max(x3, x4))));
            int ymx = Math.min(w - 1, Math.max(0, Math.max(Math.max(y1, y2) , Math.max(y3, y4))));
            double cnt = 0;
            double r = 0;
            double g = 0;
            double b = 0;
            int xm = (x1 + x2) / 2;
            int ym = (y1 + y2) / 2;
            int dx = x1 - xm;
            int dy = y1 - ym;
            int dd = dx * dx + dy * dy;
            int d1 = (int)Math.floor(Math.sqrt(dd));
            if(circles)
            {
                xmn = Math.min(h - 1, Math.max(0, xm - d1 - 1));
                ymn = Math.min(w - 1, Math.max(0, ym - d1 - 1));
                xmx = Math.min(h - 1, Math.max(0, xm + d1 + 1));
                ymx = Math.min(w - 1, Math.max(0, ym + d1 + 1));
            }
            for(int x = xmn; x <= xmx; x++)
            {
                for(int y = ymn; y <= ymx; y++)
                {
                    boolean ok = true;
                    double ww = 1;
                    if(circles)
                    {
                        dx = x - xm;
                        dy = y - ym;
                        int dd1 = dx * dx + dy * dy;
                        ok = dd1 <= dd;
                        ww = Math.exp(-wP * (float)dd1/(float)dd);
                    }
                    else{
                        if(dotP(x3-x1, y3-y1, x-x1, y-y1) < 0)
                            ok = false;
                        if(dotP(x4-x1, y4-y1, x-x1, y-y1) < 0)
                            ok = false;
                        if(dotP(x3-x2, y3-y2, x-x2, y-y2) < 0)
                            ok = false;
                        if(dotP(x4-x2, y4-y2, x-x2, y-y2) < 0)
                            ok = false;
                    }
                    if(ok)
                    {
                        cnt+=ww;
                        r += ww*(rgb[x][y][0] - rgb1[x][y][0]);
                        g += ww*(rgb[x][y][1] - rgb1[x][y][1]);
                        b += ww*(rgb[x][y][2] - rgb1[x][y][2]);
                    }
                }
            }
            r/=cnt;
            g/=cnt;
            b/=cnt;
            r = Math.min(63, Math.max(r, -64));
            g = Math.min(63, Math.max(g, -64));
            b = Math.min(63, Math.max(b, -64));
            int R = (int)(r < 0 ? Math.ceil(r): Math.floor(r));
            int G = (int)(g < 0 ? Math.ceil(g): Math.floor(g));
            int B = (int)(b < 0 ? Math.ceil(b): Math.floor(b));
            int it = 0;
            int lg = logwh;
            for (int vals : new int[]{r1, r2, R + 64, G + 64, B + 64})
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
                    boolean ok = true;
                    double ww = 1;
                    if(circles)
                    {
                        dx = x - xm;
                        dy = y - ym;
                        int dd1 = dx * dx + dy * dy;
                        ok = dd1 <= dd;
                        ww = Math.exp(-wP * (float)dd1/(float)dd);
                    }
                    else{
                        if(dotP(x3-x1, y3-y1, x-x1, y-y1) < 0)
                            ok = false;
                        if(dotP(x4-x1, y4-y1, x-x1, y-y1) < 0)
                            ok = false;
                        if(dotP(x3-x2, y3-y2, x-x2, y-y2) < 0)
                            ok = false;
                        if(dotP(x4-x2, y4-y2, x-x2, y-y2) < 0)
                            ok = false;
                    }
                    if(ok)
                    {
                        rgb1[x][y][0] += R*ww;
                        rgb1[x][y][1] += G*ww;
                        rgb1[x][y][2] += B*ww;
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
                pixels[x * ImageFile.getWidth() + y] = Color.rgb(
                        Math.min(255,Math.max(0,(int)Math.floor(rgb1[x][y][0]))),
                        Math.min(255,Math.max(0,(int)Math.floor(rgb1[x][y][1]))),
                        Math.min(255,Math.max(0,(int)Math.floor(rgb1[x][y][2]))));
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