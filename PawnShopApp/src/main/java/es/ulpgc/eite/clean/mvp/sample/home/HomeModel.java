package es.ulpgc.eite.clean.mvp.sample.home;

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

public class HomeModel
    extends GenericModel<Home.ModelToPresenter> implements Home.PresenterToModel {

  //private DatabaseFacade db;
  private ArrayList<Shop> shopList;
  private DatabaseReference connection;
  private FirebaseDatabase database;

  /**
   * Method that recovers a reference to the PRESENTER
   * You must ALWAYS call {@link super#onCreate(Object)} here
   *
   * @param presenter Presenter interface
   */
  @Override
  public void onCreate(Home.ModelToPresenter presenter) {
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

  @Override
  public void loadShopList() {
    Log.d(TAG, "calling loadShopList()");

    DatabaseReference myRef = connection.child("shops");

    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        GenericTypeIndicator<ArrayList<Shop>> indicator = new GenericTypeIndicator<ArrayList<Shop>>() {};
        shopList = dataSnapshot.getValue(indicator);

        ArrayList<String> nameShopList = new ArrayList<>();
        if (shopList.size() > 0){
          for(int i = 0; i < shopList.size(); i++){
            nameShopList.add(shopList.get(i).getName());
          }
        } else {
          nameShopList.add("No hay tiendas.");
        }
        getPresenter().setShopList(nameShopList);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error leyendo la BBDD. " + databaseError.toException());
      }
    });
  }

  @Override
  public Shop getShop(int position) {
    return null;
  }

  @Override
  public void getShopAsync(int position) {
    DatabaseReference myRef = connection.child("shops").child(Integer.toString(position));
    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        getPresenter().setShopSelected(dataSnapshot.getValue(Shop.class));
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error leyendo la BBDD. " + databaseError.toException());

      }
    });
  }

  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////


}
