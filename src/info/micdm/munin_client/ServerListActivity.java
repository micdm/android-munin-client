package info.micdm.munin_client;

import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.models.ServerList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Экран со списком доступных серверов.
 * @author Mic, 2011
 *
 */
public class ServerListActivity extends ListActivity {

    /**
     * Заполняет список серверов.
     */
    protected void _fillList() {
        ArrayAdapter<Server> adapter = new ArrayAdapter<Server>(this, android.R.layout.simple_list_item_1);
        for (Server server: ServerList.INSTANCE.getAll()) {
            adapter.add(server);
        }
        getListView().setAdapter(adapter);
    }
    
    /**
     * Выполняется, когда пользователь выбирает сервер из списка.
     */
    protected void _onSelectServer(Server server) {
        Intent intent = new Intent(this, ServerActivity.class);
        intent.putExtra("server", server.getHost());
        startActivity(intent);
    }
    
    /**
     * Слушает клик по элементу списка.
     */
    protected void _listenToSelectServer() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _onSelectServer((Server)parent.getItemAtPosition(position));
            }
        });
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_list);
        _listenToSelectServer();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        _fillList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.server_list_options, menu);
        return true;
    }
    
    /**
     * Выполняется, когда пользователь хочет добавить новый сервер.
     */
    protected void _onAddNewServer() {
        Intent intent = new Intent(this, NewServerActivity.class);
        startActivity(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.addServer:
            _onAddNewServer();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
