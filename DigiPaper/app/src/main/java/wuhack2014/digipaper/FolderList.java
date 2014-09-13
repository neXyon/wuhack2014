package wuhack2014.digipaper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class FolderList extends Activity {
    ArrayList<Folder> folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        folders = new ArrayList<Folder>();

        setContentView(R.layout.activity_folder_list);

        File documents = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        documents = new File(documents, "DigiPaper");

        if(!documents.exists())
            documents.mkdir();

        File subjects[] = documents.listFiles();

        for(File subject : subjects) {
            if(subject.isDirectory()) {
                Folder folder = new Folder(subject);
                folders.add(folder);
            }
        }

        ListView list = (ListView)findViewById(R.id.folderView);

        ArrayAdapter<Folder> adapter = new ArrayAdapter<Folder>(this, android.R.layout.simple_list_item_1, folders);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Folder folder = (Folder) parent.getItemAtPosition(position);

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.folder_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
