package sample.wickets;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class SecondPage extends WebPage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SecondPage() {
		add(new Label("header", "Welcome to Second Page!!!"));
	}
}
