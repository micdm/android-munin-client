package info.micdm.munin_client.tasks;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
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
    public void parse(ArrayList<Node> nodes, String data) {
        try {
            _nodes = nodes;
            RootElement nodesElement = new RootElement("nodes");
            _setupNodeElement(nodesElement.getChild("node"));
            Xml.parse(data, nodesElement.getContentHandler());
        } catch (SAXException e) {
            Log.e(toString(), "can not parse data: " + e.toString());
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
    protected String _getUri() {
        String url = "http://foo:bar@" + _server.getHost() + ":" + _server.getPort();
        String urn = "/munin-export/nodes/";
        return url + urn;
    }

    @Override
    protected ArrayList<Node> doInBackground(Void... params) {
        String data = _downloadData();
        if (data == null) {
            return null;
        }
        NodeListParser parser = new NodeListParser();
        ArrayList<Node> nodes = new ArrayList<Node>();
        parser.parse(nodes, data);
        Log.d(toString(), "parsed: " + nodes.toString());
        return nodes;
    }
    
    @Override
    protected void onPostExecute(ArrayList<Node> nodes) {
        Event event = new Event(Event.Type.NODE_LIST_LOADED, _server, nodes);
        EventDispatcher.dispatch(event);
    }
}
