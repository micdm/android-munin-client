package info.micdm.munin_client.custom;

import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventExtra;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Базовый экран. Умеет получать события от потоков.
 * @author Mic, 2011
 *
 */
public abstract class CustomActivity extends Activity {

    /**
     * Получатель событий.
     */
    protected BroadcastReceiver _receiver = new BroadcastReceiver() {

        /**
         * Перенаправлятор событий в UI-поток.
         */
        protected Handler _handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                String event = data.getString("event");
                EventExtra extra = data.getParcelable("extra");
                _onEvent(Event.get(event), extra);
                super.handleMessage(msg);
            }
        };

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = new Bundle();
            data.putString("event", intent.getAction());
            data.putParcelable("extra", intent.getParcelableExtra("extra"));
            Message msg = new Message();
            msg.setData(data);
            _handler.sendMessage(msg);
        }
    };
    
    /**
     * Регистрирует получатель событий при активизации экрана.
     */
    protected void _registerReceiver() {
        IntentFilter filter = new IntentFilter();
        for (Event event: Event.values()) {
            filter.addAction(event.toString());
        }
        registerReceiver(_receiver, filter);
    }
    
    /**
     * Удаляет получатель событий при деактивизации экрана.
     */
    protected void _unregisterReceiver() {
        unregisterReceiver(_receiver);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        _registerReceiver();
    }

    @Override
    protected void onPause() {
        _unregisterReceiver();
        super.onPause();
    }
    
    /**
     * Вызывается при поступлении события.
     */
    protected void _onEvent(Event event, EventExtra extra) {
        Log.d(toString(), "got event " + event);
        if (extra != null) {
            Log.d(toString(), "extra data is " + extra);
        }
    }
}
