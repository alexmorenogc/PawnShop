package es.ulpgc.eite.clean.mvp.sample.app;

import android.util.Log;
import java.util.ArrayList;

public class Register {

    private static Register log;
    private static ArrayList<String> logger;

    private Register (){
        logger = new ArrayList<>();
    }

    public static Register getLog() {
        if (log == null) {
            log = new Register();
        }
        return log;
    }

    public static void newLog(String newRegister){
        logger.add(newRegister);
    }

    public static void showLog(){
        Log.d("Register:", "Register: ");
        for(String line : logger){
            Log.d("Register", "showLog: " + line);
        }
        Log.d("Register", "showLog: Fin del Registro");
    }
}
