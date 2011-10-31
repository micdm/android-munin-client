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
     * Рисует рамку вокруг графика.
     */
    protected void _drawBorder(Canvas canvas, Paint paint) {
        canvas.drawLine(0, 0, getWidth(), 0, paint);
        canvas.drawLine(0, 0, 0, getHeight(), paint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
    }
    
    /**
     * Рисует горизонтальную линию.
     */
    protected void _drawHorizontalLine(float value, Canvas canvas, Paint paint) {
        float y = getHeight() - (getHeight() / _report.getMax().getValue()) * value;
        canvas.drawLine(0, y, getWidth(), y, paint);
        canvas.drawText(String.valueOf(value), 0, y - 2, paint);
    }
    
    /**
     * Рисует сетку для вертикальной оси.
     */
    protected void _drawHorizontalGrid(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xFFFFFF00);
        paint.setStrokeMiter(1);
        _drawHorizontalLine(_report.getMin().getValue(), canvas, paint);
        _drawHorizontalLine(_report.getMax().getValue(), canvas, paint);
    }
    
    /**
     * Рисует сетку.
     */
    protected void _drawGrid(Canvas canvas) {
        _drawHorizontalGrid(canvas);
    }
    
    /**
     * Рисует график.
     */
    protected void _drawGraph(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(1);
        ArrayList<Point> points = _report.getPoints();
        float stepX = getWidth() / (float)(_report.getEnd().getTime() - _report.getStart().getTime());
        float stepY = getHeight() / _report.getMax().getValue();
        Point firstPoint = points.get(0);
        Point prevPoint = null;
        for (Point point: points) {
            if (prevPoint != null) {
                float startX = (prevPoint.getTime() - firstPoint.getTime()) * stepX;
                float startY = getHeight() - prevPoint.getValue() * stepY;
                float endX = (point.getTime() - firstPoint.getTime()) * stepX;
                float endY = getHeight() - point.getValue() * stepY;
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
            prevPoint = point;
        }
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //_drawBorder(canvas, paint);
        _drawGrid(canvas);
        _drawGraph(canvas);
    }
}
