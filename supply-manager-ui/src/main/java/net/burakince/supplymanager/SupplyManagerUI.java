package net.burakince.supplymanager;

import javax.servlet.annotation.WebServlet;

import net.burakince.supplymanager.authentication.AccessControl;
import net.burakince.supplymanager.authentication.BasicAccessControl;
import net.burakince.supplymanager.authentication.LoginScreen;
import net.burakince.supplymanager.authentication.LoginScreen.LoginListener;
import net.burakince.supplymanager.view.MainScreen;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@SuppressWarnings("serial")
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("smtheme")
@Widgetset("net.burakince.supplymanager.SupplyManagerWidgetset")
public class SupplyManagerUI extends UI {

	private AccessControl accessControl = new BasicAccessControl();

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		Responsive.makeResponsive(this);
		setLocale(vaadinRequest.getLocale());
		getPage().setTitle("SupplyManager");
		if (!accessControl.isUserSignedIn()) {
			setContent(new LoginScreen(accessControl, new LoginListener() {
				@Override
				public void loginSuccessful() {
					showMainView();
				}
			}));
		} else {
			showMainView();
		}
	}

	protected void showMainView() {
		addStyleName(ValoTheme.UI_WITH_MENU);
		setContent(new MainScreen(SupplyManagerUI.this));
		getNavigator().navigateTo(getNavigator().getState());
	}

	public static SupplyManagerUI get() {
		return (SupplyManagerUI) UI.getCurrent();
	}

	public AccessControl getAccessControl() {
		return accessControl;
	}

	@WebServlet(urlPatterns = "/*", name = "SupplyManagerServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SupplyManagerUI.class, productionMode = false)
	public static class SupplyManagerServlet extends VaadinServlet {
	}
}
