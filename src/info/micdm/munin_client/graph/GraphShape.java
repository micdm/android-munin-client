package info.micdm.munin_client.graph;

import info.micdm.munin_client.reports.Point;
import info.micdm.munin_client.reports.Report;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 * Отрисовщик графика.
 * @author Mic, 2011
 *
 */
public class GraphShape extends Shape {
    
    /**
     * Отображаемый отчет.
     */
    protected Report _report;
    
    public GraphShape(Report report) {
        _report = report;
    }

    /**
     * Рисует график.
     */
    protected void _drawGraph(Canvas canvas, Paint paint) {
        ArrayList<Point> points = _report.getPoints();
        Float stepX = (float)canvas.getWidth() / (float)(_report.getEndTime() - _report.getStartTime());
        Float stepY = (float)canvas.getHeight() / _report.getMaxValue();
        Point firstPoint = points.get(0);
        Point prevPoint = null;
        for (Point point: points) {
            if (prevPoint != null) {
                Float startX = (prevPoint.getTime() - firstPoint.getTime()) * stepX;
                Float startY = (float)canvas.getHeight() - prevPoint.getValue() * stepY;
                Float endX = (point.getTime() - firstPoint.getTime()) * stepX;
                Float endY = (float)canvas.getHeight() - point.getValue() * stepY;
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
            prevPoint = point;
        }
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        _drawGraph(canvas, paint);
    }
}
