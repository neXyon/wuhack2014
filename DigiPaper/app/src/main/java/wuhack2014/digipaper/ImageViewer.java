package wuhack2014.digipaper;

/**
 * Created by Brian on 9/13/2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;

public class ImageViewer extends Activity{

    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String img = intent.getStringExtra("imageFile");
        imageFile = new File(img);

        setContentView(R.layout.activity_image_view);

        ImageView iv = (ImageView) findViewById(R.id.imageView);

        iv.setImageURI(Uri.fromFile(imageFile));
    }

}
