package es.ulpgc.eite.clean.mvp.sample.app;

import es.ulpgc.eite.clean.mvp.sample.calendar.Calendar;
import es.ulpgc.eite.clean.mvp.sample.chat.Chat;
import es.ulpgc.eite.clean.mvp.sample.detail.DetailChat;
import es.ulpgc.eite.clean.mvp.sample.home.Home;
import es.ulpgc.eite.clean.mvp.sample.maps.Maps;
import es.ulpgc.eite.clean.mvp.sample.webshop.Webshop;


public interface Mediator {

  interface Lifecycle {

    void startingScreen(Maps.ToMaps presenter);
    void resumingScreen(Maps.MapsTo presenter);

    void startingScreen(Home.ToHome presenter);
    void resumingScreen(Home.HomeTo presenter);

    void startingScreen(Calendar.ToCalendar presenter);
    void resumingScreen(Calendar.CalendarTo presenter);

    void startingMasterScreen(Chat.ToChat presenter);
    void resumingMasterScreen(Chat.ChatTo presenter);

    void startingDetailScreen(DetailChat.ToDetail presenter);

    void startingScreen(Webshop.ToWebshop presenter);
    void resumingScreen(Webshop.WebshopTo presenter);
  }

  interface Navigation {

    void goToNextScreen(Maps.MapsTo presenter);
    void backToPreviousScreen(Maps.MapsTo presenter);

    void goToNextScreen(Home.HomeTo presenter);
    void backToPreviousScreen(Home.HomeTo presenter);

    void goToNextScreen(Calendar.CalendarTo presenter);
    void backToPreviousScreen(Calendar.CalendarTo presenter);

    void goToNextScreen(Webshop.WebshopTo presenter);
    void backToPreviousScreen(Webshop.WebshopTo presenter);

    void goToDetailScreen(Chat.ChatTo presenter);
    void goToNextScreen(Chat.ChatTo presenter);

    void backToMasterScreen(DetailChat.DetailTo presenter);
    void goToNextScreen(DetailChat.DetailTo presenter);
  }
}
