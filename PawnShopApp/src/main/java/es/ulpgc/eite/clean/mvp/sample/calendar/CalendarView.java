package es.ulpgc.eite.clean.mvp.sample.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.sample.R;

public class CalendarView
    extends GenericActivity<Calendar.PresenterToView, Calendar.ViewToPresenter, CalendarPresenter>
    implements Calendar.PresenterToView {

  private ImageButton menuImage;
  private EditText name, phone, mail, products;
  private android.widget.CalendarView date;
  private Spinner hours;
  private Button send;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);
    Log.d(TAG, "calling onCreate()");

    // Menú inferior
    ImageButton mapsMenuImage = (ImageButton) findViewById(R.id.m_maps);
    ImageButton chatMenuImage = (ImageButton) findViewById(R.id.m_chat);
    ImageButton webMenuImage = (ImageButton) findViewById(R.id.m_shop);
    menuImage = (ImageButton) findViewById(R.id.m_calendar);
    // Listeners del menú
    mapsMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onMapsButtonClicked();
      }
    });
    chatMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onChatButtonClicked();
      }
    });
    webMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onShopButtonClicked();
      }
    });

    // Pantalla Calendar
    name = (EditText) findViewById(R.id.name);
    phone = (EditText) findViewById(R.id.phone);
    mail = (EditText) findViewById(R.id.mail);
    date = (android.widget.CalendarView) findViewById(R.id.date);
    // Listener del Calendar View, que identifica cada pulsación en los días.
    date.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener(){
      @Override
      public void onSelectedDayChange(@NonNull android.widget.CalendarView view, int year, int month, int dayOfMonth) {
        month = month + 1; // En Calendar, van de 0-11
        getPresenter().changeDate(year + "-" + month + "-" + dayOfMonth);
      }
    });
    hours = (Spinner) findViewById(R.id.hour);
    setTempAdapterToHours();
    products = (EditText) findViewById(R.id.products);
    send = (Button) findViewById(R.id.send);
    send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onSendButtonClicked();
      }
    });
  }

  /**
   * Method that initialized MVP objects
   * {@link super#onResume(Class, Object)} should always be called
   */
  @SuppressLint("MissingSuperCall")
  @Override
  protected void onResume() {
    super.onResume(CalendarPresenter.class, this);
    menuImage.setImageResource(R.drawable.ic_calendar_icon_m);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Log.d(TAG, "calling onBackPressed()");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "calling onDestroy()");
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // Presenter To View /////////////////////////////////////////////////////////////

  @Override
  public void finishScreen() {
    Log.d(TAG, "calling finishScreen()");
    finish();
  }

  /**
   * Método que pone en el CalendarView la fecha que se introduce por parámetro
   * y asigna como fecha mínima seleccionable la fecha del día de hoy.
   *
   * @param date fecha a seleccionar
   */
  @Override
  public void setDateView(String date) {
    Log.d(TAG, "setDateView: calling setDateView");
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.add(java.util.Calendar.DATE, 1); // Se puede reservar con 24h de antelación
    long dateOld = cal.getTimeInMillis();
    this.date.setMinDate(dateOld);

    if (date != null) {
      long dateNew = convertToCalendar(date).getTimeInMillis();
      if (dateNew > dateOld) {
        this.date.setDate(dateNew, true, true);
      } else {
        this.date.setDate(dateOld, true, true);
      }
    } else {
      this.date.setDate(dateOld, true, true);
      getPresenter().changeDate(convertToString(cal));
    }
  }

  /**
   * Método que se usa para cambiar el desplegable de las horas disponibles en el día seleccionado
   *
   * @param hours ArrayList de String que tiene las horas disponibles
   */
  @Override
  public void setHours(ArrayList<String> hours) {
    if (hours != null){
      this.hours.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hours));
    } else {
      ArrayList<String> empty = new ArrayList<>();
      empty.add("Loading...");
      this.hours.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empty));
    }
  }

  @Override
  public void setNameText(String name) {
    this.name.setText(name);
  }

  @Override
  public void setPhoneText(int phone) {
    this.phone.setText(Integer.toString(phone));
  }

  @Override
  public void setMailText(String mail) {
    this.mail.setText(mail);
  }

  @Override
  public void enableSendButton() {
    send.setEnabled(true);
  }

  @Override
  public void disableSendButon() {
    send.setEnabled(false);
  }

  @Override
  public String getNameInputText() {
    return name.getText().toString();
  }

  @Override
  public String getMailInputText() {
    return mail.getText().toString();
  }

  @Override
  public int getInputPhoneText() {
    return Integer.parseInt(phone.getText().toString());
  }

  @Override
  public String getInputProductsText() {
    return products.getText().toString();
  }

  @Override
  public void setProductsText(String products) {
    this.products.setText(products);
  }

  @Override
  public int getHour() {
    return hours.getSelectedItemPosition();
  }

  @Override
  public void setHourSelected(int idHour) {
    this.hours.setSelection(idHour,true);
  }

  @Override
  public void enableCalendarView() {
    date.setEnabled(true);
  }

  @Override
  public void disableCalendarView() {
    date.setEnabled(false);
  }

  @Override
  public void enableHourSpinner() {
    hours.setEnabled(true);
  }

  @Override
  public void disableHourSpinner() {
    hours.setEnabled(false);
  }

  @Override
  public void enableTextInputs() {
    name.setEnabled(true);
    phone.setEnabled(true);
    mail.setEnabled(true);
    products.setEnabled(true);
  }

  @Override
  public void disableTextInputs() {
    name.setEnabled(false);
    phone.setEnabled(false);
    mail.setEnabled(false);
    products.setEnabled(false);
  }

  @Override
  public void makeToast(String text) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
  }

  /**
   * Método que se usa para asignar al desplegable de horarios disponibles la frase:
   * Cargando horas disponibles...
   *
   */
  @Override
  public void setTempAdapterToHours() {
    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.hour, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    hours.setAdapter(adapter);
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
    int month = Integer.parseInt(parts[1]) - 1; // Los meses en Calendar van de 0-11
    int year = Integer.parseInt(parts[0]);
    cal.set(year, month, day);
    return cal;
  }

  /**
   * Método convertToString para convertir una fecha de Calendar a String.
   *
   * @param cal Fecha en formato Calendar
   * @return Fecha en formato String
   */
  private String convertToString(java.util.Calendar cal) {
    int year, month, day;
    year = cal.get(java.util.Calendar.YEAR);
    month = cal.get(java.util.Calendar.MONTH) + 1; // Los meses en Calendar van de 0-11
    day = cal.get(java.util.Calendar.DAY_OF_MONTH);
    return year + "-" + month + "-" + day;
  }

}
