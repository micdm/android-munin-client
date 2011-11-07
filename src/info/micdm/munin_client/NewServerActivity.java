package info.micdm.munin_client;

import info.micdm.munin_client.models.Server;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class NewServerActivity extends CustomActivity {

    /**
     * Слушает клик по чекбоксу "Авторизовать".
     */
    protected void _listenToNeedAuthChanged() {
        CheckBox needAuth = (CheckBox)findViewById(R.id.serverNeedAuth);
        needAuth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibility = isChecked ? View.VISIBLE : View.GONE;
                findViewById(R.id.serverUsername).setVisibility(visibility);
                findViewById(R.id.serverPassword).setVisibility(visibility);
            }
        });
    }
    
    /**
     * Возвращает введенный текст.
     */
    protected String _getInputText(int id) {
        return ((TextView)findViewById(id)).getText().toString();
    }
    
    /**
     * Слушает клик по кнопке "Создать".
     */
    protected void _listenToCreate() {
        Button create = (Button)findViewById(R.id.serverCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = _getInputText(R.id.serverHost);
                String port = _getInputText(R.id.serverPort);
                if (host.length() == 0 || port.length() == 0) {
                    return;
                }
                String username = null;
                String password = null;
                CheckBox needAuth = (CheckBox)findViewById(R.id.serverNeedAuth);
                if (needAuth.isChecked()) {
                    username = _getInputText(R.id.serverUsername);
                    password = _getInputText(R.id.serverPassword);
                    if (username.length() == 0 || password.length() == 0) {
                        return;
                    }
                }
                Server server = new Server(host, Integer.parseInt(port), username, password);
                Log.d(toString(), server.toString());
            }
        });
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_server);
        _listenToNeedAuthChanged();
        _listenToCreate();
    }
}
