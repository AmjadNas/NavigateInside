package navigate.inside.Logic;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import navigate.inside.Activities.PlaceViewActivity;
import navigate.inside.Objects.Node;
import navigate.inside.R;


public class PageAdapter extends RecyclerView.Adapter {

    // view types
    private static final int LIST_ITEMS_VIEW_TYPE = 1;
    private static final int PAGE_ITEMS_VIEW_TYPE = 2;
    // fields
    private Context mContext;
    private ArrayList<Node> itemList;
    private boolean isPage;
    private int mAzimuth;

    public PageAdapter(Context context, ArrayList<Node> itemlist, boolean isPage)  {
        mContext = context;
        this.isPage = isPage;
        this.itemList = itemlist;
        mAzimuth = 0;
    }


    public class PageViewHolder extends RecyclerView.ViewHolder {
        private TextView name, direction;

        public PageViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.node_name);
            if (isPage)
                direction = (TextView) itemView.findViewById(R.id.node_direct);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.node_name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPage)
            return PAGE_ITEMS_VIEW_TYPE;
        else
            return LIST_ITEMS_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(viewType == PAGE_ITEMS_VIEW_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.place_item_view, parent, false);
            return new PageAdapter.PageViewHolder(view);
        }
        else if(viewType == LIST_ITEMS_VIEW_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.place_list_line_item, parent, false);
            return new PageAdapter.ListViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()){
            case PAGE_ITEMS_VIEW_TYPE:
                ((PageViewHolder)holder).name.setText(String.valueOf(itemList.get(position).getId()));
                ((PageViewHolder)holder).direction.setText(Node.getDirection(mAzimuth, itemList.get(position).getDirection()));
                break;
            case LIST_ITEMS_VIEW_TYPE:
                ((ListViewHolder)holder).name.setText(String.valueOf(itemList.get(position).getId()));
                ((ListViewHolder)holder).name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((PlaceViewActivity)mContext).setPage(position);
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void reBindElementAt(int mAzimuth, int index){
        this.mAzimuth = mAzimuth;
        notifyItemChanged(index);
    }
}




