package com.ahsailabs.almuwahhidplayer.events;

import com.ahsailabs.almuwahhidplayer.pages.favourite.models.FavouriteModel;

public class PlayThisEvent {
    private FavouriteModel data;

    public FavouriteModel getData() {
        return data;
    }

    public void setData(FavouriteModel data) {
        this.data = data;
    }

    public PlayThisEvent(FavouriteModel data) {
        this.data = data;
    }
}
