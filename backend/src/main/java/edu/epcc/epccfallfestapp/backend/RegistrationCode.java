package edu.epcc.epccfallfestapp.backend;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by adewi on 10/20/2015.
 */
public class RegistrationCode {

    private String regCode;
    private Date eventDate;
    private boolean used;

    public RegistrationCode(String regCode, int month, int day, int year) {
        this.regCode = regCode;
        Calendar date = new GregorianCalendar();
        date.set(year,month,day);
        eventDate = new Date(date.getTimeInMillis());
        used = false;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public int hashCode() {
        return regCode.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return regCode.equals(object.toString());
    }

    @Override
    public String toString() {
        return regCode;
    }
}
