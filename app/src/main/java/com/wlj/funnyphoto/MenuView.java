package com.wlj.funnyphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import static com.wlj.funnyphoto.util.DimenUtils.dip2px;
import static com.wlj.funnyphoto.util.DimenUtils.sp2px;


public class MenuView extends View {


    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }

    OnMenuClickListener onMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    private static final int BTN_MARGIN = 4;//dp
    private static final int CAMERA_BTN_PADDING = 8;//dp
    private static final int MAIN_BTN_PADDING = 16;//dp

    private int btnMargin;
    private float textWidth;
    private float textHeight;
    private int mainBtnPadding;
    private int cameraBtnPadding;
    private Bitmap cameraBitmap;

    private Paint paint;
    private String TEXTURE_TEXT = "贴图";

    private Rect mainBtnRect = new Rect();
    private Rect cameraBtnRect = new Rect();


    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        btnMargin = dip2px(context, BTN_MARGIN);
        cameraBtnPadding = dip2px(context, CAMERA_BTN_PADDING);
        mainBtnPadding = dip2px(context, MAIN_BTN_PADDING);
        cameraBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.camera);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(sp2px(context, 40));
        textWidth = paint.measureText(TEXTURE_TEXT);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = (-fontMetrics.ascent - fontMetrics.descent) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) (btnMargin + mainBtnPadding + textWidth + mainBtnPadding + btnMargin +
                btnMargin + cameraBtnPadding + cameraBitmap.getWidth() + cameraBtnPadding + btnMargin);
        int height = (int) (btnMargin + mainBtnPadding + textWidth + mainBtnPadding + btnMargin +
                btnMargin + cameraBtnPadding + cameraBitmap.getHeight() + cameraBtnPadding + btnMargin);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int main_btn_real_radius = (int) (btnMargin + mainBtnPadding + textWidth / 2);
        int main_btn_center_x = main_btn_real_radius;
        int main_btn_center_y = main_btn_real_radius;

        int camera_btn_real_radius = cameraBitmap.getWidth() / 2 + btnMargin + cameraBtnPadding;
        int camera_btn_center_x = getWidth() - camera_btn_real_radius;
        int camera_btn_center_y = getHeight() - camera_btn_real_radius;


        Path path = new Path();

        int point0_x = main_btn_center_x;
        int point0_y = main_btn_center_y + main_btn_real_radius;

        int point1_x = main_btn_center_x + main_btn_real_radius;
        int point1_y = main_btn_center_y;


        int point2_x = camera_btn_center_x - camera_btn_real_radius;
        int point2_y = camera_btn_center_y;


        int point3_x = camera_btn_center_x;
        int point3_y = camera_btn_center_y - camera_btn_real_radius;


        int point02_x = (point0_x + point2_x) / 2;
        int point02_y = (point0_y + point2_y) / 2;

        int point13_x = (point1_x + point3_x) / 2;
        int point13_y = (point1_y + point3_y) / 2;

        int middle_x = (point02_x + point13_x) / 2;
        int middle_y = (point02_y + point13_y) / 2;


        path.moveTo(point0_x, point0_y);
        path.addArc(main_btn_center_x - main_btn_real_radius, main_btn_center_y - main_btn_real_radius, main_btn_center_x + main_btn_real_radius, main_btn_center_y + main_btn_real_radius, 90, 270);
        path.cubicTo(point1_x, point1_y, middle_x, middle_y, point3_x, point3_y);
        path.addArc(camera_btn_center_x - camera_btn_real_radius, camera_btn_center_y - camera_btn_real_radius, camera_btn_center_x + camera_btn_real_radius, camera_btn_center_y + camera_btn_real_radius, -90, 270);
        path.cubicTo(point2_x, point2_y, middle_x, middle_y, point0_x, point0_y);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFE066FF);
        int main_btn_bg_radius = (int) (textWidth / 2 + mainBtnPadding);
        canvas.drawCircle(main_btn_center_x, main_btn_center_y, main_btn_bg_radius, paint);

        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(TEXTURE_TEXT, main_btn_center_x, main_btn_center_y + textHeight, paint);


        int camera_btn_bg_radius = Math.max(cameraBitmap.getWidth(), cameraBitmap.getHeight()) / 2 + cameraBtnPadding;
        paint.setColor(0xFF00E5EE);
        canvas.drawCircle(camera_btn_center_x, camera_btn_center_y, camera_btn_bg_radius, paint);
        canvas.drawBitmap(cameraBitmap, camera_btn_center_x - cameraBitmap.getWidth() / 2, camera_btn_center_y - cameraBitmap.getHeight() / 2, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (onMenuClickListener == null) return false;
            int main_btn_real_radius = (int) (btnMargin + mainBtnPadding + textWidth / 2);
            int main_btn_left = 0;
            int main_btn_right = main_btn_real_radius * 2;
            int main_btn_top = 0;
            int main_btn_bottom = main_btn_real_radius * 2;

            int camera_btn_real_radius = cameraBitmap.getWidth() / 2 + btnMargin + cameraBtnPadding;
            int camera_btn_left = getWidth() - camera_btn_real_radius * 2;
            int camera_btn_right = getWidth();
            int camera_btn_top = getHeight() - camera_btn_real_radius * 2;
            int camera_btn_bottom = getHeight();

            if (x > main_btn_left && x < main_btn_right && y > main_btn_top && y < main_btn_bottom) {
                onMenuClickListener.onMenuClick(0);
            }
            if (x > camera_btn_left && x < camera_btn_right && y > camera_btn_top && y < camera_btn_bottom) {
                onMenuClickListener.onMenuClick(1);
            }

        }
        return super.onTouchEvent(event);
    }



}
