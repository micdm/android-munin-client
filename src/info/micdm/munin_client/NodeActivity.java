package info.micdm.munin_client;

import info.micdm.munin_client.data.Node;
import info.micdm.munin_client.data.Report;
import info.micdm.munin_client.data.Server;
import info.micdm.munin_client.data.ServerList;
import info.micdm.munin_client.data.loaders.ReportLoader;
import info.micdm.munin_client.events.EventDispatcher;
import info.micdm.munin_client.events.EventListener;
import info.micdm.munin_client.events.types.Event;
import info.micdm.munin_client.events.types.ReportAvailableEvent;
import info.micdm.munin_client.events.types.ReportNotAvailableEvent;
import info.micdm.munin_client.graph.GraphView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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
     * Отображаемый период.
     */
    protected Report.Period _reportPeriod = Report.Period.HOUR;
    
    /**
     * Сообщение, что данные загружаются.
     */
    protected ProgressDialog _dialog;
    
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
     * Загружает отчет.
     */
    protected void _loadReport() {
        _dialog = ProgressDialog.show(this, "", getResources().getString(R.string.dialog_loading_report));
        Report.Type type = _node.getReportTypes().get(_reportTypeIndex);
        ReportLoader.INSTANCE.load(_server, _node, type, _reportPeriod);
    }
    
    /**
     * Загружает предыдущий доступный отчет.
     */
    protected void _loadPreviousByType() {
        if (_reportTypeIndex == 0) {
            _reportTypeIndex = _node.getReportTypes().size() - 1;
        } else {
            _reportTypeIndex -= 1;
        }
        _loadReport();
    }
    
    /**
     * Загружает следующий доступный отчет.
     */
    protected void _loadNextByType() {
        if (_reportTypeIndex >= _node.getReportTypes().size() - 1) {
            _reportTypeIndex = 0;
        } else {
            _reportTypeIndex += 1;
        }
        _loadReport();
    }
    
    /**
     * Загружает предыдущий доступный отчет.
     */
    protected void _loadPreviousByPeriod() {
        _reportPeriod = (_reportPeriod == Report.Period.HOUR) ? Report.Period.DAY : Report.Period.HOUR;
        _loadReport();
    }
    
    /**
     * Загружает следующий доступный отчет.
     */
    protected void _loadNextByPeriod() {
        _reportPeriod = (_reportPeriod == Report.Period.HOUR) ? Report.Period.DAY : Report.Period.HOUR;
        _loadReport();
    }
    
    /**
     * Выполняется при событии прокрутки.
     */
    protected boolean _onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX < 0) {
                _loadNextByType();
                return true;
            }
            if (velocityX > 0) {
                _loadPreviousByType();
                return true;
            }
        } else {
            if (velocityY < 0) {
                _loadNextByPeriod();
                return true;
            }
            if (velocityY > 0) {
                _loadPreviousByPeriod();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Слушает событие прокрутки, чтобы менять графики.
     */
    protected void _listenToFling() {
        final GestureDetector detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return _onFling(e1, e2, velocityX, velocityY);
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
     * Выполняется, когда отчет станет доступен.
     */
    protected void _onReportAvailable(Node node, Report report) {
        if (_node.equals(node)) {
            GraphView view = (GraphView)findViewById(R.id.graph);
            view.setReport(report);
            if (_dialog != null) {
                _dialog.dismiss();
                _dialog = null;
            }
        }
    }
    
    /**
     * Выполняется, если отчет недоступен.
     */
    protected void _onReportNotAvailable(Node node) {
        if (_node.equals(node)) {
            if (_dialog != null) {
                _dialog.dismiss();
                _dialog = null;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.data_not_available_title);
            builder.setMessage(R.string.data_not_available_message);
            builder.setPositiveButton(R.string.data_not_available_ok, null);
            builder.show();
        }
    }
    
    /**
     * Добавляет слушатели событий.
     */
    protected void _addListeners() {
        EventDispatcher.INSTANCE.addListener(ReportAvailableEvent.class, new EventListener(this) {
            @Override
            public void notify(Event event) {
                ReportAvailableEvent typed = (ReportAvailableEvent)event;
                _onReportAvailable(typed.getNode(), typed.getReport());
            }
        });
        EventDispatcher.INSTANCE.addListener(ReportNotAvailableEvent.class, new EventListener(this) {
            @Override
            public void notify(Event event) {
                ReportNotAvailableEvent typed = (ReportNotAvailableEvent)event;
                _onReportNotAvailable(typed.getNode());
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        _addListeners();
        _loadReport();
    }
    
    /**
     * Удаляет все слушатели событий.
     */
    protected void _removeListeners() {
        EventDispatcher.INSTANCE.removeAllListeners(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        _removeListeners();
    }
}
