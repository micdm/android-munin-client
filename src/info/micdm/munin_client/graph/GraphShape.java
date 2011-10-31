package info.micdm.munin_client.graph;

import info.micdm.munin_client.reports.Point;
import info.micdm.munin_client.reports.Report;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
        paint.setColor(0xFF333300);
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
     * Возвращает матрицу для подгонки контура в пределы экрана.
     */
    protected Matrix _getGraphPathMatrix(Point start, Point end, Point max) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(-start.getTime(), 0);
        matrix.postScale(getWidth() / (end.getTime() - start.getTime()), -getHeight() / max.getValue());
        matrix.postTranslate(0, getHeight());
        return matrix;
    }
    
    /**
     * Возвращает контур графика.
     */
    protected Path _getGraphPath() {
        Path path = new Path();
        Point start = _report.getStart();
        Point end = _report.getEnd();
        Point max = _report.getMax();
        path.moveTo(start.getTime(), 0);
        for (Point point: _report.getPoints()) {
            path.lineTo(point.getTime(), point.getValue());
        }
        path.lineTo(end.getTime(), 0);
        path.close();
        Matrix matrix = _getGraphPathMatrix(start, end, max);
        path.transform(matrix);
        return path;
    }
    
    /**
     * Возвращает краску для линий графика.
     */
    protected Paint _getGraphStrokePaint() {
        Paint paint = new Paint();
        paint.setColor(0xFF777777);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setPathEffect(new CornerPathEffect(5));
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }
    
    /**
     * Возвращает краску для заливки графика.
     */
    protected Paint _getGraphFillPaint() {
        Paint paint = new Paint();
        paint.setColor(0xFF444444);
        paint.setAntiAlias(true);
        paint.setPathEffect(new CornerPathEffect(5));
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }
    
    /**
     * Рисует график.
     */
    protected void _drawGraph(Canvas canvas) {
        Path path = _getGraphPath();
        canvas.drawPath(path, _getGraphFillPaint());
        canvas.drawPath(path, _getGraphStrokePaint());
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //_drawGrid(canvas);
        _drawGraph(canvas);
    }
}
