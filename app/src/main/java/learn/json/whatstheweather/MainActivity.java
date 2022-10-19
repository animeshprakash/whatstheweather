package learn.json.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText input;
    TextView output;
    Handler mainThreadHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.location);
        output = (TextView) findViewById(R.id.outputweather);

    }
    public void getWeather(View view){
        //d6422ae65759016e94bed09b74b9a0c3 api key
        String city = input.getText().toString();
        //Creates a new Thread to pulls data from API
        new Thread(() -> {
            StringBuilder jsonRaw=new StringBuilder();
            Log.i("Thread","Thread is running");
            try {
                HttpsURLConnection jsonConnect = (HttpsURLConnection) new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=<Enter app id here>")
                        .openConnection();
                jsonConnect.connect();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(jsonConnect.getInputStream()));
                for(String line = reader.readLine();line!=null;line = reader.readLine())
                    jsonRaw.append(line);
                JSONObject values = new JSONObject(jsonRaw.toString());
                JSONArray arr = values.getJSONArray("weather");
                String desc = null;
                for(int i=0;i<arr.length();i++){
                    desc=arr.getJSONObject(i).getString("description");
                }
                final String va = desc;
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        output.setText(va);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
        }).start();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(input.getWindowToken(),0);

    }
}