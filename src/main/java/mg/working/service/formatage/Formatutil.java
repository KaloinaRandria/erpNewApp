package mg.working.service.formatage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formatutil {

    public static String formaterMontant(double montant) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');

        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
        return format.format(montant);
    }

    public static String refactorAmountFormat(String column) {
        column = column.replace("\'" , "");
        column = column.replace(".", "");
        column = column.replace(",", ".");
        column = column.replace(" ", "");

        return column;
    }
}
