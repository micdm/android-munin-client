package info.micdm.munin_client.models;

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
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.NODE_LIST_LOADED, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                Server server = (Server)extra[0];
                ArrayList<Node> nodes = (ArrayList<Node>)extra[1];
                for (Node node: nodes) {
                    server.addNode(node);
                }
                EventDispatcher.dispatch(new Event(Event.Type.NODE_LIST_AVAILABLE, server));
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
