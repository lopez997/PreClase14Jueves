package appmoviles.com.preclase13.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

public class ScrolledDatePicker extends DatePicker {
    public ScrolledDatePicker(Context context) {
        super(context);
    }

    public ScrolledDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrolledDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrolledDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //Habilita el scroll original del datepicker y inhabilita el scrollview
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null){
                p.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }
}
