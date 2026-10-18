package com.ferrytech.android_poc.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.ferrytech.android_poc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DynamicFontActivity extends AppCompatActivity {

    private Spinner spinnerFonts;
    private TextView tvFont;
    private List<String> fontList = new ArrayList<>();
   private  SharedPreferences prefs;

    private String POPPINS_URL =
            "https://github.com/google/fonts/raw/main/ofl/poppins/Poppins-Bold.ttf";

   private String ROBOTO_URL =
            "https://see.fontimg.com/api/rf5/ALPnL/YWE0MmRkZjA0ZGNlNDVkYzljZTI4MjdlMDY0ZDlkMjIudHRm/REVCUk9TRUU/debrosee.png?r=fs&h=64&w=1000&fg=000000&bg=FFFFFF&tb=1&s=64";

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font3);

        spinnerFonts = findViewById(R.id.spinnerFonts);
        tvFont = findViewById(R.id.tvFont);

        prefs = getSharedPreferences("font_pref", MODE_PRIVATE);

        // download fotn in background
        downloadFont(POPPINS_URL, "poppins_bold.ttf");
        downloadFont(ROBOTO_URL, "roboto_regular.ttf");

        // load fonts into spinner
        loadFontsFromInternalStorage();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                fontList);

        spinnerFonts.setAdapter(adapter);

        // restore saved font
        String savedFont = prefs.getString("selected_font", "default");
        applyFont(savedFont);

        int position = fontList.indexOf(savedFont);
        if(position >= 0){
            spinnerFonts.setSelection(position);
        }

        spinnerFonts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int i, long l) {

                String selectedFont = fontList.get(i);

                applyFont(selectedFont);

                prefs.edit().putString("selected_font", selectedFont).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private void loadFontsFromInternalStorage(){

        fontList.clear();
        fontList.add("default");

        File fontDir = new File(getFilesDir(), "fonts");

        if(fontDir.exists()){

            File[] files = fontDir.listFiles();

            if(files != null){
                for(File file : files){
                    fontList.add(file.getName());
                }
            }
        }
        // refresh spinner UI
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    private void applyFont(String fontName){

        if(fontName.equals("default")){
            tvFont.setTypeface(Typeface.DEFAULT);
            return;
        }

        File fontFile = new File(getFilesDir()+"/fonts/"+fontName);

        if(fontFile.exists()){
            Typeface typeface = Typeface.createFromFile(fontFile);
            tvFont.setTypeface(typeface);
        }
    }

    private void downloadFont(String urlString, String fileName){

        new Thread(() -> {

            try {

                System.out.println("fileName = " + fileName);
                File fontDir = new File(getFilesDir(),"fonts");
                if(!fontDir.exists()){
                    System.out.println("already exits file" + fileName);
                    fontDir.mkdirs();
                }

                File fontFile = new File(fontDir,fileName);

                // avoid duplicate download
                if(fontFile.exists()) return;

                URL url = new URL(urlString);
                InputStream inputStream = url.openStream();
                FileOutputStream outputStream = new FileOutputStream(fontFile);

                byte[] buffer = new byte[1024];
                int len;

                while((len = inputStream.read(buffer))>0){
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
                outputStream.close();

                System.out.println(" saved fileName = " + fileName);
//                // fter download refresh spinner
                runOnUiThread(this::loadFontsFromInternalStorage);

            }catch(Exception e){
                System.out.println("error downloading = " + fileName + e);
                e.printStackTrace();
            }

        }).start();
    }
}