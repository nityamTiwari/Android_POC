package com.ferrytech.android_poc.Utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    public static  String getJsonFromAssets(Context context, String filename){
         try{
             InputStream json = context.getAssets().open(filename); // file open fro assets

             int size = json.available();
             byte[] arr = new byte[size];// empty byte array of size json .... byte[] =[ , , , , ,]

             json.read(arr); // take data from assets .... byte[] = [byte, byte, byte,byte]

             json.close(); // close the file....

             return  new String(arr, StandardCharsets.UTF_8); // convert to human readable format String

         } catch (IOException e) {
             throw new RuntimeException(e);

         }
    }
}
