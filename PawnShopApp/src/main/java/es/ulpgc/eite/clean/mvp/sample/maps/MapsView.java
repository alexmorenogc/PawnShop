package es.ulpgc.eite.clean.mvp.sample.maps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;




import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;


import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.sample.R;
import es.ulpgc.eite.clean.mvp.sample.app.Shop;


public class MapsView
    extends GenericActivity<Maps.PresenterToView, Maps.ViewToPresenter, MapsPresenter>
    implements Maps.PresenterToView  {

  private ImageButton menuImage;
  private MapView mapView;
  private GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    Log.d(TAG, "calling onCreate()");


    // Menú inferior
    ImageButton chatMenuImage = (ImageButton) findViewById(R.id.m_chat);
    ImageButton calendarMenuImage = (ImageButton) findViewById(R.id.m_calendar);
    ImageButton webMenuImage = (ImageButton) findViewById(R.id.m_shop);
    menuImage = (ImageButton) findViewById(R.id.m_maps);


    mapView = (MapView) findViewById(R.id.cm_mapview);

    mapView.onCreate(savedInstanceState);

    map = mapView.getMap();
    map.getUiSettings().setMyLocationButtonEnabled(false); //Obtener tu ubicacion actual.
    map.getUiSettings().setMapToolbarEnabled(true); //Toolbar que nos permite la obción de abrir la aplicación de google maps.
    map.getUiSettings().setZoomControlsEnabled(true);// Herramienta de hacer zoom con el mas y con el menos.
    map.getUiSettings().setZoomGesturesEnabled(true); //Herramienta que nos permite hacer zoom con gestos con los dedos.
    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //Vista de satelite del mapa
    map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Vista normal del mapa
    map.setMapType(GoogleMap.MAP_TYPE_HYBRID); // Vista hibrida


    MapsInitializer.initialize(this);
    // Listeners del menú
    chatMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onChatButtonClicked();
      }
    });
    webMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getPresenter().onShopButtonClicked();
      }
    });
    calendarMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {getPresenter().onCalendarButtonClicked();
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
    super.onResume(MapsPresenter.class, this);
    mapView.onResume();
    menuImage.setImageResource(R.drawable.ic_maps_icon_m);
    getPresenter().startLoadMarkerList(); //Obtener del presentador los marcadores para cargarlos
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Log.d(TAG, "calling onBackPressed()");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy(); //Destruimos la vista del mapa
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
   * Método que pone los marker en el mapa.
   * @param ArrayList de tipo Shop mapShopList con las tiendas de CashConverters
   */
    @Override
    public void setMarkersToMap(ArrayList<Shop> mapShopList) {
      LatLng latLng;
      Marker marker;
        for (int i = 0; i < mapShopList.size(); i++) {
          latLng = new LatLng(mapShopList.get(i).getLatitudeD(), mapShopList.get(i).getLongitudeD());
          Log.d(TAG, "onDataChange: Latitud: " + mapShopList.get(i).getLatitude());
          BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.maker_icon);
          marker = map.addMarker(new MarkerOptions().position(latLng).title(mapShopList.get(i).getName()).icon(markerIcon));
        }
    }

  /**
   * Método que centra la camara en el marker de la tienda seleccionada.
   * @param variable de tipo Shop
   */
  @Override
  public void setCenterCamera(Shop shop) {
    LatLng latLng = new LatLng(shop.getLatitudeD(), shop.getLongitudeD());
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,10);
    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15); //valor del zoom que se le desea dar al zoom
    map.moveCamera(cameraUpdate); //segun movemos la pantalla se nos desplaza el mapa
    map.animateCamera(zoom); //El mapa se movera correctamente conforme a los nuevos atributos
  }
}