package com.example.leesunho;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
interface OnItemClickListener{
    void onReviseClick(View v, int position);
    void onDeleteClick(View v, int position);
}

public class Word_Edit_Adapter extends RecyclerView.Adapter<Word_Edit_Adapter.CustomViewHolder>{
    private ArrayList<Word> mList = null;
    private Activity context = null;

    public Word_Edit_Adapter(Activity context, ArrayList<Word> list){
        this.context = context;
        this.mList = list;
    }

    // 아이템 클릭 리스너
    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView spelling;
        protected Button btn_revise_word;
        protected Button btn_delete_word;

        public CustomViewHolder(View view) {
            super(view);
            this.spelling = (TextView)view.findViewById(R.id.recyclerview_word_spelling);
            this.btn_revise_word = (Button) view.findViewById(R.id.btn_revise_word);
            this.btn_delete_word = (Button) view.findViewById(R.id.btn_delete_word);

            btn_revise_word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onReviseClick(v, position);
                        }
                    }
                }
            });

            btn_delete_word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onDeleteClick(v, position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public Word_Edit_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerviewitem_word_edit,viewGroup,false);
        Word_Edit_Adapter.CustomViewHolder viewHolder = new Word_Edit_Adapter.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Word_Edit_Adapter.CustomViewHolder viewholder, int position) {
        viewholder.spelling.setText(mList.get(position).getSpelling());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
