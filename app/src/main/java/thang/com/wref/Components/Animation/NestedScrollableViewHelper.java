package thang.com.wref.Components.Animation;

import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.sothree.slidinguppanel.ScrollableViewHelper;

public class NestedScrollableViewHelper extends ScrollableViewHelper {
    // bouncing animation
    public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
        if (scrollableView instanceof NestedScrollView) {
            if(isSlidingUp){
                return scrollableView.getScrollY();
            } else {
                NestedScrollView nsv = ((NestedScrollView) scrollableView);
                View child = nsv.getChildAt(0);
                return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
            }
        } else {
            return 0;
        }
    }
}
