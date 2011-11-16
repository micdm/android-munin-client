package info.micdm.munin_client.data.loaders;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.events.types.Event;
import info.micdm.munin_client.events.types.NodeListAvailableEvent;
import info.micdm.munin_client.events.types.NodeListLoadedEvent;
import info.micdm.munin_client.events.types.NodeListNotAvailableEvent;
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
            EventDispatcher.INSTANCE.dispatch(new NodeListNotAvailableEvent(server));
        } else {
            for (Node node: nodes) {
                server.addNode(node);
            }
            EventDispatcher.INSTANCE.dispatch(new NodeListAvailableEvent(server));
        }
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.INSTANCE.addListener(NodeListLoadedEvent.class, new EventListener(this) {
            @Override
            public void notify(Event event) {
                NodeListLoadedEvent typed = (NodeListLoadedEvent)event;
                _onNodeListLoaded(typed.getServer(), typed.getNodes());
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
            EventDispatcher.INSTANCE.dispatch(new NodeListAvailableEvent(server));
        }
    }
}
