# AndroidCircleMenu
Android圆形旋转菜单
####实例效果
![image](https://github.com/wanliyang1990/AndroidCircleMenu/blob/master/imgs/circlemenu.gif)<br/>

###使用方法：
#####xml布局：<br/>

    <com.ywl5320.circlemenu.CircleMenuLayout
        android:id="@+id/cml_menu"
        android:layout_width="150dp"
        android:layout_height="150dp"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:layout_marginBottom="92dp"/>
        
#####菜单xml布局：<br/>

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" 
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:padding="5dp"
    android:gravity="center">
    
    <ImageView
        android:id="@+id/img"
        android:layout_width="25dp"
        android:layout_height="25dp"
        android:scaleType="fitXY"/>
    <TextView
        android:id="@+id/tv_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="菜单项"
        android:textSize="9sp"
        android:gravity="center"
        android:textColor="#ffffff"/>
    </LinearLayout>
    
#####MainActivity中调用：<br/>

        private String[] titles = {"支付宝", "银联", "微信"};
        private int[] imgs = {R.mipmap.icon_alipay, R.mipmap.icon_bankpay, R.mipmap.icon_wexin};

        cmlmenu = (CircleMenuLayout) findViewById(R.id.cml_menu);
        cmlmenu.initDatas(titles, imgs);
        cmlmenu.setOnMenuItemSelectedListener(new CircleMenuLayout.OnMenuItemSelectedListener() {
            @Override
            public void onMenuItemOnclick(int code) {
                if(code == 0)//
                {
                    Toast.makeText(MainActivity.this, "支付宝", Toast.LENGTH_SHORT).show();
                }
                else if(code == 1)
                {
                    Toast.makeText(MainActivity.this, "银联", Toast.LENGTH_SHORT).show();
                }
                else if(code == 2)
                {
                    Toast.makeText(MainActivity.this, "微信", Toast.LENGTH_SHORT).show();
                }
            }
        });
    
create by ywl5320