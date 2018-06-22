package es.ulpgc.eite.clean.mvp.sample.webshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.sample.R;
import es.ulpgc.eite.clean.mvp.sample.calendar.CalendarView;
import es.ulpgc.eite.clean.mvp.sample.chat.ChatView;
import es.ulpgc.eite.clean.mvp.sample.maps.MapsView;

public class WebshopView
    extends GenericActivity<Webshop.PresenterToView, Webshop.ViewToPresenter, WebshopPresenter>
    implements Webshop.PresenterToView {


  private WebView webView_shop;
  private ImageButton menuImage;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shop);
    Log.d(TAG, "calling onCreate()");

    ImageButton mapsMenuImage = (ImageButton) findViewById(R.id.m_maps);
    ImageButton calendarMenuImage = (ImageButton) findViewById(R.id.m_calendar);
    ImageButton chatMenuImage = (ImageButton) findViewById(R.id.m_chat);
    menuImage = (ImageButton) findViewById(R.id.m_shop);

    //WEBVIEW
    webView_shop = (WebView) findViewById(R.id.webview_shop);
    WebSettings webSettings = webView_shop.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webView_shop.loadUrl("https://canarias.cashconverters.es/");
    webView_shop.setWebViewClient(new WebViewClient());


    // Listeners del menú
    mapsMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onMapsButtonClicked();
      }
    });

    chatMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getPresenter().onChatButtonClicked();
      }
    });

    calendarMenuImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        getPresenter().onCalendarButtonClicked();
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
    super.onResume(WebshopPresenter.class, this);
    menuImage.setImageResource(R.drawable.ic_shop_icon_m);
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

}
