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

    private static List<RegistrationCode> regCodes = new ArrayList<>();

    @ApiMethod(name = "getStatus")
    public RegistrationBean getRegStatus(@Named("regCode") String regCode) {
        RegistrationBean bean = new RegistrationBean();

        // Todo: add persistent registration cod list
        regCodes.add(new RegistrationCode("g56X45"));
        regCodes.add(new RegistrationCode("l54B42"));
        regCodes.add(new RegistrationCode("AAAAAA"));

        bean.setData("invalid");
        if (!regCodes.isEmpty()) {
            for (RegistrationCode rc : regCodes) {
                if ( rc.equals(regCode) ) {
                    if( !rc.isUsed() ) {
                        rc.setUsed(true);
                        bean.setData("valid");
                        break;
                    } else {
                        bean.setData("used");
                        break;
                    }
                }
            }
        }
        return bean;
    }
}
