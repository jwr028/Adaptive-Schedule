package com.example.shoppinglist.Adapter;

import com.example.shoppinglist.CreateListActivity;
import com.example.shoppinglist.R;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder> {
    private List<listModel> checkList;
    private CreateListActivity activity;

    public listAdapter(CreateListActivity activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        listModel item = checkList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
    }

    public int getItemCount(){
        return checkList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setTasks(List<listModel>, checkList){
        this.checkList = checkList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewByID(R.id.listCheckBox);
        }
    }


}
