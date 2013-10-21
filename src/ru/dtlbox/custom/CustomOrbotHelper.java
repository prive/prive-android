package ru.dtlbox.custom;

import info.guardianproject.onionkit.ui.OrbotHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CustomOrbotHelper extends OrbotHelper {

    protected final static String LOG_TAG = "CustomOrbotHelper";
    public final static String INTENT_TOR_SERVICE = "ru.dtlbox.TOR_SERVICE";

    public CustomOrbotHelper(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public boolean orbotStart(final Activity activity) {
        try {
            if (isOrbotInstalled()) {
                if (!isOrbotRunning()) {
                    Intent intent = new Intent(URI_ORBOT);
                    intent.setAction(ACTION_START_TOR);
                    activity.startActivityForResult(intent, 1);
                }
                Log.i(LOG_TAG, "orbotinstalled.Trying to start custom tor service");
            } else {
                Log.i(LOG_TAG, "orbot is not installed.Trying to start custom tor service");
                activity.startService(new Intent(INTENT_TOR_SERVICE));
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "error during start orbot");
            e.printStackTrace();
            return false;
        }
        return true;

    }

}
