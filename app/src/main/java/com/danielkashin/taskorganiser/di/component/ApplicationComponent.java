package com.danielkashin.taskorganiser.di.component;

import com.danielkashin.taskorganiser.di.module.ApplicationModule;
import com.danielkashin.taskorganiser.di.module.MainDrawerModule;
import com.danielkashin.taskorganiser.di.module.NotificationModule;
import com.danielkashin.taskorganiser.di.module.TasksModule;
import com.danielkashin.taskorganiser.di.module.UseCaseModule;
import javax.inject.Singleton;
import dagger.Component;


@Component(modules = {ApplicationModule.class, NotificationModule.class, TasksModule.class, UseCaseModule.class})
@Singleton
public interface ApplicationComponent {

  MainDrawerComponent plusMainDrawerComponent(MainDrawerModule mainDrawerModule);

}
