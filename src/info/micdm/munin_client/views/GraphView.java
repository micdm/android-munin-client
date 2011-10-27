package info.micdm.munin_client.views;

import java.util.ArrayList;

import info.micdm.munin_client.reports.Point;
import info.micdm.munin_client.reports.Report;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {

    protected Report _report;
    
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * Рисует фон.
     */
    protected void _drawBackground(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xFF333333);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
    }

    /**
     * Рисует график.
     */
    protected void _drawGraph(Canvas canvas) {
        if (_report == null) {
            return;
        }
        
        Paint paint = new Paint();
        paint.setColor(0xFFFF3333);
        paint.setStrokeWidth(1);
        
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
                //Log.d(toString(), "coords: " + startX + " " + startY + " " + endX + " " + endY);
            }
            prevPoint = point;
        }
    }
    
    @Override
    protected void onDraw (Canvas canvas) {
        _drawBackground(canvas);
        _drawGraph(canvas);
    }
    
    public void setReport(Report report) {
        _report = report;
        invalidate();
    }
}
