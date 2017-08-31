package com.example.lixin.recyclervviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hua on 2017/8/30.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<String> list;
    public MyAdapter(){
        list = new ArrayList<>();
        for (int i = 0; i<30;i++){
            list.add("这是第"+i+"条");
        }
    }
    //创建布局和viewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate的时候,需要传入parent和attachToRoot==false; 使用传入三个参数的方法
        View recyclerViewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new MyViewHolder(recyclerViewitem);
    }

    public void add(int position) {

        //改变数据
        list.add(position+1,"这是新加的");
        //调用它带的方法去刷新
        notifyItemRangeChanged(position+1,getItemCount());
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRangeRemoved(position,getItemCount());
    }

    public void update(int position,String context) {
        list.remove(position);
        list.add(position,context);
        notifyItemChanged(position);
    }

    //单击事件的接口
    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    //长按事件的接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    private OnItemClickListener monItemClickListener;
    private OnItemLongClickListener monItemLongClickListener;
    //设置单击事件的接口
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        monItemClickListener = onItemClickListener;
    }
    //设置长按事件的接口
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        monItemLongClickListener = onItemLongClickListener;
    }
    //绑定数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.title.setText(list.get(position));
        if(position%2==1){
            holder.icon.setImageResource(R.mipmap.ic_launcher);
        }
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// TODO: 2017/8/31 暴露一个单击回调接口
                if (monItemClickListener!=null){
                    monItemClickListener.onItemClick(view,position);
                }
            }
        });
        holder.icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (monItemLongClickListener!=null){
                    monItemLongClickListener.onItemLongClick(view,position);
                }
                // true 消费这个长按事件
                return true;
            }
        });
        //整个条目的点击
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (monItemClickListener!=null){
//                    monItemClickListener.onItemClick(view,position);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //把整个view放到viewHolder中
        View itemView;
        TextView title;
        ImageView icon;
        //findviewById 给控件绑定id
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
             title = itemView.findViewById(R.id.title);
             icon = itemView.findViewById(R.id.icon);
        }
    }
}
