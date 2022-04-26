package com.example.myceshi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhoneAdapter extends BaseAdapter {
    //使用list<Peoplechart>,list会存储Peoplechart表的所有记录
    private List<Phonechart> list;
    private LayoutInflater layoutInflater;//用户将某个布局转换为view的对象

    //当创建PhoneAdapter对象的时候，我们需要list的数据
    public PhoneAdapter(Context context,List<Phonechart> list) {
        this.list = list;
        //获取上下文中获取到layoutInflater对象
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);//list.get(position)获取到的是Phonechart对象，Phonechart对象对应这表中的某条记录
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null) {
            convertView=layoutInflater.inflate(R.layout.phoneitem,null,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
//将数据库中的内容加载到对应的控件上
        Phonechart phonechart= (Phonechart) getItem(position);
        viewHolder.i_name.setText(phonechart.getName());
        viewHolder.i_number.setText(phonechart.getPhone_number());
        return convertView;
    }

    class ViewHolder {//用于给item的视图加载数据内容
        TextView i_name, i_number;
        public ViewHolder(View view) {
i_name=view.findViewById(R.id.name);
i_number=view.findViewById(R.id.phone_number);
        }
    }
}
