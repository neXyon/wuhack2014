package wuhack2014.digipaper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xenon on 13.09.14.
 */
public class OCRRunner {
    private File file;
    private Folder folder;
    private DocumentList list;

    public OCRRunner(File file, Folder folder, DocumentList list) {
        this.file = file;
        this.folder = folder;
        this.list = list;
    }

    void ocr() {
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... urls) {
                BufferedReader reader = null;
                DataOutputStream dos = null;

                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    String filename = file.getName();

                    URL url = new URL("http://172.26.59.45:8080/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(60000 /* milliseconds */);
                    conn.setConnectTimeout(65000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("file", filename);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
                            + filename + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    int bytesAvailable = fileInputStream.available();

                    int max_buff_size = 1024 * 1024;

                    int bufferSize = Math.min(bytesAvailable, max_buff_size);
                    byte[] buffer = new byte[bufferSize];

                    // read file and write it into form...
                    int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, max_buff_size);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    dos.flush();
                    dos.close();
                    fileInputStream.close();

                    int response = conn.getResponseCode();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String outpath = file.getAbsolutePath();
                    int ind = outpath.lastIndexOf('.');
                    if(ind != -1) {
                        outpath = outpath.substring(0, ind);
                    }
                    outpath += ".html";

                    BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outpath)));

                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        outputStream.write(line);
                        outputStream.newLine();
                    }

                    outputStream.close();

                    folder.getDocuments().add(new Document(new File(outpath)));
                    list.refresh();
                    Toast.makeText(list.getApplicationContext(), "OCR successful.", Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    // handle this! ;)
                    Log.e("BLA", e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch(IOException e){}
                    }
                }

                return "";
        }
        }.execute();
    }
}
