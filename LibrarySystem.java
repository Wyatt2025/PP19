/** 
 * LibrarySystem.java
 * Main program to test reading/writing library system information to/from a 
 *   database.
 * @author Instructor Chemerys
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class LibrarySystem extends JFrame {
    public static final String MAIN_TAB = "Inventory";
    public static final String ADD_PATRON_TAB = "Add Patron";
    public static final String ADD_BOOK_TAB = "Add Book";
    public static final String CHECKOUT_TAB = "Check Out";
    public static final String CHECKIN_TAB = "Check In";
    public static final Dimension FIELD_SIZE = new Dimension(350, 20);
    public static final String[] INVENTORY_COLUMN_NAMES = new String[] {
        "TITLE", "AUTHOR", "ISBN", "BORROWER", "DUE_DATE"};

    protected DefaultTableModel booksTableModel;
    protected JTabbedPane mainTab;
    protected JTextField fName;
    protected JTextField lName;
    protected JTextField dob;
    protected JTextField ssn;
    protected JTextField addIsbn;
    protected JTextField title;
    protected JTextField authorLName;
    protected JTextField authorFName; 
    protected JTextField checkinBookIsbn;
    protected JTextField checkoutSsn;
    protected JTextField checkoutBookIsbn;

    /** Constructor to display the main GUI. */
    public LibrarySystem() {
        setTitle("Blue Ridge Library System");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createMainPanel(), BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    /** Create a simple banner.
     */
    protected JPanel createHeaderPanel() {
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(173, 216, 230));
        northPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        northPanel.add(new JLabel("Blue Ridge Library System"), JLabel.CENTER);
        return northPanel;
    }

    /** Create GUI panel to display basic functions of a library system.
     *  @return JPanel
     */
    public JTabbedPane createMainPanel() {
        mainTab = new JTabbedPane();
        mainTab.add(createMainTab(), MAIN_TAB);
        mainTab.add(createPatronTab(), ADD_PATRON_TAB);
        mainTab.add(createBookTab(), ADD_BOOK_TAB);
        mainTab.add(createCheckinTab(), CHECKIN_TAB);
        mainTab.add(createCheckoutTab(), CHECKOUT_TAB);
        return mainTab;
    }

    /** Create the main panel which displays the summary of all books in
     *   the system and who they are currently checked out to (or if they
     *   are not checked out.
     */
    protected JPanel createMainTab() {
        // create an empty non-editable table with default column names
        JTable booksTable = new JTable();     
        booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        booksTableModel = new DefaultTableModel(null, // will call getBooks() below
            new Vector(Arrays.asList(INVENTORY_COLUMN_NAMES))) { // column names
            // Overridden method to indicate that no cells are editable 
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable.setModel(booksTableModel);
        refreshInventory(); // set initial data

        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh Inventory");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshInventory();
            }
        });
        buttonPanel.add(refreshButton);

        JPanel mainPanel = new JPanel(new BorderLayout());      
        mainPanel.setBorder(BorderFactory.createTitledBorder(
        "Current Inventory Status"));
        mainPanel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

    /** Creates the panel that displays controls to add a new borrower to
     *   the library system.
     */
    protected JPanel createPatronTab() {
        fName = new JTextField();
        lName = new JTextField();
        ssn = new JTextField();
        dob = new JTextField();

        // set the size for one field, when using GridLayout all others will match
        fName.setPreferredSize(FIELD_SIZE);

        JPanel labelPanel = new JPanel(new GridLayout(4, 1));
        labelPanel.add(new JLabel("First Name: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("Last Name: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("Date of Birth: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("SSN: ", JLabel.RIGHT));

        JPanel entryPanel = new JPanel(new GridLayout(4, 1));
        entryPanel.add(fName);
        entryPanel.add(lName);
        entryPanel.add(dob);
        entryPanel.add(ssn);

        Box entryBox = Box.createHorizontalBox();
        entryBox.add(labelPanel);
        entryBox.createHorizontalStrut(15);
        entryBox.add(entryPanel);      

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Add Patron");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(addPatron()) {
                    fName.setText("");
                    lName.setText("");
                    ssn.setText("");
                    dob.setText("");
                    refreshInventory();
                }
            }
        });
        buttonPanel.add(submitButton);

        JPanel wPanel = new JPanel(new BorderLayout());
        wPanel.setBorder(BorderFactory.createTitledBorder("Enter Patron Information: "));
        wPanel.add(entryBox, BorderLayout.WEST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(wPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;  
    }


    /** Creates the panel that displays controls to add a new book to
     *   the library system.
     */
    protected JPanel createBookTab() {
        addIsbn = new JTextField();
        title = new JTextField();
        authorLName = new JTextField();
        authorFName = new JTextField();

        // set the size for one field, when using GridLayout all others will match
        addIsbn.setPreferredSize(FIELD_SIZE);

        JPanel labelPanel = new JPanel(new GridLayout(4, 1));
        labelPanel.add(new JLabel("ISBN: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("Title: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("Author's Last Name: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("Author's First Name: ", JLabel.RIGHT));

        JPanel entryPanel = new JPanel(new GridLayout(4, 1));
        entryPanel.add(addIsbn);
        entryPanel.add(title);
        entryPanel.add(authorLName);
        entryPanel.add(authorFName);

        Box entryBox = Box.createHorizontalBox();
        entryBox.add(labelPanel);
        entryBox.createHorizontalStrut(15);
        entryBox.add(entryPanel);      

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Add Book");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(addBook()) {
                    addIsbn.setText("");
                    title.setText("");
                    authorLName.setText("");
                    authorFName.setText("");
                    refreshInventory();
                }
            }
        });
        buttonPanel.add(submitButton);

        JPanel wPanel = new JPanel(new BorderLayout());
        wPanel.setBorder(BorderFactory.createTitledBorder("Enter Book Information: "));
        wPanel.add(entryBox, BorderLayout.WEST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(wPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;  
    }

    /** Creates the panel that displays controls to check a book back in to
     *   the library system.
     */
    protected JPanel createCheckinTab() {
        checkinBookIsbn = new JTextField();
        checkinBookIsbn.setPreferredSize(FIELD_SIZE);

        JPanel labelPanel = new JPanel(new GridLayout(1, 1));
        labelPanel.add(new JLabel("ISBN: ", JLabel.RIGHT));

        JPanel entryPanel = new JPanel(new GridLayout(1, 1));
        entryPanel.add(checkinBookIsbn);

        Box entryBox = Box.createHorizontalBox();
        entryBox.add(labelPanel);
        entryBox.createHorizontalStrut(15);
        entryBox.add(entryPanel);      

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Checkin Book");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(checkin()) {
                    checkinBookIsbn.setText("");
                    refreshInventory();
                }
            }
        });
        buttonPanel.add(submitButton);

        JPanel wPanel = new JPanel(new BorderLayout());
        wPanel.setBorder(BorderFactory.createTitledBorder("Enter ISBN of Book being checked in: "));
        wPanel.add(entryBox, BorderLayout.WEST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(wPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;  
    }

    /** Creates the panel that displays controls to check a book out of
     *   the library system.
     */
    protected JPanel createCheckoutTab() {
        checkoutBookIsbn = new JTextField();
        checkoutSsn = new JTextField();
        checkoutBookIsbn.setPreferredSize(FIELD_SIZE);

        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.add(new JLabel("ISBN: ", JLabel.RIGHT));
        labelPanel.add(new JLabel("Patron SSN: ", JLabel.RIGHT));

        JPanel entryPanel = new JPanel(new GridLayout(2, 1));
        entryPanel.add(checkoutBookIsbn);
        entryPanel.add(checkoutSsn);

        Box entryBox = Box.createHorizontalBox();
        entryBox.add(labelPanel);
        entryBox.createHorizontalStrut(15);
        entryBox.add(entryPanel);      

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Checkout Book");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(checkout()) {
                    checkoutBookIsbn.setText("");
                    checkoutSsn.setText("");
                    refreshInventory();
                }
            }
        });
        buttonPanel.add(submitButton);

        JPanel wPanel = new JPanel(new BorderLayout());
        wPanel.setBorder(BorderFactory.createTitledBorder("Enter information for book being checked out: "));
        wPanel.add(entryBox, BorderLayout.WEST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(wPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;  
    }

    /** Method to retrieve a listing of all books in the library system
     *   along with the borrower that currently has the book checked out,
     *   or an empty cell if the book is available.
     *  This method will be called after every action (Add Patron, Add Book, 
     *    etc..) and when explicitly refreshed on the main tab.
     * @return Vector - Data to populate the booksTableModel
     *   Each Vector contains a single row of Data added to the larger
     *   Vector of data to populate the table
     */
    protected void refreshInventory() {
        // ITP 220 - create the SQL statement passed to executeQuery()
        //  the column order should match INVENTORY_COLUMN_NAMES
        Vector tableData = executeQuery(sql);
        if(tableData != null) {
            booksTableModel.setDataVector(tableData,
                new Vector(Arrays.asList(INVENTORY_COLUMN_NAMES)));
        }
    }

    /** Shared method to execute any SQL statement and return a Vector of 
     *   Vectors that can be used to populate a simple JTable.
     * @param sql
     * @return Vector of Vectors
     */
    protected Vector executeQuery(String sql) {
        // ITP 220, complete this method to create the SQL connection
        //  and retrieve results from a call to Statement.executeQuery()
        // Use the ResultSet to iterator through each row, adding to
        //  a Vector of Vectors used to populate a table model
        // To make this method reusable, use ResultSet.getObject() to retrieve values
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector tableData = new Vector();      
    }

    /** ITP 220 - complete this method.
     * @return TRUE if add was successful, FALSE otherwise
     */
    protected boolean addPatron() {
    }

    /** ITP 220 - complete this method.
     * @return TRUE if add was successful, FALSE otherwise
     */
    protected boolean addBook() {
    }

    /** ITP 220 - complete this method.
     * @return TRUE if add was successful, FALSE otherwise
     */
    protected boolean checkout() {
    }

    /** ITP 220 - complete this method.
     * @return TRUE if add was successful, FALSE otherwise
     */
    protected boolean checkin() {
    }

    /** Main driver containing a menu of options 
     */
    public static void main(String[] args) {
        new LibrarySystem();
    }
}