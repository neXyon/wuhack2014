package wuhack2014.digipaper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brian on 9/13/2014.
 */
public class AnswersInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    AnswersInterface(Context c) {
        mContext = c;
    }


    public void answer(String keyword) {
        BufferedReader reader = null;

        try {
            URL url = new URL("http://gravity.answers.com/question/search?keyword=" + keyword);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            JSONObject object = new JSONObject(sb.toString());

            String answer = object.getJSONArray("results").getJSONObject(0).getString("answer");
            TextView msg = new TextView(mContext);
            msg.setText(Html.fromHtml(answer));
            msg.setPadding(15, 15, 15, 15);

            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
            ab.setTitle(keyword);
            ab.setView(msg);
            ab.setCancelable(true);
            ab.setPositiveButton("OK", null);

            AlertDialog alert = ab.create();
            alert.show();
            return;



        } catch(Exception e) {
            // handle this! ;)
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch(IOException e){}
            }
        }

        Toast.makeText(mContext, "Failed to get answers, please check your internet connection and try again.", Toast.LENGTH_LONG).show();
    }
}