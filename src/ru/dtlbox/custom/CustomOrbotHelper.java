package ru.dtlbox.custom;

import info.guardianproject.onionkit.ui.OrbotHelper;

import org.torproject.android.Orbot.DataCount;
import org.torproject.android.service.ITorService;
import org.torproject.android.service.ITorServiceCallback;
import org.torproject.android.service.TorServiceConstants;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class CustomOrbotHelper extends OrbotHelper {

    protected final static String LOG_TAG = "CustomOrbotHelper";

    public final static String INTENT_TOR_SERVICE = "ru.dtlbox.TOR_SERVICE";

    ITorService mService = null;
    private Context mContext = null;

    public CustomOrbotHelper(Context context) {
        super(context);
        mContext = context;
        // TODO Auto-generated constructor stub
    }

    public boolean torServiceStart(final Activity activity) {
        Log.d(LOG_TAG, "tor service start");
        try {
            if (isOrbotInstalled()) {
                Log.i(LOG_TAG, "orbotinstalled.Trying to start custom tor service");
                if (!isOrbotRunning()) {
                    Intent intent = new Intent(URI_ORBOT);
                    intent.setAction(ACTION_START_TOR);
                    activity.startActivityForResult(intent, 1);
                } else
                    Log.i(LOG_TAG, "orbot already running");

            } else {
                //                Log.i(LOG_TAG, "orbot is not installed.Trying to start custom tor service");
                //                if (!isServiceRunning(TOR_CUSTOMSERVICE_NAME))
                //                    activity.startService(new Intent(INTENT_TOR_SERVICE));
                //                else
                //                    Log.i(LOG_TAG, "custom tor service already running");
                if (mService == null)
                    bindService();
                else
                    try {
                        startTor();
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "error during start orbot");
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo runningServiceInfo : activityManager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(LOG_TAG, "onServiceConnected");
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = ITorService.Stub.asInterface(service);

            try {

                startTor();

            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
                Log.d(LOG_TAG, "error registering callback to service", e);
            }

        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;

        }
    };

    private void startTor() throws RemoteException {

        // this is a bit of a strange/old/borrowed code/design i used to change the service state
        // not sure it really makes sense when what we want to say is just "startTor"
        mService.setProfile(TorServiceConstants.PROFILE_ON); //this means turn on

    }

    //this is where we bind! 
    private void bindService() {
        Log.i(LOG_TAG, "bind service");
        //since its auto create, we prob don't ever need to call startService
        //also we should again be consistent with using either iTorService.class.getName()
        //or the variable constant       
        mContext.bindService(new Intent(CustomTorService.class.getName()), mConnection,
                Context.BIND_AUTO_CREATE);

    }

}
