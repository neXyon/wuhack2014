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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

        String text = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(webFile));
            String line;

            while ((line = br.readLine()) != null) {
                text += line;
            }
            br.close();
        } catch (FileNotFoundException e){

        } catch (IOException e) {

        }

        text += "<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js'></script>";

        Log.i("text:", text);

        wv.loadData(text, "text/html", "UTF-8");

        String script = "$(document).ready(function(){$(\"body\").prepend(\"<div style='width: 100%'><input type='text' id='query' style='width: 100%' placeholder='Ask a question by typing or tapping words'></div>\"),$(\".ocrx_word\").css(\"cursor\",\"pointer\"),$(\".ocrx_word\").on(\"click\",function(){len=$(\"#query\").val().length,len>1&&\" \"!=$(\"#query\").val().substr(len-1,len)&&$(\"#query\").val($(\"#query\").val()+\" \"),$(\"#query\").val($(\"#query\").val()+$(this).html()+\" \")})});";
     }

}
