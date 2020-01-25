package app.atelier.vendor.classes;

public class Constants {

    public static final String BASE_URL1 = "http://atelierq8.com";
    // public static final String BASE_URL1 = "http://demo.atelierq8.com";

    public static final String BASE_URL = BASE_URL1 + "/api/";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String AUTHORIZATION_VALUE = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE1NjczMjYyMzMsImV4cCI6MTg4MjY4NjIzMywiaXNzIjoiaHR0cDovL2F0ZWxpZXIuaGFyZHRhc2suaW5mbyIsImF1ZCI6WyJodHRwOi8vYXRlbGllci5oYXJkdGFzay5pbmZvL3Jlc291cmNlcyIsImh0X2FwaSJdLCJjbGllbnRfaWQiOiIwZTcxMGMyMi04N2QyLTQzZWMtYjFmNy1iY2E4ZWI5MDE4NDIiLCJzdWIiOiIwZTcxMGMyMi04N2QyLTQzZWMtYjFmNy1iY2E4ZWI5MDE4NDIiLCJhdXRoX3RpbWUiOjE1NjczMjYyMzEsImlkcCI6ImxvY2FsIiwic2NvcGUiOlsiaHRfYXBpIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInB3ZCJdfQ.oz7deZ9gisNWpsL5lQqfAmE6baHoprL3iJcp4-3YgW2w9GpAv4wuaWeNelY2c2s37ylEbA_R1j42zSNJ37CnF6rHv-1FX4kzJdVVE02Z3XkP4A6KxBeKSLYJHp2bjhZIf-ftuFxplpQcAj-Xeu8TVS_nNtObztqSjhISRh6h0jrGDInF0ibHnhaFWnAX51oCKlypDDi_gvbnf9cBpAfrBLIgZOUK4ZQhAZ-JlRUpuRwKMPSo7uBiGeP_OFIgvyXXLh2pBlxzNtKyZ8BJFktfAdhADDrAKQnpn1kOORUhp14VePvnIN90N2-MnH06S-Q5mFY9IUJHXz2luPQ_2hsgWQ";


    public static final int NOTIFICATION_ID = 100;

    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static String CMS_NOTIFICATION_IMAGE_URL = BASE_URL1 + "/images/thumbs/";

    public final static String DOWNLOAD_INVOICE = BASE_URL  + "orders/{orderId}/pdf";

    public final static String ORDER_COMPLETE_STATUS = "complete";
    public final static String PAYMENT_STATUS_PAID = "paid";

}
