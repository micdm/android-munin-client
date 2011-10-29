package info.micdm.munin_client;

import info.micdm.munin_client.events.EventDispatcher;
import android.app.Activity;

/**
 * Базовый экран.
 * @author Mic, 2011
 *
 */
public abstract class CustomActivity extends Activity {

    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        
    }

    /**
     * Удаляет все слушатели событий.
     */
    protected void _removeListeners() {
        EventDispatcher.removeAllListeners(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        _addListeners();
    }

    @Override
    protected void onPause() {
        _removeListeners();
        super.onPause();
    }
}
