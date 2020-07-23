package com.utrm.firebasecrud;

import com.google.firebase.database.FirebaseDatabase;

public class MyFireBase extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
