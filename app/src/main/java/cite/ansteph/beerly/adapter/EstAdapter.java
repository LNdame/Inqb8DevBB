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
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.view.beerlylover.EstMenu;
import cite.ansteph.beerly.view.beerlylover.Profile;

/**
 * Created by loicstephan on 2017/10/12.
 */

public class EstAdapter extends RecyclerView.Adapter<EstAdapter.EstablishmentViewHolder> {


    private final Context mContext;

    private final RecyclerView mRecyclerView;

    ArrayList<Establishment> establishments;
    private int mCurrentItemId=0;

    public EstAdapter(Context context, RecyclerView mRecyclerView, ArrayList<Establishment> establishments) {
        this.mContext =context;
         int count  = establishments.size();
        this.establishments = new ArrayList<>(count);

        for(int i=0;i<count; i++)
        {
            addItem(i, establishments.get(i));
        }

        this.mRecyclerView = mRecyclerView;

    }


    public void addItem(int position, Establishment est) {
        final int id = mCurrentItemId++;
        // mItems.add(position, id);
        establishments.add(position,est);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        establishments.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public EstablishmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.establismentcard, parent, false);


        return new EstablishmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EstablishmentViewHolder holder, final int position) {

       final View itemView = holder.itemView ;

        holder.txtName.setText(establishments.get(position).getName());
        holder.txtAddress.setText(establishments.get(position).getAddress());

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent( mContext, EstMenu.class));
            }
        });


        holder.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( mContext, Profile.class);
                i.putExtra("profile", establishments.get(position) );

                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return establishments.size();
    }

    public static class EstablishmentViewHolder extends RecyclerView.ViewHolder{


    public final TextView txtName;
    public final TextView txtType;
    public final TextView txtAddress;

    public final ImageView estLogo;

    public final Button btnProfile;
    public final Button btnMenu;


    public EstablishmentViewHolder(View itemView) {
        super(itemView);

        this.txtName = (TextView) itemView.findViewById(R.id.txtname);
        this.txtType = (TextView) itemView.findViewById(R.id.txttype);
        this.txtAddress = (TextView) itemView.findViewById(R.id.txtaddress);

        this.estLogo = (ImageView) itemView.findViewById(R.id.imgLogo);
        this.btnProfile = (Button) itemView.findViewById(R.id.btnProfile);
        this.btnMenu = (Button) itemView.findViewById(R.id.btnMenu);


    }
}

}

/*
"-33.955239, 25.611931"
        "-26.181280, 28.038563"
        "-33.914472, 25.602237"
        "-33.955239, 25.611931"
        "-33.939309, 25.559880"
        "-33.980291, 25.590351"
        "-33.871632, 25.552545"
        "-33.97042649999999, 25.47025259999998"
        "-33.871632, 25.552545"
        "-33.9604379, 25.611579000000006"
        "-33.9626285, 25.62170530000003"
        "-33.94731, 25.55759999999998" */