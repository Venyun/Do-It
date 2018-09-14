package com.doit.doit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import db.Plan;

/**
 * Created by Venny on 2017/11/7.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> planList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Plan plan=planList.get(position);
        holder.planTitle.setText(plan.getPlanTitle());
        holder.planContent.setText(plan.getPlanContent());
        holder.planDate.setText(plan.getPlanDate());
        holder.planTime.setText(plan.getPlanTime());
    }


    @Override
    public int getItemCount() {
        return planList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView planTitle;
        TextView planContent;
        TextView planDate;
        TextView planTime;

        public ViewHolder(View view){
            super(view);
            planTitle= (TextView) view.findViewById(R.id.plan_title);
            planContent= (TextView) view.findViewById(R.id.plan_content);
            planDate= (TextView) view.findViewById(R.id.plan_date);
            planTime= (TextView) view.findViewById(R.id.study_time);
        }
    }
    public PlanAdapter(List<Plan> planList){
        this.planList=planList;
    }

}
