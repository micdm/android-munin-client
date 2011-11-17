package info.micdm.munin_client.graph;

import info.micdm.munin_client.data.Point;
import info.micdm.munin_client.data.Report;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

/**
 * Отрисовщик текстовой метки.
 * @author Mic, 2011
 *
 */
class LabelShape extends Shape {

    /**
     * Текст
     */
    protected String _text;

    /**
     * X-координата точки.
     */
    protected float _x;
    
    /**
     * Y-координата точки.
     */
    protected float _y;
    
    /**
     * Ширина графика.
     */
    protected float _graphWidth;
    
    public LabelShape(String text, float x, float y, float graphWidth) {
        _text = text;
        _x = x;
        _y = y;
        _graphWidth = graphWidth;
    }
    
    /**
     * Возвращает краску для горизонтальной линии-уровня.
     */
    protected Paint _getLevelPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x77333333);
        return paint;
    }
    
    /**
     * Рисует горизонтальную линию-уровень.
     */
    protected void _drawLevel(Canvas canvas) {
        canvas.drawLine(0, _y, _graphWidth, _y, _getLevelPaint());
    }
    
    /**
     * Возвращает краску для фона подписей.
     */
    protected Paint _getBackgroundPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xDD000000);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }
    
    /**
     * Рисует фон для подписи.
     */
    protected void _drawBackground(Canvas canvas, float x, float y, float width, float height) {
        Paint paint = _getBackgroundPaint();
        int padding = 7;
        canvas.drawRoundRect(new RectF(x - padding, y - padding, x + width + padding, y + height + padding), 5, 5, paint);
    }

    /**
     * Возвращает краску для текста подписей.
     */
    protected Paint _getTextPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xFFCCCCCC);
        paint.setTextSize(20);
        return paint;
    }
    
    /**
     * Рисует текст подписи.
     */
    protected void _drawText(Canvas canvas, float x, float y) {
        Paint paint = _getTextPaint();
        canvas.drawText(_text, x, y, paint);
    }
    
    /**
     * Возвращает границы текста.
     */
    protected Rect _getTextBounds() {
        Paint paint = _getTextPaint();
        Rect bounds = new Rect();
        paint.getTextBounds(_text, 0, _text.length(), bounds);
        return bounds;
    }
    
    /**
     * Возвращает X-координату текста, чтоб он не вылез за экран.
     */
    protected float _getTextX(Rect bounds) {
        if (_x + bounds.width() > _graphWidth) {
            return _x - bounds.width();
        }
        return _x;
    }
    
    /**
     * Возвращает Y-координату текста, чтобы он не вылез за экран.
     */
    protected float _getTextY(Rect bounds) {
        if (_y - bounds.height() < 0) {
            return _y + bounds.height();
        }
        return _y;
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        Rect bounds = _getTextBounds();
        float x = _getTextX(bounds);
        float y = _getTextY(bounds);
        _drawLevel(canvas);
        _drawBackground(canvas, x, y - bounds.height(), bounds.width(), bounds.height());
        _drawText(canvas, x, y);
    }
}


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
     * Рисует подписи к точкам.
     */
    protected void _drawLabels(Canvas canvas) {
        DecimalFormat formatter = new DecimalFormat("#.###");
        for (Point point: new Point[] {_report.getStart(), _report.getEnd(), _report.getMin(), _report.getMax()}) {
            int index = point.getNumber() * 2;
            String text = formatter.format(point.getValue());
            LabelShape shape = new LabelShape(text, _coords[index], _coords[index + 1], getWidth());
            shape.draw(canvas, null);
        }
    }
    
    @Override
    public void draw(Canvas canvas, Paint paint) {
        _calculateCoords();
        _drawGraph(canvas);
        _drawLabels(canvas);
    }
}
