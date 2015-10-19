package edu.epcc.epccfallfestapp.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class RegistrationBean {

    private String myData;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }
}