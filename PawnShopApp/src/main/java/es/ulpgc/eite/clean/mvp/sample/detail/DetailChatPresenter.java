package es.ulpgc.eite.clean.mvp.sample.detail;


import android.content.Context;
import android.util.Log;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.GenericPresenter;
import es.ulpgc.eite.clean.mvp.sample.app.Mediator;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.chat.Chat;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;

public class DetailChatPresenter
    extends GenericPresenter
        <DetailChat.PresenterToView, DetailChat.PresenterToModel, DetailChat.ModelToPresenter, DetailChatModel>
    implements DetailChat.ViewToPresenter, DetailChat.ModelToPresenter, DetailChat.DetailTo, DetailChat.ToDetail {

  private boolean shopClicked;
  private boolean calendarClicked;
  private boolean mapsClicked;
  //State
  private Shop shop;
  private boolean hideToolbar;

  /**
   * Operation called during VIEW creation in {@link GenericActivity#onResume(Class, Object)}
   * Responsible to initialize MODEL.
   * Always call {@link GenericPresenter#onCreate(Class, Object)} to initialize the object
   * Always call  {@link GenericPresenter#setView(ContextView)} to save a PresenterToView reference
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onCreate(DetailChat.PresenterToView view) {
    super.onCreate(DetailChatModel.class, this);
    setView(view);
    Log.d(TAG, "calling onCreate()");

    Log.d(TAG, "calling startingScreen()");
    Mediator.Lifecycle mediator = (Mediator.Lifecycle) getApplication();
    mediator.startingDetailScreen(this);
  }

  /**
   * Operation called by VIEW after its reconstruction.
   * Always call {@link GenericPresenter#setView(ContextView)}
   * to save the new instance of PresenterToView
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onResume(DetailChat.PresenterToView view) {
    setView(view);
    Log.d(TAG, "calling onResume()");

    if(configurationChangeOccurred()) {
      checkVisibility();
    }
  }

  /**
   * Helper method to inform Presenter that a onBackPressed event occurred
   * Called by {@link GenericActivity}
   */
  @Override
  public void onBackPressed() {
    Log.d(TAG, "calling onBackPressed()");

    Log.d(TAG, "calling backToPreviousScreen()");
    Mediator.Navigation mediator = (Mediator.Navigation) getApplication();
    mediator.backToMasterScreen(this);
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

  /**
   * Llamado por el mediador para actualizar el estado del maestro en caso de borrado
   *
   * @return item a ser eliminado de la lista del maestro
   */
  @Override
  public ShopItem getItem() {
    return getModel().getItem();
  }

  /**
   * Llamado ante una acción de borrado.
   * En este caso, además de notificar al maestro del nuevo estado es necesario finalizar el detalle
   */

  @Override
  public void onNextClicked(ShopItem item) {
    getModel().getNextItem();
  }

  @Override
  public void onPrevClicked(ShopItem item) {
    getModel().getPrevItem();

  }

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


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////



  ///////////////////////////////////////////////////////////////////////////////////
  // To Chat //////////////////////////////////////////////////////////////////////

  @Override
  public void onScreenStarted() {
    Log.d(TAG, "calling onScreenStarted()");
    setCurrentState();
    checkVisibility();
  }

  @Override
  public void setShop(Shop shop) {
    this.shop = shop;
  }

  @Override
  public void onScreenResumed() {
    Log.d(TAG, "calling onScreenResumed()");
    setCurrentState();
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

  @Override
  public void setItem(ShopItem item) {
    getModel().setItem(item);
  }

  @Override
  public void reloadView() {
    if (isViewRunning()){
      getView().setNewItem();
    }
  }

  @Override
  public void setToolbarVisibility(boolean visible) {
    hideToolbar = !visible;
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Detail To Master //////////////////////////////////////////////////////////////////////


  @Override
  public Context getManagedContext(){
    return getActivityContext();
  }

  @Override
  public void destroyView(){
    if(isViewRunning()) {
      getView().finishScreen();
    }
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
    }
  }

}
