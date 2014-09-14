package wuhack2014.digipaper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class DocumentList extends Activity {
    Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String f = intent.getStringExtra("folder");
        folder = new Folder(new File(f));

        setContentView(R.layout.activity_document_list);

        ListView list = (ListView)findViewById(R.id.documentView);

        ArrayAdapter<Document> adapter = new ArrayAdapter<Document>(this, android.R.layout.simple_list_item_1, folder.getDocuments());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Document document = (Document) parent.getItemAtPosition(position);

                if (document.isImage()) {
                    OCRRunner runner = new OCRRunner(document.getFile(), folder);
                    runner.ocr();

                    /*Intent intent = new Intent(parent.getContext(), ImageViewer.class);
                    intent.putExtra("imageFile", document.getFile().toString());
                    startActivity(intent);*/
                } else if (document.isWeb()) {
                    Intent intent = new Intent(parent.getContext(), WebViewer.class);
                    intent.putExtra("webFile", document.getFile().toString());
                    startActivity(intent);
                }
            }

        });
    }
}
