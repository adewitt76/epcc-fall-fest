package edu.epcc.epccfallfestapp.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 * See: http://rominirani.com/2014/08/26/gradle-tutorial-part-9-cloud-endpoints-persistence-android-studio/
 */
public class OfyService {
    static {
        ObjectifyService.register(RegistrationCode.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
