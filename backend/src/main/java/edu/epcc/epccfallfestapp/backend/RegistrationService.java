package edu.epcc.epccfallfestapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adewi on 10/20/2015.
 */
@Api(
        name = "registrationApi",
        version = "1.0",
        namespace = @ApiNamespace(
                ownerDomain = "backend.epccfallfestapp.epcc.edu",
                ownerName = "backend.epccfallfestapp.epcc.edu",
                packagePath = ""
        )
)
public class RegistrationService {

    public static final int VALID = 6401;
    public static final int USED = 6402;
    public static final int INVALID = 6403;

    private static List<RegistrationCode> regCodes = new ArrayList<>();

    @ApiMethod(name = "getStatus")
    public RegistrationBean getRegStatus(@Named("regCode") String regCode) {
        RegistrationBean bean = new RegistrationBean();

        // Todo: add persistent registration code list
        // RegistrationCode(String regCode, int month, int day, int year)
        regCodes.add(new RegistrationCode("star-fire",10,21,2015));
        regCodes.add(new RegistrationCode("hell-fire",10,21,2015));
        regCodes.add(new RegistrationCode("spit-fire",10,21,2015));
        regCodes.add(new RegistrationCode("red-fire",10,21,2015));
        regCodes.add(new RegistrationCode("blue-fire",10,21,2015));
        regCodes.add(new RegistrationCode("green-fire",10,21,2015));
        regCodes.add(new RegistrationCode("im-on-fire",10,21,2015));
        regCodes.add(new RegistrationCode("pants-on-fire",10,21,2015));
        regCodes.add(new RegistrationCode("do-not-fire",10,21,2015));
        regCodes.add(new RegistrationCode("star-ice",10,21,2015));
        regCodes.add(new RegistrationCode("hell-ice",10,21,2015));
        regCodes.add(new RegistrationCode("spit-ice",10,21,2015));
        regCodes.add(new RegistrationCode("red-ice",10,21,2015));
        regCodes.add(new RegistrationCode("blue-ice",10,21,2015));
        regCodes.add(new RegistrationCode("green-ice",10,21,2015));
        regCodes.add(new RegistrationCode("im-on-ice",10,21,2015));
        regCodes.add(new RegistrationCode("pants-on-ice",10,21,2015));
        regCodes.add(new RegistrationCode("do-not-ice",10,21,2015));

        bean.setRegistrationCodeStatus(INVALID);
        if (!regCodes.isEmpty()) {
            for (RegistrationCode rc : regCodes) {
                if ( rc.equals(regCode) ) {
                    if( !rc.isUsed() ) {
                        rc.setUsed(true);
                        bean.setRegistrationCodeStatus(VALID);
                        bean.setEventDate(rc.getEventDate());
                        break;
                    } else {
                        bean.setRegistrationCodeStatus(USED);
                        bean.setEventDate(null);
                        break;
                    }
                }
            }
        }
        return bean;
    }
}
