package info.micdm.munin_client.custom;

import info.micdm.munin_client.Event;
import android.app.Application;
import android.content.Intent;

public class CustomApplication extends Application {
    
    protected static CustomApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    /**
     * Рассылает интент.
     */
    public static void sendEvent(Event event) {
        Intent intent = new Intent(event.toString());
        _instance.sendBroadcast(intent);
    }
}
