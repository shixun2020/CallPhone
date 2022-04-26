package com.example.myceshi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class callActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView icBack, callPhone;
    private EditText addName, addPhone;
    private TextView title;
    private PhoneDBhelper phoneDBhelper;
    private String sendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        init();
        //为控件设置监听
        icBack.setOnClickListener(this);
        callPhone.setOnClickListener(this);

        //根据sendId是否为空判断用户进行的是添加还是修改操作，并且相应调整页面显示的内容便于区分
        //获取意图
        Intent intent = this.getIntent();
        //若进行的是添加操作，sendid值为空，若为修改操作，sendId不为空
        sendId = intent.getStringExtra("id");
        if (sendId != null) {
            title.setText("一键拨通");
            String name = intent.getStringExtra("name");
            String phone_number = intent.getStringExtra("phone_number");
            addName.setText(name);
            addPhone.setText(phone_number);
        }
    }

    //获取控件对象
    private void init() {
        icBack = findViewById(R.id.icBack);
        callPhone = findViewById(R.id.callPhone);
        title = findViewById(R.id.title);
        addName = findViewById(R.id.addName);
        addPhone = findViewById(R.id.addPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callPhone:
                if (sendId != null) {
                    String name = addName.getText().toString();
                    String phone_number = addPhone.getText().toString();
                    phoneDBhelper = new PhoneDBhelper(callActivity.this, "phone.db", null, 1);

                    if (ContextCompat.checkSelfPermission(callActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // 调用ActivityCompat.requestPermissions() 方法，向用户申请授权
                        ActivityCompat.requestPermissions(callActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phone_number));
                            startActivity(intent);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.icBack:
                Intent intent = new Intent(callActivity.this, CallPhoneMainActivity.class);
                startActivity(intent);
        }
    }

    private void call() {
        String phone_number = addPhone.getText().toString();
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone_number));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "你禁止了拨打电话的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
