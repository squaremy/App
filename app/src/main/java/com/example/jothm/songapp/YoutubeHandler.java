package com.example.jothm.songapp;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeHandler {
    private static final Pattern VID_ID_PATTERN = Pattern.compile("(?<=https{0,1}://www{0,1}.youtube.com/watch\\?v=)(.*?)$|(?=/n|/v|/b|/B|/s|&|/e|/r)"),
            MP3_URL_PATTERN = Pattern
                    .compile("(?<=href=\\\")https{0,1}://(\\w|\\d){3}.ytapivmp3.com.*(?=\\\"\\s)");

    public static byte[] youtubeToMP3(String youtubeUrl) throws IOException {
        String id = getID(youtubeUrl);
        String converter = loadConverter(id);
        String mp3url = getMP3URL(converter);
        byte[] mp3 = load(mp3url);
        return mp3;
    }

    private static byte[] load(String url) throws IOException {
        URL url2 = new URL(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url2.openStream();
        byte[] byteChunk = new byte[2500];
        int n;

        while ((n = is.read(byteChunk)) > 0) {
            baos.write(byteChunk, 0, n);
        }

        is.close();
        baos.flush();
        baos.close();

        return baos.toByteArray();
    }

    private static String getMP3URL(String html) {
        Matcher m = MP3_URL_PATTERN.matcher(html);
        Log.d("Pattern:::", MP3_URL_PATTERN.toString());
        Log.d("HTML:::", html);
        m.find();
        return m.group();
    }

    private static String loadConverter(String id) throws IOException {
        String url = "https://www.320youtube.com/watch?v=" + id;
        Log.d("video id:::", id);
        byte[] bytes = load(url);
        return new String(bytes);
    }

    private static String getID(String youtubeUrl) {
        Log.d("ID Pat:::", VID_ID_PATTERN.pattern());
        Matcher m = VID_ID_PATTERN.matcher(youtubeUrl);
        if (!m.find()) {
            throw new IllegalArgumentException("Invalid YouTube URL.");
        }
        return m.group();
    }
}
