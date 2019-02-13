package com.example.joyleap.islamstory;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

class SliderAdapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private List<Integer> picture;
    private List<String> category;
    private List<String> title;

    public SliderAdapter(Context context, List<Integer> picture, List<String> category, List<String>title) {
        this.context = context;
        this.picture = picture;
        this.category = category;
        this.title = title;
    }

    @Override
    public int getCount() {
        return picture.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView textCategory = (TextView) view.findViewById(R.id.textCategory);
        TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
        textTitle.setOnClickListener(this);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        textCategory.setText(category.get(position));
        textTitle.setText(title.get(position));
        linearLayout.setBackgroundColor(picture.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
