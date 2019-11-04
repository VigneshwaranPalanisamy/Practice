package sample.wickets;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class FirstWebPage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("serial")
	public FirstWebPage() {
		add(new Label("header1","This is My First Web Page Using Wickets !!!"));
		
		add(new Link<Void>("link1") {

			@Override
			public void onClick() {
				setResponsePage(SecondPage.class);
			}
			
		});
	}

}
