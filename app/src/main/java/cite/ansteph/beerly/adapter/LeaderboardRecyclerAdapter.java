package cite.ansteph.beerly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.model.Leaderboard;

/**
 * Created by loicstephan on 2018/03/22.
 */

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.LeadViewHolder>{

    ArrayList<Leaderboard> leaderboarditems;
    Context mContext;

    public LeaderboardRecyclerAdapter(ArrayList<Leaderboard> leaderboarditems, Context mContext) {
        this.leaderboarditems = leaderboarditems;
        this.mContext = mContext;
    }

    @Override
    public LeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);

        return new LeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeadViewHolder holder, int position) {
        holder .txtrank.setText(leaderboarditems.get(position).getRank());
        holder.txtusername.setText(leaderboarditems.get(position).getUsername());

        holder.txtsign.setText(leaderboarditems.get(position).getSignNum());
    }

    @Override
    public int getItemCount() {
        return leaderboarditems.size();
    }

    public class LeadViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public final TextView txtrank;
        public final TextView txtusername;
        public final TextView txtsign;

        public LeadViewHolder(View view) {
            super(view);
            this.mView = view;
            this.txtrank =(TextView) itemView.findViewById(R.id.txtldrRank);
            this.txtusername = (TextView)itemView.findViewById(R.id.txtldrUsername) ;

            this.txtsign = (TextView)itemView.findViewById(R.id.txtldrSign) ;

        }
    }
}
