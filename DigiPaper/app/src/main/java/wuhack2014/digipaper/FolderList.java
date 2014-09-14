package wuhack2014.digipaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class FolderList extends Activity {
    ArrayList<Folder> folders;
    File documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        folders = new ArrayList<Folder>();

        setContentView(R.layout.activity_folder_list);

        documents = Environment.getExternalStorageDirectory();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        documents = new File(documents, "documents/DigiPaper");

        if(!documents.exists())
            documents.mkdirs();

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
                Intent intent = new Intent(parent.getContext(), DocumentList.class);
                intent.putExtra("folder", folder.getPath().getAbsolutePath());
                startActivity(intent);
            }

        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Folder folder = (Folder) parent.getItemAtPosition(position);
                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                alert.setTitle("Delete Subject");
                alert.setMessage("Are you sure you want to delete " + folder.toString() + "?");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        folder.getPath().delete();
                        folders.remove(folder);
                        Toast.makeText(alert.getContext(), folder.toString() + " was deleted", Toast.LENGTH_LONG).show();
                        refresh();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
                return true;
            }
        });
    }

    private void refresh() {
        ListView list = (ListView)findViewById(R.id.folderView);
        ((ArrayAdapter<Folder>)list.getAdapter()).notifyDataSetChanged();
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
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.add_subject:
                folderName();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void folderName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Subject");
        alert.setMessage("What is the subject name?");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                // create Intent to take a picture and return control to the calling application
                File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "documents" + File.separator + "DigiPaper" + File.separator + value);
                directory.mkdir();
                folders.add(new Folder(directory));
                Toast.makeText(getApplicationContext(), "Subject \"" + value + "\" created", Toast.LENGTH_LONG).show();
                refresh();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });

        alert.show();
    }
}
