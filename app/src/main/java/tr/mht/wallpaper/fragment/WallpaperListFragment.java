package tr.mht.wallpaper.fragment;

import android.content.Context;
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

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import tr.mht.wallpaper.R;
import tr.mht.wallpaper.WellPaperApp;
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
    private ArrayList<Photo> mPhotos;

    private OnFragmentInteractionListener mListener;
    private RecyclerView imageList;
    private WallpaperListAdapter imageAdapter;

    public WallpaperListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photos photos to show
     * @return A new instance of fragment WallpaperListFragment.
     */
    public static WallpaperListFragment newInstance() {
        WallpaperListFragment fragment = new WallpaperListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallpaper_list, container, false);

        mPhotos = new ArrayList<>();

        imageList = (RecyclerView)view.findViewById(R.id.wallpaper_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(WellPaperApp.getContext());
        imageList.setLayoutManager(mLayoutManager);

        imageAdapter = new WallpaperListAdapter(WellPaperApp.getContext(), mPhotos);
        imageList.setAdapter(imageAdapter);

        WellPaperApi.getApi().getInterestingPhotos().enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Response<PhotosResponse> response, Retrofit retrofit) {
                mPhotos.addAll(response.body().getPhotos().getPhoto());
                Log.d(TAG, mPhotos.get(0).getId());
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

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
