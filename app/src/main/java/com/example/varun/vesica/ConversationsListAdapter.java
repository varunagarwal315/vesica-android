package com.example.varun.vesica;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.varun.vesica.models.Conversation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by varun on 15/10/16.
 */
public class ConversationsListAdapter extends RecyclerView.Adapter<ConversationsListAdapter.ViewHolder> {
    private List<Conversation> conversationList = new ArrayList<>();
    private Context context;

    public ConversationsListAdapter(List<Conversation> conversationList, Context context) {
        this.conversationList = conversationList;
        this.context =context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_user) TextView tvUser;
        @BindView(R.id.layout) LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public ConversationsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_thread, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ConversationsListAdapter.ViewHolder holder, final int position) {
        holder.tvUser.setText(String.valueOf(conversationList.get(position).getThreadPk())+" "+ conversationList.get(position).getThreadName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add bundle and fix parametres required
                Intent intent= new Intent(context, ConversationActivity.class);
                intent.putExtra("type","oldConversation");
                intent.putExtra("recipentName", conversationList.get(position).getThreadName());
                intent.putExtra("PkId", conversationList.get(position).getThreadPk());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}
