package boris.narodov.geoscanplane;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ControlDrone extends AppCompatActivity {
    private static Socket connectionToTheServer;
    private static DatagramSocket out;
    private static DatagramSocket in;

    private static byte yaw = (byte) 0b1111000;         //default values
    private static byte throttle = (byte) 0b1111000;
    private static byte roll = (byte) 0b1111000;
    private static byte pitch =(byte) 0b1111000;

    private ImageView helicopter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_drone);
        helicopter = findViewById(R.id.helicopter);
        LoadDroneTask task = new LoadDroneTask();
        task.execute();
    }

    class LoadDroneTask extends AsyncTask {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Object[] objects) {
            Bitmap bmpImg;
            try {connectionToTheServer = new Socket("192.168.43.220", 8888);
                out = new DatagramSocket(); //socket for sending array
                in =new DatagramSocket(connectionToTheServer.getLocalPort()); //socket for receiving image
                byte[] buffer = new byte[4174];
                while (!connectionToTheServer.isClosed()){
                    byte[] test = {(byte) 0b11111111, yaw, throttle, roll, pitch, 0b11};
                    DatagramPacket testPacket = new DatagramPacket(test,test.length, InetAddress.getByName("192.168.43.220"), 8001);
                    out.send(testPacket);  //sending array

                    in.setSoTimeout(300);
                    try{
                        DatagramPacket image = new DatagramPacket(buffer, buffer.length);
                        in.receive(image); //receiving image
                    }catch (SocketTimeoutException e){e.printStackTrace();}
                    try{
                        bmpImg = BitmapFactory.decodeByteArray(buffer,0,buffer.length);
                        helicopter.setImageBitmap(bmpImg); //rendering an image
                    }catch (RuntimeException e){e.printStackTrace();}
                    in.setSoTimeout(300);
                }
            }catch(UnknownHostException e){e.printStackTrace();
            }  catch(IOException e){e.printStackTrace();}
            return null;
        }
    }

    public void throttleUp(View view){if (throttle!=(byte) 253){throttle++;}}

    public void throttleDown(View view){if (throttle!=0){throttle--;}}

    public void yawLeft(View view){if (yaw!=0){yaw--;}}

    public void yawRight(View view){if (yaw!=(byte) 253){yaw++;}}

    public void pitchDown(View view){if (pitch!=0){pitch--;}}

    public void pitchUp(View view){if (pitch!=(byte) 253){pitch++;}}

    public void rollLeft(View view){if (roll!=0){roll--;}}

    public void rollRight(View view){if (roll!=(byte) 253){roll++;}}



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            connectionToTheServer.close();
        } catch (Exception e) {e.printStackTrace();}
        try {
            out.close();
        } catch (Exception e) {e.printStackTrace();}
        try {
            in.close();
        } catch (Exception e) {e.printStackTrace();}
        finish();
    }
}