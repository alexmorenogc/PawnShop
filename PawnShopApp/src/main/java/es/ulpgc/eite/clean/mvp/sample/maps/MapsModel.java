package es.ulpgc.eite.clean.mvp.sample.maps;

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
import es.ulpgc.eite.clean.mvp.sample.app.Shop;


public class MapsModel
    extends GenericModel<Maps.ModelToPresenter> implements Maps.PresenterToModel {

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

        GenericTypeIndicator<ArrayList<Shop>> indicator = new GenericTypeIndicator<ArrayList<Shop>>() {};
        shopList = dataSnapshot.getValue(indicator);
        getPresenter().setMarkerList(shopList);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error leyendo la BBDD. " + databaseError.toException());
      }
    });
  }

  @Override
  public void loadNewShopSelected(final String shopName) {
    Query query = connection.child("shops");
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<ArrayList<Shop>> indicator = new GenericTypeIndicator<ArrayList<Shop>>() {};
        shopList = dataSnapshot.getValue(indicator);
        for (Shop item : shopList){
          if (item.getName().equals(shopName)){
            getPresenter().setNewShopSelected(item);
          }
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error leyendo la BBDD. " + databaseError.toException());
      }
    });

    /*
    connection = database.getReference();
    Query query = connection.child("shops").orderByChild("name").equalTo(shopName);
    query.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<Shop> indicator = new GenericTypeIndicator<Shop>() {};
        Shop item = dataSnapshot.getValue(indicator);
        Log.d(TAG, "onDataChange: newShop " + item.getName());
        getPresenter().setNewShopSelected(item);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error leyendo la BBDD. " + databaseError.toException());
      }
    });
    */
  }
}
