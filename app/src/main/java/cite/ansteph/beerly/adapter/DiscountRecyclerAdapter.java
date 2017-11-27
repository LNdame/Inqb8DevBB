package cite.ansteph.beerly.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.model.DiscountM;


/**
 * Created by loicstephan on 2017/11/27.
 */

public class DiscountRecyclerAdapter extends RecyclerView.Adapter<DiscountRecyclerAdapter.DiscViewHolder> {


    private ArrayList<DiscountM> discounts;

    Context mContext;

    public DiscountRecyclerAdapter(ArrayList<DiscountM> discounts, Context mContext) {
        this.discounts = discounts;
        this.mContext = mContext;
    }

    @Override
    public DiscViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activited_discount_item, parent, false);

        return new  DiscountRecyclerAdapter.DiscViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final DiscViewHolder holder, int position) {
        holder .txtEst.setText(discounts.get(position).getEstablishmentName());
        holder.txtTime.setText(discounts.get(position).getTimecreated());


        new CountDownTimer(43200000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                holder.txtTime.setText(""+String.format("%d: %d: %d ",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished) -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                holder.txtTime.setText("done!");
            }
        }.start();
    }

    @Override
    public int getItemCount() {
        return discounts.size();
    }


    public void startCountDown()
    {

    }



    public class DiscViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public final TextView txtEst;
        public final TextView txtTime;


        public DiscViewHolder(View view) {
            super(view);
            this.mView = view;
            this.txtEst =(TextView) itemView.findViewById(R.id.txtdisEstablishment);
            this.txtTime = (TextView)itemView.findViewById(R.id.txtdiscTime) ;


        }
    }

}
