package com.example.authme.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory {

    private ViewModelFactory() {}

    public static <T extends ViewModel> ViewModelProvider.Factory createFor(final T model) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @SuppressWarnings("unchecked")
            @Override
            public <t extends ViewModel> t create(@NonNull Class<t> modelClass) {
                if (modelClass.isAssignableFrom(model.getClass())) {
                    return (t) model;
                }
                throw new IllegalArgumentException("unexpected model class " + modelClass);
            }
        };
    }
}
