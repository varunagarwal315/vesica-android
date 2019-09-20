package com.example.varun.vesica;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.varun.vesica.database.DbBriteHelper;
import com.example.varun.vesica.database.DbConstants;
import com.example.varun.vesica.database.DbOpenHelper;
import com.example.varun.vesica.database.tables.ConversationsTable;
import com.example.varun.vesica.models.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.varun.vesica.database.DbConstants.SELECT_ALL_FROM;
import static com.example.varun.vesica.database.DbConstants.WHERE;

/**
 * Created by varun on 20/10/16.
 */
public class NewConversationAdapter extends RecyclerView.Adapter<NewConversationAdapter.ViewHolder> {

    private Context context;
    private List<Contact> contactList= new ArrayList<>();
    private static final String TAG="NewConversationAdapter";

    public NewConversationAdapter(List<Contact> contactList, Context context){
        this.context= context;
        this.contactList= contactList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_username)TextView tvUsername;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public NewConversationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_new_conversation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewConversationAdapter.ViewHolder holder, int pos) {
        //Show display name if available?
        holder.tvUsername.setText(contactList.get(pos).getUserName());
        holder.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConversation(contactList.get(holder.getAdapterPosition()).getUserName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void startConversation(String name){
        SQLiteDatabase db= new DbOpenHelper(context).getReadableDatabase();
        String query = SELECT_ALL_FROM
                + ConversationsTable.CONVERSATION_TABLE_NAME
                + WHERE + ConversationsTable.CONVERSATION_TABLE_KEY_NAME + "='" + name + "'";
        Cursor c= db.rawQuery(query, null);

        if (c.moveToFirst()){
            //User exists
            Log.d(TAG, "conversation exists");
            Intent intent=new Intent(context, ConversationActivity.class);
            intent.putExtra("PkId",DbBriteHelper.getLong(c,DbConstants.COLUMN_ID));
            intent.putExtra("type","oldConversation");
            context.startActivity(intent);

        }else {
            Log.d(TAG, "Creating new conversation thread");
            //no such user exists
            Intent intent=new Intent(context, ConversationActivity.class);
            intent.putExtra("recipentName",name);
            intent.putExtra("type","newConversation");
            context.startActivity(intent);
        }


    }
}
