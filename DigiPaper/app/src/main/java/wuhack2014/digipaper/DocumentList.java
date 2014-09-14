package wuhack2014.digipaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class DocumentList extends Activity {
    Folder folder;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String value;
    private Uri fileUri;
    private String f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        f = intent.getStringExtra("folder");
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
                    Intent intent = new Intent(parent.getContext(), ImageViewer.class);
                    intent.putExtra("imageFile", document.getFile().toString());
                    startActivity(intent);
                } else if (document.isWeb()) {
                    Intent intent = new Intent(parent.getContext(), WebViewer.class);
                    intent.putExtra("webFile", document.getFile().toString());
                    startActivity(intent);
                }

            }

        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Document document = (Document) parent.getItemAtPosition(position);
                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                alert.setTitle("Delete File");
                alert.setMessage("Are you sure you want to delete " + document.toString() + "?");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        document.getFile().delete();
                        Toast.makeText(alert.getContext(), document.toString() + " was deleted", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(parent.getContext(), ImageViewer.class);
                    intent.putExtra("imageFile", document.getFile().toString());
                    startActivity(intent);
                } else if (document.isWeb()) {
                    Intent intent = new Intent(parent.getContext(), WebViewer.class);
                    intent.putExtra("webFile", document.getFile().toString());
                    startActivity(intent);
                }

            }

        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Document document = (Document) parent.getItemAtPosition(position);
                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                alert.setTitle("Delete File");
                alert.setMessage("Are you sure you want to delete " + document.toString() + "?");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        document.getFile().delete();
                        Toast.makeText(alert.getContext(), document.toString() + " was deleted", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.document_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.add_picture:
                picture();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void picture() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Picture");
        alert.setMessage("Select a picture name:");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value = input.getText().toString() + ".jpg";

                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = new File(folder.getPath() + File.separator);
                file = new File(file, value);
                Log.d("file", file.getPath());
                fileUri = Uri.fromFile(file); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved!", Toast.LENGTH_LONG).show();
                refresh();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Picture Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
