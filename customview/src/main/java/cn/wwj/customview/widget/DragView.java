package cn.wwj.customview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.wwj.customview.R;
import cn.wwj.customview.ScreenUtilsKt;


/**
 * Created by sky_li on 2017/8/17.
 * 说明:
 */

public class DragView extends FrameLayout implements View.OnTouchListener {

    private ImageView mDragImg;

    private int lastX;
    private int lastY;
    private int startX;

    private int left;
    private int top;

    private int mWidth;
    private int mHeight;

    private boolean isMoved;
    private LinearLayout mDragContent;
    private DragViewListener mListener;

    private boolean isRight = true;


    public DragView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.base_module_drag_view, this, true);
        mDragContent = (LinearLayout) findViewById(R.id.drag_content);
        mDragImg = (ImageView) findViewById(R.id.drag_img);
        mDragContent.setOnTouchListener(this);

    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        ImageView imageView = this.findViewById(R.id.close_btn);
        if (imageView != null) {
            if (visibility == View.VISIBLE) {
                imageView.setBackgroundResource(R.drawable.base_module_plugin_close);
                mDragImg.setVisibility(VISIBLE);
            } else {
                imageView.setBackgroundResource(0);
                mDragImg.setVisibility(GONE);
            }
        }
    }

    public void startScroll(final int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end).setDuration(100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) mDragContent.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.leftMargin = (int) animation.getAnimatedValue();
                    mDragContent.setLayoutParams(layoutParams);
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 初始化位置
     */
    public void initLocation(boolean isHomePage) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mDragContent.getLayoutParams();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (layoutParams != null) {
            layoutParams.leftMargin = (int) (width - ScreenUtilsKt.dp2px(100f));
            if (isHomePage) {
                layoutParams.topMargin = (int) (height - ScreenUtilsKt.dp2px(400f));
            } else {
                layoutParams.topMargin = (int) (height - ScreenUtilsKt.dp2px(300f));
            }
            mDragContent.setLayoutParams(layoutParams);
        }
    }


    /**
     * 初始化位置
     * 用于精选页
     */
    public void initLocation() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mDragContent.getLayoutParams();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (layoutParams != null) {
//            layoutParams.leftMargin = width - getResources().getDimensionPixelSize(R.dimen.x100);
//            layoutParams.topMargin = height - getResources().getDimensionPixelSize(R.dimen.x475) - DeviceInfoUtils.getStatusBarHeight(getContext());
            mDragContent.setLayoutParams(layoutParams);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                startX = lastX;
                break;
            case MotionEvent.ACTION_MOVE:
                isMoved = true;
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                left = v.getLeft() + dx;
                top = v.getTop() + dy;
                int right = v.getRight() + dx;
                int bottom = v.getBottom() + dy;
                // 设置不能出界
                if (left < 0) {
                    left = 0;
                    right = left + v.getWidth();
                }
                if (right > mWidth) {
                    right = mWidth;
                    left = right - v.getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + v.getHeight();
                }
                if (bottom > mHeight) {
                    bottom = mHeight;
                    top = bottom - v.getHeight();
                }
                v.layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //只有滑动改变上边距时，抬起才进行设置
                if (isMoved) {
                    MarginLayoutParams layoutParams = (MarginLayoutParams) mDragContent.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.topMargin = top;
                        mDragContent.setLayoutParams(layoutParams);
                    }
                }
                int endX = (int) event.getRawX();
                //滑动距离比较小，当作点击事件处理
                if (Math.abs(startX - endX) < 5) {
                    if (mListener != null) {
                        if (event.getY() > 0 && event.getY() < mDragImg.getBottom()) {
                            mListener.onClickImg();
                        } else {
                            mListener.onClickClose();
                        }
                    }
                    return false;
                }
                if (left + v.getWidth() / 2 < mWidth / 2) {
                    isRight = false;
                    startScroll(left, 0);
                } else {
                    isRight = true;
                    startScroll(left, mWidth - v.getWidth());
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void setDragViewListener(DragViewListener listener) {
        mListener = listener;
    }

    /**
     * 设置显示的图片
     *
     * @param url
     */
    public void setImageUrl(Context context, String url) {
        if (mDragImg != null && !TextUtils.isEmpty(url)) {
        }
    }

    public interface DragViewListener {
        void onClickImg();

        void onClickClose();
    }

    public void scrollToSide() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mDragContent.getLayoutParams();
        if (!isRight) {
            startScroll(mDragImg.getLeft(), 0);
            layoutParams.leftMargin = 0;
        } else {
            mWidth = getMeasuredWidth();
            layoutParams.leftMargin = mWidth - mDragImg.getWidth();
        }
        mDragContent.setLayoutParams(layoutParams);
    }
}
