package info.micdm.munin_client;

import info.micdm.munin_client.events.Event;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.graph.GraphView;
import info.micdm.munin_client.models.Node;
import info.micdm.munin_client.models.Server;
import info.micdm.munin_client.models.ServerList;
import info.micdm.munin_client.reports.Report;
import info.micdm.munin_client.reports.ReportLoader;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Экран с информацией о ноде.
 * @author Mic, 2011
 *
 */
public class NodeActivity extends Activity {

    /**
     * Отображаемый сервер.
     */
    protected Server _server;
    
    /**
     * Отображаемая нода.
     */
    protected Node _node;
    
    /**
     * Отображаемый тип отчетов.
     */
    protected int _reportTypeIndex = 0;
    
    /**
     * Запоминает сервер и ноду для отображения.
     */
    protected void _setServerAndNode(Bundle bundle) {
        String serverName = bundle.getString("server");
        String nodeName = bundle.getString("node");
        _server = ServerList.INSTANCE.get(serverName);
        _node = _server.getNode(nodeName);
    }
    
    /**
     * Загружает отчет за час.
     */
    protected void _loadByHour(int index) {
        Report.Type type = _node.getReportTypes().get(index);
        ReportLoader.INSTANCE.load(_server, _node, type, Report.Period.HOUR);
    }
    
    /**
     * Загружает предыдущий доступный отчет.
     */
    protected void _loadPrevious() {
        if (_reportTypeIndex == 0) {
            _reportTypeIndex = _node.getReportTypes().size() - 1;
        } else {
            _reportTypeIndex -= 1;
        }
        _loadByHour(_reportTypeIndex);
    }
    
    /**
     * Загружает следующий доступный отчет.
     */
    protected void _loadNext() {
        if (_reportTypeIndex >= _node.getReportTypes().size() - 1) {
            _reportTypeIndex = 0;
        } else {
            _reportTypeIndex += 1;
        }
        _loadByHour(_reportTypeIndex);
    }
    
    /**
     * Слушает событие прокрутки, чтобы менять графики.
     */
    protected void _listenToFling() {
        final GestureDetector detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX < 0) {
                    _loadNext();
                    return true;
                }
                if (velocityX > 0) {
                    _loadPrevious();
                    return true;
                }
                return false;
            }
        });
    
        findViewById(R.id.graph).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.node);
        _setServerAndNode(getIntent().getExtras());
        _listenToFling();
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.addListener(Event.Type.REPORT_AVAILABLE, new EventListener(this) {
            @Override
            public void notify(Event event) {
                Object[] extra = event.getExtra();
                Report report = (Report)extra[0];
                GraphView view = (GraphView)findViewById(R.id.graph);
                view.setReport(report);
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        _addListeners();
        _loadByHour(0);
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
