package io.helidon.examples.lra;

import io.narayana.lra.filter.ClientLRARequestFilter;
import io.narayana.lra.filter.ClientLRAResponseFilter;
import io.narayana.lra.filter.FilterRegistration;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class InventoryApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(FilterRegistration.class);
        s.add(ClientLRARequestFilter.class);
        s.add(ClientLRAResponseFilter.class);
        s.add(InventoryResource.class);
        return s;
    }

}
