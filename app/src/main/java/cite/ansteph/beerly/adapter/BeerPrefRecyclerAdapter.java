package cite.ansteph.beerly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.model.Beer;

/**
 * Created by loicstephan on 2017/11/01.
 */

public class BeerPrefRecyclerAdapter extends RecyclerView.Adapter<BeerPrefRecyclerAdapter.PrefViewHolder>{

    private ArrayList<Beer> beers;

    Context mContext;


    public BeerPrefRecyclerAdapter(ArrayList<Beer> beers, Context mContext) {
        this.beers = beers;
        this.mContext = mContext;
    }

    @Override
    public PrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_pref_item, parent, false);

        return new  BeerPrefRecyclerAdapter.PrefViewHolder(view);


    }

    @Override
    public void onBindViewHolder(PrefViewHolder holder, int position) {
        holder .txtbeeritem.setText(beers.get(position).getName());


        final View itemView = holder.itemView;
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public void removeItem(int position) {
        beers.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Beer item, int position) {
        beers.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class PrefViewHolder extends RecyclerView.ViewHolder {


        public RelativeLayout viewBackground, viewForeground;

        public final TextView txtbeeritem;
        public final ImageView imgBeerSelect;

        public PrefViewHolder(View view) {
            super(view);
            this.txtbeeritem =(TextView) itemView.findViewById(R.id.txtbeeritem);
            this.imgBeerSelect = (ImageView)itemView.findViewById(R.id.imgBeerSelect) ;

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

}
