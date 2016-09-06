package com.ywl5320.circlemenu;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywl on 2016/8/7.
 */
public class CircleMenuLayout extends RelativeLayout {

    private static final int ID_CENTER_VIEW = 0x001;//中心点id
    private Context context;
    private View centerview;
    private ProgressBar progressBar;
    private RotateAnimation mRotateUpAnim;
    private LinearInterpolator lir;
    private LayoutInflater layoutInflater;
    private List<View> menuitems;
    private float radus = 0; //菜单之间的相隔度数
    private float offsetradus = 0; //旋转角度的和
    private float oldradus = 0; //根据菜单点击的项来设置起始度数
    private float sub = 0; //累计旋转角度 用于判断是否到零界点
    private boolean isrun = false; //是否处于旋转中
    private float finishdus = 0; //点击菜单旋转结束度数
    private OnMenuItemSelectedListener onMenuItemSelectedListener;
    private int tag;
    private boolean isright = false;
    private int currentPosition = 0;
    private float bgdus = 0; //背景旋转的角度


    public CircleMenuLayout(Context context) {
        this(context, null);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 初始化布局 把旋转背景和中心点添加进去
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        menuitems = new ArrayList<View>();
        centerview = new View(context);//中心点
        centerview.setId(ID_CENTER_VIEW);
        LayoutParams lp = new LayoutParams(0, 0);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(centerview, lp); //添加中心的 用于旋转定位

        progressBar = new ProgressBar(context);//旋转的背景
        LayoutParams lp2 = new LayoutParams(dip2px(context, 90), dip2px(context, 90));
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(progressBar, lp2);
        progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.mipmap.icon_circle_menu));
    }
    
    /**
     * 点击回调接口
     * @param onMenuItemSelectedListener
     */
    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener onMenuItemSelectedListener) {
		this.onMenuItemSelectedListener = onMenuItemSelectedListener;
	}

    /**
     * 初始化数据
     * @param titles
     * @param imgs
     */
	public void initDatas(String[] titles, int[] imgs)
    {
    	initMenuItem(3, dip2px(context, 45), titles, imgs);
    }


    /**
     * 菜单的数量 和 半径 名字 和图片 这里只为3个菜单做了适配
     * @param size
     * @param center_distance
     */
    public void initMenuItem(int size, int center_distance, String[] titles, int[] imgs)
    {
        radus = 360f / size;
        int width = dip2px(context, 50);
        int height = dip2px(context, 50);
        for(int i = 0; i < size; i++)
        {
            int top = 0;
            int left = 0;

            top = -(int)(Math.sin(radus * i * 3.1415f / 180) * center_distance); //r   *   cos(ao   *   3.14   /180   )
            left = -(int)(Math.cos(radus * i * 3.1415f / 180) * center_distance);
            LayoutParams lp = new LayoutParams(dip2px(context, 50), dip2px(context, 50));
            View view = layoutInflater.inflate(R.layout.item_circle_menu, this, false);
            view.setTag(i);
            TextView tvname = (TextView) view.findViewById(R.id.tv_name);
            ImageView ivimg = (ImageView) view.findViewById(R.id.img);
            tvname.setText(titles[i]);
            ivimg.setImageResource(imgs[i]);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {//根据点击的区域 旋转菜单
                    if(!isrun) {
                    	tag = (int) v.getTag();
                        currentPosition = tag;
                    	if(tag == 0)
                        {
                            finishdus = -360;
                        }
                        else if(tag == 1)
                        {
                            finishdus = -120;
                        }
                        else if(tag == 2)
                        {
                            finishdus = -240;
                        }
                        LayoutParams lp = (LayoutParams) v.getLayoutParams();
                        int l = lp.leftMargin;
                        int t = lp.topMargin;

                        if (t > -dip2px(context, 5) && l > -dip2px(context, 5)) {
                            oldradus = 120f;
                            isright = false;
                        } else if (t >  -dip2px(context, 5) && l <  -dip2px(context, 5)) {
                            oldradus = 120f;
                            isright = true;
                        } else if (t <  -dip2px(context, 5)) {
                            oldradus = 0f;
                        }
                        sub = 0;
                        circleMenu(8, dip2px(context, 45), oldradus, isright);
                        
                    }
                }
            });
            lp.addRule(RelativeLayout.BELOW, centerview.getId());
            lp.addRule(RelativeLayout.RIGHT_OF, centerview.getId());
            lp.setMargins(-width / 2 + top, -height / 2 + left, 0, 0);
            addView(view, lp);
            menuitems.add(view);
        }

        handler.postDelayed(runnable, 0);
    }
    
    /**
     * 根据度数来旋转菜单 菜单中心都在一个圆上面 采用圆周运动来旋转
     * @param offserradius
     * @param center_distance
     * @param d
     * @param right
     */
    public void circleMenu(float offserradius, int center_distance, float d, boolean right)
    {
    	if(oldradus != 0)
    	{
	    	progressBar.clearAnimation();
	    	if(isright)
	    	{
	    		mRotateUpAnim = new RotateAnimation(bgdus, bgdus + 120,
	                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
	                    0.5f);
	    		bgdus += 120;
	    	}
	    	else
	    	{
	    		mRotateUpAnim = new RotateAnimation(bgdus, bgdus - 120,
	                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
	                    0.5f);
	    		bgdus -= 120;
	    	}
	    	
	        lir = new LinearInterpolator();
	        mRotateUpAnim.setDuration(350);
	        mRotateUpAnim.setFillAfter(true);
	        mRotateUpAnim.setInterpolator(lir);
	//        mRotateUpAnim.setRepeatCount(Animation.INFINITE);
	        progressBar.startAnimation(mRotateUpAnim);
    	}
        circleMenuItem(offserradius, center_distance, d, right);
    }

    /**
     * 菜单旋转
     * @param offserradius
     * @param center_distance
     * @param d
     * @param right
     */
    public void circleMenuItem(float offserradius, int center_distance, float d, boolean right)
    {
        sub += offserradius;
        if(sub > d)
        {
        	if(onMenuItemSelectedListener != null)
            {
            	onMenuItemSelectedListener.onMenuItemOnclick(tag);
            }
            isrun = false;
            return;
        }
        if(right) {
            offsetradus -= offserradius;
        }
        else
        {
            offsetradus += offserradius;
        }
        int size = menuitems.size();
        int width = dip2px(context, 50);
        int height = dip2px(context, 50);
        for(int i = 0; i < size; i++)
        {
            if(Math.abs(sub - d) <= 8)
            {
                offsetradus = finishdus;
            }
            LayoutParams lp = (LayoutParams) menuitems.get(i).getLayoutParams();
            float ds = radus * i + offsetradus;
            int top = -(int)(Math.sin(ds * 3.1415f / 180) * center_distance); //r   *   cos(ao   *   3.14   /180   )
            int left = -(int)(Math.cos(ds * 3.1415f / 180) * center_distance);
            lp.setMargins(-width / 2 + top, -height / 2 + left, 0, 0);
            menuitems.get(i).requestLayout();
        }

        if(sub <= d) {
            isrun = true;
            offsetradus = offsetradus % 360;
            handler.postDelayed(runnable, 5);
        }
        else
        {
        	if(onMenuItemSelectedListener != null)
            {
            	onMenuItemSelectedListener.onMenuItemOnclick(tag);
            }
            isrun = false;
        }
    }



    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            circleMenuItem(8, dip2px(context, 45), oldradus, isright);
        }
    };
    
    public interface OnMenuItemSelectedListener
    {
    	void onMenuItemOnclick(int code);
    }

    /**
     * 设置旋转到哪个菜单项
     * @param tag
     */
    public void setCurrentTag(int tag)
    {
        if(currentPosition == tag)
        {
            return;
        }
        if(tag == 0)
        {
            finishdus = -360;
        }
        else if(tag == 1)
        {
            finishdus = -120;
        }
        else if(tag == 2)
        {
            finishdus = -240;
        }

        if(currentPosition == 0) //当前是0
        {
            if(tag == 1)
            {
                oldradus = 120f;
                isright = true;
            }
            else if(tag == 2)
            {
                oldradus = 120f;
                isright = false;
            }
        }
        else if(currentPosition == 1)
        {
            if(tag == 2)
            {
                oldradus = 120f;
                isright = true;
            }
            else if(tag == 0)
            {
                oldradus = 120f;
                isright = false;
            }
        }
        else if(currentPosition == 2)
        {
            if(tag == 0)
            {
                oldradus = 120f;
                isright = true;
            }
            else if(tag == 1)
            {
                oldradus = 120f;
                isright = false;
            }
        }

        currentPosition = tag;
        this.tag = tag;
        sub = 0;
        circleMenu(8, dip2px(context, 45), oldradus, isright);

    }
}
