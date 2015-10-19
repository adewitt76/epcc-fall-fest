package edu.epcc.epccfallfestapp.backend;

/**
 * Created by adewi on 10/20/2015.
 */
public class RegistrationCode {

    private String regCode;
    private boolean used;

    public RegistrationCode(String regCode) {
        this.regCode = regCode;
        used = false;
    }

    public String getRegCode() {
        return regCode;
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
