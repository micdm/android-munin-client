package info.micdm.munin_client;

import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.data.ServerList;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Экран для добавления нового сервера.
 * @author Mic, 2011
 *
 */
public class EditServerActivity extends Activity {

    /**
     * Сервер для редактирования.
     * Равно null, если добавляется новый сервер. 
     */
    protected Server _server;
    
    /**
     * Запоминает сервер для редактирования.
     */
    protected void _setServer(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("server")) {
            return;
        }
        String serverName = bundle.getString("server");
        _server = ServerList.INSTANCE.get(serverName);
        ((TextView)findViewById(R.id.server_uri)).setText(_server.getUri().toString());
    }
    
    /**
     * Формирует объект нового сервера по введенным данным.
     */
    protected Server _getServer() {
        String input = ((TextView)findViewById(R.id.server_uri)).getText().toString();
        try {
            URI uri = new URI(input);
            return new Server(uri);
        } catch (URISyntaxException e) {
            Log.d(toString(), e.toString());
            return null;
        }
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
    protected void _onAddServer() {
        Server server = _getServer();
        if (server != null) {
            if (_server != null) {
                ServerList.INSTANCE.delete(_server);
                _server = null;
            }
            ServerList.INSTANCE.add(server);
            ServerList.INSTANCE.save();
            _goToServerList();
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_server);
        _setServer(getIntent().getExtras());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_server_options, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.server_save:
            _onAddServer();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
