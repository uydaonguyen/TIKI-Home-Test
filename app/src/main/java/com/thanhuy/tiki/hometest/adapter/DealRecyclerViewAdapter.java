package com.thanhuy.tiki.hometest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thanhuy.tiki.hometest.R;
import com.thanhuy.tiki.hometest.data.response.Deal;
import com.thanhuy.tiki.hometest.interfaces.RenderTimerCallback;
import com.thanhuy.tiki.hometest.interfaces.ViewHolderHelper;
import com.thanhuy.tiki.hometest.util.CountDownTimerManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * author:uy.daonguyen@gmail.com
 */
public class DealRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Deal> listDeal;
    private Context context;
    private LayoutInflater layoutInflater;

    public DealRecyclerViewAdapter(List<Deal> listData, Context context){
        if(listData == null){
            listData = new ArrayList<>();
        }
        this.listDeal = listData;
        this.context = context;
        this.layoutInflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public void notifyData(List<Deal> listData){
        if(listData == null){
            listData = new ArrayList<>();
        }
        this.listDeal = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DealViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DealViewHolder dealViewHolder = (DealViewHolder) holder;
        Deal deal = listDeal.get(position);

        Picasso.with(context)
                .load(deal.getProductThumbnail()).resize(120, 150).into(dealViewHolder.imgThumbnail);
//        Drawable thumbnailDrawable = Util.getImageFromAssetsFile(deal.getProductThumbnail());
//        dealViewHolder.imgThumbnail.setImageDrawable(thumbnailDrawable);
        dealViewHolder.tvName.setText(deal.getProductName());
        dealViewHolder.tvPrice.setText(deal.getProductPrice() + " $");
        long countDownTimer = deal.getEndDate().getTime() - deal.getStartedDate().getTime();
        CountDownTimerManager.getInstance()
                .registerRenderTime(deal.getUuid(), dealViewHolder);
        countDownTimer = CountDownTimerManager.getInstance()
                .getCacheTimerByComponentId(deal.getUuid());

        setTextCountdownTimer(dealViewHolder, countDownTimer);
    }

    private void setTextCountdownTimer(DealViewHolder dealViewHolder, long timer){
        if(dealViewHolder.countDownLayout.getVisibility() == View.GONE){
            dealViewHolder.countDownLayout.setVisibility(View.VISIBLE);
        }
        String hours = CountDownTimerManager.getHour(timer);
        String seconds = CountDownTimerManager.getSecond(timer);
        String minute = CountDownTimerManager.getMinute(timer);
        if(dealViewHolder != null){
            dealViewHolder.tvCountDownTimer.setText(String.format("%s:%s:%s", hours, minute, seconds));
        }

    }

    @Override
    public int getItemCount() {
        return listDeal.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if(holder instanceof ViewHolderHelper){
            ViewHolderHelper viewHolderHelper =
                    (ViewHolderHelper) holder;
            viewHolderHelper.onViewRecycled();
        }
        super.onViewRecycled(holder);

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder instanceof ViewHolderHelper){
            ViewHolderHelper viewHolderHelper =
                    (ViewHolderHelper) holder;
            viewHolderHelper.onViewDetachedFromWindow();
        }
    }

    class DealViewHolder extends RecyclerView.ViewHolder implements ViewHolderHelper, RenderTimerCallback {
        private RelativeLayout countDownLayout;
        private TextView tvCountDownTimer;

        private ImageView imgThumbnail;
        private TextView tvName;
        private TextView tvPrice;

        public DealViewHolder(View view) {
            super(view);
            this.countDownLayout = (RelativeLayout) view.findViewById(R.id.countDownLayout);
            this.imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
            this.tvCountDownTimer = (TextView) view.findViewById(R.id.tvCountDownTimer);
            this.tvName = (TextView) view.findViewById(R.id.tvName);
            this.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        }

        @Override
        public void onViewRecycled() {
            CountDownTimerManager.getInstance()
                    .unRegisterRenderTime(getAdapterPosition());
        }

        @Override
        public void onViewDetachedFromWindow() {

        }

        @Override
        public void renderTimer(long timer, Deal deal) {
            setTextCountdownTimer(this, timer);
            if(timer <= 0L && deal != null){
                setTextCountdownTimer(this, 0);

                listDeal.remove(deal);


                notifyDataSetChanged();
            }

        }
    }
}
