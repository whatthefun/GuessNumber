package com.example.user.guessnumber.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.user.guessnumber.MainActivity;
import com.example.user.guessnumber.R;
import java.util.List;

/**
 * Created by USER on 2017/03/27.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Boolean> isInRange;
    final private ListItemClickListener mOnListItemClickListener;

    public MyAdapter(List<Boolean> isInRange, ListItemClickListener listener) {
        this.isInRange = isInRange;
        mOnListItemClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClickListener(int clickItemIndex);
    }

    @Override public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtItemNum.setText(position + 1 + "");

        //超出範圍的將背景設為灰色
        if (!(isInRange.get(position))) {
            Log.d(position + "", isInRange.get(position).toString());
            holder.itemLayout.setBackgroundResource(R.drawable.item_frame_gray);
        } else {//還沒猜過的要是白色，邏輯上不用加這段，但沒加的話會很奇怪的一些項目變灰色，但本身還是true
            holder.itemLayout.setBackgroundResource(R.drawable.item_frame_white);
        }
        //答案在範圍內了表示被猜到，設為紅色背景
        if ((!isInRange.get(position)) && position + 1 == MainActivity.answer) {
            holder.itemLayout.setBackgroundResource(R.drawable.item_frame_red);
        }
    }

    @Override public int getItemCount() {
        return isInRange.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtItemNum;
        public LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
            txtItemNum = (TextView) itemView.findViewById(R.id.txtItemNum);

            itemLayout.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnListItemClickListener.onListItemClickListener(clickPosition);
        }
    }
}
