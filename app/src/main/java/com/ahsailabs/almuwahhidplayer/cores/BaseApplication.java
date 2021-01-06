package com.ahsailabs.almuwahhidplayer.cores;

import android.content.Context;

import com.ahsailabs.alcore.api.APIConstant;
import com.ahsailabs.almuwahhidplayer.pages.favourite.models.FavouriteModel;
import com.ahsailabs.alutils.PrefsData;
import com.ahsailabs.sqlitewrapper.Lookup;
import com.ahsailabs.sqlitewrapper.SQLiteWrapper;

public class BaseApplication extends com.zaitunlabs.zlcore.core.BaseApplication {
    public static final String DATABASE_NAME = "qudsplayer.db";
    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteWrapper.addDatabase(new SQLiteWrapper.Database() {
            @Override
            public Context getContext() {
                return BaseApplication.this;
            }

            @Override
            public String getDatabaseName() {
                return DATABASE_NAME;
            }

            @Override
            public int getDatabaseVersion() {
                return 1;
            }

            @Override
            public void configure(SQLiteWrapper sqLiteWrapper) {
                sqLiteWrapper.addTable(new SQLiteWrapper.Table(FavouriteModel.class)
                        .addStringField("name")
                        .addStringField("number")
                        .addStringField("filename")
                        .addStringField("pathname")
                        .addStringField("playlist")
                        .enableRecordLog()
                        .addIndex("playlist"));
            }
        });

        Lookup.init(this, true);


        APIConstant.setApiAppid("6");
        //APIConstant.setApiKey("1321ffgfsdfsdfsgweegfdsgdf");
        PrefsData.setApiKey("312555553fserewrwer5435fs");
        APIConstant.setApiVersion("v1");
    }
}
