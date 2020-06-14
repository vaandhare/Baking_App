package com.indekode.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.indekode.bakingapp.R;
import com.indekode.bakingapp.model.Steps;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private int mPosition;
    private List<Steps> list;
    private final StepsAdapter.StepAdapterClickHandler mClickHandler;
    private int checker = 10;

    public interface StepAdapterClickHandler {
        void onClickHandler(int position);
    }

    public StepsAdapter(Context context, List<Steps> list, StepAdapterClickHandler handler, int position) {
        this.list = list;
        mClickHandler = handler;
        mPosition = position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.steps_item, parent, false);

        final MyViewHolder viewHolder = new MyViewHolder(view);

        viewHolder.stepCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vi) {
                int position = viewHolder.getAdapterPosition();

                notifyDataSetChanged();

                if (checker != position) {
                    mClickHandler.onClickHandler(position);
                    checker = position;
                } else {
                    Toast.makeText(context,"You are clicking the same step ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return viewHolder;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitleSteps)
        TextView shortDescriptionView;
        @BindView(R.id.cvSteps)
        CardView stepCard;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.shortDescriptionView.setText(list.get(position).getShortDescription());

       if (mPosition == 10){
           mPosition=position;
       }else {
           ItemSelected();
       }
    }

    private void ItemSelected() {
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
