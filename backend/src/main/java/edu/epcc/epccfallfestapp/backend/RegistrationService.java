package edu.epcc.epccfallfestapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import static edu.epcc.epccfallfestapp.backend.OfyService.ofy;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public RegistrationService() {

    }

    @ApiMethod(name = "getStatus")
    public RegistrationBean getRegStatus(@Named("regCode") String regCode) {

        Query<RegistrationCode> querry = ofy().load().type(RegistrationCode.class);
        List<RegistrationCode> regCodes = new ArrayList<>();
        QueryResultIterator<RegistrationCode> iterator = querry.iterator();
        if(iterator != null)
            while (iterator.hasNext()) regCodes.add(iterator.next());

        RegistrationBean bean = new RegistrationBean();
        bean.setRegistrationCodeStatus(INVALID);

        if (!regCodes.isEmpty()) {
            for (RegistrationCode rc : regCodes) {
                if(rc.getRegCode() != null ) {
                    if (rc.equals(regCode)) {
                        if (!rc.isUsed()) {
                            rc.setUsed(true);
                            bean.setRegistrationCodeStatus(VALID);
                            bean.setEventDate(rc.getEventDate());
                            ofy().save().entity(rc).now();
                            break;
                        } else {
                            bean.setRegistrationCodeStatus(USED);
                            bean.setEventDate(null);
                            break;
                        }
                    }
                }
            }
        }
        return bean;
    }

    @ApiMethod(name = "addCode")
    public void addRegCode(@Named("code") String code, @Named("month") int month,
                           @Named("day") int day, @Named("year") int year) throws ConflictException
    {
        if(code != null) {
            if (findRegCode(code) != null) {
                throw new ConflictException("Registration Code already exists");
            }
            ofy().save().entity(new RegistrationCode(code,month,day,year)).now();
        }
    }

    @ApiMethod(name = "clearCodeList")
    public void clearCodeList() {
        Query<RegistrationCode> querry = ofy().load().type(RegistrationCode.class);
        List<RegistrationCode> regCodes = new ArrayList<>();
        QueryResultIterator<RegistrationCode> iterator = querry.iterator();
        if(iterator != null)
            while (iterator.hasNext()) ofy().delete().entity(iterator.next()).now();
    }

    private RegistrationCode findRegCode(String id) {
        return ofy().load().type(RegistrationCode.class).id(id).now();
    }
}
