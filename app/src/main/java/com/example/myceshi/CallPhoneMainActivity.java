package com.example.myceshi;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class CallPhoneMainActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView add;
    private PhoneAdapter phoneAdapter;
    private PhoneDBhelper phoneDBhelper;
    private List<Phonechart> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_phone_main);
        listView = findViewById(R.id.listview);
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击添加按钮，跳转进行联系人添加
                Intent intent = new Intent(CallPhoneMainActivity.this, RecordPhoneActivity.class);
//        startActivity(intent);
                //数据回传：跳转之后期望第二个页面回传数据
                startActivityForResult(intent, 1);
            }
        });
//数据初始化
        init();
        //设置列表项的点击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//当列表项被点击时对该项内容进行修改操作
                Phonechart phonechart = (Phonechart) phoneAdapter.getItem(position);
//创建意图，将用户选中的内容传递到要修改的页面
                Intent intent = new Intent(CallPhoneMainActivity.this, callActivity.class);
                String sendId = phonechart.getId();
                String sendName = phonechart.getName();
                String sendPhone = phonechart.getPhone_number();
                String sendTime = phonechart.getPhone_time();
                intent.putExtra("id", sendId);
                intent.putExtra("name", sendName);
                intent.putExtra("phone_number", sendPhone);
                intent.putExtra("phone_time", sendTime);
                startActivityForResult(intent, 1);

            }
        });
        //设置列表项的长按监听器，删除对应项内容
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //对话框删除
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(CallPhoneMainActivity.this);
                builder.setTitle("删除记录")
                        .setMessage("你确定要删除这条记录吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //确定要删除的记录是哪一条
                                Phonechart phonechart = (Phonechart) phoneAdapter.getItem(position);
                                String deleteId = phonechart.getId();

                                if (phoneDBhelper.deleteData(deleteId)) {
                                    init();
                                    Toast.makeText(CallPhoneMainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CallPhoneMainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    //查询数据库的内容，将表中的数据显示在listview上面
    private void init() {
        if (resultList != null) {
            resultList.clear();
        }
        phoneDBhelper = new PhoneDBhelper(CallPhoneMainActivity.this, "phone.db", null, 1);
        resultList = phoneDBhelper.query();
        phoneAdapter = new PhoneAdapter(CallPhoneMainActivity.this, resultList);
        listView.setAdapter(phoneAdapter);
    }

    //数据回传时自动执行的方法，用于接收回传过来的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            //说明数据的添加操作是正常执行的。数据库新增一条记录，主页中listview的内容就应该更新一下
            init();
        }
    }
}