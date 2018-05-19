package com.example.jothm.songapp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.axet.vget.VGet;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainApp extends AppCompatActivity {

    Mp3File song;
    ID3v24Tag tags;
    ImageHandler albumImg;
    File rawmp3;
    VGet downloader;
    String home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        home = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
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

//        try {
//            downloader = new VGet(new URL("www.sample.com/sample.mp3"));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        tags = new ID3v24Tag();
        tags.setAlbum("album");
        tags.setAlbumArtist("artist");
        tags.setArtist("artist");
        tags.setTitle("title");
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
        downloader.download();
    }
}
