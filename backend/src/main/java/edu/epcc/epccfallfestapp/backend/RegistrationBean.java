package edu.epcc.epccfallfestapp.backend;

import java.util.Date;

/**
 * The object model for the data we are sending through endpoints
 */
public class RegistrationBean {

    private int registrationCodeStatus;
    private Date eventDate;

    public int getRegistrationCodeStatus() {
        return registrationCodeStatus;
    }

    public void setRegistrationCodeStatus(int status) {
        registrationCodeStatus = status;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}