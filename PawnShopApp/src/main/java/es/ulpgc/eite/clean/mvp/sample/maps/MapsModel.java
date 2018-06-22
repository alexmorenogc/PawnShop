package es.ulpgc.eite.clean.mvp.sample.maps;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.ulpgc.eite.clean.mvp.GenericModel;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;


public class MapsModel
    extends GenericModel<Maps.ModelToPresenter> implements Maps.PresenterToModel {


  private Shop shop;
  //private DatabaseFacade db;
  ArrayList<Shop> shopList;
  DatabaseReference connection;
  FirebaseDatabase database;

  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Maps.ModelToPresenter presenter) {
    super.onCreate(presenter);
    Log.d(TAG, "calling onCreate()");

    Log.d(TAG, "onCreate: Conectando con la BBDD");
    // Conectar con la BBDD
    database = FirebaseDatabase.getInstance();
    // Generar una referencia con la que conectar.
    connection = database.getReference();
    //db = DatabaseFacade.getInstance();
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

  /**
   * Metodo que coge de firebase las coordenadas de la lista de tiendas y se las pasa al presentador
   */
  @Override
  public void loadMapMarker() {
    Log.d(TAG, "calling loadMapMarker()");

    DatabaseReference myRef = connection.child("shops");
    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        GenericTypeIndicator<ArrayList<Shop>> indicator = new GenericTypeIndicator<ArrayList<Shop>>() {
        };
        shopList = dataSnapshot.getValue(indicator);
        getPresenter().setMarkerList(shopList);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error leyendo la BBDD. " + databaseError.toException());
      }
    });
  }
}
