package es.ulpgc.eite.clean.mvp.sample.maps;

import android.content.Context;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;

public interface Maps {


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface State {
  }

  interface ToMaps extends State {
    void onScreenStarted();
    void setShop(Shop shop);
  }

  interface MapsTo extends State{
    Context getManagedContext();
    void destroyView();
    boolean isShopClicked();
    boolean isCalendarClicked();
    boolean isChatClicked();
    void onScreenResumed();
    Shop getShop();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Screen ////////////////////////////////////////////////////////////////////////

  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {
    void onShopButtonClicked();
    void onChatButtonClicked();
    void onCalendarButtonClicked();
    void startLoadMarkerList();
    void changeShopSelected(String shopName);
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();
    void setMarkersToMap(ArrayList<Shop> mapShopList);
    void setCenterCamera(Shop shop);
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    void loadMapMarker();
    void loadNewShopSelected(String shopName);
  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    void setMarkerList(ArrayList<Shop> mapShopList);
    void setNewShopSelected(Shop shop);
  }

}
