package com.example.aut_final_project;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

public class audioStego extends Activity {
    Button b, c, embedding, e, g, share;
    RadioButton rb1, rb2;
    EditText et;
    MediaPlayer mp1, mp2;
    int p1 = 1;
    int p2 = 1;
    byte audio[] = null;
    byte audio1[] = null;
    final ContentResolver cr = null;
    final Context Cont = null;
    byte stego[] = null;

    File audio_file;
    String message = "\t";
    String bit = "";
    int n[];
    int u, j, f, t = 0;
    int algorim = 0;
    char m[];
    int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiostego);
        b = (Button) findViewById(R.id.bt_2);
        c = (Button) findViewById(R.id.bt_3);
        embedding = (Button) findViewById(R.id.bt_4);
        e = (Button) findViewById(R.id.bt_5);
        g = (Button) findViewById(R.id.bt_6);
        et = (EditText) findViewById(R.id.et_1);
        rb1 = (RadioButton) findViewById(R.id.rb_1);
        rb2 = (RadioButton) findViewById(R.id.rb_2);
        share = (Button) findViewById(R.id.share);
        final String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Aut_steganography");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String fname = "صدا_" + timeStamp + ".mp3";
        final File audiofile = new File(myDir, fname);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        } else {

        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("audio/*");
                    startActivityForResult(Intent.createChooser(intent, "Select audio"), 2);
                } else {
                    Intent intent = new Intent();
                    intent.setType("audio/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select audio"), 2);

                }

            }

        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p1 == 1) {
                    mp1.start();
                    p1 = 0;
                    c.setText("توقف پخش");
                } else if (p1 == 0) {
                    mp1.pause();
                    c.setText("پخش");
                    p1 = 1;
                }
            }
        });
        embedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp1.pause();
                if (et.getText() != null) {
                    message = message + et.getText().toString();
                    m = message.toCharArray();
                    for (int i = 0; i < m.length; i++) {
                        String h = Integer.toBinaryString(m[i]);
                        int w = 8 - (h.length());
                        while (w > 0) {
                            h = 0 + h;
                            w--;
                        }
                        bit = h + bit;
                        h = "";
                        et.setText("");
                    }
                    n = new int[bit.length()];
                    for (int i = 0; i < bit.length(); i++) {
                        n[i] = Character.digit(bit.charAt(i), 8);
                    }
                    if (algorim == 0) {
                        stego = pvd(audio, n, -6);
                        mp2 = Mp3(stego);
                        n = null;
                        bit = "";
                        m = null;

                        try {
                            FileOutputStream fos = new FileOutputStream(audiofile);
                            fos.write(stego);
                            fos.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "پیام " + message + " در صوت درج شد .", Toast.LENGTH_LONG).show();
                        message = "";

                    }
                    if (algorim == 1) {
                        stego = newalgoritm(audio, n, -7);
                        mp2 = Mp3(stego);
                        n = null;
                        bit = "";
                        m = null;
                        try {
                            FileOutputStream fos = new FileOutputStream(audiofile);
                            fos.write(stego);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "پیام " + message + " در صوت درج شد .", Toast.LENGTH_LONG).show();
                        message = "";
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "پیام را وارد کنید.", Toast.LENGTH_SHORT).show();
                }
                embedding.setVisibility(View.GONE);
                c.setVisibility(View.GONE);
                e.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                g.setVisibility(View.VISIBLE);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p2 == 1) {
                    mp2.start();
                    p2 = 0;
                    e.setText("توقف پخش");
                } else if (p2 == 0) {
                    mp2.pause();
                    p2 = 1;
                    e.setText("پخش");
                }
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stego == null) {
                    Toast.makeText(getApplicationContext(), "ابتدا درج کنید.", Toast.LENGTH_LONG).show();
                } else {
                    int h = getblock() - 999;
                    int b1[] = new int[h];
                    int b2[] = new int[h];


                    for (int j = 1000; j < 1000 + h; j++) {
                        b1[j - 1000] = (int) audio1[j];
                    }
                    for (int j = 1000; j < 1000 + h; j++) {
                        b2[j - 1000] = (int) stego[j];
                    }
                    Intent intent = new Intent(getApplicationContext(), san.class);
                    intent.putExtra("cover", b1);
                    intent.putExtra("stego", b2);
                    i = 1000;
                    startActivity(intent);
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_SEND);
                String path = root + "/Aut_steganography/" + fname;
                Uri uri = Uri.parse(path);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "اشرتاک گذاری صوت "));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (requestCode == 2 && null != data) {
                if (requestCode == 2) {
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        String path = uri.getPath();
                        mp1 = MediaPlayer.create(getApplicationContext(), uri);
                        audio_file = new File(getRealPathFromURI_API19(getApplicationContext(), uri));
                        audio = getbyte(audio_file);
                        audio1 = getbyte(audio_file);
                        // Toast.makeText(getApplicationContext(), "ظرفیت با الگوریتم pvd"+pvdCapacity(audio),Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext()," ظرفیت با الگوریتl جدید " + newMethodCapacity(audio),Toast.LENGTH_LONG).show();
                        e.setVisibility(View.GONE);
                        share.setVisibility(View.GONE);
                        g.setVisibility(View.GONE);
                        c.setVisibility(View.VISIBLE);
                        embedding.setVisibility(View.VISIBLE);
                    }
                }

            }
        }
    }

    byte[] getbyte(File file) {
        FileInputStream input = null;
        if (file.exists())
            try {

                input = new FileInputStream(file);
                int lengh = (int) file.length();
                byte[] audiobyte = new byte[lengh];
                int i, j = 0;
                while ((i = input.read(audiobyte, j, lengh - j)) > 0)
                    j += i;
                return audiobyte;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "erroe", Toast.LENGTH_SHORT).show();
            }

        return null;
    }

    public void press(View v) {
        int id = v.getId();
        if (id == R.id.rb_1) {
            rb2.setChecked(false);
            algoritmset(0);
        }
        if (id == R.id.rb_2) {
            rb1.setChecked(false);
            algoritmset(1);
        }
    }

    public void algoritmset(int t) {
        algorim = t;
    }

    public MediaPlayer Mp3(byte[] mp3SoundByteArray) {
        try {

            File path = new File(getCacheDir() + "/musicfile.3gp");
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(mp3SoundByteArray);
            fos.close();
            MediaPlayer mediaPlayer = new MediaPlayer();
            FileInputStream fis = new FileInputStream(path);
            mediaPlayer.setDataSource(getCacheDir() + "/musicfile.3gp");
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public byte[] pvd(byte audio[], int messeage[], int p) {
        int l = messeage.length;
        String c = Integer.toBinaryString(l);
        char ch[] = c.toCharArray();
        int r = ch.length;
        for (int q = 1; q < 11; q++) {
            int rem = (int) (audio[511 - q]) % 2;
            if (q <= r) {
                if ((int) audio[511 - q] < 2) {
                    if (ch[r - q] == '0') {
                        audio[511 - q] = (byte) 0;
                    }
                    if (ch[r - q] == '1') {
                        audio[511 - q] = (byte) 1;
                    }
                } else {
                    if (rem == 1) {
                        if (ch[r - q] == '0') {
                            audio[511 - q] = (byte) (audio[511 - q] + 1);
                        }
                    }
                    if (rem == 0) {
                        if (ch[r - q] == '1') {
                            audio[511 - q] = (byte) (audio[511 - q] + 1);
                        }
                    }
                }
            }
            if (q > r) {
                audio[511 - q] = 0;
            }
        }
        audio[400] = (byte) p;
        int i = 1000;
        String vb = "";
        while (j < n.length) {
            int x = (int) audio[i];
            int y = (int) audio[i + 1];
            int mes = 0;
            int u = 0;
            int d = abs(y - x);
            u = abs((int) floor((log10(d) / (log10(2)))));
            if (u < 2) {
                i = i + 2;
            } else {

                f = j + u;
                int e = 0;
                if (f >= l) {
                    e = l - j;
                    int z = 0;
                    while (z < e) {
                        mes = (messeage[l - z - 1] * (int) pow(2, z)) + mes;
                        z++;
                    }
                } else {
                    while (e < u) {
                        mes = (messeage[f - e - 1] * (int) pow(2, e)) + mes;

                        e++;
                    }

                }
                int k = mes + (int) pow(2, u);
                int m = abs(d - k);
                if (audio[i] >= audio[i + 1]) {
                    if (d >= k) {
                        x = x - (int) ceil(m / 2);
                        y = y + (int) floor(m / 2);
                    }
                    if (d < k) {
                        x = x + (int) ceil(m / 2);
                        y = y - (int) floor(m / 2);
                    }
                }
                if (audio[i] < audio[i + 1]) {
                    if (d >= k) {
                        x = x + (int) ceil(m / 2);
                        y = y - (int) floor(m / 2);
                    }
                    if (d < k) {
                        x = x - (int) floor(m / 2);
                        y = y + (int) ceil(m / 2);
                    }
                }
                audio[i] = (byte) x;
                audio[i + 1] = (byte) y;
                int val = abs(audio[i] - audio[i + 1]);
                int dif = val - ((int) pow(2, u));
                if (dif < mes) {
                    if (audio[i] > audio[i + 1]) {
                        audio[i] = (byte) (audio[i] + 1);
                    }
                    if (audio[i] < audio[i + 1]) {
                        audio[i + 1] = (byte) (audio[i + 1] + 1);
                    }
                }
                if (dif > mes) {
                    if (audio[i] > audio[i + 1]) {
                        audio[i] = (byte) (audio[i] - 1);
                    }
                    if (audio[i] < audio[i + 1]) {
                        audio[i + 1] = (byte) (audio[i + 1] - 1);
                    }
                }
                val = abs(audio[i] - audio[i + 1]);
                dif = val - ((int) pow(2, u));
                j = j + u;
                i = i + 2;
                t = t + 2;
                mes = 0;
            }
        }
        Toast.makeText(getApplicationContext(), "lengh=" + i, Toast.LENGTH_LONG).show();
        setblock(i);
        return audio;

    }

    public byte[] newalgoritm(byte audio[], int message[], int p) {
        int len = message.length;
        int alg = 0;
        i = 1000;
        String c = Integer.toBinaryString(len);
        char ch[] = c.toCharArray();
        int r = ch.length;
        for (int q = 1; q < 11; q++) {
            int rem = (int) (audio[511 - q]) % 2;
            if (q <= r) {
                if ((int) audio[511 - q] < 2) {
                    if (ch[r - q] == '0') {
                        audio[511 - q] = (byte) 0;
                    }
                    if (ch[r - q] == '1') {
                        audio[511 - q] = (byte) 1;
                    }
                } else {
                    if (rem == 1) {
                        if (ch[r - q] == '0') {
                            audio[511 - q] = (byte) (audio[511 - q] + 1);
                        }
                    }
                    if (rem == 0) {
                        if (ch[r - q] == '1') {
                            audio[511 - q] = (byte) (audio[511 - q] + 1);
                        }
                    }
                }
            }
            if (q > r) {
                audio[511 - q] = 0;
            }
        }
        audio[400] = (byte) p;
        int j = 0;
        int f = 0;
        int mes = 0;

        while (j < len) {

            int p1 = (int) audio[i] + 127;
            int p2 = (int) audio[i + 1] + 127;
            int p3 = (int) audio[i + 2] + 127;
            int d = abs(p3 - p1);
            int k = (int) floor(log10(d) / log10(2));
            if (k <= 5) {
                k = 5;
            }
            if (k > 5) {
                k = 5;
            }
            f = k + j;
            int e = 0;
            if (f >= len) {
                e = len - j;
                int z = 0;
                while (z < e) {
                    mes = (message[len - z - 1] * (int) pow(2, z)) + mes;
                    z++;
                }
            } else {

                while (e < k) {
                    mes = (message[f - e - 1] * (int) pow(2, e)) + mes;
                    e++;
                }
            }
            int d1 = (2 * p1 + 3 * p2 + 7 * p3) % (int) pow(2, k);
            int d2 = d1 - mes;
            if (d2 == 0) {
                p1 = p1;
                p2 = p2;
                p3 = p3;
            }
            if (abs(d2) <= (pow(2, k) / 2)) {
                if (d2 > 0) {

                    int g = d2 / 7;
                    int rem = (d2 % 7);

                    if (rem >= 4) {
                        alg = 1;
                        g = g + 1;
                        p3 = p3 - g;

                        int rem2 = 7 - rem;
                        if (rem2 == 1) {
                            p2 = p2 + 1;
                            p1 = p1 - 1;
                        }
                        if (rem2 == 2) {
                            p1 = p1 + 1;
                        }
                        if (rem2 == 3) {
                            p2 = p2 + 1;
                        }
                    }
                    if (rem <= 3) {
                        alg = 2;
                        p3 = p3 - g;
                        if (rem == 1) {
                            p2 = p2 - 1;
                            p1 = p1 + 1;
                        }
                        if (rem == 2) {
                            p1 = p1 - 1;
                        }
                        if (rem == 3) {
                            p2 = p2 - 1;
                        }
                    }
                }
                if (d2 < 0) {

                    int g = abs(d2) / 7;
                    int rem = abs(d2) % 7;
                    if (rem >= 4) {
                        alg = 3;
                        g = g + 1;
                        p3 = p3 + g;
                        int rem2 = 7 - rem;
                        if (rem2 == 1) {
                            p2 = p2 - 1;
                            p1 = p1 + 1;
                        }
                        if (rem2 == 2) {
                            p1 = p1 - 1;
                        }
                        if (rem2 == 3) {
                            p2 = p2 - 1;
                        }
                    }
                    if (rem <= 3) {
                        alg = 4;
                        p3 = p3 + g;
                        if (rem == 1) {
                            p2 = p2 + 1;
                            p1 = p1 - 1;
                        }
                        if (rem == 2) {
                            p1 = p1 + 1;
                        }
                        if (rem == 3) {
                            p2 = p2 + 1;
                        }
                    }
                }
            }
            if (abs(d2) > (pow(2, k) / 2)) {
                if (d2 > 0) {
                    d2 = (int) (pow(2, k) - d2);
                    int g = abs(d2) / 7;
                    int rem = abs(d2) % 7;
                    if (rem >= 4) {
                        alg = 5;
                        g = g + 1;
                        p3 = p3 + g;
                        int rem2 = 7 - rem;
                        if (rem2 == 1) {
                            p2 = p2 - 1;
                            p1 = p1 + 1;
                        }
                        if (rem2 == 2) {
                            p1 = p1 - 1;
                        }
                        if (rem2 == 3) {
                            p2 = p2 - 1;
                        }
                    }
                    if (rem <= 3) {
                        alg = 6;
                        p3 = p3 + g;
                        if (rem == 1) {
                            p2 = p2 + 1;
                            p1 = p1 - 1;
                        }
                        if (rem == 2) {
                            p1 = p1 + 1;
                        }
                        if (rem == 3) {
                            p2 = p2 + 1;
                        }
                    }
                }
                if (d2 < 0) {
                    alg = 7;
                    d2 = (int) (pow(2, k) - abs(d2));
                    int g = abs(d2) / 7;
                    int rem = abs(d2) % 7;
                    if (rem >= 4) {
                        //  Toast.makeText(getApplicationContext(),"d2="+d2+"d1="+d1+"mes="+mes,Toast.LENGTH_LONG).show();
                        g = g + 1;
                        p3 = p3 - g;
                        int rem2 = 7 - rem;
                        if (rem2 == 1) {
                            p2 = p2 + 1;
                            p1 = p1 - 1;
                        }
                        if (rem2 == 2) {
                            p1 = p1 + 1;
                        }
                        if (rem2 == 3) {
                            p2 = p2 + 1;
                        }
                    }
                    if (rem <= 3) {
                        alg = 8;
                        p3 = p3 - g;
                        if (rem == 1) {
                            p2 = p2 - 1;
                            p1 = p1 + 1;
                        }
                        if (rem == 2) {
                            p1 = p1 - 1;
                        }
                        if (rem == 3) {
                            p2 = p2 - 1;
                        }

                    }

                }
            }

            int d3 = (2 * p1 + 3 * p2 + 7 * p3) % (int) pow(2, k);

            int d4 = abs(p3 - p1);
            int k2 = (int) floor(log10(d4) / log10(2));
            {
                if (k2 <= 5) {
                    k2 = 5;
                }
            }
            int q1 = p1 - 127;
            int q2 = p2 - 127;
            int q3 = p3 - 127;
            audio[i] = (byte) q1;
            audio[i + 1] = (byte) q2;
            audio[i + 2] = (byte) q3;
            j = j + k;
            i = i + 3;
            t = t + 3;
            mes = 0;
        }
        Toast.makeText(getApplicationContext(), "lenght=" + i, Toast.LENGTH_LONG).show();
        setblock(i);
        return audio;
    }

    public void setblock(int j) {
        i = j;
    }

    public int getblock() {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public double pvdCapacity(byte audio[]) {
        double capacity = 0;
        int lenght = audio.length;
        int i = 100;
        while (i < lenght - 1) {
            int x = (int) (audio[i] + 127);
            int y = (int) (audio[i + 1] + 127);
            int d = abs(y - x);
            if (d != 0) {
                capacity = capacity + (abs((int) floor((log10(d) / (log10(2))))));
            }
            i = i + 2;
        }
        return capacity;
    }

    public int newMethodCapacity(byte audio[]) {
        int length = (int) audio.length / 3;
        return length * 5;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mp1 != null) {
            mp1.pause();
        }
        if (mp2 != null) {
            mp2.pause();
        }

    }
}

