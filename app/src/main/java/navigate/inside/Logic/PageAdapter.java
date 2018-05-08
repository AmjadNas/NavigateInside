package navigate.inside.Logic;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(holder.getItemViewType()){
            case PAGE_ITEMS_VIEW_TYPE:
                ((PageViewHolder)holder).bind(position);
                break;
            case LIST_ITEMS_VIEW_TYPE:
                ((ListViewHolder)holder).bind(position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


  /*  @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder instanceof PageViewHolder)
            ((PageViewHolder)holder).resumeVR();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder instanceof PageViewHolder)
            ((PageViewHolder)holder).pauseVR();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof PageViewHolder)
            ((PageViewHolder)holder).shutdounVR();
    }*/

    public class PageViewHolder extends RecyclerView.ViewHolder {
        private TextView name, direction;
        private ImageView image;
        private CheckBox checkBox;
       // private VrPanoramaView panoWidgetView;

        public PageViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.node_name);
            direction = (TextView) itemView.findViewById(R.id.node_direct);
            image =  (ImageView) itemView.findViewById(R.id.place_img_rec);
            checkBox = (CheckBox) itemView.findViewById(R.id.arrive_check);
           // panoWidgetView = (VrPanoramaView) itemView.findViewById(R.id.pano_view);

        }

        public void bind(int position) {

            name.setText(String.valueOf(itemList.get(position).getId()));
            direction.setText(getDirection(mAzimuth, itemList.get(position).getDirection()));
            if(itemList.get(position).getImage() != null) {
                image.setImageBitmap(itemList.get(position).getImage());
                /*VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
                viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
                panoWidgetView.loadImageFromBitmap(itemList.get(position).getImage(), viewOptions);*/
            } 
        }
       /* public void resumeVR(){
            panoWidgetView.resumeRendering();
        }
        public void pauseVR() {
            panoWidgetView.pauseRendering();
        }

        public void shutdounVR() {
            panoWidgetView.shutdown();
        }*/
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.node_name);
        }

        public void bind(final int position) {

           name.setText(String.valueOf(itemList.get(position).getId()));
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PlaceViewActivity)mContext).setPage(position);
                }
            });
        }
    }


    public void reBindElementAt(int mAzimuth, int index){
        this.mAzimuth = mAzimuth;
        notifyItemChanged(index);
    }


    private String getDirection(int mAzimuth, int direction){
            int diff = mAzimuth - direction;
            String dir = null;

            if(diff < 44 && diff > (-44) ){
                dir = mContext.getString(R.string.GoForward) + diff;
            }else if( diff < (359) && diff > (315) ){
                dir = mContext.getString(R.string.GoForward) + diff;
            }else if( diff < (-45) && diff > (-135) ){
                dir = mContext.getString(R.string.GoRight) + diff;
            }else if( diff < (315) && diff > 225 ){
                dir = mContext.getString(R.string.GoRight)+ diff;
            }else if( diff < (-135) && diff > (-225) ){
                dir = mContext.getString(R.string.TurnAround) + diff;
            }else if( diff < (225) && diff > (135) ){
                dir = mContext.getString(R.string.TurnAround) + diff;
            }else if( diff < (135) && diff > (45) ){
                dir = mContext.getString(R.string.GoLeft) + diff;
            }else if( diff < (-225) && diff > (-315) ){
                dir = mContext.getString(R.string.GoLeft)+ diff;
            }
            return dir;

    }
}




