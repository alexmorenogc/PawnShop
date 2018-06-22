package es.ulpgc.eite.clean.mvp.sample.data;


import es.ulpgc.eite.clean.mvp.sample.app.Shop;

public class ShopItem {

  private Shop dbItem;

  //  Disabling the default constructor
  private ShopItem() {

  }

  public ShopItem(Shop dbItem) {
    this.dbItem = dbItem;
  }

  public String getContent() {
    return dbItem.getName();
  }

  public Shop getDbItem() {
    return dbItem;
  }

  public String getDetails() {
    String mail = dbItem.getMail();
    String nombre = dbItem.getName();

    return "Nombre: " + nombre + "\n" + "Mail: " + mail + "\n";
  }

  public String getId() {
    return Integer.toString(dbItem.getId());
  }

  @Override
  public String toString() {
    return this.getContent();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ShopItem){
      ShopItem item = (ShopItem) obj;
      if(item.getId() == getId()){
        return true;
      }
    }
    return false;
  }
}
