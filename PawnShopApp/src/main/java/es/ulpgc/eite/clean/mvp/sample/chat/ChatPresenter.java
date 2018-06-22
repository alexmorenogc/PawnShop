package es.ulpgc.eite.clean.mvp.sample.chat;


import android.content.Context;
import android.util.Log;

import java.util.List;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.GenericPresenter;
import es.ulpgc.eite.clean.mvp.sample.app.Mediator;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;

public class ChatPresenter
    extends GenericPresenter
        <Chat.PresenterToView, Chat.PresenterToModel, Chat.ModelToPresenter, ChatModel>
    implements Chat.ViewToPresenter, Chat.ModelToPresenter, Chat.ChatTo, Chat.ToChat{

  private boolean shopClicked;
  private boolean calendarClicked;
  private boolean mapsClicked;
  //State
  private Shop shop;
  private boolean hideToolbar;
  private ShopItem selectedItem;
  private ShopItem itemToDelete;
  private boolean hideProgress;

  /**
   * Operation called during VIEW creation in {@link GenericActivity#onResume(Class, Object)}
   * Responsible to initialize MODEL.
   * Always call {@link GenericPresenter#onCreate(Class, Object)} to initialize the object
   * Always call  {@link GenericPresenter#setView(ContextView)} to save a PresenterToView reference
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onCreate(Chat.PresenterToView view) {
    super.onCreate(ChatModel.class, this);
    setView(view);
    Log.d(TAG, "calling onCreate()");

    Log.d(TAG, "calling startingScreen()");
    Mediator.Lifecycle mediator = (Mediator.Lifecycle) getApplication();
    mediator.startingMasterScreen(this);
  }

  /**
   * Operation called by VIEW after its reconstruction.
   * Always call {@link GenericPresenter#setView(ContextView)}
   * to save the new instance of PresenterToView
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onResume(Chat.PresenterToView view) {
    setView(view);
    Log.d(TAG, "calling onResume()");
      Mediator.Lifecycle mediator = (Mediator.Lifecycle) getApplication();
      mediator.resumingMasterScreen(this);

  }

  /**
   * Helper method to inform Presenter that a onBackPressed event occurred
   * Called by {@link GenericActivity}
   */

  @Override
  public void onBackPressed() {
    Log.d(TAG, "calling onBackPressed()");
    /*
    Log.d(TAG, "calling backToPreviousScreen()");
    Mediator.Navigation mediator = (Mediator.Navigation) getApplication();
    mediator.backToPreviousScreen(this);*/
  }

  /**
   * Hook method called when the VIEW is being destroyed or
   * having its configuration changed.
   * Responsible to maintain MVP synchronized with Activity lifecycle.
   * Called by onDestroy methods of the VIEW layer, like: {@link GenericActivity#onDestroy()}
   *
   * @param isChangingConfiguration true: configuration changing & false: being destroyed
   */
  @Override
  public void onDestroy(boolean isChangingConfiguration) {
    Log.d(TAG, "calling onDestroy()");
    super.onDestroy(isChangingConfiguration);

    if(isChangingConfiguration) {
      hideToolbar = !hideToolbar;
      Log.d(TAG, "calling onChangingConfiguration()");
    } else {
      Log.d(TAG, "calling onDestroy()");
    }
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // View To Presenter /////////////////////////////////////////////////////////////

  @Override
  public void onShopButtonClicked() {
    Log.d(TAG, "calling onShopButtonClicked()");
    shopClicked = true;

    Mediator.Navigation mediator = (Mediator.Navigation) getApplication();
    mediator.goToNextScreen(this);
  }

  @Override
  public void onMapsButtonClicked() {
    Log.d(TAG, "calling onMapsButtonClicked()");
    mapsClicked = true;

    Mediator.Navigation mediator = (Mediator.Navigation) getApplication();
    mediator.goToNextScreen(this);
  }

  @Override
  public void onCalendarButtonClicked() {
    Log.d(TAG, "calling onMapsButtonClicked()");
    calendarClicked = true;

    Mediator.Navigation mediator = (Mediator.Navigation) getApplication();
    mediator.goToNextScreen(this);
  }

  /**
   * Llamado al hacer click en uno de los elementos de la lista
   *
   * @param item seleccionado en la lista al hacer click
   */
  @Override
  public void onItemClicked(ShopItem item) {
    selectedItem = item;
    Log.d(TAG, "calling goToDetailScreen()");

    // Al haber hecho click en uno de los elementos de la lista del maestro es necesario
    // arrancar el detalle pasándole el estado inicial correspondiente que, en este caso,
    // es el item seleccionado. Será el mediador quien se encargue de obtener este estado
    // desde el maestro y pasarselo al detalle
    Mediator.Navigation mediator = (Mediator.Navigation) getApplication();
    mediator.goToDetailScreen(this);
  }


  /**
   * Llamado para restaurar el contenido inicial de la lista del maestro
   * ya que pueden haberse borrado elementos desde el detalle
   */
  @Override
  public void onRestoreActionClicked() {
    Log.d(TAG, "calling reloadItems()");
    // Llamado para restaurar el contenido inicial de la lista y su funcionamiento es
    // semejante al encargado de cargar los datos la primera vez
    getModel().reloadItems();
  }

  /**
   * Llamado desde la vista cada vez que se reinicia el maestro.
   * Esta llamada puede hacerse por giro de pantalla o por finalización del detalle pero,
   * en cualquier caso, habrá que actualizar el contenido de la lista
   */
  @Override
  public void onResumingContent() {
    Log.d(TAG, "calling loadItems()");
    // Si la tarea para la obtención del contenido de la lista ha finalizado,
    // el contenido estará disponible inmediatamente, sino habrá que esperar su finalización.
    // En cualquier caso, el presentador será notificado desde el modelo
    getModel().loadItems();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////



  ///////////////////////////////////////////////////////////////////////////////////
  // To Chat //////////////////////////////////////////////////////////////////////

  @Override
  public void onScreenStarted() {
    Log.d(TAG, "calling onScreenStarted()");
    setCurrentState();

  }

  @Override
  public void setShop(Shop shop) {
    this.shop = shop;
  }

  @Override
  public void onScreenResumed() {
    Log.d(TAG, "calling onScreenResumed()");
    setCurrentState();
    if(itemToDelete != null) {
      Log.d(TAG, "calling deleteItem()");
      getModel().deleteItem(itemToDelete);
    }
  }

  @Override
  public void setItemToDelete(ShopItem item) {
    itemToDelete = item;
  }

  @Override
  public void setDatabaseValidity(boolean valid) {
    getModel().setDatabaseValidity(valid);
  }

  @Override
  public void setToolbarVisibility(boolean visible) {
    hideToolbar = !visible;
  }

  @Override
  public Shop getShop() {
    return shop;
  }

  @Override
  public boolean isShopClicked() {
    return shopClicked;
  }

  @Override
  public boolean isCalendarClicked() {
    return calendarClicked;
  }

  @Override
  public boolean isMapsClicked() {
    return mapsClicked;
  }



  ///////////////////////////////////////////////////////////////////////////////////
  // Master To Detail //////////////////////////////////////////////////////////////////////


  @Override
  public Context getManagedContext(){
    return getActivityContext();
  }

  /**
   * Llamado por el mediador para recoger el estado a pasar al detalle
   *
   * @return item seleccionado en la lista al hacer click
   */
  @Override
  public ShopItem getSelectedItem() {
    return selectedItem;
  }

  @Override
  public boolean getToolbarVisibility() {
    return !hideToolbar;
  }

  @Override
  public void destroyView(){
    if(isViewRunning()) {
      getView().finishScreen();
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////
  // Model To Presenter //////////////////////////////////////////////////////////////

  @Override
  public void onErrorDeletingItem(ShopItem item) {
    if(isViewRunning()) {
      getView().showError(getModel().getErrorMessage());
    }
  }


  /**
   * Llamado desde el modelo cuando finaliza la tarea para la obtención del contenido de la lista
   *
   * @param items como contenido de la lista a mostrar en pantalla
   */
  @Override
  public void onLoadItemsTaskFinished(List<ShopItem> items) {
    // Una vez finaliza la tarea para la obtención del contenido de la lista,
    // hacemos desaparecer de pantalla el círculo de progreso
    // y actualizamos el contenido de la lista
    hideProgress = true;
    checkVisibility();
    getView().setRecyclerAdapterContent(items);
  }


  /**
   * Llamado desde el modelo cuando comienza la tarea para la obtención del contenido de la lista
   */
  @Override
  public void onLoadItemsTaskStarted() {
    hideProgress = false;
    checkVisibility();
  }

  ///////////////////////////////////////////////////////////////////////////////////


  private void setCurrentState() {
    Log.d(TAG, "calling setCurrentState()");
  }

  private void checkVisibility(){
    if(isViewRunning()) {
      if (hideToolbar) {
        getView().hideToolbar();
      }
      if (hideProgress) {
        getView().hideProgress();
      } else {
        getView().showProgress();
      }
    }
  }

}
