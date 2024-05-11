package com.github.leodan11.stepper.internal.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.RestrictTo;
import androidx.viewpager.widget.ViewPager;

import com.github.leodan11.stepper.internal.widget.pagetransformer.StepPageTransformerFactory;

/**
 * A non-swipeable viewpager with RTL support.<br>
 * <a href="http://stackoverflow.com/questions/9650265/how-do-disable-paging-by-swiping-with-finger-in-viewpager-but-still-be-able-to-s">Source</a>
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class StepViewPager extends ViewPager {

    private boolean mBlockTouchEventsFromChildrenEnabled;

    public StepViewPager(Context context) {
        this(context, null);
    }

    public StepViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, StepPageTransformerFactory.createPageTransformer(context));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return mBlockTouchEventsFromChildrenEnabled;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return mBlockTouchEventsFromChildrenEnabled;
    }

    /**
     * @param blockTouchEventsFromChildrenEnabled true if children should not receive touch events
     */
    public void setBlockTouchEventsFromChildrenEnabled(boolean blockTouchEventsFromChildrenEnabled) {
        this.mBlockTouchEventsFromChildrenEnabled = blockTouchEventsFromChildrenEnabled;
    }
}
