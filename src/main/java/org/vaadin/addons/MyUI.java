package org.vaadin.addons;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.lang3.text.WordUtils;
import org.vaadin.addons.searchbox.SearchBox;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        SearchBox searchBox1 = new SearchBox("Search", SearchBox.ButtonPosition.RIGHT);
        searchBox1.setCaption("Simple search box");
        searchBox1.addSearchListener(e -> Notification.show(e.getSearchTerm()));
        layout.addComponent(searchBox1);

        SearchBox searchBox5 = new SearchBox("Search", SearchBox.ButtonPosition.RIGHT);
        searchBox5.setCaption("Search suggestions");
        searchBox5.setSuggestionGenerator(this::suggestUsers, this::convertValueUser, this::convertCaptionUser);
        searchBox5.addSearchListener(e -> Notification.show(e.getSearchTerm()));
        searchBox5.setWidth("350px");
        layout.addComponent(searchBox5);

        SearchBox searchBox2 = new SearchBox(VaadinIcons.SEARCH, SearchBox.ButtonPosition.LEFT);
        searchBox2.setCaption("Button with icon on the left and placeholder text");
        searchBox2.setPlaceholder("Search...");
        searchBox2.addSearchListener(e -> Notification.show(e.getSearchTerm()));
        layout.addComponent(searchBox2);

        SearchBox searchBox3 = new SearchBox((String) null, SearchBox.ButtonPosition.HIDDEN);
        searchBox3.setCaption("Search field without a button (search with ENTER key)");
        searchBox3.setPlaceholder("Search...");
        searchBox3.addSearchListener(e -> Notification.show(e.getSearchTerm()));
        layout.addComponent(searchBox3);

        SearchBox searchBox4 = new SearchBox(VaadinIcons.SEARCH, SearchBox.ButtonPosition.RIGHT);
        searchBox4.setCaption("Button joined with field");
        searchBox4.setButtonJoined(true);
        searchBox4.setPlaceholder("Search...");
        searchBox4.addSearchListener(e -> Notification.show(e.getSearchTerm()));
        layout.addComponent(searchBox4);

        // Search modes
        SearchBox searchBoxMode1 = new SearchBox(VaadinIcons.SEARCH, SearchBox.ButtonPosition.RIGHT);
        searchBoxMode1.setCaption("<i>EXPLICIT</i> = search on button click or ENTER");
        searchBoxMode1.setCaptionAsHtml(true);
        searchBoxMode1.setSearchMode(SearchBox.SearchMode.EXPLICIT);
        searchBoxMode1.addSearchListener(e -> Notification.show(e.getSearchTerm()));

        SearchBox searchBoxMode2 = new SearchBox(VaadinIcons.SEARCH, SearchBox.ButtonPosition.RIGHT);
        searchBoxMode2.setCaption("<i>DEBOUNCE</i> = search after typing stopped");
        searchBoxMode2.setCaptionAsHtml(true);
        searchBoxMode2.setSearchMode(SearchBox.SearchMode.DEBOUNCE);
        searchBoxMode2.addSearchListener(e -> Notification.show(e.getSearchTerm()));

        SearchBox searchBoxMode3 = new SearchBox(VaadinIcons.SEARCH, SearchBox.ButtonPosition.RIGHT);
        searchBoxMode3.setCaption("<i>EAGER</i> = search while typing");
        searchBoxMode3.setCaptionAsHtml(true);
        searchBoxMode3.setSearchMode(SearchBox.SearchMode.EAGER);
        searchBoxMode3.addSearchListener(e -> Notification.show(e.getSearchTerm()));

        HorizontalLayout modesLayout = new HorizontalLayout(searchBoxMode1, searchBoxMode2, searchBoxMode3);
        modesLayout.setCaption("<b>Search modes</b>");
        modesLayout.setCaptionAsHtml(true);
        layout.addComponent(modesLayout);

        Label dummy = new Label();
        layout.addComponent(dummy);
        layout.setExpandRatio(dummy, 1);

        layout.setSizeFull();
        Panel panel = new Panel(layout);
        panel.setSizeFull();
        setContent(panel);
    }

    private List<DataSource.User> suggestUsers(String query, int cap) {
        return DataSource.getUsers().stream()
                .filter(user -> user.getName().contains(query.toLowerCase()))
                .limit(cap).collect(Collectors.toList());
    }

    private String convertValueUser(DataSource.User user) {
        return WordUtils.capitalizeFully(user.getName(), ' ');
    }

    private String convertCaptionUser(DataSource.User user, String query) {
        return "<div class='suggestion-container'>"
                + "<img src='" + user.getPicture() + "' class='userimage'>"
                + "<span class='username'>"
                + WordUtils.capitalizeFully(user.getName(), ' ').replaceAll("(?i)(" + query + ")", "<b>$1</b>")
                + "</span>"
                + "</div>";
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
