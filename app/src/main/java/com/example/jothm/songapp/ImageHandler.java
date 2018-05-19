package com.example.jothm.songapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageHandler {
    Bitmap image;

    ImageHandler(){

    }

    ImageHandler(String imgPath){
        image = BitmapFactory.decodeFile(imgPath);
    }

    public void setImage(String imgPath){
        image = BitmapFactory.decodeFile(imgPath);
    }

    public byte[] getImageBytes(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public Bitmap getImage(){
        return image.copy(Bitmap.Config.ALPHA_8, false);
    }
}
