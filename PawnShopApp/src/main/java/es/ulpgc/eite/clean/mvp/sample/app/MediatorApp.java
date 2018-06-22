package es.ulpgc.eite.clean.mvp.sample.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.ulpgc.eite.clean.mvp.sample.calendar.Calendar;
import es.ulpgc.eite.clean.mvp.sample.calendar.CalendarView;
import es.ulpgc.eite.clean.mvp.sample.chat.Chat;
import es.ulpgc.eite.clean.mvp.sample.chat.ChatView;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;
import es.ulpgc.eite.clean.mvp.sample.detail.DetailChat;
import es.ulpgc.eite.clean.mvp.sample.detail.DetailChatView;
import es.ulpgc.eite.clean.mvp.sample.home.Home;
import es.ulpgc.eite.clean.mvp.sample.maps.Maps;
import es.ulpgc.eite.clean.mvp.sample.maps.MapsView;
import es.ulpgc.eite.clean.mvp.sample.webshop.Webshop;
import es.ulpgc.eite.clean.mvp.sample.webshop.WebshopView;
import io.realm.Realm;


public class MediatorApp extends Application implements Mediator.Lifecycle, Mediator.Navigation {

  protected final String TAG = this.getClass().getSimpleName();

  private CalendarState toCalendarState;
  private MapState toMapsState;
  private ChatState toChatState;
  private ShopState toShopState;
  private DetailState toDetailState;

  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);

    Log.d(TAG, "calling onCreate()");

    Log.d(TAG, "calling creatingInitialState()");
    }

  @Override
  public void onTerminate() {
    super.onTerminate();

    Realm.getDefaultInstance().close();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Lifecycle /////////////////////////////////////////////////////////////////////


  ///////////////HOME///////////////////////////////////////////////////////////

  @Override
  public void startingScreen(Home.ToHome presenter) {
    presenter.onScreenStarted();
  }

  @Override
  public void resumingScreen(Home.HomeTo presenter) {
    presenter.onScreenResumed();
  }


  ///////////////MAPS//////////////////////////////////////////////////////////////

  @Override
  public void startingScreen(Maps.ToMaps presenter) {
    Register.getLog().newLog("MAPAS");
    Register.getLog().showLog();
    if (toMapsState != null) {
      Log.d(TAG, "calling settingMapsState()");
      presenter.setShop(toMapsState.shop);

      Log.d(TAG, "calling removingInitialMapsState()");
      toMapsState = null;
    }
    presenter.onScreenStarted();
  }

  @Override
  public void resumingScreen(Maps.MapsTo presenter) {
    presenter.onScreenResumed();
  }


  ///////////////CHAT//////////////////////////////////////////////////////////////

  @Override
  public void startingMasterScreen(Chat.ToChat presenter) {
    Register.getLog().newLog("MAESTRO TIENDAS");
    Register.getLog().showLog();

    if (toChatState != null) {
      Log.d(TAG, "calling settingChatState()");
      presenter.setShop(toChatState.shop);
      presenter.setToolbarVisibility(!toChatState.hideToolbar);
      presenter.setDatabaseValidity(toChatState.validDatabase);
      Log.d(TAG, "calling removingInitialChatState()");
      toChatState = null;
    }
    presenter.onScreenStarted();

  }

  @Override
  public void resumingMasterScreen(Chat.ChatTo presenter) {
    presenter.onScreenResumed();
  }


  ///////////////DETAIL//////////////////////////////////////////////////////////////

  @Override
  public void startingDetailScreen(DetailChat.ToDetail presenter){
    Register.getLog().newLog("DETALLE TIENDAS");
    Register.getLog().showLog();

    if(toDetailState != null) {
      Log.d(TAG, "calling settingDetailChatState()");
      presenter.setShop(toDetailState.shop);
      presenter.setToolbarVisibility(!toDetailState.hideToolbar);
      presenter.setItem(toDetailState.selectedItem);
      toDetailState = null;
    }
    // Una vez fijado el estado inicial, el detalle puede iniciarse normalmente
    presenter.onScreenStarted();
  }

  ///////////////WEBSHOP//////////////////////////////////////////////////////////////


  @Override
  public void startingScreen(Webshop.ToWebshop presenter) {
    Register.getLog().newLog("WEBSHOP");
    Register.getLog().showLog();

    if (toShopState != null) {
      Log.d(TAG, "calling settingChatState()");
      presenter.setShop(toShopState.shop);

      Log.d(TAG, "calling removingInitialChatState()");
      toShopState = null;
    }
    presenter.onScreenStarted();
  }

  @Override
  public void resumingScreen(Webshop.WebshopTo presenter) {
    presenter.onScreenResumed();
  }


  ///////////////CALENDAR//////////////////////////////////////////////////////////////

  @Override
  public void startingScreen(Calendar.ToCalendar presenter) {
    Register.getLog().newLog("CALENDARIO");
    Register.getLog().showLog();

    if (toCalendarState != null) {
      Log.d(TAG, "calling settingCalendarState()");
      presenter.setShop(toCalendarState.shop);
      presenter.setDateView(toCalendarState.dateSelected);
      presenter.setAppointment(toCalendarState.appointment);
      presenter.setNameInputText(toCalendarState.name);
      presenter.setPhoneInputText(toCalendarState.phone);
      presenter.setMailInputText(toCalendarState.mail);
      presenter.setProductsInputText(toCalendarState.products);
      presenter.setHourSelected(toCalendarState.idHour);
      presenter.setAppointment(toCalendarState.appointment);

      Log.d(TAG, "calling removingInitialCalendarState()");
      toCalendarState = null;
    }
    presenter.onScreenStarted();
  }

  @Override
  public void resumingScreen(Calendar.CalendarTo presenter) {
    presenter.onScreenResumed();
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // Navigation ////////////////////////////////////////////////////////////////////

  ///////////////HOME//////////////////////////////////////////////////////////////

  @Override
  public void goToNextScreen(Home.HomeTo presenter) {
    toMapsState = new MapState();
    toMapsState.shop = presenter.getShop();

    Context view = presenter.getManagedContext();
    if (view != null) {
      Log.d(TAG, "calling startingMapsScreen()");
      view.startActivity(new Intent(view, MapsView.class));
      Log.d(TAG, "calling destroyView()");
      presenter.destroyView();
    }
  }

  @Override
  public void backToPreviousScreen(Home.HomeTo presenter) {

  }


  ///////////////MAPS//////////////////////////////////////////////////////////////

  @Override
  public void goToNextScreen(Maps.MapsTo presenter) {
    if (presenter.isChatClicked()){
      toChatState = new ChatState();
      toChatState.shop = presenter.getShop();
      toChatState.hideToolbar = false;

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingChatScreen()");
        view.startActivity(new Intent(view, ChatView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isCalendarClicked()){

      if (toCalendarState == null) {
        toCalendarState = new CalendarState();
        toCalendarState.shop = presenter.getShop();
      }

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingCalendarScreen()");
        view.startActivity(new Intent(view, CalendarView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isShopClicked()){

      toShopState = new ShopState();
      toShopState.shop = presenter.getShop();
      toShopState.url = "https://canarias.cashconverters.es";

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingWebshopScreen()");
        view.startActivity(new Intent(view, WebshopView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    }
  }


  @Override
  public void backToPreviousScreen(Maps.MapsTo presenter) {

  }


  ///////////////CHAT//////////////////////////////////////////////////////////////

  @Override
  public void goToDetailScreen(Chat.ChatTo presenter) {
    toDetailState = new DetailState();
    toDetailState.shop = presenter.getShop();
    toDetailState.hideToolbar = !presenter.getToolbarVisibility();
    toDetailState.selectedItem = presenter.getSelectedItem();

    // Arrancamos la pantalla del detalle sin finalizar la del maestro
    Context view = presenter.getManagedContext();
    if (view != null) {
      Log.d(TAG, "calling startingDetailChatScreen()");
      view.startActivity(new Intent(view, DetailChatView.class));
    }
  }


  @Override
  public void goToNextScreen(Chat.ChatTo presenter) {
    if (presenter.isCalendarClicked()){
      if (toCalendarState == null) {
        toCalendarState = new CalendarState();
        toCalendarState.shop = presenter.getShop();
      }

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingCalendarScreen()");
        view.startActivity(new Intent(view, CalendarView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isShopClicked()){
      toShopState = new ShopState();
      toShopState.shop = presenter.getShop();
      toShopState.url = "https://canarias.cashconverters.es";

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingWebshopScreen()");
        view.startActivity(new Intent(view, WebshopView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isMapsClicked()){
      toMapsState = new MapState();
      toMapsState.shop = presenter.getShop();

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingMapsScreen()");
        view.startActivity(new Intent(view, MapsView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    }
  }


  ///////////////DETAIL//////////////////////////////////////////////////////////////

  @Override
  public void backToMasterScreen(DetailChat.DetailTo presenter) {
    toChatState = new ChatState();
    toChatState.shop = presenter.getShop();
    toChatState.validDatabase = true;

    Context view = presenter.getManagedContext();
    if (view != null){
      Log.d(TAG, "calling destroyView()");
      presenter.destroyView();
    }
  }

  @Override
  public void goToNextScreen(DetailChat.DetailTo presenter) {
    if (presenter.isCalendarClicked()){
      if (toCalendarState == null) {
        toCalendarState = new CalendarState();
        toCalendarState.shop = presenter.getShop();
      }

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingCalendarScreen()");
        view.startActivity(new Intent(view, CalendarView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isShopClicked()){
      toShopState = new ShopState();
      toShopState.shop = presenter.getShop();
      toShopState.url = "https://canarias.cashconverters.es";

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingWebshopScreen()");
        view.startActivity(new Intent(view, WebshopView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isMapsClicked()){
      toMapsState = new MapState();
      toMapsState.shop = presenter.getShop();


      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingMapsScreen()");
        view.startActivity(new Intent(view, MapsView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    }
  }


  ///////////////WEBSHOP//////////////////////////////////////////////////////////////

  @Override
  public void goToNextScreen(Webshop.WebshopTo presenter) {
    if (presenter.isChatClicked()){
      toChatState = new ChatState();
      toChatState.shop = presenter.getShop();
      toChatState.hideToolbar = false;

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingChatScreen()");
        view.startActivity(new Intent(view, ChatView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isCalendarClicked()){
      if (toCalendarState == null) {
        toCalendarState = new CalendarState();
        toCalendarState.shop = presenter.getShop();
      }

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingCalendarScreen()");
        view.startActivity(new Intent(view, CalendarView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isMapsClicked()){
      toMapsState = new MapState();
      toMapsState.shop = presenter.getShop();


      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingMapsScreen()");
        view.startActivity(new Intent(view, MapsView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    }
  }

  @Override
  public void backToPreviousScreen(Webshop.WebshopTo presenter) {
  }


  ///////////////CALENDAR//////////////////////////////////////////////////////////////

  @Override
  public void goToNextScreen(Calendar.CalendarTo presenter) {
    Log.d(TAG, "saving CalendarState()");

    toCalendarState = new CalendarState();
    toCalendarState.dateSelected = presenter.getDate();
    toCalendarState.idHour = presenter.getHour();
    toCalendarState.shop = presenter.getShop();
    toCalendarState.name = presenter.getNameInputText();
    toCalendarState.mail = presenter.getMailInputText();
    toCalendarState.phone = presenter.getPhoneInputText();
    toCalendarState.products = presenter.getProductsInputText();
    toCalendarState.appointment = presenter.isAppointment();

    if (presenter.isChatClicked()){
      toChatState = new ChatState();
      toChatState.shop = presenter.getShop();
      toChatState.hideToolbar = false;

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingChatScreen()");
        view.startActivity(new Intent(view, ChatView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isShopClicked()){
      toShopState = new ShopState();
      toShopState.shop = presenter.getShop();
      toShopState.url = "https://canarias.cashconverters.es";

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingWebshopScreen()");
        view.startActivity(new Intent(view, WebshopView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    } else if (presenter.isMapsClicked()){
      toMapsState = new MapState();
      toMapsState.shop = presenter.getShop();

      Context view = presenter.getManagedContext();
      if (view != null) {
        Log.d(TAG, "calling startingMapsScreen()");
        view.startActivity(new Intent(view, MapsView.class));
        Log.d(TAG, "calling destroyView()");
        presenter.destroyView();
      }
    }
  }

  @Override
  public void backToPreviousScreen(Calendar.CalendarTo presenter) {

  }


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  private class MapState {
    Shop shop;
  }

  private class ChatState {
    Shop shop;
    boolean hideToolbar;
    boolean validDatabase;
  }

  private class ShopState {
    Shop shop;
    String url;
  }

  private class CalendarState {
    Shop shop;
    String dateSelected;
    int idHour;
    boolean appointment;
    String name;
    String mail;
    int phone;
    String products;
  }

  /**
   * Estado inicial del detalle pasado por el maestro
   */
  private class DetailState {
    boolean hideToolbar;
    ShopItem selectedItem;
    Shop shop;
  }

}
