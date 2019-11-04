package sample.wickets;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class FirstWicketApplication extends WebApplication{

	@Override
	public Class<? extends Page> getHomePage() {
		return FirstWebPage.class;
	}

}
