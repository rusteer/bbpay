package com.bbads.android.wmf;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.RelativeLayout;
import com.bbads.android.Constants;

public class SlideBar extends RelativeLayout {
    GradientView gradientView;
    private int b;
    private float c;
    private float d;
    private TriggerListener triggerListener;
    private VelocityTracker velocityTracker;
    private int g;
    private int h;
    private int i;
    private int j;
    private ObjectAnimator k;
    private ObjectAnimator l;
    public SlideBar(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        velocityTracker = null;
        b = 8 + context.getResources().getDimensionPixelSize(com.bbads.android.AR.dimen.gradient_view_margin_left);
        TypedArray typedarray = context.obtainStyledAttributes(attributeset, Constants.SlideBar);
        g = typedarray.getInt(0, 2000);
        h = typedarray.getInt(1, 300);
        i = typedarray.getInt(2, 250);
        j = typedarray.getInt(3, 300);
        typedarray.recycle();
    }
    private boolean a() {
        VelocityTracker velocitytracker = velocityTracker;
        velocitytracker.computeCurrentVelocity(1000);
        int i1 = (int) velocitytracker.getXVelocity();
        Log.v("SlideBar", new StringBuilder("velocityX:").append(i1).toString());
        if (i1 > g) {
            b();
            return true;
        }
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
        return false;
    }
    private void a(MotionEvent motionevent) {
        Log.v("SlideBar", new StringBuilder("handleUp,mIndicateLeft:").append(d).toString());
        if (d >= h) b();
        else if (!a()) {
            c();
            return;
        }
    }
    private void b() {
        triggerListener.onTrigger();
        float af[] = new float[2];
        af[0] = gradientView.getX();
        af[1] = 400F;
        l = ObjectAnimator.ofFloat(gradientView, "x", af).setDuration(j);
        l.start();
        WUtil.requestAdList2();
    }
    private void b(MotionEvent motionevent) {
        d = motionevent.getX() - c + b;
        if (d <= b) d = b;
        gradientView.setX(d);
    }
    private void c() {
        gradientView.b();
        float af[] = new float[2];
        af[0] = gradientView.getX();
        af[1] = b;
        k = ObjectAnimator.ofFloat(gradientView, "x", af).setDuration(i);
        k.start();
    }
    private void c(MotionEvent motionevent) {
        c = motionevent.getX();
        if (gradientView == null) gradientView = (GradientView) findViewById(com.bbads.android.AR.id.gradientView);
        gradientView.stopGradient();
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionevent) {
        int i1;
        boolean flag;
        i1 = motionevent.getActionMasked();
        if (velocityTracker == null) velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(motionevent);
        flag = false;
        switch (i1) {
            case 0://L_L2
            case 5://L_L2 _L2:
                c(motionevent);
                flag = true;
                break;
            case 1://L_L3
            case 6://L_L3_L3:
                a(motionevent);
                flag = true;
                break;
            case 2://L_L4_L4:
                b(motionevent);
                flag = true;
                break;
            case 3://L _L5
                flag = true;
                break;
            case 4://L_L1
                break;
        }
        invalidate();
        if (flag) return true;
        else return super.onTouchEvent(motionevent);
    }
    public void setOnTriggerListener(TriggerListener class1047) {
        triggerListener = class1047;
    }
}
