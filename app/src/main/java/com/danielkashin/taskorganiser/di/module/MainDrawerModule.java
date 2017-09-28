package com.danielkashin.taskorganiser.di.module;

import com.danielkashin.taskorganiser.di.scope.MainDrawerScope;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteDoneTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTagUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTaskUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTagUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer.MainDrawerPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IMainDrawerView;
import dagger.Module;
import dagger.Provides;


@Module
public class MainDrawerModule {

  @Provides
  @MainDrawerScope
  public IPresenterFactory<MainDrawerPresenter, IMainDrawerView> provideMainDrawerPresenterFactory(
      GetTagsUseCase getTagsUseCase, SaveTagUseCase saveTagUseCase, DeleteTagUseCase deleteTagUseCase,
      DeleteDoneTasksUseCase deleteDoneTasksUseCase, SaveTaskUseCase saveTaskUseCase,
      DeleteTaskUseCase deleteTaskUseCase) {
    return new MainDrawerPresenter.Factory(getTagsUseCase, saveTagUseCase, deleteTagUseCase,
        deleteDoneTasksUseCase, saveTaskUseCase, deleteTaskUseCase);
  }

}
