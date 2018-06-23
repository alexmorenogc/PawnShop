package es.ulpgc.eite.clean.mvp.sample.home;

import android.content.Context;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;

public interface Home {


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface State {

  }

  interface ToHome extends State {
    void onScreenStarted();
  }

  interface HomeTo extends State{
    Context getManagedContext();
    void destroyView();
    void onScreenResumed();
    Shop getShop();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Screen ////////////////////////////////////////////////////////////////////////

  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {
    void onButtonClicked(int selectedItemPosition);

    void startLoadShopList();
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();

    void setShopNames(ArrayList<String> names);
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    void loadShopList();
    void getShopAsync(int position);
    void getShopAsyncToMaps(int shopId);
  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    void setShopList(ArrayList<String> names);
    void setShopSelected(Shop shop);
    void setShopSelectedToMaps(Shop shop);
  }

}
