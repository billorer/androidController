package suto.zoran.firsttry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity implements JoyStick.JoystickListener {

    private Socket socket;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JoyStick j = new JoyStick(this);
        setContentView(R.layout.activity_main);

        setListeners();
    }

    public void setListeners(){

        Button attackButton = (Button)findViewById(R.id.attackButton);
        attackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        attack(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        attack(false);
                        break;
                }
                return true;
            }
        });

        Button upButton = (Button)findViewById(R.id.upButton);
        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        up(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        up(false);
                        break;
                }
                return true;
            }
        });

        Button downButton = (Button)findViewById(R.id.downButton);
        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        down(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        down(false);
                        break;
                }
                return true;
            }
        });

        Button leftButton = (Button)findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        left(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        left(false);
                        break;
                }
                return true;
            }
        });

        Button rightButton = (Button)findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        right(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        right(false);
                        break;
                }
                return true;
            }
        });
    }

    /** This is the action for the connection button*/
    public void onTapButton(View v) {

        EditText editText = (EditText) findViewById(R.id.codeText);
        code = editText.getText().toString();

        if(checkIfRightCodeFormat(code)) {
            if(connectSocket()){

                //sendSocketMessage(code);
                Toast.makeText(this, "Succes", Toast.LENGTH_SHORT).show();
                setScene();
            }else{
                alertBox("Connection problems!", "Unfortunately we could not reach the server...");
            }
        }else{
            alertBox("Wrong code!", "The code you typed must be the code from your browser...");
        }
    }

    /** Sends the adequte string via socket*/
    private void sendSocketMessage(String editTextStr) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", editTextStr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("code", object);
    }

    /** Creates a simple alert box*/
    private void alertBox(String alertTitle, String alertMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(alertTitle);
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /** Switches between the connection and the controller layout*/
    public void setScene() {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.connectionLayout);
        linearLayout.setVisibility(View.INVISIBLE);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.controllerLayout);
        gridLayout.setVisibility(View.VISIBLE);

        GridLayout gridLayout2 = (GridLayout) findViewById(R.id.joystickLayout);
        gridLayout2.setVisibility(View.VISIBLE);

    }

    /** Checks if a string is integer or not*/
    private boolean isNumber(String editTextStr){
        try {
            int tmp = Integer.parseInt(editTextStr);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /** Checks whether the string string is in a right format*/
    private boolean checkIfRightCodeFormat(String editTextStr){
        if(isNumber(editTextStr)){
            int tmp = Integer.parseInt(editTextStr);
            if(tmp > 0 && tmp < 1000){
                return true;
            }
        }
        return false;
    }

    /** Tries to connect to the server*/
    public boolean connectSocket() {
        try {
            socket = IO.socket("http://192.168.43.87:2000"); //("https://morning-brushlands-91423.herokuapp.com/");//("http://192.168.100.7:2000");
            socket.connect();

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public void up(boolean value){//View v) {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("inputId", "up");
            object.put("state", Boolean.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast myToast = Toast.makeText(getApplicationContext(), Boolean.toString(value), Toast.LENGTH_LONG);
        //myToast.show();
        socket.emit("code", object);
    }

    public void down(boolean value){//View v) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("inputId", "down");
            object.put("state", Boolean.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("code", object);
    }

    public void left(boolean value){//View v) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("inputId", "left");
            object.put("state", Boolean.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("code", object);
    }

    public void right(boolean value){//View v) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("inputId", "right");
            object.put("state", Boolean.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("code", object);
    }

    public void attack(boolean value) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("inputId", "attack");
            object.put("state", Boolean.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("code", object);
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        //Log.d("Main Method", "X percent: " + xPercent + " Y percent: " + yPercent);
        double curMouseAngle = Math.atan2(yPercent, xPercent);
        //Log.d("Main Method", "ANGLE11111111111111: " +  curMouseAngle);

        curMouseAngle = curMouseAngle / Math.PI * 180;

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("inputId", "mouseAngle");
            object.put("state", curMouseAngle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("code", object);
    }
}

