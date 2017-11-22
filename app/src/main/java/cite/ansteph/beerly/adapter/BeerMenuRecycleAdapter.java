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
import cite.ansteph.beerly.model.Promotion;

/**
 * Created by loicstephan on 2017/11/11.
 */

public class BeerMenuRecycleAdapter extends RecyclerView.Adapter<BeerMenuRecycleAdapter.PrefViewHolder> {


    private ArrayList<Promotion> promotions;

    Context mContext;


    public BeerMenuRecycleAdapter(ArrayList<Promotion> promos, Context mContext) {
        this.promotions = promos;
        this.mContext = mContext;
    }

    @Override
    public BeerMenuRecycleAdapter.PrefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_item, parent, false);

        return new  BeerMenuRecycleAdapter.PrefViewHolder(view);


    }

    @Override
    public void onBindViewHolder(BeerMenuRecycleAdapter.PrefViewHolder holder, int position) {
        holder .txtbeeritem.setText(promotions.get(position).getBeer().getName());
        holder.txtPromoTitle.setText(promotions.get(position).getTitle());

        holder.txtbeerPrice.setText("R "+String.valueOf(promotions.get(position).getPrice()) );
        holder.txtPromoStart.setText(promotions.get(position).getStart_date());
        holder.txtPromoEnd.setText(promotions.get(position).getEnd_date());

        final View itemView = holder.itemView;
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public void removeItem(int position) {
        promotions.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Promotion item, int position) {
        promotions.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class PrefViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public final TextView txtbeeritem;
        public final TextView txtbeerPrice;
        public final TextView txtPromoTitle;
        public final TextView txtPromoStart;
        public final TextView txtPromoEnd;
        public PrefViewHolder(View view) {
            super(view);
            this.mView = view;
            this.txtbeeritem =(TextView) itemView.findViewById(R.id.txtbrprbeeritem);
            this.txtbeerPrice = (TextView)itemView.findViewById(R.id.txtbrPrice) ;

            this.txtPromoTitle = (TextView)itemView.findViewById(R.id.txtbrprtitle) ;
            this.txtPromoStart = (TextView)itemView.findViewById(R.id.txtbrprStartdate) ;
            this.txtPromoEnd = (TextView)itemView.findViewById(R.id.txtbrprEnddate) ;

        }
    }

}
