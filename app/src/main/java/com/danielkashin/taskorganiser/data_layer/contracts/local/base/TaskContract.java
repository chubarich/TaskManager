package com.danielkashin.taskorganiser.data_layer.contracts.local.base;



public interface TaskContract extends IdContract {

  String COLUMN_NAME_NAME = "name";

  String COLUMN_NAME_DATE = "date";

  String COLUMN_NAME_DURATION = "duration";

  String COLUMN_NAME_NOTE = "note";

  String COLUMN_NAME_DONE = "done";

  String COLUMN_NAME_IMPORTANT = "important";

  // -------------------------------- synchronization ---------------------------------------------

  String COLUMN_NAME_UUID = "uuid";

  String COLUMN_NAME_CHANGED_LOCAL = "changed_local";

  String COLUMN_NAME_DELETED_LOCAL = "deleted_local";

  String COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP = "change_or_delete_date";

}
