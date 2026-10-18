package com.ferrytech.android_poc.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.ferrytech.android_poc.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpConnectionActivity extends AppCompatActivity {
    static class Employee {
        String first_name;
        Employee(String first_name) {
            this.first_name = first_name;
        }
        public static Employee parse(JSONObject object) {
            Employee employee = null;
            try {
                String firstname = object.getString("first_name");
                employee = new Employee(firstname);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return employee;
        }
    }

    ArrayList<Employee> list = new ArrayList<>();

    TextView tvString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_http_connection);

        tvString = findViewById(R.id.tvString);
        callApi();

    }

    public void callApi() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                try {
                    URL url = new URL("https://my.api.mockaroo.com/users.json?key=1bf35ab0");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();
                    String st = inputStreamToString(in);
                    byte[] byteResult = inputStreamToBytes(in);
                    String Result = new String(byteResult);
                    in.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvString.setText("String Result:\n" + Result);
                        }
                    });

                    JSONArray arr = new JSONArray(Result);
                    System.out.println("jObject = " + arr);
                    if(null != arr && arr.length() > 0) {
                        for(int i = 0; i< arr.length(); i++) {
                            JSONObject object = arr.getJSONObject(i);
                            Employee employee = Employee.parse(object);
                            list.add(employee);
                        }
                    }

                    System.out.println("list = " + list);


                } catch (Exception e) {
                    System.out.println("e.getMessage() = " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private String inputStreamToString(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[1024];
            int nRead;

            while ((nRead = is.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toString("UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private byte[] inputStreamToBytes(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] data = new byte[1024];
            int nRead;


            while ((nRead = is.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


}