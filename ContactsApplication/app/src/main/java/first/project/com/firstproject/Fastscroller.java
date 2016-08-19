package first.project.com.firstproject;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import first.project.com.firstproject.adapter.PhoneContactsAdapter;

/**
 * Created by user on 8/5/16.
 */
public class Fastscroller extends LinearLayout {
    private View bubble;
    private View handle;

    private final ScrollListener scrollListener = new ScrollListener();
    RecyclerView recyclerView;
    private static final int HANDLE_ANIMATION_DURATION = 100;
    private int height;
    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";
    private static final String ALPHA = "alpha";

    private AnimatorSet currentAnimator = null;
    private static final int HANDLE_HIDE_DELAY = 1000;
    private static final int TRACK_SNAP_RANGE = 5;

    private final HandleHider handleHider = new HandleHider();



    public Fastscroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public Fastscroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context);
    }

    private void initialise(Context context) {
        setOrientation(HORIZONTAL);
        setClipChildren(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.fastscoller, this);
        bubble = findViewById(R.id.fastscroller_bubble);
        handle = findViewById(R.id.fastscroller_handle);
    }


    private void showHandle() {
        handle.setVisibility(VISIBLE);
    }

    private void hideHandle() {
        handle.setVisibility(INVISIBLE);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        System.out.println( " height  = " + height  + " width " + w + " old w " + oldw + " oldh" + oldh);
    }

    private void setPosition(float y,String a) {
        float position = y / height;
        System.out.println( " placing position " + position);
        int bubbleHeight = bubble.getHeight();
        bubble.setY( (int) ((height - bubbleHeight) * position));
        System.out.println ( "  height "   +height );
        System.out.println( " bubble height  " + bubbleHeight);
        System.out.println(((height - bubbleHeight) * position));
        System.out.println(" position   " + getValueInRange(0, height - bubbleHeight, (int) ((height - bubbleHeight) * position)));

        int handleHeight = handle.getHeight();

        handle.setY(getValueInRange(0, height - handleHeight, (int) ((height - handleHeight) * position)));
        ((TextView)handle).setText(a);
    }

    private int getValueInRange(int min, int max, int value) {

        System.out.println( min   + "  "+ max + "  " + "   "+ value);

        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setOnScrollListener(scrollListener);
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView rv, int dx, int dy) {
            View child = recyclerView.findChildViewUnder(dx,dy);
            View firstVisibleView = recyclerView.getChildAt(0);
            int firstVisiblePosition = recyclerView.getChildPosition(firstVisibleView);
            System.out.println("  first visible position" + firstVisiblePosition);
            int visibleRange = recyclerView.getChildCount();
            System.out.println(" vsible range  " +visibleRange );
            int lastVisiblePosition = firstVisiblePosition + visibleRange;
            System.out.println("  last visible posoton  " +lastVisiblePosition );
            int itemCount = recyclerView.getAdapter().getItemCount();
            int position;
            if (firstVisiblePosition == 0) {
                position = 0;
            } else if (lastVisiblePosition == itemCount - 1) {
                position = itemCount - 1;
            } else {
                position = firstVisiblePosition;
            }
            float proportion = (float) position / (float) itemCount;


            System.out.println(" proportion  " + proportion);
            PhoneContactsAdapter adap = (PhoneContactsAdapter)recyclerView.getAdapter();
            try {
                position =  recyclerView.getChildAdapterPosition(child);
                if(position >= 0)
                    setPosition(height * proportion,String.valueOf(adap.getFirstletter(position)));
            }catch (Exception ex){

            }

        }
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            PhoneContactsAdapter adap = (PhoneContactsAdapter)recyclerView.getAdapter();
            View child = recyclerView.findChildViewUnder(event.getX(),event.getY());
            try {
                int position = recyclerView.getChildAdapterPosition(child);
                if(position >= 0)
                setPosition(event.getY(), String.valueOf(adap.getFirstletter(position)));

            }catch (Exception ex){

            }

            getHandler().removeCallbacks(handleHider);
            if (handle.getVisibility() == INVISIBLE) {
                showHandle();
            }
            setRecyclerViewPosition(event.getY());
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            getHandler().postDelayed(handleHider, HANDLE_HIDE_DELAY);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class HandleHider implements Runnable {
        @Override
        public void run() {
            hideHandle();
        }
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null) {
            int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;
            if (bubble.getY() == 0) {
                proportion = 0f;
            } else if (bubble.getY() + bubble.getHeight() >= height - TRACK_SNAP_RANGE) {
                proportion = 1f;
            } else {
                proportion = y / (float) height;
            }
            int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
            recyclerView.scrollToPosition(targetPos);
        }
    }
}