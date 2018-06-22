package es.ulpgc.eite.clean.mvp.sample.detail;

import android.util.Log;

import java.util.List;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.sample.data.MasterDetailData;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;


public class DetailChatModel
    extends GenericModel<DetailChat.ModelToPresenter> implements DetailChat.PresenterToModel {

  private ShopItem item;


  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(DetailChat.ModelToPresenter presenter) {
    super.onCreate(presenter);
    Log.d(TAG, "calling onCreate()");

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
  @Override
  public ShopItem getItem() {
    return item;
  }

  @Override
  public void setItem(ShopItem item) {
    this.item = item;
  }

  @Override
  public void getNextItem() {
    List<ShopItem> list = MasterDetailData.getItemsFromDatabase();
    for (int i=0; i < list.size(); i++){
      if (list.get(i).equals(item)){
        if (i== list.size() -1){
          item = list.get(0);
          break;
        } else {
          item = list.get(i+1);
          break;
        }
      }
    }
    getPresenter().setItem(item);
    getPresenter().reloadView();
  }

  @Override
  public void getPrevItem() {
    List<ShopItem> list = MasterDetailData.getItemsFromDatabase();
    for (int i=0; i < list.size(); i++){
      if (list.get(i).equals(item)){
        if (i==0){
          item = list.get(list.size() - 1);
          break;
        } else {
          item = list.get(i-1);
          break;
        }
      }
    }
    getPresenter().setItem(item);
    getPresenter().reloadView();
  }

}
