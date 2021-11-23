package com.example.leesunho;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class MyWordListAdapter extends RecyclerView.Adapter<MyWordListAdapter.CustomViewHolder> {
    private ArrayList<MyWordList> mList = null;
    private Activity context = null;

    public MyWordListAdapter(Activity context, ArrayList<MyWordList> list){
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
    public MyWordListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewitem_mywordlist,viewGroup,false);
        MyWordListAdapter.CustomViewHolder viewHolder = new MyWordListAdapter.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyWordListAdapter.CustomViewHolder viewholder, int position) {
        viewholder.title.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
