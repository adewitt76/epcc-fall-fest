package edu.epcc.epccfallfestapp.registration_server;

import java.util.IllegalFormatException;

/**
 * Created by Aaron on 10/15/2015.
 */
public class Reg_Number {
    private String reg_number;
    private boolean isUsed;

    public Reg_Number(String reg_number) {
        if(reg_number.length() < 5 || 5 < reg_number.length())
            throw new IllegalArgumentException("Registration number must be 5 characters");
        this.reg_number = reg_number;
        isUsed = false;
    }

    public String getReg_number() {
        return reg_number;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void used() {
        isUsed = true;
    }

    @Override
    public int hashCode() {
        for (int i = 0; i < 5; i++){

        }
        return 0;
    }
}
