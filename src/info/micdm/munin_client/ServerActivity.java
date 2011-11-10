package info.micdm.munin_client;

import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.models.Node;
import info.micdm.munin_client.models.NodeListLoader;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.models.ServerList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Экран со списком доступных нод.
 * @author Mic, 2011
 *
 */
public class ServerActivity extends ListActivity {

    /**
     * Отображаемый сервер.
     */
    protected Server _server;
    
    /**
     * Запоминает сервер, который надо отобразить.
     */
    protected void _setServer(Bundle bundle) {
        String serverName = bundle.getString("server");
        _server = ServerList.INSTANCE.get(serverName);
    }
    
    /**
     * Выполняется, когда пользователь выбирает ноду из списка.
     */
    protected void _onSelectNode(Node node) {
        Intent intent = new Intent(this, NodeActivity.class);
        intent.putExtra("server", _server.getName());
        intent.putExtra("node", node.getName());
        startActivity(intent);
    }
    
    /**
     * Заполняет список нод.
     */
    protected void _fillList() {
        ArrayAdapter<Node> adapter = new ArrayAdapter<Node>(this, android.R.layout.simple_list_item_1);
        for (Node node: _server.getNodes()) {
            adapter.add(node);
        }
        setListAdapter(adapter);
    }
    
    /**
     * Слушает клик по элементу списка.
     */
    protected void _listenToSelectNode() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _onSelectNode((Node)parent.getItemAtPosition(position));
            }
        });
    }
    
    /**
     * Загружает список нод.
     */
    protected void _loadNodeList() {
        NodeListLoader.INSTANCE.load(_server);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);
        _setServer(getIntent().getExtras()); 
        _listenToSelectNode();
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.NODE_LIST_AVAILABLE, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                Server server = (Server)extra[0];
                if (_server.equals(server)) {
                    _fillList();
                }
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        _addListeners();
        _loadNodeList();
    }
    
    /**
     * Удаляет все слушатели событий.
     */
    protected void _removeListeners() {
        EventDispatcher.removeAllListeners(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        _removeListeners();
    }
}
