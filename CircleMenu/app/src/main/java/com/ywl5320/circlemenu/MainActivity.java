package com.ywl5320.circlemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] titles = {"支付宝", "银联", "微信"};
    private int[] imgs = {R.mipmap.icon_alipay, R.mipmap.icon_bankpay, R.mipmap.icon_wexin};
    private CircleMenuLayout cmlmenu;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cmlmenu = (CircleMenuLayout) findViewById(R.id.cml_menu);
        btn = (Button) findViewById(R.id.btn);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmlmenu.setCurrentTag(2);
            }
        });

    }
}
