package com.example.lixin.recyclervviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //http://www.jianshu.com/p/4eff036360da
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //listView的形式展示
        linearLayoutManager = new LinearLayoutManager(this);
        //设置横向还是竖向
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //gridview的形式展示,可以通过setSpanSizeLookup 来自定义每个item占的列数
        gridLayoutManager = new GridLayoutManager(this,3);

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 3 - position % 3;
//            }
//        });
        //瀑布流的形式
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //横向展示
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
       adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(View v, int position) {
               //增加和删除的话 不直接对postion做处理,因为此处的position是没有刷新以前的position
                adapter.add(position);

           }
       });
        adapter.setOnItemLongClickListener(new MyAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                //remove的操作
//                adapter.remove(position);

                //更新的操作
                adapter.update(position,"这是我更新的内容");
            }
        });
        recyclerView.addItemDecoration(new MyDecoration(this,LinearLayoutManager.VERTICAL));
    }
    public void onClick(View view){
        //切换布局
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null){
            return;
        }

        //if和elseif是有先后顺序的,先判断范围小的,然后再判断范围大的,
        //因为GridLayoutManager 是继承 LinearLayoutManager ,所以他本质上也是LinearLayoutManager,
        //所以不能先判断是否是LinearLayoutManager (LinearLayoutManager范围大)
        if (layoutManager instanceof GridLayoutManager){
            recyclerView.setLayoutManager(linearLayoutManager);
            //重新设置adapter之后，会重新走oncreateViewholder可以改变我们的布局
            recyclerView.setAdapter(adapter);
        }else if (layoutManager instanceof LinearLayoutManager){
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
    public void onClick_all(View view){
        adapter.selectedAll();
    }
    public void onClick_noall(View view){
        adapter.revertSelected();
    }
}
