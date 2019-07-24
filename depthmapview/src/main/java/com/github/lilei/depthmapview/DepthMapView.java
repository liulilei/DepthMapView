package com.github.lilei.depthmapview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @author: lll
 * @date: 2019-07-19
 */
public class DepthMapView extends View {

    private int mWidth;
    //圆点半径
    private int mDotRadius = 2;
    //圆圈半径
    private int mCircleRadius = 8;
    private float mGridWidth;
    //底部价格区域高度
    private int mBottomPriceHeight;
    //右侧委托量绘制个数
    private int mLineCount;
    //背景颜色
    private int mBackgroundColor;

    private boolean mIsHave;
    //是否是长按
    private boolean mIsLongPress;

    //最大的委托量
    private float mMaxVolume;
    private float mMultiple;
    private int mLastPosition;
    private int mDrawWidth = 0;
    private int mDrawHeight;
    //触摸点的X轴值
    private int mEventX;

    //文案绘制画笔
    private Paint mTextPaint;
    //买入区域边线绘制画笔
    private Paint mBuyLinePaint;
    //卖出区域边线绘制画笔
    private Paint mSellLinePaint;
    //买入区域绘制画笔
    private Paint mBuyPathPaint;
    //卖出取悦绘制画笔
    private Paint mSellPathPaint;
    //选中时圆点绘制画笔
    private Paint mRadioPaint;
    //选中时圆圈绘制画笔
    private Paint mCirclePaint;
    //选中时中间文案背景画笔
    private Paint mSelectorBackgroundPaint;
    //选中时中间文案画笔
    private Paint mSelectorTextPaint;

    private Path mBuyPath = new Path();
    private Path mSellPath = new Path();

    private List<DepthBuySellData> mBuyData;
    private List<DepthBuySellData> mSellData;

    private List<Float> buyAmountMoney;
    private List<Float> sellAmountMoney;

    //    价格显示精度限制
    public int mPriceLimit = 7;
//    private int mVolumeLimit = 5;

    private HashMap<Integer, DepthBuySellData> mMapX;
    private HashMap<Integer, Float> mMapY;
    private HashMap<Integer, Float> mAmount;
    private Float[] mBottomPrice;

    private boolean onTouch = false;
    private boolean onLongPress = false;
    private boolean onVerticalMove = false;

    private String tradePost = "";
    private String coin = "";
    private int pricisionCoin = 0;
    private int pricisionPrice = 0;
    private int priTradePost = 2;//显示委托价精度

    public DepthMapView(Context context) {
        this(context, null);
    }

    public DepthMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        gestureCompat.setTouchSlop(touchSlop);

        init(attrs);
    }

    @SuppressLint("UseSparseArrays")
    private void init(AttributeSet attrs) {
        mBottomPriceHeight = 40;
        mMapX = new HashMap<>();
        mMapY = new HashMap<>();
        mAmount = new HashMap<>();
        mBottomPrice = new Float[4];
        mBuyData = new ArrayList<>();
        mSellData = new ArrayList<>();
        buyAmountMoney = new ArrayList<>();
        sellAmountMoney = new ArrayList<>();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);

        mBuyLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBuyLinePaint.setStyle(Paint.Style.STROKE);
        mBuyLinePaint.setTextAlign(Paint.Align.CENTER);
        mBuyPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSellLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSellLinePaint.setStyle(Paint.Style.STROKE);
        mSellLinePaint.setTextAlign(Paint.Align.CENTER);
        mSellPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRadioPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadioPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);

        mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectorTextPaint.setTextAlign(Paint.Align.RIGHT);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DepthMapView);
        if (typedArray != null) {
            try {
                mLineCount = typedArray.getInt(R.styleable.DepthMapView_line_count, 4);
                mDotRadius = typedArray.getDimensionPixelSize(R.styleable.DepthMapView_dot_radius, dip2px(mDotRadius));
                mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.DepthMapView_circle_radius, dip2px(mCircleRadius));
                mBackgroundColor = typedArray.getColor(R.styleable.DepthMapView_background_color, ContextCompat.getColor(getContext(), android.R.color.white));
                mBuyLinePaint.setStrokeWidth(typedArray.getDimensionPixelSize(R.styleable.DepthMapView_line_width, dip2px(1.5f)));
                mSellLinePaint.setStrokeWidth(typedArray.getDimensionPixelSize(R.styleable.DepthMapView_line_width, dip2px(1.5f)));
                mTextPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_text_color, Color.parseColor("#9B9B9B")));
                mTextPaint.setTextSize(typedArray.getDimension(R.styleable.DepthMapView_text_size, dip2px(10f)));
                mBuyLinePaint.setColor(typedArray.getColor(R.styleable.DepthMapView_buy_line_color, Color.parseColor("#00BE66")));
                mSellLinePaint.setColor(typedArray.getColor(R.styleable.DepthMapView_sell_line_color, Color.parseColor("#EA573C")));
                int buyColor = typedArray.getColor(R.styleable.DepthMapView_buy_path_color, Color.parseColor("#00BE66"));
                int sellColor = typedArray.getColor(R.styleable.DepthMapView_sell_path_color, Color.parseColor("#EA573C"));
                mSelectorBackgroundPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_selector_background_color, Color.parseColor("#252B3E")));
                mSelectorTextPaint.setColor(typedArray.getColor(R.styleable.DepthMapView_selector_text_color, ContextCompat.getColor(getContext(), android.R.color.white)));
                mSelectorTextPaint.setTextSize(typedArray.getDimension(R.styleable.DepthMapView_selector_text_size, dip2px(10f)));
                LinearGradient buyLg = new LinearGradient(0, 0, 0, 800, buyColor, Color.parseColor("#2000BE66"), Shader.TileMode.CLAMP);
                mBuyPathPaint.setShader(buyLg);
                LinearGradient sellLg = new LinearGradient(getWidth(), 0, getWidth(), 800, sellColor, Color.parseColor("#20EA573C"), Shader.TileMode.CLAMP);
                mSellPathPaint.setShader(sellLg);
            } finally {
                typedArray.recycle();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        mDrawWidth = mWidth / 2 - 1;
        mDrawHeight = h - mBottomPriceHeight;
    }

    //设置交易区精度
    public void setPriTradePost(int priTradePost) {
        this.priTradePost = priTradePost;
    }

    public void setData(List<DepthBuySellData> buyData, List<DepthBuySellData> sellData, String name, int prinPrice, int priCoin) {
        double vol = 0;
        float amountmoney = 0;
        if (buyData.size() > 0) {
            mBuyData.clear();
            buyAmountMoney.clear();
            //买入数据按价格进行排序
            Collections.sort(buyData, new comparePrice());
            DepthBuySellData depthDataBean;
            //累加买入委托量
            for (int index = buyData.size() - 1; index >= 0; index--) {
                depthDataBean = buyData.get(index);
                vol += Double.valueOf(depthDataBean.getAmount());
                amountmoney += Float.valueOf(depthDataBean.getAmount()) * Float.valueOf(depthDataBean.getPrice());
                depthDataBean.setAmount("" + vol);
                mBuyData.add(0, depthDataBean);
                buyAmountMoney.add(0, amountmoney);
            }
            //修改底部买入价格展示
            mBottomPrice[0] = Float.valueOf(mBuyData.get(0).getPrice());
            mBottomPrice[1] = Float.valueOf(mBuyData.get(mBuyData.size() > 1 ? mBuyData.size() - 1 : 0).getPrice());
            mMaxVolume = Float.valueOf(mBuyData.get(0).getAmount());
        }

        if (sellData.size() > 0) {
            mSellData.clear();
            sellAmountMoney.clear();
            vol = 0;
            amountmoney = 0;
            //卖出数据按价格进行排序
            Collections.sort(sellData, new comparePrice());
            //累加卖出委托量
            for (DepthBuySellData depthDataBean : sellData) {
                vol += Double.valueOf(depthDataBean.getAmount());
                amountmoney += Float.valueOf(depthDataBean.getAmount()) * Float.valueOf(depthDataBean.getPrice());
                depthDataBean.setAmount("" + vol);
                mSellData.add(depthDataBean);
                sellAmountMoney.add(amountmoney);
            }
            //修改底部卖出价格展示
            mBottomPrice[2] = Float.valueOf(mSellData.get(0).getPrice());
            mBottomPrice[3] = Float.valueOf(mSellData.get(mSellData.size() > 1 ? mSellData.size() - 1 : 0).getPrice());
            mMaxVolume = Math.max(mMaxVolume, Float.valueOf(mSellData.get(mSellData.size() - 1).getAmount()));
        }
        mMaxVolume = mMaxVolume * 1.05f;
        mMultiple = mMaxVolume / mLineCount;
        tradePost = name;
        coin = name;
        pricisionPrice = prinPrice;
        pricisionCoin = priCoin;
        invalidate();
    }

    private final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent e) {
            if (onTouch) {
                onLongPress = true;
                mIsLongPress = true;
                invalidate();
            }
        }
    });

    private GestureMoveActionCompat gestureCompat = new GestureMoveActionCompat(null);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        mEventX = (int) event.getX();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                onTouch = true;
                mIsLongPress = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
//                    mIsLongPress = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                onTouch = false;
                onLongPress = false;
                mIsLongPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                onTouch = false;
                onLongPress = false;
//                mIsLongPress = false;
//                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean onHorizontalMove = gestureCompat.onTouchEvent(event, event.getX(), event.getY());
        final int action = MotionEventCompat.getActionMasked(event);

        onVerticalMove = false;

        if (action == MotionEvent.ACTION_MOVE) {
            if (!onHorizontalMove && !onLongPress && gestureCompat.isDragging()) {
                onTouch = false;
                onVerticalMove = true;
            }
        }

        getParent().requestDisallowInterceptTouchEvent(!onVerticalMove);

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        canvas.save();
        //绘制买入区域
        drawBuy(canvas);
        //绘制卖出区域
        drawSell(canvas);
        //绘制界面相关文案
        drawText(canvas);
        canvas.restore();
    }

    private void drawBuy(Canvas canvas) {
        mGridWidth = (mDrawWidth * 1.0f / (mBuyData.size() - 1 == 0 ? 1 : mBuyData.size() - 1));
        mBuyPath.reset();
        mMapX.clear();
        mMapY.clear();
        mAmount.clear();
        float x;
        float y;
        for (int i = 0; i < mBuyData.size(); i++) {
            if (i == 0) {
                mBuyPath.moveTo(0, getY(Float.valueOf(mBuyData.get(0).getAmount())));
            }
            y = getY(Float.valueOf(mBuyData.get(i).getAmount()));
            if (i >= 1) {
                canvas.drawLine(mGridWidth * (i - 1), getY(Float.valueOf(mBuyData.get(i - 1).getAmount())), mGridWidth * i, y, mBuyLinePaint);
            }
            if (i != mBuyData.size() - 1) {
                mBuyPath.quadTo(mGridWidth * i, y, mGridWidth * (i + 1), getY(Float.valueOf(mBuyData.get(i + 1).getAmount())));
            }

            x = mGridWidth * i;
            mMapX.put((int) x, mBuyData.get(i));
            mMapY.put((int) x, y);
            mAmount.put((int) x, buyAmountMoney.get(i));
            if (i == mBuyData.size() - 1) {
                mBuyPath.quadTo(mGridWidth * i, y, mGridWidth * i, mDrawHeight);
                mBuyPath.quadTo(mGridWidth * i, mDrawHeight, 0, mDrawHeight);
                mBuyPath.close();
            }
        }
        if (mBuyData.size() > 1) {
            canvas.drawPath(mBuyPath, mBuyPathPaint);
        }
    }

    private void drawSell(Canvas canvas) {
        mGridWidth = (mDrawWidth * 1.0f / (mSellData.size() - 1 == 0 ? 1 : mSellData.size() - 1));
        mSellPath.reset();
        float x;
        float y;
        for (int i = 0; i < mSellData.size(); i++) {
            if (i == 0) {
                mSellPath.moveTo(mDrawWidth, getY(Float.valueOf(mSellData.get(0).getAmount())));
            }
            y = getY(Float.valueOf(mSellData.get(i).getAmount()));
            if (i >= 1) {
                canvas.drawLine((mGridWidth * (i - 1)) + mDrawWidth, getY(Float.valueOf(mSellData.get(i - 1).getAmount())),
                        (mGridWidth * i) + mDrawWidth, y, mSellLinePaint);
            }
            if (i != mSellData.size() - 1) {
                mSellPath.quadTo((mGridWidth * i) + mDrawWidth, y,
                        (mGridWidth * (i + 1)) + mDrawWidth, getY(Float.valueOf(mSellData.get(i + 1).getAmount())));
            }
            x = (mGridWidth * i) + mDrawWidth;
            mMapX.put((int) x, mSellData.get(i));
            mMapY.put((int) x, y);
            mAmount.put((int) x, sellAmountMoney.get(i));
            if (i == mSellData.size() - 1) {
                mSellPath.quadTo(mWidth, y, (mGridWidth * i) + mDrawWidth, mDrawHeight);
                mSellPath.quadTo((mGridWidth * i) + mDrawWidth, mDrawHeight, mDrawWidth, mDrawHeight);
                mSellPath.close();
            }
        }
        if (mSellData.size() > 1) {
            canvas.drawPath(mSellPath, mSellPathPaint);
        }
    }

    private void drawText(Canvas canvas) {
        float value;
        String str;
        for (int j = 0; j < mLineCount; j++) {
            value = mMaxVolume - mMultiple * j;
            str = getSelectTotalValue(value);
            canvas.drawText(str, mWidth, mDrawHeight / mLineCount * j + 30, mTextPaint);
        }
        int size = mBottomPrice.length;
        int height = mDrawHeight + mBottomPriceHeight / 2 + 10;
        if (size > 0 && mBottomPrice[0] != null) {
            String data = getSelectPriceValue(mBottomPrice[0]);
            canvas.drawText(data, mTextPaint.measureText(data), height, mTextPaint);
            data = getSelectPriceValue(mBottomPrice[1]);
            canvas.drawText(data, mDrawWidth - 10, height, mTextPaint);
            data = getSelectPriceValue(mBottomPrice[2]);
            canvas.drawText(data, mDrawWidth + mTextPaint.measureText(data) + 10, height, mTextPaint);
            data = getSelectPriceValue(mBottomPrice[3]);
            canvas.drawText(data, mWidth, height, mTextPaint);
        }
        if (mIsLongPress) {
            boolean mIsHave = false;
            for (int key : mMapX.keySet()) {
                if (key == mEventX) {
                    mIsHave = true;
                    mLastPosition = mEventX;
                    break;
                }
            }
            if (mIsHave) {
                drawSelectorView(canvas, mLastPosition);
            } else {
                mLastPosition = getApproximate(mEventX);
                drawSelectorView(canvas, mLastPosition);
            }
        }
    }

    private int getApproximate(int x) {
        int minDifference = Integer.MAX_VALUE;
        int key1 = 0;
        for (int key : mMapX.keySet()) {
            int temp = Math.abs(key - x);
            if (temp < minDifference) {
                minDifference = temp;
                key1 = key;
            }
        }
        return key1;
    }

    private void drawSelectorView(Canvas canvas, int position) {
//        mIsHave = true;
        Float y = mMapY.get(position);
        if (y == null) {
            return;
        }
        if (position < mDrawWidth) {
            mCirclePaint.setColor(Color.parseColor("#454E6B"));
            mRadioPaint.setColor(Color.parseColor("#7E9AEF"));
        } else {
            mCirclePaint.setColor(Color.parseColor("#454E6B"));
            mRadioPaint.setColor(Color.parseColor("#7E9AEF"));
        }
        canvas.drawCircle(position, y, mCircleRadius, mCirclePaint);
        canvas.drawCircle(position, y, mDotRadius, mRadioPaint);

        String volume = "累计数量：" + getSelectVolumeValue(Double.valueOf(mMapX.get(position).getAmount())) + " " + coin;
        String price = "委托价：" + getSelectPriceValue(Float.valueOf(mMapX.get(position).getPrice())) + " " + tradePost;
        float totalPrice = mAmount.get(position);
        String total = "累计金额：" + getSelectTotalValue(totalPrice) + " " + tradePost;
        float width = Math.max(mSelectorTextPaint.measureText(volume), mSelectorTextPaint.measureText(price));
        width = Math.max(width, mSelectorTextPaint.measureText(total));
        Paint.FontMetrics metrics = mSelectorTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int padding = dip2px(5);
        canvas.drawRoundRect(new RectF(mDrawWidth - width / 2 - padding, 20, mDrawWidth + width / 2 + padding * 2, textHeight * 3 + padding * 3 + 20), 10, 10, mSelectorBackgroundPaint);
        canvas.drawText("委托价：",
                mDrawWidth - width / 2 + padding + mSelectorTextPaint.measureText("委托价："), textHeight + 22, mSelectorTextPaint);
        canvas.drawText(getSelectPriceValue(Float.valueOf(mMapX.get(position).getPrice())) + " " + tradePost, mDrawWidth + width / 2 + padding, textHeight + 22, mSelectorTextPaint);
        canvas.drawText("累计数量：",
                mDrawWidth - width / 2 + padding + mSelectorTextPaint.measureText("累计数量："), textHeight * 2 + padding + 20, mSelectorTextPaint);
        canvas.drawText(getSelectVolumeValue(Double.valueOf(mMapX.get(position).getAmount())) + " " + coin, mDrawWidth + width / 2 + padding, textHeight * 2 + padding + 20, mSelectorTextPaint);
        canvas.drawText("累计金额：",
                mDrawWidth - width / 2 + padding + mSelectorTextPaint.measureText("累计金额："), textHeight * 3 + padding * 2 + 20, mSelectorTextPaint);
        canvas.drawText(getSelectTotalValue(totalPrice) + " " + tradePost, mDrawWidth + width / 2 + padding, textHeight * 3 + padding * 2 + 20, mSelectorTextPaint);
    }

    public class comparePrice implements Comparator<DepthBuySellData> {
        @Override
        public int compare(DepthBuySellData o1, DepthBuySellData o2) {
            float str1 = Float.valueOf(o1.getPrice());
            float str2 = Float.valueOf(o2.getPrice());
            return Float.compare(str1, str2);
        }
    }

    private float getY(float volume) {
        return mDrawHeight - (mDrawHeight) * volume / mMaxVolume;
    }

    private String getValue(float value) {
//        String value = new BigDecimal(data).toPlainString();
//        return subZeroAndDot(value);
        return String.format("%." + mPriceLimit + "f", value);
    }

    @SuppressLint("DefaultLocale")
    private String getVolumeValue(float value) {
        return String.format("%.4f", value);
    }

    private String getSelectVolumeValue(double targetValue) {
        if (targetValue > 100000) {
            BigDecimal bigDecimal = new BigDecimal(targetValue / 1000);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        } else {
            BigDecimal bigDecimal = new BigDecimal(targetValue);
            return bigDecimal.setScale(pricisionCoin, BigDecimal.ROUND_HALF_UP).toPlainString();
        }
    }

    private String getSelectPriceValue(float targetValue) {
        BigDecimal bigDecimal = new BigDecimal(targetValue);
        return bigDecimal.setScale(pricisionPrice, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private String getSelectTotalValue(float targetValue) {
        if (targetValue > 100000) {
            BigDecimal bigDecimal = new BigDecimal(targetValue / 1000);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        } else {
            BigDecimal bigDecimal = new BigDecimal(targetValue);
            return bigDecimal.setScale(priTradePost, BigDecimal.ROUND_HALF_UP).toPlainString();
        }
    }

    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
