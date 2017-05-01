package com.danielkashin.taskorganiser.data_layer.services.translate.local;

import com.danielkashin.taskorganiser.data_layer.services.base.DatabaseService;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

public class TasksLocalService extends DatabaseService implements ITasksLocalService {

  private TasksLocalService(StorIOSQLite sqLite) {
    super(sqLite);
  }

  // ---------------------------- ITasksLocalService ---------------------------------------

  //               ------------------ database languages -------------------
/*
  @Override
  public PreparedGetObject<DatabaseLanguage> getLanguage(String language) {
    return getSQLite().get()
        .object(DatabaseLanguage.class)
        .withQuery(Query.builder()
            .table(LanguageContract.TABLE_NAME)
            .where(LanguageContract.COLUMN_NAME_LANGUAGE + " = \"" + language + "\"")
            .build())
        .prepare();
  }

  @Override
  public PreparedGetObject<DatabaseLanguage> getLanguage(Long index) {
    return getSQLite().get()
        .object(DatabaseLanguage.class)
        .withQuery(Query.builder()
            .table(LanguageContract.TABLE_NAME)
            .where(LanguageContract.COLUMN_NAME_ID + " = " + index)
            .build())
        .prepare();
  }

  @Override
  public PreparedPutObject<DatabaseLanguage> putLanguage(String language) {
    return getSQLite().put()
        .object(new DatabaseLanguage(null, language))
        .prepare();
  }

  //               ---------------- database translations --------------------

  //                          ----------- delete -------------

  public PreparedDeleteObject<DatabaseTranslation> deleteTranslation(DatabaseTranslation translation) {
    return getSQLite().delete()
        .object(translation)
        .prepare();
  }

  @Override
  public PreparedDeleteByQuery deleteNonFavoriteTranslations() {
    return getSQLite().delete()
        .byQuery(DeleteQuery.builder()
            .table(TranslationContract.TABLE_NAME)
            .where(TranslationContract.COLUMN_NAME_IS_FAVOURITE + " = " + 0)
            .build()
        ).prepare();
  }

  //                           ------------ get ------------

  @Override
  public PreparedGetObject<DatabaseTranslation> getLastTranslation() {
    return getSQLite().get()
        .object(DatabaseTranslation.class)
        .withQuery(Query.builder()
            .table(TranslationContract.TABLE_NAME)
            .orderBy(TranslationContract.COLUMN_NAME_ID + " DESC")
            .limit(1)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetObject<DatabaseTranslation> getLastTranslationOfType(boolean favorite) {
    return getSQLite().get()
        .object(DatabaseTranslation.class)
        .withQuery(Query.builder()
            .table(TranslationContract.TABLE_NAME)
            .orderBy(TranslationContract.COLUMN_NAME_ID + " DESC")
            .where(TranslationContract.COLUMN_NAME_IS_FAVOURITE + " = " + (favorite ? 1 : 0))
            .limit(1)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetObject<DatabaseTranslation> getTranslation(String originalText, int languageCode) {
    return getSQLite().get()
        .object(DatabaseTranslation.class)
        .withQuery(Query.builder()
            .table(TranslationContract.TABLE_NAME)
            .where(TranslationContract.getGetTranslationSearchQuery(originalText, languageCode))
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<DatabaseTranslation> getTranslations(int offset,
                                                                       int count,
                                                                       boolean onlyFavourite,
                                                                       String searchRequest) {
    // create builder and add common search bounds
    Query.CompleteBuilder queryBuilder = Query.builder()
        .table(TranslationContract.TABLE_NAME)
        .orderBy(TranslationContract.COLUMN_NAME_ID + " DESC")
        .limit(offset, count);

    // add another bounds if needed
    String searchQuery = TranslationContract.getGetTranslationsSearchQuery(onlyFavourite, searchRequest);
    if (!searchQuery.equals("")) {
      queryBuilder = queryBuilder.where(searchQuery);
    }

    return getSQLite().get()
        .listOfObjects(DatabaseTranslation.class)
        .withQuery(queryBuilder.build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<DatabaseTranslation> getAllFavoriteTranslations() {
    return getSQLite().get()
        .listOfObjects(DatabaseTranslation.class)
        .withQuery(Query.builder()
            .table(TranslationContract.TABLE_NAME)
            .where(TranslationContract.COLUMN_NAME_IS_FAVOURITE + " =" + 1)
            .build())
        .prepare();
  }

  //                          ------------- put ------------

  @Override
  public PreparedPutCollectionOfObjects putTranslations(List<DatabaseTranslation> translations) {
    return getSQLite().put()
        .objects(translations)
        .prepare();
  }

  @Override
  public PreparedPutObject<DatabaseTranslation> putTranslation(DatabaseTranslation translation) {
    return getSQLite().put()
        .object(translation)
        .prepare();
  }

  //               ------------------ exceptions parsing -----------------------


  @Override
  public void checkDeleteResultForExceptions(DeleteResult deleteResult) throws ExceptionBundle {
    if (deleteResult.numberOfRowsDeleted() == 0) {
      throw new ExceptionBundle(ExceptionBundle.Reason.DELETE_DENIED);
    }
  }

  @Override
  public void checkPutResultForExceptions(PutResult putResult) throws ExceptionBundle {
    if (putResult.wasNotInserted() && putResult.wasNotUpdated()) {
      throw new ExceptionBundle(ExceptionBundle.Reason.PUT_DENIED);
    }
  }

  @Override
  public void checkPutResultsForExceptions(PutResults putResults) throws ExceptionBundle {
    if (putResults.numberOfInserts() == 0 && putResults.numberOfUpdates() == 0) {
      throw new ExceptionBundle(ExceptionBundle.Reason.PUT_DENIED);
    }
  }

  @Override
  public void checkInsertResultForExceptions(PutResult putResult) throws ExceptionBundle {
    if (putResult.wasNotInserted() || putResult.insertedId() == null) {
      throw new ExceptionBundle(ExceptionBundle.Reason.PUT_DENIED);
    }
  }

  @Override
  public void checkDatabaseLanguageForExceptions(DatabaseLanguage databaseLanguage) throws ExceptionBundle {
    if (databaseLanguage.getId() == null || databaseLanguage.getLanguage() == null) {
      throw new ExceptionBundle(ExceptionBundle.Reason.NULL_POINTER);
    }
  }

  @Override
  public void checkDatabaseTranslationForExceptions(DatabaseTranslation databaseTranslation) throws ExceptionBundle {
    if (databaseTranslation == null || databaseTranslation.getId() == null
        || databaseTranslation.getOriginalText() == null
        || databaseTranslation.getTranslatedText() == null
        || databaseTranslation.isFavorite() == null) {
      throw new ExceptionBundle(ExceptionBundle.Reason.NULL_POINTER);
    }
  }

  // ----------------------------------- inner types ----------------------------------------------

  public static class Factory {

    private Factory() {
    }

    public static ITasksLocalService create(StorIOSQLite sqLite) {
      return new TasksLocalService(sqLite);
    }

  }
*/
}
