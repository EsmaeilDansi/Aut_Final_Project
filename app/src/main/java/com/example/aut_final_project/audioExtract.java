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
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

public class audioExtract extends Activity {
    TextView tv;
    Button a,b,c;
    MediaPlayer mp1;
    int p1=0;
    File file;
    int len =0;
    String letters="";
    byte audio[]=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extract);
        a=(Button)findViewById(R.id.bt_1);
        b=(Button)findViewById(R.id.bt_2);
        c=(Button)findViewById(R.id.bt_3);
        tv=(TextView)findViewById(R.id.tv_1);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        } else {

        }
        a.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                { if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
            }
        });
        c.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(audio==null)
                {
                    Toast.makeText(getApplicationContext(),"ابتدا صوت را انتخاب کنید " ,Toast.LENGTH_LONG).show();
                }

                else {

                    if ((int) audio[0] == -6) {
                        for (int i = 1; i < 11; i++) {
                            if (audio[11 - i] >= 2) {
                                int k = (int) audio[11 - i] % 2;
                                len = k * (int) pow(2, i - 1) + len;
                            } else {
                                len = (int) audio[11 - i] * (int) pow(2, i - 1) + len;
                            }

                        }
                        String message = back(decoding(audio));
                        tv.setText(message);
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"الگوریتم pvd " ,Toast.LENGTH_SHORT).show();

                    }
                    if ((int) audio[300] == -7) {

                        for (int i = 1; i < 11; i++) {
                            if (audio[11 - i] >= 2) {
                                int k = (int) audio[11 - i] % 2;
                                len = k * (int) pow(2, i - 1) + len;
                            } else {
                                len = (int) audio[11 - i] * (int) pow(2, i - 1) + len;
                            }

                        }
                        String message = back(decoding2(audio));
                        tv.setText(message);
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"الگوریتم جدید" ,Toast.LENGTH_LONG).show();
                    }
                    else if(audio[300]!=-6  & audio[300]!=-7)
                    {
                        Toast.makeText(getApplicationContext(), "درج صورت نگرفته است ", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {
                Uri uri=data.getData();
                mp1 = MediaPlayer.create(getApplicationContext(),uri );
                file =new File(getRealPathFromURI_API19(getApplicationContext(),uri));
                audio=getBytes(file);

            }
        }
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    byte[] getBytes (File file)
    {
        FileInputStream input = null;
        if (file.exists())
            try
            {
                input = new FileInputStream (file);
                int len = (int) file.length();
                byte[] data = new byte[len];
                int count, total = 0;
                while ((count = input.read (data, total, len - total)) > 0)
                    total += count;
                return data;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                if (input != null) try
                {
                    input.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        return null;

    }
    public String  back (String bit)
    {
        int r=len/8;
        char mes[]=bit.toCharArray();
        int w=0;
        int g=0;
        while (w<r)
        {
            for (int i = 0; i < 8; i++)
            {
                letters = letters + mes[i+g];
            }
            letters= letters+"\t";
            g=g+8;
            w++;
        }
        String s = "";
        for (int index = 0; index < letters.length(); index += 9)
        {
            String temp = letters.substring(index, index + 8);
            int num = Integer.parseInt(temp, 2);
            char letter = (char) num;
            s = letter + s;
        }
        return s;
    }
    public String decoding(byte [] audio)
    {
        int t;
        String m="";
        int j=0;
        String h="";
        int i=1000;
        while(j<len)
        {
            int d=(int)abs(audio[i+1]-audio[i]);
            int u=(int)(floor(log10(d)/log10(2)));
            if(u<2)
            {
                i=i+2;
            }
            else
            {
                if(j+u>len)
                {
                    t=d-(int)pow(2,u);
                    h=Integer.toBinaryString(t);
                    int v=len-(j+h.length());
                    while(v>0)
                    {
                        h=0+h;
                        v--;
                    }
                    m=m+h;
                    j=j+u;
                }
                else
                {
                    t = d - (int) pow(2, u);
                    h = Integer.toBinaryString(t);
                    int v = u - h.length();
                    while (v > 0)
                    {
                        h = 0 + h;
                        v--;
                    }
                    m = m + h;
                    i = i + 2;
                    j = j + u;
                }
            }

        }
        return m;

    }
    public String decoding2 (byte [] audio)
    {
        int t;
        String m="";
        int j=0;
        String h="";
        int i=1000;
        while(j<len)
        {
            int p1=(int)audio[i]+127;
            int p2=(int)audio[i+1]+127;
            int p3=(int)audio[i+2]+127;
            int d=(int)abs(p3-p1);
            int u=(int)(floor(log10(d)/log10(2)));
            if(u<=5)
            {
                u=5;
            }
            if(u>5)
            {
                u=5;
            }
            int rem = ((7 * p3 + 3 * p2 + 2 * p1) % (int) pow(2, u));
            if(j+u>len)
            {
                t=d-(int)pow(2,u);
                h=Integer.toBinaryString(t);
                int v=len-(j+h.length());
                while(v>0)
                {
                    h=0+h;
                    v--;
                }
                m=m+h;
                j=j+u;
            }
            else
            {

                h = Integer.toBinaryString(rem);
                int v = u - h.length();
                while (v > 0) {
                    h = 0 + h;
                    v--;
                }
                m = m + h;
                i = i + 3;
                j = j + u;
            }

        }
        return  m;
    }
    public void play(View v) {
        int id = v.getId();
        if (id == R.id.bt_2) {
            if (p1 == 0)
            {
                mp1.start();
                p1 = 1;
            }
            else if (p1 == 1) {
                mp1.pause();
                p1 = 0;
            }
        }
    }
    public static String getRealPathFromURI_API19(final Context context, final Uri uri)
    {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
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
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
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


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private String getFilePathFromContentUri(Uri selectedVideoUri,
                                             ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }



}
