package com.danielkashin.taskorganiser.data_layer.services.preferences;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesService {

  private static final String KEY_PREF_NAME = "TASKS_PREFERENCES";

  private static final String KEY_LAST_EMAIL = "KEY_LAST_EMAIL";

  private static final String KEY_CURRENT_EMAIL = "KEY_CURRENT_EMAIL";

  private static final String KEY_TOKEN = "KEY_TOKEN";

  private static final String KEY_LAST_SYNC = "KEY_LAST_SYNC";


  private SharedPreferences sharedPreferences;


  public PreferencesService(Context context) {
    sharedPreferences = context.getApplicationContext()
        .getSharedPreferences(KEY_PREF_NAME, MODE_PRIVATE);
  }

  public void saveNewUser(String email, String token) {
    onDeleteLastUser();

    if (sharedPreferences != null) {
      sharedPreferences.edit()
          .putString(KEY_CURRENT_EMAIL, email)
          .putString(KEY_TOKEN, token)
          .apply();
    }
  }

  public void saveLastSync(long lastSync) {
    if (sharedPreferences != null) {
      sharedPreferences.edit()
          .putLong(KEY_LAST_SYNC, lastSync)
          .apply();
    }
  }

  public long getLastSyncTimestamp() {
    if (sharedPreferences != null && sharedPreferences.contains(KEY_LAST_SYNC)) {
      return sharedPreferences.getLong(KEY_LAST_SYNC, -1);
    } else {
      return -1;
    }
  }

  public String getCurrentEmail() {
    if (sharedPreferences != null && sharedPreferences.contains(KEY_CURRENT_EMAIL)) {
      return sharedPreferences.getString(KEY_CURRENT_EMAIL, "");
    } else {
      return "";
    }
  }

  public void onDeleteLastUser() {
    if (sharedPreferences != null) {
      String lastEmail = "";

      if (sharedPreferences.contains(KEY_CURRENT_EMAIL)) {
        lastEmail = sharedPreferences.getString(KEY_CURRENT_EMAIL, "");
      }

      if (!lastEmail.equals("")) {
        sharedPreferences.edit()
            .putString(KEY_LAST_EMAIL, lastEmail)
            .apply();
      }

      if (sharedPreferences.contains(KEY_TOKEN)) {
        sharedPreferences.edit()
            .remove(KEY_TOKEN)
            .apply();
      }
      if (sharedPreferences.contains(KEY_CURRENT_EMAIL)) {
        sharedPreferences.edit()
            .remove(KEY_CURRENT_EMAIL)
            .apply();
      }

    }
  }


}
