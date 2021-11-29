package com.example.leesunho;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.CustomViewHolder> {
    private ArrayList<Word> mList = null;
    private Activity context = null;

    public WordAdapter(Activity context, ArrayList<Word> list){
        this.context = context;
        this.mList = list;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView spelling;
        protected TextView meaning;

        public CustomViewHolder(View view) {
            super(view);
            this.spelling = (TextView)view.findViewById(R.id.recyclerview_word_spelling);
            this.meaning = (TextView)view.findViewById(R.id.recyclerview_word_meaning);
        }
    }

    @NonNull
    @Override
    public WordAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewitem_word_list,viewGroup,false);
        WordAdapter.CustomViewHolder viewHolder = new WordAdapter.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.CustomViewHolder viewholder, int position) {
        viewholder.spelling.setText(mList.get(position).getSpelling());
        viewholder.meaning.setText(mList.get(position).getMeaning());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
