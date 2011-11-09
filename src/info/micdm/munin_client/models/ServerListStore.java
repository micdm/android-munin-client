package info.micdm.munin_client.models;

import info.micdm.munin_client.CustomApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
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
     * Временная переменная для названия хоста.
     */
    protected String _host;
    
    /**
     * Временная переменная для номера порта.
     */
    protected int _port;
    
    /**
     * Временная переменная для логина.
     */
    protected String _username;
    
    /**
     * Временная переменная для пароля.
     */
    protected String _password;
    
    /**
     * Возвращает поток для чтения.
     */
    protected FileInputStream _getStream(String filename) throws FileNotFoundException {
        return CustomApplication.INSTANCE.openFileInput(filename);
    }
    
    /**
     * Настраивает элемент "host".
     */
    protected void _setupHostElement(Element element) {
        element.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                _host = body;
            }
        });
    }
    
    /**
     * Настраивает элемент "port".
     */
    protected void _setupPortElement(Element element) {
        element.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                _port = Integer.parseInt(body);
            }
        });
    }
    
    /**
     * Настраивает элемент "username".
     */
    protected void _setupUsernameElement(Element element) {
        element.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                _username = body;
            }
        });
    }
    
    /**
     * Настраивает элемент "password".
     */
    protected void _setupPasswordElement(Element element) {
        element.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                _password = body;
            }
        });
    }
    
    /**
     * Настраивает элемент "server".
     */
    protected void _setupServerElement(Element element) {
        element.setElementListener(new ElementListener() {
            @Override
            public void start(Attributes attributes) {
                _host = null;
                _port = 0;
                _username = null;
                _password = null;
            }
            
            @Override
            public void end() {
                _serverList.add(new Server(_host, _port, _username, _password));
            }
        });
        _setupHostElement(element.getChild("host"));
        _setupPortElement(element.getChild("port"));
        _setupUsernameElement(element.getChild("username"));
        _setupPasswordElement(element.getChild("password"));
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
     * Записывает открывающий и закрывающий теги + текст между ними.
     */
    protected void _writeTag(String name, String data) throws IOException {
        if (data != null) {
            _serializer.startTag("", name);
            _serializer.text(data);
            _serializer.endTag("", name);
        }
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
            _writeTag("host", server.getHost());
            _writeTag("port", String.valueOf(server.getPort()));
            _writeTag("username", server.getUsername());
            _writeTag("password", server.getPassword());
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
