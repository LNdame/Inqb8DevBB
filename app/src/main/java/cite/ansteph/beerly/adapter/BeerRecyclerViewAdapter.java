package cite.ansteph.beerly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.listener.RecyclerViewClickListener;
import cite.ansteph.beerly.model.Beer;

/**
 * Created by loicstephan on 2017/10/03.
 */

public class BeerRecyclerViewAdapter extends RecyclerView.Adapter<BeerRecyclerViewAdapter.ViewHolder> {

    RecyclerViewClickListener recyclerViewClickListener;

    private ArrayList<Beer> beers;

    Context mContext;

    public BeerRecyclerViewAdapter(RecyclerViewClickListener recyclerViewClickListener, ArrayList<Beer> beers, Context context) {
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.beers = beers;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beerlyitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder .txtbeeritem.setText(beers.get(position).getName());


       final View itemView = holder.itemView;

      /*  itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.imgBeerSelect.getDrawable().getConstantState() ==
                        mContext.getResources().getDrawable(R.mipmap.ic_beer_unselected).getConstantState()){

                    holder.imgBeerSelect.setImageResource(R.mipmap.ic_beer_selected);
                }else {
                    holder.imgBeerSelect.setImageResource(R.mipmap.ic_beer_unselected);
                }
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public final TextView txtbeeritem;
        public final ImageView imgBeerSelect;

       // public final RadioButton imgLogo;imgBeerSelect

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            this.txtbeeritem =(TextView) itemView.findViewById(R.id.txtbeeritem);
            this.imgBeerSelect = (ImageView)itemView.findViewById(R.id.imgBeerSelect) ;
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void onClick(View v) {
            int position =getLayoutPosition();
            recyclerViewClickListener.onRecyclerViewItemClicked(v, position);
        }
    }
}
