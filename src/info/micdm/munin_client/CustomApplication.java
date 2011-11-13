package info.micdm.munin_client;

import info.micdm.utils.Log;
import android.app.Application;

/**
 * Слегка переопределяем класс приложения, чтобы иметь доступ из любой точки.
 * @author Mic, 2011
 *
 */
public class CustomApplication extends Application {

    /**
     * Объект текущего приложения.
     */
    public static CustomApplication INSTANCE;
    
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Log.init("munin_client", true);
    }
}
