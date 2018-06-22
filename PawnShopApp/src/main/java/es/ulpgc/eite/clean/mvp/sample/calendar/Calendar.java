package es.ulpgc.eite.clean.mvp.sample.calendar;

import android.content.Context;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.Model;
import es.ulpgc.eite.clean.mvp.Presenter;
import es.ulpgc.eite.clean.mvp.sample.app.Booking;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.app.Timetable;


public interface Calendar {

  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////

  interface State {

  }

  interface ToCalendar extends State {
    void onScreenStarted();
    void setShop(Shop shop);
    void setAppointment(boolean ifAppointment);
    void setNameInputText(String name);
    void setPhoneInputText(int phone);
    void setMailInputText(String mail);
    void setProductsInputText(String products);
    void setDateView(String date);
    void setHourSelected(int idHour);
  }

  interface CalendarTo extends State{
    Context getManagedContext();
    void destroyView();
    void onScreenResumed();
    boolean isShopClicked();
    boolean isChatClicked();
    boolean isMapsClicked();
    Shop getShop();
    String getNameInputText();
    String getMailInputText();
    int getPhoneInputText();
    String getProductsInputText();
    boolean isAppointment();
    String getDate();
    int getHour();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  // Screen ////////////////////////////////////////////////////////////////////////

  /**
   * Methods offered to VIEW to communicate with PRESENTER
   */
  interface ViewToPresenter extends Presenter<PresenterToView> {
    void onShopButtonClicked();
    void onMapsButtonClicked();
    void onChatButtonClicked();
    void onSendButtonClicked();
    void changeDate(String date);
  }

  /**
   * Required VIEW methods available to PRESENTER
   */
  interface PresenterToView extends ContextView {
    void finishScreen();
    void setDateView(String date);
    void setHours(ArrayList<String> hours);
    void setNameText(String name);
    void setPhoneText(int phone);
    void setMailText(String mail);
    void enableSendButton();
    void disableSendButon();
    String getNameInputText();
    String getMailInputText();
    int getInputPhoneText();
    String getInputProductsText();
    void setProductsText(String products);
    int getHour();
    void setHourSelected(int idHour);
    void enableCalendarView();
    void disableCalendarView();
    void enableHourSpinner();
    void disableHourSpinner();
    void enableTextInputs();
    void disableTextInputs();
    void makeToast(String cita);
    void setTempAdapterToHours();
  }

  /**
   * Methods offered to MODEL to communicate with PRESENTER
   */
  interface PresenterToModel extends Model<ModelToPresenter> {
    void setBooking(Booking booking, Shop shop);
    void setTimetableList(String date, Shop shop);
  }

  /**
   * Required PRESENTER methods available to MODEL
   */
  interface ModelToPresenter {
    void setAppointment();
    void setAvailableHours(ArrayList<Timetable> hours);
  }
}
