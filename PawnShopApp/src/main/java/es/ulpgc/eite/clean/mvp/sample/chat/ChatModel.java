package es.ulpgc.eite.clean.mvp.sample.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.data.MasterDetailData;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;


public class ChatModel
    extends GenericModel<Chat.ModelToPresenter> implements Chat.PresenterToModel {


  private boolean runningTask;
  private boolean validDatabase;
  private String errorMsg;

  private static final String VALIDATABASE = "VALIDDATABASE";


  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Chat.ModelToPresenter presenter) {
    super.onCreate(presenter);
    Log.d(TAG, "calling onCreate()");
    errorMsg = "Error deleting item!";

  }

  /**
   * Called by layer PRESENTER when VIEW pass for a reconstruction/destruction.
   * Usefull for kill/stop activities that could be running on the background Threads
   *
   * @param isChangingConfiguration Informs that a change is occurring on the configuration
   */
  @Override
  public void onDestroy(boolean isChangingConfiguration) {

  }


  ///////////////////////////////////////////////////////////////////////////////////
  // Presenter To Model ////////////////////////////////////////////////////////////

  /**
   * Llamado para recuperar los elementos a mostrar en la lista.
   * Si el contenido ya ha sido fijado antes, se notificará inmediatamente al presentador y,
   * sino es el caso, la notificación se realizará al finalizar la tarea que fija este contenido
   */
  @Override
  public void loadItems() {
    SharedPreferences sharedPref = getPresenter().getManagedContext().getSharedPreferences(VALIDATABASE, Context.MODE_PRIVATE);
    validDatabase = sharedPref.getBoolean(VALIDATABASE,false);

    if(!validDatabase && !runningTask) {
      startDelayedTask();

      SharedPreferences sharedPref1 = getPresenter().getManagedContext().getSharedPreferences(VALIDATABASE, Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref1.edit();
      editor.putBoolean(VALIDATABASE,true);
      editor.apply();
    } else if(!runningTask){
      Log.d(TAG, "calling onLoadItemsTaskFinished() method");
      getPresenter().onLoadItemsTaskFinished(MasterDetailData.getItemsFromDatabase());

    } else {
      Log.d(TAG, "calling onLoadItemsTaskStarted() method");
      getPresenter().onLoadItemsTaskStarted();
    }
  }

  @Override
  public void deleteItem(ShopItem item) {
    if (MasterDetailData.getItemsFromDatabase().contains(item)){
      MasterDetailData.deleteItem(item);
    } else {
      getPresenter().onErrorDeletingItem(item);
    }
  }


  /**
   * Llamado para recuperar los elementos iniciales de la lista.
   * En este caso siempre se llamará a la tarea asíncrona
   */
  @Override
  public void reloadItems() {
    MasterDetailData.deleteAllDatabaseItems();
    validDatabase = false;
    loadItems();
  }

  @Override
  public void setDatabaseValidity(boolean valid) {
    validDatabase = valid;
  }

  @Override
  public String getErrorMessage() {
    return errorMsg;
  }

  /////////////////////////////////////////////////////////////////////////////////////

  /**
   * Llamado para recuperar los elementos a mostrar en la lista.
   * Consiste en una tarea asíncrona que retrasa un tiempo la obtención del contenido.
   * El modelo notificará al presentador cuando se inicia y cuando finaliza esta tarea.
   */
  private void startDelayedTask() {
    Log.d(TAG, "calling startDelayedTask() method");
    runningTask = true;
    Log.d(TAG, "calling onLoadItemsTaskStarted() method");
    getPresenter().onLoadItemsTaskStarted();

    // Mock Hello: A handler to delay the answer
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Log.d(TAG, "calling loadItemsFromJsonFile() method");
        MasterDetailData.loadItemsFromJsonFile
                (getPresenter().getManagedContext(), "data.json");

        runningTask = false;
        validDatabase = true;
        Log.d(TAG, "calling onLoadItemsTaskFinished() method");
        getPresenter().onLoadItemsTaskFinished(MasterDetailData.getItemsFromDatabase());
      }
    }, 1000);
  }
}
