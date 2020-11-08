package com.example.jothm.songapp;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainApp extends AppCompatActivity {

    Mp3File song;
    ID3v24Tag tags;
    ImageHandler albumImg;
    File rawmp3;
    public static String home = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        rawmp3 = new File(home+"/song.mp3");

        if(rawmp3.exists()) Log.d("Mp3", "exists");
        else Log.e("Mp3", "nonexistent");
        if(rawmp3.canRead()) Log.d("Mp3", "readable");
        else Log.e("Mp3", "not readable");
        Log.d("Song Directory", home+"/song.mp3");

        albumImg = new ImageHandler(home+"/img.jpg");

        try {
            song = new Mp3File(rawmp3);
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        tags = new ID3v24Tag();
        tags.setAlbum("album");
        tags.setAlbumArtist("artist");
        tags.setArtist("artist");
        tags.setTitle("title");
        tags.setYear("1999");
        tags.setAlbumImage(albumImg.getImageBytes(), "image/jpeg");
        song.setId3v2Tag(tags);
    }

    public void saveMP3(View view){
        deleteNewMP3(view);
        try {
            song.save(home+"/songWithTags.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void deleteNewMP3(View view){
        File mp3 = new File(home+"/songWithTags.mp3");
        if(mp3.exists()) mp3.delete();
    }

    public void downloadMP3(View view){
        String url = "https://www.youtube.com/watch?v=i_y-6z02rY8";
        new mp3Downloader().execute(url);
    }

    private class mp3Downloader extends AsyncTask<String, Void, File> {

        private Exception e;

        public File doInBackground(String... urls) {
            try {
                File out = new File(home + "/out.mp3");
                OutputStream os = new FileOutputStream(out);
                Log.d("url:::", urls[0]);
                os.write(YoutubeHandler.youtubeToMP3(urls[0]));
                os.close();
                Log.d("Download:::", "complete!");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File out) {
        }
    }
}
