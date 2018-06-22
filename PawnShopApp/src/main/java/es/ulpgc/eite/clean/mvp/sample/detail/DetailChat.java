package es.ulpgc.eite.clean.mvp.sample.detail;

import android.content.Context;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;

/**
 * Created by Luis on 12/11/16.
 */

public interface DetailChat {


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface State {
  }

  interface ToDetail {
    void onScreenStarted();
    void setToolbarVisibility(boolean visible);
    void setItem(ShopItem item);
    void setShop(Shop shop);
  }

  interface DetailTo extends State{
    Context getManagedContext();
    void destroyView();
    boolean isShopClicked();
    boolean isCalendarClicked();
    boolean isMapsClicked();
    void onScreenResumed();
    ShopItem getItemToDelete();
    Shop getShop();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Screen ////////////////////////////////////////////////////////////////////////

  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {
    void onShopButtonClicked();
    void onMapsButtonClicked();
    void onCalendarButtonClicked();
    ShopItem getItem();
    void onDeleteActionClicked();
    void onNextClicked(ShopItem item);
    void onPrevClicked(ShopItem item);
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();
    void hideToolbar();
    void setNewItem();
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    ShopItem getItem();
    void setItem(ShopItem item);
    void getNextItem();
    void getPrevItem();
  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    void setItem(ShopItem item);
    void reloadView();
  }

}
