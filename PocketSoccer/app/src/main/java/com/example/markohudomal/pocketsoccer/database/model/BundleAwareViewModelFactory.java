package com.example.markohudomal.pocketsoccer.database.model;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class BundleAwareViewModelFactory<T extends BundleAwareViewModel>
        implements ViewModelProvider.Factory {

    private Bundle bundle;
    private ViewModelProvider provider;

    public BundleAwareViewModelFactory(Bundle bundle, ViewModelProvider provider) {
        this.bundle = bundle;
        this.provider = provider;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public T create(@NonNull Class modelClass) {
        T viewModel = (T) provider.get(modelClass);
        viewModel.readFrom(bundle);
        return viewModel;
    }
}
