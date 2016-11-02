package com.example.seiyakurokome.myaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;


public class MyService extends Service {
    public IBinder onBind(Intent intent) {
        if(ISampleService.class.getName().equals(intent.getAction())){
            return myServiceIf;
        }
        return null;
    }

    private ISampleService.Stub myServiceIf = new ISampleService.Stub(){
        @Override

        public String count(int a) throws RemoteException {
            String strNum = String.valueOf(a);
            return strNum+"秒経過!";
        }
        public int buttonFlag(int b) throws RemoteException {
            return b;
        }
    };
}

