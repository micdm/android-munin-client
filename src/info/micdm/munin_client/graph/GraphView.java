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
     * Создает и настраивает отрисовку.
     */
    protected ShapeDrawable _setupDrawable(GraphShape shape) {
        ShapeDrawable drawable = new ShapeDrawable(shape);
        shape.resize(getWidth(), getHeight());
        Paint paint = drawable.getPaint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(1);
        return drawable;
    }
    
    @Override
    protected void onDraw (Canvas canvas) {
        if (_report != null) {
            GraphShape shape = new GraphShape(_report);
            ShapeDrawable drawable = _setupDrawable(shape);
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
