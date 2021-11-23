package com.example.leesunho;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.CustomViewHolder> {
    private ArrayList<RecommendList> mList = null;
    private Activity context = null;

    public RecommendListAdapter(Activity context, ArrayList<RecommendList> list){
        this.context = context;
        this.mList = list;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;

        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView)view.findViewById(R.id.recyclerview_title);

            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, Word_list.class);
                        intent.putExtra("title", mList.get(pos).getTitle());

                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewitem_recommend,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.title.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
