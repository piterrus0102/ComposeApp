package com.example.composeapp.tests.battery.battery_moon.utility;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IO {

    public static String readRawText(Resources res, int resId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(res.openRawResource(resId)))) {
            String line;
            StringBuilder text = new StringBuilder();
            while (null != (line = reader.readLine())) {
                text.append(line);
                text.append('\n');
            }
            return text.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
