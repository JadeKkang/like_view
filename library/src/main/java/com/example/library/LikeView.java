package com.example.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 时间：2018/8/22 09:23
 * 姓名：韩晓康
 * 功能：
 * 描述：dotColors 可以自己设置
 */
public class LikeView extends View {
    private Context context;
    private int ivResore;
    private int dotNum = 7;//小圆点组数  必需和dotColors 相同
    private Paint mCircle;//圆形画笔
    private Paint mBitmap;//图片画笔
    private int centerX;//中心点x
    private int centerY;//中心点y
    private Bitmap bitmap;//心形图片
    private Float mBimt = 0f;//图片变化值  控制什么时候绘制小圆点
    private String canvasType = "Bitmap";//标识 在一定的情况下绘制图形
    private Float circleR = 0f;//绘制小圆点的基础圆形半径
    private Float circleR0 = 0f;//绘制大圆的半径
    private Float dotR = 10f;//小圆点的半径
    private int circleColor;
    private int[] dotColors = {0xffdaa9fa, 0xfff2bf4b, 0xffe3bca6, 0xff329aed,
            0xffb1eb99, 0xff67c9ad, 0xffde6bac};//小圆点的颜色值
    private int width;
    private int height;

    public LikeView(@NonNull Context context) {
        this(context, null);
    }


    public LikeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public LikeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LikeView);
        if (typedArray != null) {
            ivResore = typedArray.getResourceId(R.styleable.LikeView_bitmap, R.mipmap.heart);
            circleColor = typedArray.getColor(R.styleable.LikeView_circleColor, ContextCompat.getColor(context, R.color.colorPrimary));
            dotNum = typedArray.getInteger(R.styleable.LikeView_dotNum, 7);
        }
        typedArray.recycle();
        init(context);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                start();//点击开启动画
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (bitmap != null) {
            width = bitmap.getWidth()*4;
            height = bitmap.getWidth()*4;
            setMeasuredDimension(width, height);
        }
    }

    private void init(Context context) {
        this.context = context;
        mCircle = new Paint();
        mCircle.setAntiAlias(true);
        mBitmap = new Paint();
        mBitmap.setAntiAlias(true);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), ivResore,options);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        switch (canvasType) {
            case "Bitmap":
                canvasHeart(canvas);
                break;
            case "Circle":
                canvasCircle(canvas);
                break;
            case "Dot":
                canvasDot(canvas);
                break;
        }
    }

    /**
     * 作者  韩晓康
     * 时间  2018/8/27 9:20
     * 描述  绘制心形和小圆点
     */
    private void canvasDot(Canvas canvas) {
        canvasHeart(canvas);
        if (mBimt == 36) {
            float angleA = 0;
            float angleB = (float) (-Math.PI / 20);
            for (int i = 0; i < dotNum; i++) {
                mCircle.setColor(dotColors[i]);
                canvas.drawCircle((float) ((circleR + 5) * (Math.sin(angleB))), (float) ((circleR +5) * (Math.cos(angleB))), dotR, mCircle);
                angleA += 2 * Math.PI / dotNum;
                canvas.drawCircle((float) (circleR * (Math.sin(angleA))), (float) (circleR * (Math.cos(angleA))), dotR - 2, mCircle);
                angleB += 2 * Math.PI / dotNum;
            }
        }
    }

    /**
     * 作者  韩晓康
     * 时间  2018/8/27 9:20
     * 描述  绘制变大圆形
     */
    private void canvasCircle(Canvas canvas) {
        mCircle.setColor(circleColor);
        canvas.drawCircle((float) centerX, centerY, circleR0, mCircle);
    }

    /**
     * 作者  韩晓康
     * 时间  2018/8/27 9:20
     * 描述  绘制心形图片
     */
    private void canvasHeart(Canvas canvas) {
        canvas.translate(centerX, centerY);
        canvas.drawBitmap(bitmap, -bitmap.getWidth() / 2, -bitmap.getWidth() / 2, mBitmap);
    }

    public void start() {
        circleR = Float.valueOf(bitmap.getWidth());
        circleR0 = 0f;
        dotR = 6f;
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 72);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取到动画每次该变得float值，赋值给xpoint
                Float value = (Float) animation.getAnimatedValue();
                if (value <= 36) {
                    mBimt = value;
                    canvasType = "Circle";
                } else {
                    canvasType = "Dot";
                    circleR = circleR + 1f;
                    dotR = dotR - 0.2f;
                }
                    circleR0 = circleR0+1f;
                if (value == 72) {
                    mBimt = value/2;
                    try {
                        Thread.sleep(50);
                        new Thread(new progrssThread()).start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 通知view重绘
                invalidate();
            }
        });
        animator.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                postInvalidate();
            }
        }
    };

    public class progrssThread implements Runnable {
        @Override
        public void run() {
            while (dotR > 0) {
                try {
                    Thread.sleep(9);
                    Message msg = Message.obtain();
                    msg.what = 0;
                    dotR = dotR - 0.3f;
                    if (circleR < 36) {
                        circleR = circleR + 1f;
                    }
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 作者  韩晓康
     * 时间  2018/8/27 9:20
     * 描述  设置图片资源
     */
    public void setIvResore(int ivResore) {
        this.ivResore = ivResore;
    }
    /**
     * 作者  韩晓康
     * 时间  2018/8/27 9:20
     * 描述  设置圆形颜色
     */
    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }
    /**
     * 作者  韩晓康
     * 时间  2018/8/27 9:20
     * 描述  设置圆点数量和颜色值
     */
    public void setDotNum(int dotNum,int[] dotColors) {
        this.dotNum = dotNum;
        this.dotColors = dotColors;
    }

}
