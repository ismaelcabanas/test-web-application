package cabanas.garcia.ismael.opportunity.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionRegularValidator {

    public static boolean isPathValidate(String pattern, String pathToCheck){

        assert pattern != null && pathToCheck != null;

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(pathToCheck);
        return matcher.matches();
    }

}
