package com.example.markohudomal.pocketsoccer.database.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class BundleAwareViewModel extends AndroidViewModel {
    public BundleAwareViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void writeTo(Bundle bundle);

    public abstract void readFrom(Bundle bundle);
}
