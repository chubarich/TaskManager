package com.danielkashin.taskorganiser.data_layer.contracts.local.base;

import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public interface TaskContract extends IdContract {

  String COLUMN_NAME_NAME = "name";

  String COLUMN_NAME_MAIN_TIMESTAMP = "main_timestamp";

  String COLUMN_NAME_DURATION = "duration";

  String COLUMN_NAME_NOTE = "note";

  String COLUMN_NAME_GLOBAL_TASK_ID = "global_task_id";

  // -------------------------------- synchronization ---------------------------------------------

  String COLUMN_NAME_UNIQUE_ID = "unique_id";

  String COLUMN_NAME_CHANGED_LOCAL = "changed_local";

  String COLUMN_NAME_DELETED_LOCAL = "deleted_local";

  String COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP = "change_or_delete_timestamp";

}
