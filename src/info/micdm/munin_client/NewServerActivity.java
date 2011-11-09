package info.micdm.munin_client;

import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.models.ServerList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Экран для добавления нового сервера.
 * @author Mic, 2011
 *
 */
public class NewServerActivity extends CustomActivity {

    /**
     * Вызывается при переключении флажка.
     */
    protected void _onNeedAuthChanged(boolean isChecked) {
        int visibility = isChecked ? View.VISIBLE : View.GONE;
        findViewById(R.id.serverUsername).setVisibility(visibility);
        findViewById(R.id.serverPassword).setVisibility(visibility);
    }
    
    /**
     * Слушает клик по чекбоксу "Авторизовать".
     */
    protected void _listenToNeedAuthChanged() {
        CheckBox needAuth = (CheckBox)findViewById(R.id.serverNeedAuth);
        needAuth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _onNeedAuthChanged(isChecked);
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
     * Формирует объект нового сервера по введенным данным.
     */
    protected Server _getNewServer() {
        String host = _getInputText(R.id.serverHost);
        String port = _getInputText(R.id.serverPort);
        if (host.length() == 0 || port.length() == 0) {
            return null;
        }
        String username = null;
        String password = null;
        CheckBox needAuth = (CheckBox)findViewById(R.id.serverNeedAuth);
        if (needAuth.isChecked()) {
            username = _getInputText(R.id.serverUsername);
            password = _getInputText(R.id.serverPassword);
            if (username.length() == 0 || password.length() == 0) {
                return null;
            }
        }
        return new Server(host, Integer.parseInt(port), username, password);
    }
    
    /**
     * Добавляет сервер в список.
     */
    protected void _addServer(Server server) {
        ServerList.INSTANCE.add(server);
        ServerList.INSTANCE.save();
    }
    
    /**
     * Возвращает к списку серверов.
     */
    protected void _goToServerList() {
        Intent intent = new Intent(this, ServerListActivity.class);
        startActivity(intent);
    }
    
    /**
     * Вызывается для добавления нового сервера.
     */
    protected void _onCreateServer() {
        Server server = _getNewServer();
        if (server != null) {
            _addServer(server);
            _goToServerList();
        }
    }
    
    /**
     * Слушает клик по кнопке "Создать".
     */
    protected void _listenToCreateServer() {
        Button create = (Button)findViewById(R.id.serverCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _onCreateServer();
            }
        });
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_server);
        _listenToNeedAuthChanged();
        _listenToCreateServer();
    }
}
