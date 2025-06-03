package mg.working.service.RH.util;


import java.time.Month;
import java.util.Locale;

public class DateUtils {
    static String[] moisFrancais = {
            "", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };


    public static String[] getMoisFrancais() {
        return moisFrancais;
    }

    public static void setMoisFrancais(String[] moisFrancais) {
        DateUtils.moisFrancais = moisFrancais;
    }
}
