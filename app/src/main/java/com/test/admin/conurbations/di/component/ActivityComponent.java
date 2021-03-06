package com.test.admin.conurbations.di.component;

import android.app.Activity;

import com.test.admin.conurbations.activitys.MainActivity;
import com.test.admin.conurbations.di.annotation.ActivityScope;
import com.test.admin.conurbations.di.module.ActivityModule;

import dagger.Component;

/**
 * Created by ZQiong on 2018/10/10.
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity activity);

}
