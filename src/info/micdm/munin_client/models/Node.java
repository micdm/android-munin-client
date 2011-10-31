package info.micdm.munin_client.models;

import info.micdm.munin_client.reports.Report;

import java.util.ArrayList;

/**
 * Ведомый сервер ("нода").
 * @author Mic, 2011
 *
 */
public class Node {

    /**
     * Название ноды.
     */
    protected String _name;
    
    /**
     * Отчеты для ноды.
     */
    protected ArrayList<Report> _reports = new ArrayList<Report>();
    
    public Node(String name) {
        _name = name;
    }
    
    /**
     * Добавляет новый отчет в список либо перезаписывает старый такой же.
     */
    public void addReport(Report report) {
        int index = _reports.indexOf(report);
        if (index == -1) {
            _reports.add(report);
        } else {
            _reports.set(index, report);
        }
    }
    
    /**
     * Находит отчет.
     */
    public Report getReport(Report.Type type, Report.Period period) {
        for (Report report: _reports) {
            if (report.getType().equals(type) && report.getPeriod().equals(period)) {
                return report;
            }
        }
        return null;
    }
}
