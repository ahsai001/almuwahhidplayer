package com.ahsailabs.almuwahhidplayer.pages.favourite.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahsailabs.alcore.core.BaseActivity;
import com.ahsailabs.alcore.core.BaseFragment;
import com.ahsailabs.alcore.core.BaseRecyclerViewAdapter;
import com.ahsailabs.alcore.views.CustomRecylerView;
import com.ahsailabs.almuwahhidplayer.R;
import com.ahsailabs.almuwahhidplayer.pages.favourite.FavouriteActivity;
import com.ahsailabs.almuwahhidplayer.pages.favourite.adapters.FavouritePlayListAdapter;
import com.ahsailabs.almuwahhidplayer.pages.favourite.models.FavouriteModel;
import com.ahsailabs.alutils.CommonUtil;
import com.ahsailabs.alutils.SwipeRefreshLayoutUtil;
import com.ahsailabs.alutils.ViewBindingUtil;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavouritePlayListActivityFragment extends BaseFragment {
    ViewBindingUtil viewBindingUtil;
    SwipeRefreshLayoutUtil swipeRefreshLayoutUtil;
    FavouritePlayListAdapter favouriteAdapter;
    List<FavouriteModel> favouriteModelList;
    String folderlistId;
    String folderlistName;
    public FavouritePlayListActivityFragment() {
    }

    public FavouritePlayListActivityFragment setFolderListName(String folderlistId, String folderlistName){
        getBundle().putString("folderlistId", folderlistId);
        getBundle().putString("folderlistName", folderlistName);
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        favouriteModelList = new ArrayList<>();
        favouriteAdapter = new FavouritePlayListAdapter(favouriteModelList);

        favouriteAdapter.addOnChildViewClickListener(new BaseRecyclerViewAdapter.OnChildViewClickListener() {
            @Override
            public void onClick(View view, Object o, int i) {
                FavouriteModel selectedItem = (FavouriteModel)o;
                FavouriteActivityFragment nextFragment = (FavouriteActivityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("favlist");
                if(nextFragment == null){
                    nextFragment = new FavouriteActivityFragment();
                }
                nextFragment.setPlayListName(selectedItem.getPlaylist(), selectedItem.getPlaylistName()).setFolderListName(folderlistId, folderlistName).saveAsArgument();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction
                        .replace(R.id.fragment,nextFragment,"favlist").addToBackStack("favlist").commit();

                manager.executePendingTransactions();

            }

            @Override
            public void onLongClick(View view, Object dataModel, int position) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ((BaseActivity)getActivity()).getSupportActionBar().setTitle(folderlistName+" Play List");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewBindingUtil = ViewBindingUtil.initWithParentView(view);
        folderlistId = CommonUtil.getStringFragmentArgument(getArguments(),"folderlistId", "");
        folderlistName = CommonUtil.getStringFragmentArgument(getArguments(),"folderlistName", "");
        swipeRefreshLayoutUtil = SwipeRefreshLayoutUtil.init(viewBindingUtil.getSwipeRefreshLayout(R.id.favourite_refreshLayout), new Runnable() {
            @Override
            public void run() {
                loadCloudPlayList();
            }
        });

        CustomRecylerView recylerView = (CustomRecylerView) viewBindingUtil.getViewWithId(R.id.favourite_recylerView);
        recylerView.init();
        recylerView.setEmptyView(viewBindingUtil.getViewWithId(R.id.favourite_empty_view));
        recylerView.setAdapter(favouriteAdapter);
        swipeRefreshLayoutUtil.refreshNow();
        ((FavouriteActivity)getActivity()).fab.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    private void loadCloudPlayList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cards/"+folderlistId+"/list").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<FavouriteModel> cloudPlayList = new ArrayList<>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        Map<String, Object> data = snapshot.getData();
                        FavouriteModel item = new FavouriteModel();
                        String docName = snapshot.getId();
                        item.setPlaylist(docName);
                        item.setPlaylistName(docName);
                        cloudPlayList.add(item);
                    }
                }
                loadDB(cloudPlayList);

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                loadDB(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadDB(null);
            }
        });
    }

    private void loadDB(List<FavouriteModel> cloudPlayList) {
        favouriteModelList.clear();

        if (cloudPlayList != null && cloudPlayList.size() > 0){
            favouriteModelList.addAll(cloudPlayList);
        }

        List<FavouriteModel> dataList = FavouriteModel.getPlayList();
        if(dataList != null && dataList.size() > 0) {
            favouriteModelList.addAll(dataList);
        }

        favouriteAdapter.notifyDataSetChanged();
        swipeRefreshLayoutUtil.refreshDone();
    }
}
