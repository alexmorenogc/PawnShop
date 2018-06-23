package es.ulpgc.eite.clean.mvp.sample.maps;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.GenericPresenter;
import es.ulpgc.eite.clean.mvp.sample.app.Mediator;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;

public class MapsPresenter
    extends GenericPresenter
        <Maps.PresenterToView, Maps.PresenterToModel, Maps.ModelToPresenter, MapsModel>
    implements Maps.ViewToPresenter, Maps.ModelToPresenter, Maps.MapsTo, Maps.ToMaps {

  private boolean shopClicked;
  private boolean calendarClicked;
  private boolean chatClicked;
  //State
  private Shop shop;

  /**
   * Operation called during VIEW creation in {@link GenericActivity#onResume(Class, Object)}
   * Responsible to initialize MODEL.
   * Always call {@link GenericPresenter#onCreate(Class, Object)} to initialize the object
   * Always call  {@link GenericPresenter#setView(ContextView)} to save a PresenterToView reference
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onCreate(Maps.PresenterToView view) {
    super.onCreate(MapsModel.class, this);
    setView(view);
    Log.d(TAG, "calling onCreate()");

    Log.d(TAG, "calling startingScreen()");
    Mediator.Lifecycle mediator = (Mediator.Lifecycle) getApplication();
    mediator.startingScreen(this);
  }

  /**
   * Operation called by VIEW after its reconstruction.
   * Always call {@link GenericPresenter#setView(ContextView)}
   * to save the new instance of PresenterToView
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onResume(Maps.PresenterToView view) {
    setView(view);
    Log.d(TAG, "calling onResume()");

    Mediator.Lifecycle mediator = (Mediator.Lifecycle) getApplication();
    mediator.resumingScreen(this);
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
    mediator.backToPreviousScreen(this);
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
  public void onChatButtonClicked() {
    Log.d(TAG, "calling onMapsButtonClicked()");
    chatClicked = true;

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

  @Override
  public void startLoadMarkerList() {
    getModel().loadMapMarker();
  }

  @Override
  public void changeShopSelected(String shopName) {
    getModel().loadNewShopSelected(shopName);
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////


  ///////////////////////////////////////////////////////////////////////////////////
  // To Maps //////////////////////////////////////////////////////////////////////

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
  public boolean isChatClicked() {
    return chatClicked;
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Maps To //////////////////////////////////////////////////////////////////////


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


  @Override
  public void setMarkerList(ArrayList<Shop> mapShopList) {
    if (isViewRunning()){
      getView().setMarkersToMap(mapShopList);
      getView().setCenterCamera(shop);
    }
  }

  @Override
  public void setNewShopSelected(Shop shop) {
    setShop(shop);
    if (isViewRunning()){
      getView().setCenterCamera(shop);
    }
  }
}
