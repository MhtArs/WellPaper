package tr.mht.wallpaper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tr.mht.wallpaper.R;
import tr.mht.wallpaper.model.Photo;

/**
 * Created by Administrator on 8.2.2016.
 */
public class WallpaperListAdapter extends RecyclerView.Adapter<WallpaperListAdapter.ViewHolder> {
    public static final String TAG = "WallpaperListAdapter";
    private Context mContext;
    private List<Photo> mPhotos;

    public WallpaperListAdapter(Context ctx, List<Photo> photos) {
        super();
        mContext = ctx;
        mPhotos = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallpaper, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = mPhotos.get(position);

        holder.imageTitle.setText(photo.getTitle());
        holder.imageAuthor.setText(photo.getOwnername());
        Picasso.with(mContext).load(photo.getPhotoUrl(640)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected final FrameLayout imageTextContainer;
        protected final ImageView imageView;
        protected final TextView imageTitle, imageAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            imageTextContainer = (FrameLayout) itemView.findViewById(R.id.item_image_text_container);
            imageView = (ImageView) itemView.findViewById(R.id.item_image_img);
            imageTitle = (TextView) itemView.findViewById(R.id.item_image_title);
            imageAuthor = (TextView) itemView.findViewById(R.id.item_image_author);
        }
    }
}
