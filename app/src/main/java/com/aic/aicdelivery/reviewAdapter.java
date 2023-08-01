package com.aic.aicdelivery;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.MyViewHolder>  {
    Context ctx;
    ArrayList<reviewVO> messageList = new ArrayList<reviewVO>();
    private LayoutInflater layoutInflator;
    private HMCoreData myDB;
    private int cuslayout;
    private ProgressBar progressBar4;
    private HMAppVariables hmAppVariables=new HMAppVariables();

    public reviewAdapter(Context context,ArrayList<reviewVO> items,int cuslayout) throws HMOwnException {
        this.ctx=context;
        this.messageList=items;
        this.cuslayout=cuslayout;
        myDB = new HMCoreData(context);
        hmAppVariables=myDB.getUserData();
    }

    @NonNull
    @Override
    public reviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=layoutInflator.inflate(cuslayout,parent,false);

        Log.i("Debugging","Item Created in View Holder");
        return new reviewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewAdapter.MyViewHolder holder, int position) {

        final reviewVO review = messageList.get(position);
        if (review.getImageUrl() != null) {
            Picasso.with(ctx)
                    .load(review.getImageUrl())
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .noFade()
                    .into(holder.listImage);
        }
        holder.outlet.setText(review.getOutletname());
        holder.category.setText(review.getServicecategory());
        holder.ratingnum.setRating(review.getStarrating());
        holder.ratingnumx.setRating(review.getStarrating());
        holder.message120.setText(review.getReviewtext());

        holder.userid.setText(review.getCustomer());
        holder.usercity.setText(review.getCity());
        holder.mday.setText(review.getReviewdate());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mday,message120,outlet,category,userid,usercity;
        private CardView cardview;
        private RatingBar ratingnum,ratingnumx;
        private CircleImageView listImage;
        private RelativeLayout section1,section2;
        int lwidth;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardview=(CardView) itemView.findViewById(R.id.card_view);
            mday=(TextView) itemView.findViewById(R.id.txtdate);
            message120=(TextView) itemView.findViewById(R.id.message120);
            outlet=(TextView) itemView.findViewById(R.id.outlet);
            category=(TextView) itemView.findViewById(R.id.category);
            userid=(TextView) itemView.findViewById(R.id.userid);
            usercity=(TextView) itemView.findViewById(R.id.usercity);
            ratingnum=(RatingBar) itemView.findViewById(R.id.ratingnum);
            ratingnumx=(RatingBar) itemView.findViewById(R.id.ratingnumx);
            listImage=(CircleImageView) itemView.findViewById(R.id.listImage);
            progressBar4=(ProgressBar) itemView.findViewById(R.id.progressBar4);
            section1=(RelativeLayout) itemView.findViewById(R.id.section1);
            section2=(RelativeLayout) itemView.findViewById(R.id.section2);
            lwidth=HMConstants.screenWidth/2;
            section1.getLayoutParams().width = lwidth;
            section1.requestLayout();
            section2.getLayoutParams().width = lwidth;
            section2.requestLayout();

        }
    }

}
