package com.wlj.funnyphoto.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wlj.funnyphoto.Function;
import com.wlj.funnyphoto.R;

import java.util.List;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder> {

    private List<Function> functions;

    public interface OnFunctionClickListener {
        void onFunctionClick(Function function);
    }

    private OnFunctionClickListener onFunctionClickListener;

    public FunctionAdapter(List<Function> functions) {
        this.functions = functions;
    }

    public void setOnFunctionClickListener(OnFunctionClickListener onFunctionClickListener) {
        this.onFunctionClickListener = onFunctionClickListener;
    }

    @NonNull
    @Override
    public FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_function_main, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onFunctionClickListener != null) {
                    int position = (int) view.getTag();
                    onFunctionClickListener.onFunctionClick(functions.get(position));
                }
            }
        });
        FunctionViewHolder functionViewHolder = new FunctionViewHolder(itemView);
        return functionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bindView(functions.get(position));
    }

    @Override
    public int getItemCount() {
        return functions == null ? 0 : functions.size();
    }

    public static class FunctionViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_function_res;
        TextView tv_function_name;

        public FunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_function_res = itemView.findViewById(R.id.iv_function_res);
            tv_function_name = itemView.findViewById(R.id.tv_function_name);
        }

        public void bindView(Function function) {
            tv_function_name.setText(function.getName());
        }
    }

}
