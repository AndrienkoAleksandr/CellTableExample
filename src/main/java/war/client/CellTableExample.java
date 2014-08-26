package war.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class CellTableExample implements EntryPoint {

    /**
     * A simple data type that represents a contact.
     */
    private static class Contact {
        private final String address;
        private final Date birthday;
        private final String name;

        public Contact(String name, Date birthday, String address) {
            this.name = name;
            this.birthday = birthday;
            this.address = address;
        }
    }

    /**
     * The list of data to display.
     */
    @SuppressWarnings("deprecation")
    private static List<Contact> CONTACTS = new ArrayList<>();
    static {
        CONTACTS.add(new Contact("John", new Date(80, 4, 12), "123 Abc Avenue"));
        CONTACTS.add(new Contact("Joe", new Date(85, 2, 22), "22 Lance Ln"));
        CONTACTS.add(new Contact("Tom", new Date(85, 3, 22), "33 Lance Ln"));
        CONTACTS.add(new Contact("Jack", new Date(85, 4, 22), "44 Lance Ln"));
        CONTACTS.add(new Contact("Tim", new Date(85, 5, 22), "55 Lance Ln"));
        CONTACTS.add(new Contact("Mike", new Date(85, 6, 22), "66 Lance Ln"));
        CONTACTS.add(new Contact("George", new Date(46, 6, 6),"77 Lance Ln"));
    }


    public void onModuleLoad() {
        // Create a CellTable.
        final CellTable<Contact> table = new CellTable<Contact>();
        // Display 3 rows in one page
        table.setPageSize(3);

        // Add a text column to show the name.
        TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact object) {
                return object.name;
            }
        };
        table.addColumn(nameColumn, "Name");

        // Add a date column to show the birthday.
        DateCell dateCell = new DateCell();
        Column<Contact, Date> dateColumn = new Column<Contact, Date>(dateCell) {
            @Override
            public Date getValue(Contact object) {
                return object.birthday;
            }
        };
        table.addColumn(dateColumn, "Birthday");

        // Add a text column to show the address.
        TextColumn<Contact> addressColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact object) {
                return object.address;
            }
        };
        table.addColumn(addressColumn, "Address");

        // Associate an async data provider to the table
        // XXX: Use AsyncCallback in the method onRangeChanged
        // to actaully get the data from the server side
        final AsyncDataProvider<Contact> provider = new AsyncDataProvider<Contact>() {
            @Override
            protected void onRangeChanged(HasData<Contact> display) {
                int start = display.getVisibleRange().getStart();
                int end = start + display.getVisibleRange().getLength();
                end = end >= CONTACTS.size() ? CONTACTS.size() : end;
                List<Contact> sub = CONTACTS.subList(start, end);
                updateRowData(start, sub);
            }
        };
        provider.addDataDisplay(table);
        provider.updateRowCount(CONTACTS.size(), true);

        SimplePager pager = new SimplePager();
        pager.setDisplay(table);

        VerticalPanel vp = new VerticalPanel();
        vp.add(table);
        vp.add(pager);
        Button button = new Button("Add new User");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                CONTACTS.add(new Contact("Vlad", new Date(System.currentTimeMillis()), "fdf"));
                provider.updateRowCount(CONTACTS.size(), true);
                GWT.log(CONTACTS.size() + "");
            }
        });
        // Add it to the root panel.
        RootPanel.get().add(vp);
        RootPanel.get().add(button);
    }
}
