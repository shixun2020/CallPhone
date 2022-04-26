package com.example.myceshi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecordPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backPhone, savePhone;
    private EditText addName, addPhone;
    private TextView title;
    private PhoneDBhelper phoneDBhelper;
    private String sendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_phone);
        init();
        //为控件设置监听
        savePhone.setOnClickListener(this);
        //根据sendId是否为空判断用户进行的是添加还是修改操作，并且相应调整页面显示的内容便于区分
        //获取意图
        Intent intent = this.getIntent();
        //若进行的是添加操作，sendid值为空，若为修改操作，sendId不为空
        sendId = intent.getStringExtra("id");
        if (sendId == null) {
            title.setText("添加数据");
        } else {
            title.setText("修改数据");
            String name = intent.getStringExtra("name");
            String phone_number = intent.getStringExtra("phone_number");
            //把原来的文本信息显示在编辑框
            addName.setText(name);
            addPhone.setText(phone_number);
        }
    }

    //获取控件对象
    private void init() {
        backPhone = findViewById(R.id.ic_back);
        savePhone = findViewById(R.id.sureadd);
        title = findViewById(R.id.title);
        addName = findViewById(R.id.add_name);
        addPhone = findViewById(R.id.add_phone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sureadd:
                //获取编辑框中输入的内容
                //判断用户进行的是添加数据还是修改数据
                if (sendId == null) {
                    String name = addName.getText().toString();
                    String phone_name = addPhone.getText().toString();
                    if (name == null || phone_name == null) {
                        Toast.makeText(RecordPhoneActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
//数据添加
                        phoneDBhelper = new PhoneDBhelper(RecordPhoneActivity.this, "phone.db", null, 1);
                        boolean flag = phoneDBhelper.insertData(name, phone_name);
                        if (flag == true) {
                            //如果添加成功将数据回传的结果码设置为2
                            setResult(2);
                            Toast.makeText(RecordPhoneActivity.this, "记录添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecordPhoneActivity.this, "记录添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    String name = addName.getText().toString();
                    String phone_number = addPhone.getText().toString();
                    if (name == null || phone_number == null) {
                        Toast.makeText(RecordPhoneActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        phoneDBhelper = new PhoneDBhelper(RecordPhoneActivity.this, "phone.db", null, 1);

                        if (phoneDBhelper.updateData(sendId, phone_number)) {
                            setResult(2);
                            Toast.makeText(RecordPhoneActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecordPhoneActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    public void myclick(View view) {//返回点击事件
        Intent intent = new Intent(RecordPhoneActivity.this, CallPhoneMainActivity.class);
        startActivity(intent);
    }

}