package es.ulpgc.eite.clean.mvp.sample.chat;

import android.content.Context;

import java.util.List;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;

/**
 * Created by Luis on 12/11/16.
 */

public interface Chat {


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface State {
  }

  interface ToChat extends State {
    void onScreenStarted();
    void setShop(Shop shop);
    void setDatabaseValidity(boolean valid);
    void setToolbarVisibility(boolean visible);
    Shop getShop();
    boolean getToolbarVisibility();
    ShopItem getSelectedItem();
    Context getManagedContext();
  }


  interface ChatTo extends State{
    Context getManagedContext();
    ShopItem getSelectedItem();
    boolean getToolbarVisibility();
    void destroyView();
    boolean isShopClicked();
    boolean isCalendarClicked();
    boolean isMapsClicked();
    void onScreenResumed();
    void setItemToDelete(ShopItem item);
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
    void onItemClicked(ShopItem item);
    void onRestoreActionClicked();
    void onResumingContent();
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();
    void hideProgress();
    void hideToolbar();
    void showError(String msg);
    void showProgress();
    void setRecyclerAdapterContent(List<ShopItem> items);
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    void deleteItem(ShopItem item);
    void loadItems();
    void reloadItems();
    void setDatabaseValidity(boolean valid);
    String getErrorMessage();

  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    Context getManagedContext();
    void onErrorDeletingItem(ShopItem item);
    void onLoadItemsTaskFinished(List<ShopItem> items);
    void onLoadItemsTaskStarted();
  }

}
