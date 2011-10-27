package info.micdm.munin_client.custom;

import info.micdm.munin_client.Event;
import android.app.Application;
import android.content.Intent;
import android.os.Parcelable;

/**
 * Базовый класс для приложения.
 * Умеет рассылать широковещательные интенты.
 * @author Mic, 2011
 *
 */
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
    public static void sendEvent(Event event, Parcelable extra) {
        Intent intent = new Intent(event.toString());
        intent.putExtra("extra", extra);
        _instance.sendBroadcast(intent);
    }
    
    /**
     * Рассылает интент.
     */
    public static void sendEvent(Event event) {
        sendEvent(event, null);
    }
}
