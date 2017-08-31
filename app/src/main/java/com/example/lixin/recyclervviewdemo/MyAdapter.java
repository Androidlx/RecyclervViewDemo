package com.example.lixin.recyclervviewdemo;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hua on 2017/8/30.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<String> list;

    HashMap<Integer, Boolean> isCheckedHashMap;


    public MyAdapter() {

        isCheckedHashMap = new HashMap<>();

        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("这是第" + i + "条");
            isCheckedHashMap.put(i, false);
        }
    }

    //创建布局和viewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerViewitem = null;
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            //inflate的时候,需要传入parent和attachToRoot==false; 使用传入三个参数的方法
            recyclerViewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_vertical, parent, false);
        } else if (layoutManager instanceof LinearLayoutManager) {
            //inflate的时候,需要传入parent和attachToRoot==false; 使用传入三个参数的方法
            recyclerViewitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        }
        return new MyViewHolder(recyclerViewitem);
    }


    //绑定数据
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText(list.get(position));
        if (position % 2 == 1) {
            holder.icon.setImageResource(R.mipmap.ic_launcher);
        }
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// TODO: 2017/8/31 暴露一个单击回调接口
                if (monItemClickListener != null) {
                    monItemClickListener.onItemClick(view, position);
                }
            }
        });
        holder.icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (monItemLongClickListener != null) {
                    monItemLongClickListener.onItemLongClick(view, position);
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

        //针对ListviewcheckBox错乱问题，解决办法是只操作数据而不操作checkBox，数据改变之后去刷新
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckedHashMap.put(position, !isCheckedHashMap.get(position));
                notifyDataSetChanged();
            }
        });
        holder.checkBox.setChecked(isCheckedHashMap.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //把整个view放到viewHolder中
        View itemView;
        TextView title;
        ImageView icon;
        CheckBox checkBox;

        //findviewById 给控件绑定id
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
    public class MyViewHolder1 extends RecyclerView.ViewHolder {

        //findviewById 给控件绑定id
        public MyViewHolder1(View itemView) {
            super(itemView);
        }
    }

    public void add(int position) {

        //改变数据
        list.add(position + 1, "这是新加的");
        //调用它带的方法去刷新
        notifyItemRangeChanged(position + 1, getItemCount());
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRangeRemoved(position, getItemCount());
    }

    public void update(int position, String context) {
        list.remove(position);
        list.add(position, context);
        notifyItemChanged(position);
    }

    //全选
    public void selectedAll() {
        Set<Map.Entry<Integer, Boolean>> entries = isCheckedHashMap.entrySet();

        //如果发现有没有选中的item,我就应该去全部选中,这个变量就应该设置成true,否则就是false
        boolean shouldSelectedAll = false;

        //这个for循环就是判断一下接下来要全部选中,还是全部不选中
        for (Map.Entry<Integer, Boolean> entry : entries) {
            Boolean value = entry.getValue();

            //如果有没选中的,那就去全部选中 ,如果发现全都选中了那就全部不选中,
            if (!value) {
                shouldSelectedAll = true;
                break;
            }
        }

        //如果shouldSelectedAll为true说明需要全部选中,
        // 如果为false说明没有没有选中的,已经是是全部选中的状态,需要全部不选中
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(shouldSelectedAll);
        }
        notifyDataSetChanged();
    }

    //反选
    public void revertSelected() {
        Set<Map.Entry<Integer, Boolean>> entries = isCheckedHashMap.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(!entry.getValue());
        }
        notifyDataSetChanged();
    }

    //单选,点击checkBox的时候只选中当前的item,在checkBox的点击事件中调用
    public void singleSelected(int postion) {
        Set<Map.Entry<Integer, Boolean>> entries = isCheckedHashMap.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(false);
        }
        isCheckedHashMap.put(postion, true);
        notifyDataSetChanged();
    }

    //单击事件的接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //长按事件的接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    private OnItemClickListener monItemClickListener;
    private OnItemLongClickListener monItemLongClickListener;

    //设置单击事件的接口
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        monItemClickListener = onItemClickListener;
    }

    //设置长按事件的接口
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        monItemLongClickListener = onItemLongClickListener;
    }
}
