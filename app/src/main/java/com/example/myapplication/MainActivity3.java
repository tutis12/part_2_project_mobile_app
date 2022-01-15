package com.example.myapplication;

import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private Button button;
    private TextView textView;
    private String name;
    private int del = 1500;
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
        //path = path.substring(path.indexOf(":")+1);
//        Toast.makeText(getApplicationContext(), path,
//                Toast.LENGTH_SHORT).show();
        String text=byteToString(file);
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
    final int mx = 110;
    int par = 0;
    private void showTextSmall(String s)
    {
        par = 1 - par;
        ImageView iv = (ImageView) findViewById(R.id.imageView);

        Bitmap bitmap = Bitmap.createBitmap(10, 13, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paintBg = new Paint();
        paintBg.setColor(Color.WHITE);
        canvas.drawRect(0, 0, 11, 14, paintBg);
        StringBuilder a = new StringBuilder();
        for(int i=0;i<10;i++)
            if((i+par)%2==0)
                a.append('2');
            else
                a.append('3');
        while(s.length()<110)
            s = s + "0";
        s = a + s + a;
        for(int i=0;i<s.length();i++)
        {
            int x = i%10;
            int y = i/10;
            Paint col = paintBg;
            if(s.charAt(i)=='0')
                col.setColor(Color.WHITE);
            else if(s.charAt(i)=='1')
                col.setColor(Color.BLACK);
            else if(s.charAt(i)=='2')
                col.setColor(Color.WHITE);
            else if(s.charAt(i)=='3')
                col.setColor(Color.BLACK);
            canvas.drawRect(x, y, x+1, y+1, col);
        }

        iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 1300, false));
    }
    private void showText(String name, String s, int ms)
    {
        final int cntBeg = Math.max(4, 4000/ms);
        textView.setText(name);
        List<String>T = new ArrayList<>();
        {
            StringBuilder beg = new StringBuilder();
            StringBuilder beg1 = new StringBuilder();
            for(int j=0;j<11;j++)
            {
                for(int i=0;i<10;i++)
                {
                    if((i+j)%2==0)
                        beg.append('2');
                    else
                        beg.append('3');
                }
            }
            for(int j=0;j<11;j++)
            {
                for(int i=0;i<10;i++)
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
            x += 128;
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
    public void goBack()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}