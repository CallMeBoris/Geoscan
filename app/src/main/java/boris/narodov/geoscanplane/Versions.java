package boris.narodov.geoscanplane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Versions extends AppCompatActivity {
    TextView versions;
    String versionStr = "Loading...";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versions);
        versions= findViewById(R.id.versions);
        LoadVersionsTask task = new LoadVersionsTask();
        task.execute();
        versions.setText(versionStr);
    }

    class LoadVersionsTask extends AsyncTask{
        @Override
        protected Void doInBackground(Object[] objects) {
            try {
                URL url = new URL("http://192.168.43.220:8889/info");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                byte[] buf = new byte[100];
                try{InputStream in = new BufferedInputStream(connection.getInputStream());  //stream for receiving byte array
                    in.read(buf,0,100); //reading bytes into an array
                    in.close();
                }catch (Exception e){versionStr=e.getMessage();}finally {connection.disconnect();}
                versionStr="";
                for (byte b : buf) {versionStr = versionStr + (char) b;} //writing characters to a string from a byte array
                }catch (IOException e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            versions.setText(versionStr);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}