package es.ulpgc.eite.clean.mvp.sample.data;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import es.ulpgc.eite.clean.mvp.sample.data.database.DatabaseFacade;

public class MasterDetailData {

    private static final String TAG = "MasterDetailData";

    public static void deleteAllDatabaseItems(){
        for(ShopItem item: getItemsFromDatabase()){
            deleteItem(item);
        }
    }


    public static void deleteItem(ShopItem item) {
        DatabaseFacade.deleteShop(item.getDbItem());
    }

    public static List<ShopItem> getItemsFromDatabase(){
        Log.d(TAG, "calling getItemsFromDatabase() method");

        List<Shop> dbItems = DatabaseFacade.getShops();

        List<ShopItem> modelItems = new ArrayList();
        for(Shop dbItem: dbItems) {
            modelItems.add(new ShopItem(dbItem));
        }

        Log.d(TAG, "items=" +  modelItems);
        return modelItems;
    }


    public static void loadItemsFromJsonFile(Context context, String filename) {
        Log.d(TAG, "calling loadItemsFromJsonFile() method");
        DatabaseFacade.createDbItemsFromJsonFile(context, filename);
    }
}
