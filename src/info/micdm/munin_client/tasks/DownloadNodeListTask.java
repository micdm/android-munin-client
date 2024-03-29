package info.micdm.munin_client.tasks;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.types.NodeListLoadedEvent;
import info.micdm.utils.Log;

import java.net.URI;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

/**
 * Парсер для списка нод.
 * @author Mic, 2011
 *
 */
class NodeListParser {
    
    /**
     * Промежуточная переменная для списка нод.
     */
    protected ArrayList<Node> _nodes;
    
    /**
     * Промежуточная переменная для ноды.
     */
    protected Node _node;
    
    /**
     * Настраивает парсер для элемента типа отчета.
     */
    protected void _setupReportElement(Element element) {
        element.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                String typeName = attributes.getValue("type");
                _node.addReportType(Report.Type.getByName(typeName));
            }
        });
    }
    
    /**
     * Настраивает парсер для элемента ноды.
     */
    protected void _setupNodeElement(Element element) {
        element.setElementListener(new ElementListener() {
            @Override
            public void start(Attributes attributes) {
                _node = new Node(attributes.getValue("name"));
            }

            @Override
            public void end() {
                _nodes.add(_node);
                _node = null;
            }
        });
        _setupReportElement(element.getChild("report"));
    }
    
    /**
     * Парсит данные, заполняет список нод.
     */
    public ArrayList<Node> parse(String data) throws RuntimeException {
        try {
            _nodes = new ArrayList<Node>();
            RootElement nodesElement = new RootElement("nodes");
            _setupNodeElement(nodesElement.getChild("node"));
            Xml.parse(data, nodesElement.getContentHandler());
            return _nodes;
        } catch (SAXException e) {
            if (Log.isEnabled) {
                Log.error("can not parse data: " + e.toString());
            }
            throw new RuntimeException("can not parse data");
        }
    }
}


/**
 * Загрузчик списка нод.
 * @author Mic, 2011
 *
 */
public class DownloadNodeListTask extends DownloadTask<Void, Void, ArrayList<Node>> {

    /**
     * Сервер, с которого грузим данные.
     */
    protected Server _server;
    
    public DownloadNodeListTask(Server server) {
        _server = server;
    }
    
    @Override
    protected URI _getUri() {
        return URI.create(_server.getUri() + "/nodes/");
    }

    @Override
    protected ArrayList<Node> doInBackground(Void... params) {
        String data = _downloadData();
        if (data == null) {
            return null;
        }
        NodeListParser parser = new NodeListParser();
        try {
            ArrayList<Node> nodes = parser.parse(data);
            if (Log.isEnabled) {
                Log.debug("parsed: " + nodes.toString());
            }
            return nodes;
        } catch (RuntimeException e) {
            return null;
        }
    }
    
    @Override
    protected void onPostExecute(ArrayList<Node> nodes) {
        NodeListLoadedEvent event = new NodeListLoadedEvent(_server, nodes);
        EventDispatcher.INSTANCE.dispatch(event);
    }
}
