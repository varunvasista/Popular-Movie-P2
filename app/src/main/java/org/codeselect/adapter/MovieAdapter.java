package org.codeselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.codeselect.model.Movie;
import org.codeselect.movieproject1.Api;
import org.codeselect.movieproject1.R;

import java.util.List;

/**
 * Created by CodeMyMobile on 06-02-2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private List<Movie> mList;

    public MovieAdapter(Context context, List<Movie> list) {
        super(context, 0, list);
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Movie getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_movie_grid, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);

        if (movie.getTitle() != null && !movie.getTitle().isEmpty()) {
            holder.title.setText(movie.getTitle());
        }else{
            holder.title.setText(Api.NOT_AVAILABLE);

        }

            Glide.with(mContext).load(Api.IMAGE_BASE_URL + getItem(position).getImage())
                    .error(R.drawable.no_thumb)
                    .placeholder(R.drawable.no_thumb)
                    .into(holder.imageView);

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView title;
        public ViewHolder(View rootView) {
            imageView = (ImageView) rootView.findViewById(R.id.thumb);
            title = (TextView) rootView.findViewById(R.id.title);
        }
    }
}
