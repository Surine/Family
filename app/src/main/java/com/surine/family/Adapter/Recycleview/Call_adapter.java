package com.surine.family.Adapter.Recycleview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.JavaBean.Call;
import com.surine.family.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by surine on 2017/5/8.
 */

public class Call_adapter extends RecyclerView.Adapter<Call_adapter.ViewHolder> {
    private Context mContext;
    private List<Call> mCallList = new ArrayList<>();

    public Call_adapter(Context context, List<Call> callList) {
        mContext = context;
        mCallList = callList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_in_three,parent,false);
        final ViewHolder viewholder= new ViewHolder(view);
        viewholder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewholder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                 final int position = viewholder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("删除");
                builder.setMessage("确定删除本条数据？");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = mCallList.get(position).getId();
                        DataSupport.delete(Call.class, id);
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        mCallList.remove(position);
                        notifyItemRemoved(position);
                        EventBus.getDefault().post(new SimpleEvent(3,"UPDATE"));
                    }
                });
                builder.show();
                return true;
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Call call = mCallList.get(position);
        holder.call_date.setText(call.getCall_date());
        if(call.getCall_time().equals("00:00")) {
            holder.call_time.setText("未拨通");
            holder.call_time.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            holder.call_time.setText(call.getCall_time());
            holder.call_time.setTextColor(holder.call_date.getTextColors());
        }
    }

    @Override
    public int getItemCount() {
        return mCallList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView call_date;
        TextView call_time;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            head = (ImageView) itemView.findViewById(R.id.head);
            call_date = (TextView) itemView.findViewById(R.id.call_date);
            call_time = (TextView) itemView.findViewById(R.id.call_time);
            mCardView = (CardView) itemView.findViewById(R.id.card_call_item);
        }
    }
}
