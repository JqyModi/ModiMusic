package com.xfdream.music.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xfdream.music.R;
import com.xfdream.music.data.SystemSetting;
import com.xfdream.music.util.CircleIndicator;
import com.xfdream.music.util.QuickTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dream on 16/6/6.
 */
public class GuideActivity extends BaseActivity {
    private static final String TAG = "GuideActivity-----";

    //图片资源编号列表
    private List<Integer> imageList;
    //存放具体的视图列表
    private List<ImageView> imageViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //配置布局
        setContentView(R.layout.activity_guide);

        //取消显示标题栏
        //setTitleVisable(false);

        //沉浸状态栏
        setImmersive(true);

        //功能三步曲
        //第一步:准备资源

        //第二步:编写布局文件
        //布局容器里面有三个控件
        //第一个:ViewPager---滑动分页
        //第二个:自定义分页页码
        //第三个:跳转页码需要的按钮
        //用的布局:FrameLayout---帧布局---图层覆盖


        //第三步:代码实现功能---分为三个步骤
        //1.初始化ViewPager分页组件,同时配置Adapter数据
        initImageList();
        initView();
        //2.配置分页页码---采用自定义布局容器(采用高级UI组件开发)

        //3.添加跳转功能
    }

    private void initImageList(){
        imageList = new ArrayList<>();
        //添加图片
        imageList.add(R.mipmap.surprise_background_default);
        imageList.add(R.mipmap.surprise_background_grass);
        imageList.add(R.mipmap.surprise_background_roof);
        imageList.add(R.mipmap.surprise_background_window);

        imageViewList = new ArrayList<>();
        for (Integer idRes : imageList){
            //创建显示图片的视图
            ImageView imageView = new ImageView(this);
            imageViewList.add(imageView);
        }
    }

    private void initView(){
        //1.初始化ViewPager分页组件,同时配置Adapter数据
        //ViewPager:系统组件
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new GuideAdapter(this,imageList,imageViewList));

        //第二个:自定义分页页码
        //获取页码视图
        //CircleIndicator:第三方组件
        //组件实现原理核心:
        //第一步:初始化自定义属性
        //第二步:加载动画
        //第三步:创建圆点
        //第四步:监听ViewPager滑动,通过属性动画切换分页
        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        //绑定页码
        circleIndicator.setViewPager(viewPager);

        //第三步:添加跳转
        //监听是否滑动到了最后一页?
        final TextView tv_guide = (TextView) findViewById(R.id.tv_guide);
        tv_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行跳转
                new QuickTimer().start(new QuickTimer.OnTimeListener() {
                    public void onTimer() {
                        new SystemSetting(GuideActivity.this, true).setValue(
                                SystemSetting.KEY_ISSTARTUP, "false");
                        Intent it = new Intent(GuideActivity.this,
                                MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                }, 1000);
                //startActivity(new Intent(GuideActivity.this,MainActivity.class));
                //finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageList.size() - 1){
                    //显示
                    tv_guide.setVisibility(View.VISIBLE);
                }else {
                    //隐藏
                    tv_guide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //扩展知识点--源码分析自定义组件
        //一旦想到了自定义组件,必然离不开自定义属性(企业自定义组件开发标准)

        //
    }

    //Adapter数据
    //目的:创建并且显示每一个分页
    public static class GuideAdapter extends PagerAdapter{

        private Context context;
        private List<Integer> imageList;
        private List<ImageView> imageViewList;

        public GuideAdapter(Context context,List<Integer> imageList,List<ImageView> imageViewList){
            this.context = context;
            this.imageList = imageList;
            this.imageViewList = imageViewList;
        }

        //页码大小
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        //当前页
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        //当前的分页是不是一个View
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //创建和绑定每一个分页
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewList.get(position);
            //绑定图片资源
            imageView.setImageResource(imageList.get(position));
            //填充视图,超过的部分我就截取掉---而且图片不变形,保持原样
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //绑定视图
            container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        //销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }
    }

}
