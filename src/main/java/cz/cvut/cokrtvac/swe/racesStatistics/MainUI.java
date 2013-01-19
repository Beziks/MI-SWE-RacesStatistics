package cz.cvut.cokrtvac.swe.racesStatistics;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Constants;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import cz.cvut.cokrtvac.swe.racesStatistics.utils.Props;
import org.vaadin.virkki.cdiutils.application.UIContext.UIScoped;

import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * The Application's "main" class
 */

@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
@UIScoped
public class MainUI extends UI {
    @Inject
    private TestEjb ejb;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window mainWindow = new Window(ejb.sayHi());
        mainWindow.setSizeFull();
        addWindow(mainWindow);
    }

    @WebServlet(urlPatterns = "/*", initParams = {
            @WebInitParam(name = VaadinSession.UI_PARAMETER, value = Props.UI_NAME),
            @WebInitParam(name = Constants.SERVLET_PARAMETER_UI_PROVIDER, value = Props.UI_PROVIDER_NAME),
            @WebInitParam(name = "heartbeatInterval", value = "1") })
    public static class AddressBookApplicationServlet extends VaadinServlet {
    }


}
