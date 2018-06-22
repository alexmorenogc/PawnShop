package es.ulpgc.eite.clean.mvp.sample.calendar;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.ContextView;
import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.GenericPresenter;
import es.ulpgc.eite.clean.mvp.sample.app.Booking;
import es.ulpgc.eite.clean.mvp.sample.app.Mediator;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.app.Timetable;

public class CalendarPresenter
    extends GenericPresenter
        <Calendar.PresenterToView, Calendar.PresenterToModel, Calendar.ModelToPresenter, CalendarModel>
    implements Calendar.ViewToPresenter, Calendar.ModelToPresenter, Calendar.CalendarTo, Calendar.ToCalendar {

  private boolean shopClicked;
  private boolean chatClicked;
  private boolean mapsClicked;
  //State
  private Shop shop;
  private String name, mail, dateSelected, products;
  private boolean ifAppointment;
  private int phone;
  private ArrayList<String> hours;
  private int idHour;
  private static final String APPOINTMENT = "APPOINTMENT";
  private static final String APPOINTMENT_DATE = "APPOINTMENT_DATE";

  /**
   * Operation called during VIEW creation in {@link GenericActivity#onResume(Class, Object)}
   * Responsible to initialize MODEL.
   * Always call {@link GenericPresenter#onCreate(Class, Object)} to initialize the object
   * Always call  {@link GenericPresenter#setView(ContextView)} to save a PresenterToView reference
   *
   * @param view The current VIEW instance
   */
  @Override
  public void onCreate(Calendar.PresenterToView view) {
    super.onCreate(CalendarModel.class, this);
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
  public void onResume(Calendar.PresenterToView view) {
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
  public void onMapsButtonClicked() {
    Log.d(TAG, "calling onMapsButtonClicked()");
    mapsClicked = true;

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

  /**
   * Botón de reserva, se genera la cita previa y se pasa al modelo para guardar en Firebase
   */
  @Override
  public void onSendButtonClicked() {
    if(isViewRunning()){
      if (getView().getInputPhoneText() != 0
            && getView().getInputProductsText() != null
            && getView().getMailInputText() != null
            && getView().getNameInputText() != null){

        String nameBooking = getNameInputText();
        String mailBooking = getMailInputText();
        String dateBooking = dateSelected;
        String productsBooking = getProductsInputText();
        int phoneBooking = getView().getInputPhoneText();
        int idHour = getView().getHour();
        Booking booking = new Booking(shop.getId(),nameBooking,mailBooking,dateBooking,productsBooking,phoneBooking,idHour);
        getModel().setBooking(booking, shop);
      }
    }
  }

  /**
   * Método usado para cambiar la fecha seleccionada, cada vez que se cambia, se carga de nuevo
   * la lista de horas disponibles.
   *
   * @param date fecha a cambiar
   */
  @Override
  public void changeDate(String date) {
    this.dateSelected = date;
    if (isViewRunning()){
      getView().setTempAdapterToHours();
    }
    getModel().setTimetableList(dateSelected,shop);
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // State /////////////////////////////////////////////////////////////////////////



  ///////////////////////////////////////////////////////////////////////////////////
  // To Calendar //////////////////////////////////////////////////////////////////////

  @Override
  public void onScreenStarted() {
    Log.d(TAG, "calling onScreenStarted()");

    // Lectura en SharedPreferences de la reserva
    SharedPreferences sharedPref = getManagedContext().getSharedPreferences(APPOINTMENT, Context.MODE_PRIVATE);
    ifAppointment = sharedPref.getBoolean(APPOINTMENT,false);


    if (ifAppointment){
      // Obtención de la fecha reservada, y comparación con la fecha de hoy para bloquear futuras reservas
      String appointment = sharedPref.getString(APPOINTMENT_DATE,null);
      java.util.Calendar appointmentCalendar = convertToCalendar(appointment);
      java.util.Calendar todayCalendar = java.util.Calendar.getInstance();
      if (todayCalendar.getTimeInMillis() >= appointmentCalendar.getTimeInMillis()){
        ifAppointment = false;
        // TODO: 28/5/18 ELIMINAR SHAREDPREFERENCES
      } else {
        dateSelected = appointment;
      }
    }
    setCurrentState();
  }

  @Override
  public void setShop(Shop shop) {
    this.shop = shop;
  }

  @Override
  public void setAppointment(boolean ifAppointment) {
    this.ifAppointment = ifAppointment;
  }

  @Override
  public void setNameInputText(String name) {
    this.name = name;
  }

  @Override
  public void setPhoneInputText(int phone) {
    this.phone = phone;
  }

  @Override
  public void setMailInputText(String mail) {
    this.mail = mail;
  }

  @Override
  public void setProductsInputText(String products) {
    this.products = products;
  }

  @Override
  public void setDateView(String date) {
      this.dateSelected = date;
  }

  @Override
  public void setHourSelected(int idHour) {
    this.idHour = idHour;
  }

  @Override
  public void onScreenResumed() {
    Log.d(TAG, "calling onScreenResumed()");
    setCurrentState();
  }

  @Override
  public boolean isShopClicked() {
    return shopClicked;
  }

  @Override
  public boolean isChatClicked() {
    return chatClicked;
  }

  @Override
  public boolean isMapsClicked() {
    return mapsClicked;
  }

  @Override
  public Shop getShop() {
    return shop;
  }

  @Override
  public String getNameInputText() {
    if (isViewRunning()){
      return getView().getNameInputText();
    }
    return null;
  }

  @Override
  public String getMailInputText() {
    if (isViewRunning()){
      return getView().getMailInputText();
    }
    return null;
  }

  @Override
  public int getPhoneInputText() {
    if (isViewRunning()){
      return getView().getInputPhoneText();
    }
    return 0;
  }


  @Override
  public String getProductsInputText() {
    if (isViewRunning()){
      return getView().getInputProductsText();
    }
    return null;
  }


  @Override
  public boolean isAppointment() {
    return ifAppointment;
  }

  @Override
  public String getDate() {
    return this.dateSelected;
  }

  @Override
  public int getHour() {
    if (isViewRunning()){
      return getView().getHour();
    }
    return 0;
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // Calendar To //////////////////////////////////////////////////////////////////////

  @Override
  public Context getManagedContext(){
    return getActivityContext();
  }

  @Override
  public void destroyView(){
    Log.d(TAG, "destroyView: calling finishScreen");
    if(isViewRunning()) {
      getView().finishScreen();
    }
  }

  ///////////////////////////////////////////////////////////////////////////////////


  private void setCurrentState() {
    Log.d(TAG, "calling setCurrentState()");

    if (isViewRunning()) {
      if (dateSelected == null){
        // Se genera la fecha seleccionada como la fecha mínima a reservar
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year, month, day;
        year = cal.get(java.util.Calendar.YEAR);
        month = cal.get(java.util.Calendar.MONTH) + 1;
        day = cal.get(java.util.Calendar.DAY_OF_MONTH) + 1;
        dateSelected = year + "-" + month + "-" + day;
        getModel().setTimetableList(dateSelected,shop);
      } else {
        getModel().setTimetableList(dateSelected,shop);
      }
      getView().setDateView(dateSelected);
      getView().setNameText(name);
      getView().setPhoneText(phone);
      getView().setMailText(mail);
      getView().setProductsText(products);
    }
    checkButtonEnable();
  }

  /**
   * Método que comprueba si existe reserva para bloquear todas las interacciones.
   */
  private void checkButtonEnable() {
    if (isViewRunning()) {
      if (!ifAppointment) {
        getView().enableSendButton();
        getView().enableCalendarView();
        getView().enableHourSpinner();
        getView().enableTextInputs();
      } else {
        getView().disableSendButon();
        getView().disableCalendarView();
        getView().disableHourSpinner();
        getView().disableTextInputs();
        getView().makeToast("Cita el día " + dateSelected);
      }
    }
  }

  /**
   * Método para asignar una reserva, para conservar los datos, se guarda en SharedPreferences.
   */
  @Override
  public void setAppointment() {
    ifAppointment = true;
    checkButtonEnable();

    SharedPreferences sharedPref = getManagedContext().getSharedPreferences(APPOINTMENT, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putBoolean(APPOINTMENT,true);
    editor.putString(APPOINTMENT_DATE,dateSelected);
    editor.apply();
  }

  @Override
  public void setAvailableHours(ArrayList<Timetable> hours) {
    this.hours = new ArrayList<>();
    for (Timetable item : hours){
      this.hours.add(item.getHour());
    }
    getView().setHours(this.hours);
    getView().setHourSelected(idHour);
  }

  /**
   * Método convertToCalendar para convertir una fecha de String a Calendar.
   *
   * @param date Fecha en formato String
   * @return Fecha en formato Calendar
   */
  private java.util.Calendar convertToCalendar(String date) {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    String parts[] = date.split("-");
    int day = Integer.parseInt(parts[2]);
    int month = Integer.parseInt(parts[1]) - 1;
    int year = Integer.parseInt(parts[0]);
    cal.set(year, month, day);
    return cal;
  }

}
