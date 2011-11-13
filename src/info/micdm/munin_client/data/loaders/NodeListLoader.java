package info.micdm.munin_client.data.loaders;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.tasks.DownloadNodeListTask;

import java.util.ArrayList;

/**
 * Загрузчик нод.
 * @author Mic, 2011
 *
 */
public class NodeListLoader {

    /**
     * Синглтон.
     */
    public final static NodeListLoader INSTANCE = new NodeListLoader();

    public NodeListLoader() {
        _addListeners();
    }
    
    /**
     * Выполняется, когда список нод загрузится.
     */
    protected void _onNodeListLoaded(Server server, ArrayList<Node> nodes) {
        if (nodes == null) {
            EventDispatcher.dispatch(new Event(Event.Type.NODE_LIST_NOT_AVAILABLE, server));
        } else {
            for (Node node: nodes) {
                server.addNode(node);
            }
            EventDispatcher.dispatch(new Event(Event.Type.NODE_LIST_AVAILABLE, server));
        }
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.NODE_LIST_LOADED, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                _onNodeListLoaded((Server)extra[0], (ArrayList<Node>)extra[1]);
            }
        });
    }
    
    /**
     * Загружает список нод для указанного сервера.
     */
    public void load(Server server) {
        if (server.getNodes() == null) {
            DownloadNodeListTask task = new DownloadNodeListTask(server);
            task.execute();
        } else {
            EventDispatcher.dispatch(new Event(Event.Type.NODE_LIST_AVAILABLE, server));
        }
    }
}
