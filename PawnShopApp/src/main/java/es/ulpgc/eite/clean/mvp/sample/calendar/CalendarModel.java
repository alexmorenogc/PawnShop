package es.ulpgc.eite.clean.mvp.sample.calendar;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.sample.app.Booking;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.app.Timetable;


public class CalendarModel
    extends GenericModel<Calendar.ModelToPresenter> implements Calendar.PresenterToModel {

  private DatabaseReference connection;
  private FirebaseDatabase database;

  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Calendar.ModelToPresenter presenter) {
    super.onCreate(presenter);
    Log.d(TAG, "calling onCreate()");

    // Conectar con la BBDD
    database = FirebaseDatabase.getInstance();
    // Generar una referencia con la que conectar.
    connection = database.getReference();
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


  /**
   * Método para asignar la cita previa.
   *
   * @param booking datos de la cita previa
   * @param shop tienda en la que se reserva
   */
  @Override
  public void setBooking(final Booking booking, Shop shop) {
    getPresenter().setAppointment();
    connection.child("booking").push().setValue(booking);
    // TODO: 26/5/18 Mandar mail
  }

  /**
   * Método para consultar las horas disponibles en una fecha concreta y una tienda concreta,
   * luego llama al método checkTimetableOnDate para comprobar las horas ya reservadas.
   *
   * @param date fecha para comprobar
   * @param shop tienda a comprobar
   */
  @Override
  public void setTimetableList(final String date, final Shop shop) {
    connection.child("timetable").child(Integer.toString(shop.getTimetable())).child("timetable").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<Timetable> hours;
        GenericTypeIndicator<ArrayList<Timetable>> indicator = new GenericTypeIndicator<ArrayList<Timetable>>() {};
        hours = dataSnapshot.getValue(indicator);
        checkTimetableOnDate(date, hours, shop);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // TODO: 27/5/18 Poner que guarde tire de Realm
      }
    });
  }

  /**
   * Método privado que se usa por limitaciones en consultas compuestas de Firebase, se utiliza para
   * una vez obtenido en Firebase la lista de horas de ese día, compararlas con las reservas y eliminar
   * aquellas horas que ya están reservadas.
   *
   * @param date fecha a consultar
   * @param hours lista de horarios disponibles ese día.
   * @param shop tienda para comparar las reservas sólo de esa tienda
   */
  private void checkTimetableOnDate(final String date, final ArrayList<Timetable> hours, final Shop shop) {
    Query query = connection.child("booking").orderByChild("date").equalTo(date);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          GenericTypeIndicator<Booking> indicator = new GenericTypeIndicator<Booking>() {};
          Booking bookings;
          for (DataSnapshot data : dataSnapshot.getChildren()) {
            bookings = data.getValue(indicator);
            if (bookings.getShopId() == shop.getId()){
              hours.remove(bookings.getHourId());
            }
          }
          getPresenter().setAvailableHours(hours);
        } else {
          getPresenter().setAvailableHours(hours);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // TODO: 27/5/18 Poner que saque de Realm
      }
    });
  }


  ///////////////////////////////////////////////////////////////////////////////////
  // Presenter To Model ////////////////////////////////////////////////////////////



}
