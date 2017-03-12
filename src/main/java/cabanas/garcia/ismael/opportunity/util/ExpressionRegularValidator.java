package cabanas.garcia.ismael.opportunity.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionRegularValidator {

    public static boolean isValidate(String pattern, String pathToCheck){

        if(pattern == null || pathToCheck == null)
            return false;

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(pathToCheck);
        return matcher.matches();
    }

}
