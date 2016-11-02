package com.example.seiyakurokome.myaidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private ISampleService myServiceIf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, MyService.class);
        intent.setAction(ISampleService.class.getName());
        bindService(intent, myServiceConn, Context.BIND_AUTO_CREATE);

        findViewById(R.id.button1).setOnClickListener(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(myServiceConn);
    }

    private final Handler handler = new Handler();
    private final Runnable showMessageTask = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getBaseContext(), "時間です！！！！", Toast.LENGTH_LONG).show();
        }
    };

    static int count = 0;
    static int bF = 0;

    private final Runnable updateCount = new Runnable() {
        @Override
        public void run() {
            count++;
            bF++;
            handler.postDelayed(updateCount, 1000);

            String view_text = "";
            try {
                view_text = myServiceIf.count(count);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            TextView textView = (TextView) findViewById(R.id.textView1);
            textView.setText(view_text);

        }

    };

    @Override
    public void onClick(View v) {
        if (v != null) {
            EditText edit2 = (EditText) findViewById(R.id.editText1);
            String text2 = edit2.getText().toString();
            int number = Integer.parseInt(text2);
            count = 0;
            try {
                if (myServiceIf.buttonFlag(bF) == 0) {
                    handler.postDelayed(updateCount, 1000);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            handler.postDelayed(showMessageTask, number * 1000);
        }
    }

        private ServiceConnection myServiceConn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myServiceIf = ISampleService.Stub.asInterface(service);
                Log.v("s.connect", "サービスを接続しました。");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                myServiceIf = null;
                Log.v("s.disconnect", "サービスを切断しました。");
            }
        };
    }
