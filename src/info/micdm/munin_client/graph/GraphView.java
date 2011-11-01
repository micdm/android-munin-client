package info.micdm.munin_client.graph;

import info.micdm.munin_client.reports.Report;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Виджет с графиком внутри.
 * @author Mic, 2011
 *
 */
public class GraphView extends View {

    /**
     * Отображаемый отчет.
     */
    protected Report _report;
    
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * Создает и настраивает фигуру.
     */
    protected GraphShape _getShape() {
        GraphShape shape = new GraphShape(_report);
        shape.resize(getWidth(), getHeight());
        return shape;
    }
    
    /**
     * Создает и настраивает отрисовку.
     */
    protected ShapeDrawable _getShapeDrawable() {
        ShapeDrawable drawable = new ShapeDrawable(_getShape());
        Paint paint = drawable.getPaint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(1);
        return drawable;
    }
    
    @Override
    protected void onDraw (Canvas canvas) {
        if (_report != null) {
            ShapeDrawable drawable = _getShapeDrawable();
            drawable.draw(canvas);
        }
    }
    
    /**
     * Устанавливает отчет для отображения.
     */
    public void setReport(Report report) {
        _report = report;
        invalidate();
    }
}
