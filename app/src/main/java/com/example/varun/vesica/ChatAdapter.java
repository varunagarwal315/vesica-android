package com.example.varun.vesica;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.varun.vesica.models.Message;

import java.util.List;

/**
 * Created by varun on 24/8/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> messageList;
    private String userName;


    public ChatAdapter(List<Message> messageList, String userName) {
        this.messageList = messageList;
        this.userName= userName;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage, tvUser, tvTimer;
        public Button btnDelete;

        public ViewHolder(View view, int type) {
            super(view);
            tvMessage = (TextView)view.findViewById(R.id.tv_message);
            tvUser =(TextView)view.findViewById(R.id.tv_user);
            tvTimer=(TextView)view.findViewById(R.id.tv_timer);
        }
    }


    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        switch (viewType){
            case 0:
                View v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.layout_chat_message_other, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);
                return vh;

            default:
                View v1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_chat_message_self, parent, false);
                ViewHolder vh1 = new ViewHolder(v1, viewType);
                return vh1;
        }

    }


    @Override
    public int getItemViewType(int position) {
        if ((messageList.get(position).getSenderName()).equals(userName)) {
            return 1;
        }else return 0;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvMessage.setText(messageList.get(position).getCipherText());
        holder.tvUser.setText(messageList.get(position).getSenderName());
        final int val=holder.getAdapterPosition();

        if (messageList.get(val).getBurnable()==1
                && messageList.get(val).getBurnTime()!=0){
            //set up scheduler to delete the message!
            holder.tvMessage.setTextColor(Color.RED);

            long time=messageList.get(val).getBurnTime();
            //ALARM
            CountDownTimer mCountDownTimer = new CountDownTimer(time * 1000, 1000) {
                long time1 =messageList.get(val).getBurnTime();
                long mTimeToGo=time1*1000;
                public void onTick(long millisUntilFinished) {
                    holder.tvTimer.setText(String.valueOf(time1));
                    time1--;
                    mTimeToGo -= 1000;
                    try{
                        Log.d("message ",messageList.get(val).getCipherText());
                        messageList.get(val).setBurnTime(time1);
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
                public void onFinish() {
                    try {
                        messageList.remove(messageList.get(holder.getAdapterPosition()));
                        notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }.start();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private void showCountDownTimer(){

    }
}




//delayhandler.post(run);
//            delayhandler.postDelayed(run, messgeList.get(position).getBurnTime()*1000);
//            Observable//
//                    .interval(0, 1, TimeUnit.SECONDS).take(timer)//
//                    .subscribe(new Observer<Long>() {
//                        @Override
//                        public void onCompleted() {
//                            Log.d("DONE ","");
//                            delayhandler.post(run);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Timber.e(e, "something went wrong in TimingDemoFragment example");
//                        }
//
//                        @Override
//                        public void onNext(Long number) {
//                            Log.d("TIMING NUMBER 4", String.valueOf(timer - number));
//                            holder.tvTimer.setText(String.valueOf(timer - number));
//
//                        }
//                    });

//    final Handler delayhandler = new Handler();
//    final Integer timer = messageList.get(position).getBurnTime();
//
//    final Runnable run = new Runnable() {
//        @Override
//        public void run() {
//            messageList.remove(messageList.get(holder.getAdapterPosition()));
//            notifyDataSetChanged();
//        }
//    };
