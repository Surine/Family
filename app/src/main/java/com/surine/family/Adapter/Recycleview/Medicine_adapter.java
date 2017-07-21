package com.surine.family.Adapter.Recycleview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.family.EventBus.SimpleEvent;
import com.surine.family.JavaBean.Medicine;
import com.surine.family.R;
import com.surine.family.UI.AddMedicineActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by surine on 2017/5/8.
 */

public class Medicine_adapter extends RecyclerView.Adapter<Medicine_adapter.ViewHolder> {
    private Context mContext;
    private List<Medicine> mMedicineList = new ArrayList<>();

    public Medicine_adapter(Context context, List<Medicine> mMedicineList) {
        mContext = context;
        this.mMedicineList = mMedicineList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rec_two,parent,false);
        final ViewHolder viewholder= new ViewHolder(view);
        viewholder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = viewholder.getAdapterPosition();
                int id = mMedicineList.get(position).getId();
                Intent intent = new Intent(mContext,AddMedicineActivity.class);
                intent.putExtra("CARD",true);
                intent.putExtra("ID",id);
                mContext.startActivity(intent);
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
                        int id = mMedicineList.get(position).getId();
                        DataSupport.delete(Medicine.class, id);
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        mMedicineList.remove(position);
                        notifyItemRemoved(position);
                        EventBus.getDefault().post(new SimpleEvent(1,"UPDATE"));
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
        Medicine medicine = mMedicineList.get(position);
        holder.medicine_name.setText(medicine.getMedicine_name());
        holder.times.setText(medicine.getTimes()+"");
        holder.amount.setText(medicine.getAmount()+"");
        holder.s_TIME.setText(medicine.getS_time());
        holder.e_TIME.setText(medicine.getE_time());
    }

    @Override
    public int getItemCount() {
        return mMedicineList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView medicine_name;
        TextView times;
        TextView amount;
        TextView s_TIME;
        TextView e_TIME;
        CardView mCardView;

        ViewHolder(View itemView) {
            super(itemView);
            medicine_name = (TextView) itemView.findViewById(R.id.medicine_name);
            times = (TextView) itemView.findViewById(R.id.times);
            amount = (TextView) itemView.findViewById(R.id.amount);
            s_TIME = (TextView) itemView.findViewById(R.id.s_TIME);
            e_TIME = (TextView) itemView.findViewById(R.id.e_TIME);
            mCardView = (CardView) itemView.findViewById(R.id.card_heart_item);
        }
    }
}
