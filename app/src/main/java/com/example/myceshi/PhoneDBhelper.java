package com.example.myceshi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneDBhelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    //创建数据库和表
    public PhoneDBhelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "phone.db", factory, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table phonechart(id integer primary key autoincrement,name varcher(100),phone_number varcher(36),phone_time text)");
    }

    //对phonechart表的增删改查
    //添加数据
    public boolean insertData(String name, String phone_number) {
        //设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        //获取当前系统时间，但是以外文的方式呈现的
        Date date = new Date();
        //格式化日期
        String time = sdf.format(date);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone_number", phone_number);
        contentValues.put("phone_time", time);
        long i = db.insert("phonechart", null, contentValues);
        return i > 0 ? true : false;
    }

    //删除数据，根据记录的id进行删除
    public boolean deleteData(String deleteId) {
        int i = db.delete("phonechart", "id=?", new String[]{deleteId});
        return i > 0 ? true : false;
    }

    //修改数据，根据记录的id进行更新
    public boolean updateData(String updateId, String update_number) {
        //设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        //获取当前系统时间，但是以外文的方式呈现的
        Date date = new Date();
        //格式化日期
        String time = sdf.format(date);
        //将需要更新的内容存到contentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone_number", update_number);
        contentValues.put("phone_time", time);
        int i = db.update("phonechart", contentValues, "id=?", new String[]{updateId});
        return i > 0 ? true : false;
    }

    //查询数据，查询表中所有内容，将查询内容用phonechart对象属性进行存储，并将该对象存入集合中
    public List<Phonechart> query() {
        List<Phonechart> list = new ArrayList<>();
        Cursor cursor = db.query("phonechart", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Phonechart phonechart = new Phonechart();
            phonechart.setId(String.valueOf(cursor.getInt(0)));
//            String.valueOf(cursor.getInt(0));//整数转换成字符串
            phonechart.setName(cursor.getString(1));
            phonechart.setPhone_number(cursor.getString(2));
            phonechart.setPhone_time(cursor.getString(3));
            list.add(phonechart);
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
