package es.ulpgc.eite.clean.mvp.sample.webshop;

import android.content.Context;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;

public interface Webshop {


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface State {
  }

  interface ToWebshop extends State {
    void onScreenStarted();
    void setShop(Shop shop);
  }

  interface WebshopTo extends State{
    Context getManagedContext();
    void destroyView();
    boolean isChatClicked();
    boolean isCalendarClicked();
    boolean isMapsClicked();
    void onScreenResumed();
    Shop getShop();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Screen ////////////////////////////////////////////////////////////////////////

  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {
    void onChatButtonClicked();
    void onMapsButtonClicked();
    void onCalendarButtonClicked();

    void setShop(Shop shop);
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {

  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {

  }

}
