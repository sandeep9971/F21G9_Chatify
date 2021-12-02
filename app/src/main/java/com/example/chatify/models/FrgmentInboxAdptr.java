package com.example.chatify.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.R;
import com.example.chatify.UserChat;
import com.example.chatify.fragments.Frgment_Inbox;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



public class FrgmentInboxAdptr extends RecyclerView.Adapter<FrgmentInboxAdptr.MyViewHolder> {

    List<InboxModel> inboxModelListnew;
    Context context;
    Frgment_Inbox frgmentInbox;
    List<InboxModel> inboxModelList;
    public FrgmentInboxAdptr(Context context, List<InboxModel> inboxModelList, Frgment_Inbox frgmentInbox) {
        this.context = context;
        this.inboxModelList = inboxModelList;
        this.frgmentInbox = frgmentInbox;
        this.inboxModelListnew= new ArrayList<>();
        this.inboxModelListnew.addAll(inboxModelList);

Collections.sort(this.inboxModelList, new Comparator<InboxModel>() {
    @Override
    public int compare(InboxModel o1, InboxModel o2) {
        Long time1=o1.getInboxtime();
        Long time2=o2.getInboxtime();
        return time2.compareTo(time1);
    }
});
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inboxview, parent, false);
MyViewHolder myViewHolder=new MyViewHolder(v);
return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
holder.textTv.setText(inboxModelList.get(position).getOppName());
holder.textMsg.setText(inboxModelList.get(position).getLastMsg());
        SimpleDateFormat sfd = new SimpleDateFormat("h:mm aa");
        holder.tvTime.setText(sfd.format(new Date(inboxModelList.get(position).getInboxtime())));

        if(inboxModelList.get(position).getUnread_count()==0)
        {
            holder.tvUnreadCount.setVisibility(View.GONE);
        }
        else {
            holder.tvUnreadCount.setText((int) inboxModelList.get(position).getUnread_count() + "");
        }
        holder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, UserChat.class);
                intent.putExtra("frgInboxAdptr_name",inboxModelList.get(position).getOppName());
                intent.putExtra("frgInboxAdptr_img",inboxModelList.get(position).getOppImg());
                intent.putExtra("frgInboxAdptr_lstMsg",inboxModelList.get(position).getLastMsg());
                intent.putExtra("frgInboxAdptr_uid",inboxModelList.get(position).getOppUid());
                intent.putExtra("frgInboxAdptr_tokenId",inboxModelList.get(position).getTokenId());
                intent.putExtra("frgInboxAdptr_unread_count",inboxModelList.get(position).getUnread_count());
                intent.putExtra("frgInboxAdptr_chatroom",inboxModelList.get(position).getChatRoom());
                context.startActivity(intent);

            }
        });

    }
    @Override
    public int getItemCount() {
        return inboxModelList.size();
    }
    public void getFilterValue(CharSequence charsequence) {
        inboxModelList.clear();
        if(charsequence.length()==0)
        {
inboxModelList.addAll(inboxModelListnew);
        }
        else {
            for (int i = 0; i < inboxModelListnew.size(); i++) {
                if (inboxModelListnew.get(i).getOppName().toLowerCase().contains(charsequence.toString().toLowerCase())
                        || inboxModelListnew.get(i).getLastMsg().toLowerCase().contains(charsequence.toString().toLowerCase()))
                {
                    inboxModelList.add(inboxModelListnew.get(i));
                }

            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTv,textMsg,tvTime,tvUnreadCount;
ImageView userIndexImg;
RelativeLayout rl1;
        public MyViewHolder(View itemView) {
            super(itemView);
            textTv=itemView.findViewById(R.id.user_inbox_name);
            textMsg=itemView.findViewById(R.id.textmsg);
            userIndexImg=itemView.findViewById(R.id.inbox_index_img);
            tvTime=itemView.findViewById(R.id.tv_time);
            rl1=itemView.findViewById(R.id.rl1);
            tvUnreadCount=itemView.findViewById(R.id.tv_unreadcount);
        }
    }
}
