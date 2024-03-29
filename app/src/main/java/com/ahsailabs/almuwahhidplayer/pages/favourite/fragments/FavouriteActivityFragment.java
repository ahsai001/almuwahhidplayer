package com.ahsailabs.almuwahhidplayer.pages.favourite.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.ahsailabs.alcore.core.BaseFragment;
import com.ahsailabs.alcore.core.BaseRecyclerViewAdapter;
import com.ahsailabs.alcore.views.CustomRecylerView;
import com.ahsailabs.almuwahhidplayer.R;
import com.ahsailabs.almuwahhidplayer.events.FavFABEvent;
import com.ahsailabs.almuwahhidplayer.events.PlayThisEvent;
import com.ahsailabs.almuwahhidplayer.events.PlayThisListEvent;
import com.ahsailabs.almuwahhidplayer.pages.favourite.FavouriteActivity;
import com.ahsailabs.almuwahhidplayer.pages.favourite.adapters.FavouriteAdapter;
import com.ahsailabs.almuwahhidplayer.pages.favourite.models.FavouriteModel;
import com.ahsailabs.alutils.CommonUtil;
import com.ahsailabs.alutils.EventsUtil;
import com.ahsailabs.alutils.SwipeRefreshLayoutUtil;
import com.ahsailabs.alutils.ViewBindingUtil;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavouriteActivityFragment extends BaseFragment {
    ViewBindingUtil viewBindingUtil;
    SwipeRefreshLayoutUtil swipeRefreshLayoutUtil;
    FavouriteAdapter favouriteAdapter;
    List<FavouriteModel> favouriteModelList;
    String playlistName;
    String playlistID;

    String folderlistId;
    String folderlistName;
    public FavouriteActivityFragment() {
    }


    public FavouriteActivityFragment setPlayListName(String playListID, String playListName){
        getBundle().putString("playlistId", playListID);
        getBundle().putString("playlistName", playListName);
        return this;
    }

    public FavouriteActivityFragment setFolderListName(String folderlistId, String folderlistName){
        getBundle().putString("folderlistId", folderlistId);
        getBundle().putString("folderlistName", folderlistName);
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        favouriteModelList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(favouriteModelList);

        EventsUtil.register(this);

    }

    @Override
    public void onDestroy() {
        EventsUtil.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(FavFABEvent event){
        EventBus.getDefault().post(new PlayThisListEvent(favouriteModelList));
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewBindingUtil = viewBindingUtil.initWithParentView(view);
        playlistID = CommonUtil.getStringFragmentArgument(getArguments(),"playlistId", "");
        playlistName = CommonUtil.getStringFragmentArgument(getArguments(),"playlistName", "");
        folderlistId = CommonUtil.getStringFragmentArgument(getArguments(),"folderlistId", "");
        folderlistName = CommonUtil.getStringFragmentArgument(getArguments(),"folderlistName", "");
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(enter?MoveAnimation.LEFT:MoveAnimation.RIGHT,enter,500);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayoutUtil = swipeRefreshLayoutUtil.init(viewBindingUtil.getSwipeRefreshLayout(R.id.favourite_refreshLayout), new Runnable() {
            @Override
            public void run() {
                loadDB();
            }
        });

        CustomRecylerView recylerView = (CustomRecylerView) viewBindingUtil.getViewWithId(R.id.favourite_recylerView);
        recylerView.init();
        recylerView.setEmptyView(viewBindingUtil.getViewWithId(R.id.favourite_empty_view));
        recylerView.setAdapter(favouriteAdapter);

        favouriteAdapter.addOnChildViewClickListener(new BaseRecyclerViewAdapter.OnChildViewClickListener() {
            @Override
            public void onClick(View view, Object o, int i) {
                FavouriteModel selectedItem = (FavouriteModel)o;
                EventBus.getDefault().post(new PlayThisEvent(selectedItem));
                getActivity().finish();
            }

            @Override
            public void onLongClick(View view, Object dataModel, int position) {

            }
        });

        swipeRefreshLayoutUtil.refreshNow();
        ((FavouriteActivity)getActivity()).fab.setVisibility(View.VISIBLE);
        ((FavouriteActivity)getActivity()).getSupportActionBar().setTitle(playlistName);
    }

    private void loadQudsQidsIndexList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cards/"+folderlistId+"/list/"+playlistID+"/list").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<FavouriteModel> qudsQidsIndexList = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        Map<String, Object> data = snapshot.getData();
                        FavouriteModel item = new FavouriteModel();
                        item.setName((String) data.get("name"));
                        item.setPlaylist(playlistName);
                        item.setNumber((String) data.get("number"));
                        qudsQidsIndexList.add(item);
                    }

                    favouriteModelList.clear();
                    favouriteModelList.addAll(qudsQidsIndexList);
                    favouriteAdapter.notifyDataSetChanged();
                }

                swipeRefreshLayoutUtil.refreshDone();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                swipeRefreshLayoutUtil.refreshDone();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                swipeRefreshLayoutUtil.refreshDone();
            }
        });
    }

    private void loadDB(){
        List<FavouriteModel> dataList = FavouriteModel.getAllFromPlayList(playlistName);
        if (dataList != null && dataList.size() > 0) {
            favouriteModelList.clear();
            favouriteModelList.addAll(dataList);
            favouriteAdapter.notifyDataSetChanged();
            swipeRefreshLayoutUtil.refreshDone();
        } else {
            loadQudsQidsIndexList();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favourite,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_play_this_list){
            EventBus.getDefault().post(new PlayThisListEvent(favouriteModelList));
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
