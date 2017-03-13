package cabanas.garcia.ismael.opportunity.view;


public interface WebView extends View{
    String RAW_CONTENT = "<html>\n" +
            "\t<body>\n" +
            "\t<h1>Hi! %s</h1>\n" +
            "\t<p>You are in %s</p>\t\n" +
            "\t<a href=\"/logout\">Logout\n" +
            "\t</a>\n" +
            "\t</body>\n" +
            "</html>";

    String getName();
}
