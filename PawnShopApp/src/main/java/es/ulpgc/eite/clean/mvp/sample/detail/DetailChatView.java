package es.ulpgc.eite.clean.mvp.sample.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import es.ulpgc.eite.clean.mvp.GenericActivity;
import es.ulpgc.eite.clean.mvp.sample.R;
import es.ulpgc.eite.clean.mvp.sample.app.Register;
import es.ulpgc.eite.clean.mvp.sample.data.ShopItem;

public class DetailChatView
    extends GenericActivity<DetailChat.PresenterToView, DetailChat.ViewToPresenter, DetailChatPresenter>
    implements DetailChat.PresenterToView {


  //private ImageButton menuImage;
  private ShopItem item;
  private Toolbar toolbar;
  private CollapsingToolbarLayout toolbarLayout;
  private AppBarLayout appbarLayout;

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_detail);
    Log.d(TAG, "calling onCreate()");

    toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);

    // Show the Up button in the action bar.
    ActionBar actionbar = getSupportActionBar();
    if (actionbar != null) {
      actionbar.setDisplayHomeAsUpEnabled(true);
    }


    toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
    appbarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
    appbarLayout.setExpanded(true);

    // Definiciones de los buttons
    Button next = (Button) findViewById(R.id.button_next);
    Button prev = (Button) findViewById(R.id.button_prev);

    // Listeners del next y prev
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onNextClicked(item);
      }
    });
    prev.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getPresenter().onPrevClicked(item);
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
    super.onResume(DetailChatPresenter.class, this);

    item = getPresenter().getItem();
    if (toolbarLayout != null && item != null) {
      toolbarLayout.setTitle(item.getContent());
    }
    // Show the content as text in a TextView.
    if (item != null) {
      ((TextView) findViewById(R.id.item_detail)).setText(item.getDetails());
    }
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

  @Override
  public void hideToolbar() {
    toolbar.setVisibility(View.GONE);
    appbarLayout.setExpanded(false);
  }

  @Override
  public void setNewItem() {
    item = getPresenter().getItem();
    if (toolbarLayout != null && item != null) {
      toolbarLayout.setTitle(item.getContent());
    }
    // Show the dummy content as text in a TextView.
    if (item != null) {
      ((TextView) findViewById(R.id.item_detail)).setText(item.getDetails());
    }
    Register.getLog().newLog("DETALLE TIENDAS");
    Register.getLog().showLog();
  }

}
