package com.danielkashin.taskorganiser.presentation_layer.presenter.base;


import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

/*
 * factory that is passed to PresenterLoader that initializes presenter
 */
public interface IPresenterFactory<P extends Presenter<V>, V extends IView> {

  P create();

}