package org.codeselect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.codeselect.model.Youtube;
import org.codeselect.movieproject1.Api;
import org.codeselect.movieproject1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeMyMobile on 11-04-2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Youtube> mList;
    private LayoutInflater inflater;
    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public TrailerAdapter(Context context, List<Youtube> list) {
        this.mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
        if (list != null) {
            mList.addAll(list);
        }
    }

    public void refereshAdapter(List<Youtube> list) {
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_trailer, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, final int position) {
        Youtube youtube = getItem(position);
        if (youtube.getKey() != null) {
            Glide.with(mContext).load(Api.trailerThumbUrl(youtube.getKey()))
                                .centerCrop()
                                .into(holder.imageView);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItemClickListener != null) {
                    listItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    public Youtube getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        View root;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.trailer_thumb);
            root = itemView.findViewById(R.id.root);
        }
    }
}
