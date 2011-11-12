package info.micdm.munin_client.data.stores;

import info.micdm.munin_client.CustomApplication;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.data.ServerList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.sax.Element;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

/**
 * Читатель списка серверов.
 * @author Mic, 2011
 *
 */
class ServerListReader {
    
    /**
     * Целевой список-приемник.
     */
    protected ServerList _serverList;
    
    /**
     * Возвращает поток для чтения.
     */
    protected FileInputStream _getStream(String filename) throws FileNotFoundException {
        return CustomApplication.INSTANCE.openFileInput(filename);
    }
    
    /**
     * Настраивает элемент "server".
     */
    protected void _setupServerElement(Element element) {
        element.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                String uriValue = attributes.getValue("uri");
                try {
                    URI uri = new URI(uriValue);
                    _serverList.add(new Server(uri));
                } catch (URISyntaxException e) {
                    Log.w(toString(), "can not parse uri " + uriValue);
                }
            }
        });
    }
    
    /**
     * Читает XML из указанного потока.
     */
    protected void _parseXml(FileInputStream stream) throws IOException, SAXException {
        RootElement serversElement = new RootElement("servers");
        _setupServerElement(serversElement.getChild("server"));
        InputStreamReader reader = new InputStreamReader(stream);
        Xml.parse(reader, serversElement.getContentHandler());
    }
    
    /**
     * Заполняет список серверов.
     */
    public void read(ServerList serverList, String filename) {
        try {
            _serverList = serverList;
            FileInputStream stream = _getStream(filename);
            _parseXml(stream);
        } catch (IOException e) {
            Log.e(toString(), e.toString());
        } catch (SAXException e) {
            Log.e(toString(), e.toString());
        }
    }
}


/**
 * Писатель списка серверов.
 * @author Mic, 2011
 *
 */
class ServerListWriter {

    /**
     * XML-строитель.
     */
    protected XmlSerializer _serializer = Xml.newSerializer();
    
    /**
     * Возвращает поток для записи.
     */
    protected FileOutputStream _getStream(String filename) throws FileNotFoundException {
        return CustomApplication.INSTANCE.openFileOutput(filename, Context.MODE_PRIVATE);
    }
    
    /**
     * Записывает XML в указанный поток.
     */
    protected void _writeXml(ServerList serverList, FileOutputStream stream) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        _serializer.setOutput(writer);
        _serializer.startDocument("UTF-8", true);
        _serializer.startTag("", "servers");
        for (Server server: serverList.getAll()) {
            _serializer.startTag("", "server");
            _serializer.attribute("", "uri", server.getUri().toString());
            _serializer.endTag("", "server");
        }
        _serializer.endTag("", "servers");
        _serializer.endDocument();
    }
    
    /**
     * Сохраняет список серверов.
     */
    public void write(ServerList serverList, String filename) {
        try {
            FileOutputStream stream = _getStream(filename);
            _writeXml(serverList, stream);
        } catch (IOException e) {
            Log.e(toString(), e.toString());
        }
    }
}


/**
 * Хранилище для списка серверов.
 * @author Mic, 2011
 *
 */
public class ServerListStore {

    /**
     * Название файла для хранения списка.
     */
    protected final String FILENAME = "servers.xml";
    
    /**
     * Загружает и заполняет список серверов.
     */
    public void load(ServerList serverList) {
        ServerListReader reader = new ServerListReader();
        reader.read(serverList, FILENAME);
    }

    /**
     * Сохраняет список серверов.
     */
    public void save(ServerList serverList) {
        ServerListWriter writer = new ServerListWriter();
        writer.write(serverList, FILENAME);
    }
}
