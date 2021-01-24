package boris.narodov.geoscanplane;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button control = findViewById(R.id.controlDrone);
        Button versions = findViewById(R.id.versions);
            control.setText(getText(R.string.controlDrone));
            versions.setText(getText(R.string.versions));
    }

    public void controlDrone(View view){
        Intent intent = new Intent(this,ControlDrone.class);
        startActivity(intent);
        finish();
    }

    public void versions(View view){
        Intent intent = new Intent(this,Versions.class);
        startActivity(intent);
        finish();
    }
}