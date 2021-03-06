package wuhack2014.digipaper;

/**
 * Created by Brian on 9/13/2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewer extends Activity{

    private File webFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String img = intent.getStringExtra("webFile");
        webFile = new File(img);

        setContentView(R.layout.activity_web_view);

        WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(webFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (FileNotFoundException e){

        } catch (IOException e) {

        }

        String toAppend = "<head><script src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>" +
                "<script>function getAnswer(n){Android.answer(n);}$(window).load(function() {$(\".ocrx_word\").click(function(){getAnswer($(this).html());});});</script>";
        int index = text.indexOf("<head>");
        if (index != -1) {
            text = text.replace(index, index + 6, toAppend);
        } else {
            text.insert(0, toAppend);
        }

        wv.loadData(text.toString(), "text/html", "UTF-8");
        wv.addJavascriptInterface(new AnswersInterface(this), "Android");


     }

}
