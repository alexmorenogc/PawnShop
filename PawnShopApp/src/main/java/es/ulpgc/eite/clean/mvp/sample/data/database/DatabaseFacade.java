package es.ulpgc.eite.clean.mvp.sample.data.database;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ulpgc.eite.clean.mvp.sample.app.Shop;
import io.realm.Realm;

public class DatabaseFacade {
    
    private static Realm realmDatabase;

    private static Realm getDatabase() {
        if (realmDatabase == null) {
            realmDatabase = Realm.getDefaultInstance();
        }
        return realmDatabase;
    }

    public static JSONObject createJsonObject(int id){   
        Map<String, String> item = new HashMap();
        item.put("id", String.valueOf(id));
        item.put("content", "Item " + id);
        item.put("details", makeDetails(id));
        final JSONObject json = new JSONObject(item);
        Log.d(".createJsonObject", "json=" + json);
        return json;
    }

    public static void deleteDatabase(){
        deleteShops();
    }

    public static void deleteShops(){
        for(Shop item: getShops()){
            deleteShop(item);
        }
    }

    public static void deleteShop(Shop item) {
        final Integer id = item.getId();
        getDatabase().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Shop.class).equalTo("id", id)
                        .findAll()
                        .deleteAllFromRealm();
            }
        });
    }

    public static void deleteShop(Integer id) {
        final Integer idToRemove = id;

        getDatabase().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Shop.class).equalTo("id", idToRemove)
                        .findAll()
                        .deleteAllFromRealm();
            }
        });
    }

    public static List<Shop> getShops(){
        Log.d("DatabaseFacade", "calling getShops() method");
        return getDatabase().where(Shop.class).findAll();
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position).append("\n");
        for (int count = 0; count < position; count++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    // -------------------------------------------------------------------------------
    //  Fill the Realm database with the contents of a JSON file

    public static void createDbItemsFromJsonFile(Context context, String filename) {
        Log.d("DatabaseFacade", "calling createDbItemsFromJsonFile() method");
        try {
            InputStream stream = context.getAssets().open(filename);

            // Open a transaction to store items into the realmDatabase
            getDatabase().beginTransaction();

            try {
                getDatabase().createAllFromJson(Shop.class, stream);
                getDatabase().commitTransaction();

            } catch (IOException e) {
                // Remember to cancel the transaction if anything goes wrong.
                getDatabase().cancelTransaction();
            }

            stream.close();

        } catch (IOException ex) {
            Log.d("DatabaseFacade", "createDbItemsFromJsonFile: file not found");

        }
    }
}
