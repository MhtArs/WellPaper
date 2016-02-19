package tr.mht.wallpaper.fragment;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import tr.mht.wallpaper.R;
import tr.mht.wallpaper.WellPaperApp;
import tr.mht.wallpaper.activity.MainActivity;
import tr.mht.wallpaper.adapter.WallpaperListAdapter;
import tr.mht.wallpaper.model.Photo;
import tr.mht.wallpaper.model.PhotosResponse;
import tr.mht.wallpaper.network.WellPaperApi;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallpaperListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallpaperListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallpaperListFragment extends Fragment {
    private static final String TAG = WallpaperListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private RecyclerView imageList;
    private WallpaperListAdapter imageAdapter;
    private List<Photo> mCurrentPhotos;

    private Location location;

    private int perpage = 100;

    public WallpaperListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WallpaperListFragment.
     */
    public static WallpaperListFragment newInstance() {
        WallpaperListFragment fragment = new WallpaperListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(WallpaperListFragment.this.getActivity() instanceof MainActivity) {
            ((MainActivity) WallpaperListFragment.this.getActivity()).setOnCategoryChangedListener(new MainActivity.OnCategoryChangedListener() {
                @Override
                public void onCategoryChanged(int category) {
                    if(category == MainActivity.Category.TRENDING.id) {
                        showTrending();
                    } else if(category == MainActivity.Category.RECENT.id) {
                        showRecent();
                    } else if(category == MainActivity.Category.NEARME.id) {
                        showNearMe();
                    } else if(category == MainActivity.Category.SHUFFLE.id) {
                        shuffle();
                    }
                }
            });
        }
    }

    private void shuffle() {
        List<Photo> shuffled = mCurrentPhotos;
        Collections.shuffle(shuffled);
        updateAdapter(shuffled);
    }

    private void showRecent() {
        WellPaperApi.getApi().getRecentPhotos(1, perpage).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Response<PhotosResponse> response, Retrofit retrofit) {
                Log.d(TAG, "Response code is: " + response.code());
                if (response.body().getStat().equals("ok")) {
                    updateAdapter(response.body().getPhotos().getPhoto());
                } else {
                    Log.w(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void showTrending() {
        WellPaperApi.getApi().getInterestingPhotos(1, perpage).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Response<PhotosResponse> response, Retrofit retrofit) {
                Log.d(TAG, "Response code is: " + response.code());
                if(response.body().getStat().equals("ok")) {
                    updateAdapter(response.body().getPhotos().getPhoto());
                } else {
                    Log.w(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void showNearMe() {
        // Dolmabahce, for testing purposes.
        double lon = 41.0388293;
        double lat = 28.9995681;

        WellPaperApi.getApi().getPhotosNearMe(lon, lat, 1, perpage).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Response<PhotosResponse> response, Retrofit retrofit) {
                Log.d(TAG, "Response code is: " + response.code());
                if(response.body().getStat().equals("ok")) {
                    updateAdapter(response.body().getPhotos().getPhoto());
                } else {
                    Log.w(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallpaper_list, container, false);

        mCurrentPhotos = new ArrayList<>();

        imageList = (RecyclerView)view.findViewById(R.id.wallpaper_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(WellPaperApp.getContext());
        imageList.setLayoutManager(mLayoutManager);

        imageAdapter = new WallpaperListAdapter(getActivity(), mCurrentPhotos);
        imageList.setAdapter(imageAdapter);

        showTrending();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateAdapter(List<Photo> photos) {
        mCurrentPhotos = photos;
        imageAdapter.updatePhotos(photos);
        imageList.scrollToPosition(0);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
