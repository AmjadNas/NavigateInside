package navigate.inside.Logic;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import navigate.inside.Objects.Node;
import navigate.inside.R;


public class PageAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<Node> itemList;
    private TextView  title;

    public PageAdapter(Context context,ArrayList<Node> itemlist) {
        mContext = context;
        this.itemList = itemlist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout =  inflater.inflate(R.layout.place_item_view , container, false);

        title = (TextView)layout.findViewById(R.id.node_name);
        title.setText(String.valueOf(itemList.get(position).getId()));

        container.addView(layout);
        return layout;

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    public void updateTextView(int position, int mAzimuth) {

        itemList.get(position);
    }
}

