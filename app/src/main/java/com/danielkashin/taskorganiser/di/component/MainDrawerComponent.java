package com.danielkashin.taskorganiser.di.component;

import com.danielkashin.taskorganiser.di.module.MainDrawerModule;
import com.danielkashin.taskorganiser.di.scope.MainDrawerScope;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer.MainDrawerPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IMainDrawerView;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.MainDrawerActivity;

import dagger.Subcomponent;


@Subcomponent(modules = {MainDrawerModule.class})
@MainDrawerScope
public interface MainDrawerComponent {

  void inject(MainDrawerActivity mainDrawerView);

}
