package info.micdm.munin_client.graph;

import java.text.DecimalFormat;

import info.micdm.munin_client.reports.Point;
import info.micdm.munin_client.reports.Report;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;

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
    
    /**
     * Вычисленные точки отчета, вписанные внутрь фигуры.
     */
    protected float[] _coords;
    
    public GraphShape(Report report) {
        _report = report;
    }
    
    /**
     * Заполняет массив координат.
     */
    protected void _fillCoords() {
        _coords = new float[_report.getPoints().size() * 2];
        int i = 0;
        for (Point point: _report.getPoints()) {
            _coords[i] = point.getTime();
            _coords[i + 1] = point.getValue();
            i += 2;
        }
    }
    
    /**
     * Преобразовывает координаты так, чтобы они вписались в видимую область экрана.
     * Делаем в две матрицы, так как при использовании одной то ли погрешность накапливается, то ли что.
     */
    protected void _transformCoords() {
        Point start = _report.getStart();
        Point end = _report.getEnd();
        Point max = _report.getMax();
        Matrix translateMatrix = new Matrix();
        translateMatrix.setTranslate(-start.getTime(), 0);
        translateMatrix.mapPoints(_coords);
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(getWidth() / (end.getTime() - start.getTime()), -getHeight() / max.getValue());
        scaleMatrix.postTranslate(0, getHeight());
        scaleMatrix.mapPoints(_coords);
    }
    
    /**
     * Вычисляет координаты всех точек графика.
     */
    protected void _calculateCoords() {
        if (_coords == null) {
            _fillCoords();
            _transformCoords();
        }
    }
    
    /**
     * Возвращает контур графика.
     */
    protected Path _getGraphPath() {
        Path path = new Path();
        path.moveTo(0, getHeight());
        for (int i = 0; i < _coords.length; i += 2) {
            path.lineTo(_coords[i], _coords[i + 1]);
        }
        path.lineTo(getWidth(), getHeight());
        path.close();
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
    
    /**
     * Возвращает краску для текста подписей.
     */
    protected Paint _getLabelTextPaint() {
        Paint paint = new Paint();
        paint.setColor(0xFFFFFF00);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);
        return paint;
    }
    
    /**
     * Рисует подписи к точкам.
     */
    protected void _drawLabels(Canvas canvas) {
        Paint paint = _getLabelTextPaint();
        DecimalFormat formatter = new DecimalFormat("#.###");
        int i = 0;
        for (Point point: _report.getPoints()) {
            String text = formatter.format(point.getValue());
            canvas.drawText(text, _coords[i], _coords[i + 1], paint);
            i += 2;
        }
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        _calculateCoords();
        _drawGraph(canvas);
        _drawLabels(canvas);
    }
}
