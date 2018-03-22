package cite.ansteph.beerly.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.model.BeerlyEvent;
import cite.ansteph.beerly.view.beerlylover.EstMenu;
import cite.ansteph.beerly.view.beerlylover.Profile;
import cite.ansteph.beerly.view.beerlylover.event.EventDetails;

/**
 * Created by loicstephan on 2018/02/06.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {


    private final Context mContext;

    private final RecyclerView mRecyclerView;

    ArrayList<BeerlyEvent> beerlyEvents;
    private int mCurrentItemId=0;

    public EventAdapter(Context mContext, RecyclerView mRecyclerView, ArrayList<BeerlyEvent> beerlyEvents) {
        this.mContext = mContext;
        int count  = beerlyEvents.size();

        this.beerlyEvents = new ArrayList<>(count);
        for(int i=0;i<count; i++)
        {
            addItem(i, beerlyEvents.get(i));
        }

        this.mRecyclerView = mRecyclerView;
    }

    public void addItem(int position, BeerlyEvent est) {
        final int id = mCurrentItemId++;
        // mItems.add(position, id);
        beerlyEvents.add(position,est);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        beerlyEvents.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.establismentcard, parent, false);

        return  new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder,final int position) {
        final View itemView = holder.itemView ;

        holder.txtName.setText(beerlyEvents.get(position).getName());
        holder.txtAddress.setText(beerlyEvents.get(position).getAddress());

        // holder.btnPromotion.setText("PROMOTION (" + establishments.get(position).getPromoNumber()+ ")");

        holder.btnPromotion.setText("PROMOTION");
        holder.btnPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent i = new Intent( mContext, EstMenu.class);
                //i.putExtra("Establishment", beerlyEvents.get(position) );
                //mContext.startActivity(i);
            }
        });

        holder.btnProfile.setText("DETAILS");
        holder.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( mContext, EventDetails.class);
                i.putExtra("profile", beerlyEvents.get(position) );

                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beerlyEvents.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{


        public final TextView txtName;
        public final TextView txtType;
        public final TextView txtAddress;

        public final ImageView estLogo;

        public final Button btnProfile;
        public final Button btnPromotion;


        public EventViewHolder(View itemView) {
            super(itemView);

            this.txtName = (TextView) itemView.findViewById(R.id.txtname);
            this.txtType = (TextView) itemView.findViewById(R.id.txttype);
            this.txtAddress = (TextView) itemView.findViewById(R.id.txtaddress);

            this.estLogo = (ImageView) itemView.findViewById(R.id.imgLogo);
            this.btnProfile = (Button) itemView.findViewById(R.id.btnProfile);
            this.btnPromotion = (Button) itemView.findViewById(R.id.btnMenu);


        }
    }
}
