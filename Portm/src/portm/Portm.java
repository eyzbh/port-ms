package portm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.table.*;

import java.util.*;

import java.sql.*;
import java.util.Properties;

class MyGUI extends JFrame implements ActionListener {

    Image i = Toolkit.getDefaultToolkit().createImage("port.jpg");
    
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    JMenuBar menuBar;
    JMenu contactMenu;
    JPanel servicesMenu;
    JTextField firstNameField, lastNameField, containerField, addressfield, phonenofield, itemType, itemName, emailField;
    JComboBox<String> countriesori, countriesdest, customers;
    JRadioButton importRadio, exportRadio;
    JButton bill;

    JLabel emailLabel, phoneLabel;

    JTextField serviceCostField, priceField, taxRateField, weight, travelDistance;
    JTextArea receiptArea;
    JButton calculateButton, clearButton;
    JCheckBox total;
    Component comp[];
    JPanel receiptPanel;
    Connection conn;
    JButton shipButton;
    JMenuItem newCust, oldCust;
    JButton addCustomerButton;
    JFrame newCustomerFrame; 
    JButton updateTable, deleteTable;
    JTable t;
    DefaultTableModel model;
    
    public MyGUI() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        super("Port Management");
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        //SwingUtilities.updateComponentTreeUI(this);

        setLayout(new CardLayout());
        // Login page
        JPanel loginPanel = new JPanel();
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        emailField = new JTextField(10);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        shipButton = new JButton("Ship");
        shipButton.addActionListener(this);
        addCustomerButton = new JButton("Add");
        addCustomerButton.addActionListener(this);
        deleteTable = new JButton("Delete");
        deleteTable.addActionListener(this);
        updateTable = new JButton("Update");
        updateTable.addActionListener(this);

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        total = new JCheckBox("Reciept");
        total.addActionListener(this);
        
        travelDistance = new JTextField(10);
        weight = new JTextField(10);

        // Services page
        JPanel servicesPanel = new JPanel();

        menuBar = new JMenuBar();
        
        JMenu customerMenu = new JMenu("Customers");
        oldCust = new JMenuItem("Existing Customers");
        oldCust.addActionListener(this);
        newCust = new JMenuItem("Add Customer");
        newCust.addActionListener(this);
        customerMenu.add(newCust);
        customerMenu.add(oldCust);
        menuBar.add(customerMenu);
        
        servicesMenu = new JPanel();
        firstNameField = new JTextField(10);
        lastNameField = new JTextField(10);
        containerField = new JTextField(10);
        addressfield= new JTextField(10);
        phonenofield= new JTextField(10);
        itemType = new JTextField(10);
        itemName = new JTextField(10);
        String[] countries = {"Ethiopia", "India", "USA", "UK","China","Taiwan","Germany","Kenya"};
        
        countriesori=new JComboBox<>(countries);
        countriesdest=new JComboBox<>(countries);
        
        importRadio = new JRadioButton("Import");
        exportRadio = new JRadioButton("Export");
        bill = new JButton("Bill");
        importRadio.setSelected(true);
        

        ButtonGroup gr = new ButtonGroup();
        gr.add(exportRadio);
        gr.add(importRadio);
        
        conn = DriverManager.getConnection("jdbc:mysql://localhost/port?user=root&password=password");
        listAllUsers();
        
        /*
        Automatically arrange the components in c into the left side
        of the screen.
        */
        
        Component c[] = { new JLabel("Customer: "), customers, new JLabel("Container:")
                            , containerField, new JLabel("Item Type"), itemType, new JLabel("Item Name: "), itemName, new JLabel("Country of Origin")
                            , countriesori, new JLabel("Country of Destination: "), countriesdest, importRadio, exportRadio, new JLabel(), total, shipButton, clearButton};
        comp = c;
        
        for(int i = 0; i < comp.length - 1; i+=2) {
            JPanel p = new JPanel();
            p.add(comp[i]);
            p.add(comp[i+1]);
            servicesMenu.add(p);
        }

        servicesMenu.setLayout(new BoxLayout(servicesMenu, BoxLayout.Y_AXIS));

        contactMenu = new JMenu("Contact Us");
        emailLabel = new JLabel("Email: info@mycompany.com");
        phoneLabel = new JLabel("Phone: +251986173149");
        contactMenu.add(emailLabel);
        contactMenu.add(phoneLabel);

        menuBar.add(contactMenu);

        servicesPanel.add(menuBar);

        // Receipt page
        receiptPanel = new JPanel();
        serviceCostField = new JTextField(10);
        priceField = new JTextField(10);
        taxRateField = new JTextField(10);
        receiptArea = new JTextArea(10, 20);
        receiptArea.setEditable(false);
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);
 
        receiptPanel.setLayout(new BorderLayout());
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        JPanel p2 = new JPanel();
        p2.add(new JLabel("Weight: "));
        p2.add(weight);
        p2.add(new JLabel("Distance Traveled: "));
        p2.add(travelDistance);
        JPanel p3 = new JPanel();
        p3.add(new JLabel("Tax Rate:"));
        p3.add(taxRateField);
        p1.add(p2);
        p1.add(p3);
        receiptPanel.add(p1, BorderLayout.NORTH);
        priceField.setEditable(false);

        receiptPanel.add(new JScrollPane(receiptArea));
        JPanel p4 = new JPanel();
        p4.add(calculateButton);
        receiptPanel.add(p4, BorderLayout.SOUTH);
        bill.addActionListener(this);

         add(loginPanel);
        setSize(800,600);
        ImagePanel smenuWrapper = new ImagePanel();
        smenuWrapper.setLayout(new BorderLayout());
        smenuWrapper.add(new JScrollPane(servicesMenu), BorderLayout.WEST);
        add(smenuWrapper);

        setVisible(true);
        setSize(801,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    }

    /*JPanel with background image
    */
    
    class ImagePanel extends JPanel {
        ImagePanel() {
        }
        
        Image im = i;
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(im, 0, 0, this);
        }
    }
    
    double cprice;
    double subtotal;
    double scharge;
    double tot;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks

            if(e.getSource() == loginButton) {

                // validate login
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {

                    if( username.equals("admin")&&password.equals("password")){
                        // login success
                        CardLayout layout = (CardLayout) getContentPane().getLayout();
                        layout.next(getContentPane());
                        setJMenuBar(menuBar);
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Wrong username or password!");
                    }

                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }
            else if(e.getSource() == calculateButton) {
                
                try {
                    double w = Double.parseDouble(weight.getText());
                    double d = Double.parseDouble(travelDistance.getText());
                    double t = Double.parseDouble(taxRateField.getText());
                    
                    double r = 0;
                    
                    if(w < 60)
                        r = 0.5;
                    else if (w <= 200)
                        r = 1;
                    else
                        r = 4;
                    
                    cprice = d * r;
                    scharge = 200;
                    subtotal = cprice + scharge;

                    t *= subtotal;
                    tot = subtotal + scharge + t;
                    
                    String reciept =
                            "Receipt\n********************\n" +
                            "Buyer:, " + ((String)customers.getSelectedItem()).split(",")[1] + "\n" +
                            "Cost for travel: " + cprice + "\n" +
                            "Service charge: " + scharge + "\n" +
                            "Subtotal: " + subtotal + "\n" +
                            "Taxes: " + t + "\n" +
                            "Total overall cost: " + tot + " Birr";
                    
                    receiptArea.setText(reciept);
                    total.setText("Total: " + tot + " Birr");
                    total.setSelected(!receiptArea.getText().equals(""));
                } catch(Exception ex) {
                    ex.printStackTrace();
                }

            }

            else if(e.getSource() == clearButton) {
                for(int i = 0; i < comp.length; i++)
                    if(comp[i] instanceof JTextField) {
                        JTextField f = (JTextField) comp[i];
                        f.setText("");
                    }
            }
            
            else if (e.getSource() == total) {
                
                JFrame f = new JFrame();
                f.add(receiptPanel);
                f.setSize(500, 400);
                f.setLocation(400, 400);
                f.setVisible(true);
                total.setSelected(!receiptArea.getText().equals(""));
            } else if (e.getSource() == shipButton ) {
                if(total.isSelected()) {
                    String cquery =
                            "INSERT INTO COST(cprice, serviceCharge, taxRate, subtotal, total) VALUES (" +
                            cprice + ", " + scharge + ", " + taxRateField.getText() + ", " + subtotal + ", " + tot + ")";
                    
                    try{
                    conn.prepareStatement(cquery).executeUpdate();  
                    
                    ResultSet rs = conn.prepareStatement("SELECT coid FROM cost", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery();
                    rs.last();
                   
                    String lastCost = rs.getString("coid");
                    String selectedCustomer = (String)customers.getSelectedItem();
                    String iquery =
                            "INSERT INTO ITEM(itype, iname, container, origin, destination, imp, price, buyer) VALUES (" +
                            getValue(itemType) + ", " + getValue(itemName) + " , " + getValue(containerField) + ", '" + (String) countriesori.getSelectedItem()
                            + "', '" + (String) countriesdest.getSelectedItem() + "', " + importRadio.isSelected() + ", " + lastCost + ", " + selectedCustomer.split(",")[0] + ")";
                    conn.prepareStatement(iquery).executeUpdate();
                    
                    JOptionPane.showMessageDialog(this, "Order Successfully Submitted");
                    
                    } catch(SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                    
            }else if(e.getSource() == newCust) {
                newCustomerFrame = new JFrame();
                newCustomerFrame.setLayout(new BorderLayout());
                newCustomerFrame.setSize(400, 200);
                newCustomerFrame.setLocation(400, 400);
                
                JPanel p = new JPanel();
                
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
                
                JPanel p1 = new JPanel();
                p1.add(new JLabel("First Name: "));
                p1.add(firstNameField);
                p1.add(new JLabel("Last Name: "));
                p1.add(lastNameField);
                JPanel p3 = new JPanel();
                p3.add(new JLabel("Phone Number:"));
                p3.add(phonenofield);
                JPanel p2 = new JPanel();
                p2.add(new JLabel("Address: "));
                p2.add(addressfield);
                p2.add(new JLabel("Email: "));
                p2.add(emailField);
                p.add(p1);
                p.add(p3);
                p.add(p2);
                p.add(addCustomerButton);
                
                newCustomerFrame.add(p);
                
                newCustomerFrame.setVisible(true);
            } else if(e.getSource() == addCustomerButton) {
                String query =
                        "INSERT INTO CUSTOMER(firstname, lastname, address, email, phonenumber) VALUES(" +
                        getValue(firstNameField) + ", " + getValue(lastNameField) + ", " + getValue(addressfield) +
                        ", " + getValue(emailField) + ", " + getValue(phonenofield) + ")";
                try {
                    conn.prepareStatement(query).executeUpdate();
                    listAllUsers();
                    customers.validate();
                    newCustomerFrame.setVisible(false);
                    
                    emailField.setText("");
                    firstNameField.setText("");
                    lastNameField.setText("");
                    phonenofield.setText("");
                    emailField.setText("");
                    addressfield.setText("");
                } catch (Exception ex) {
                   ex.printStackTrace();
                }
            } else if(e.getSource() == oldCust) {
                JFrame oldCustomerFrame = new JFrame();
                oldCustomerFrame.setSize(600, 300);
                oldCustomerFrame.setLocation(400, 400);
                
                JPanel panel = new JPanel();
                t = null;
                
                initUserList();
                panel.add(deleteTable);
                panel.add(updateTable);
                panel.add(bill);
                oldCustomerFrame.add(new JScrollPane(t));
                oldCustomerFrame.add(panel, BorderLayout.SOUTH);
                
                oldCustomerFrame.setVisible(true);
//delete
            } else if(e.getSource() == deleteTable) {
                int deleted[] = t.getSelectedRows();

                try {
                    for(int i : deleted) {
                        String deletedId = (String)t.getValueAt(i, 0);
                        
                        conn.prepareStatement("DELETE FROM item " +
                                              "WHERE buyer = " + deletedId).executeUpdate();
                        conn.prepareStatement("DELETE FROM customer " +
                                                    "WHERE cid = " + deletedId).executeUpdate();
                        listAllUsers();
                        initUserList();
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                //update
            } else if (e.getSource() == bill) {
                String s = (String)t.getValueAt(t.getSelectedRow(), 0);
                try {
                    String query =
                            "SELECT SUM(total) FROM item_cost WHERE buyer = " + s;
                    ResultSet rs = conn.prepareStatement(query).executeQuery();
                    rs.next();
                    
                    JOptionPane.showMessageDialog(this, "The customer has spent " + rs.getString(1) + " Birr total.");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }else {
                if(t.isEditing())
     t.getCellEditor().stopCellEditing();
                try {
                    for(int i = 0; i < t.getRowCount(); i++) {
                        for(int j = 1; j < t.getColumnCount(); j++) {
                            conn.prepareStatement("UPDATE Customer " +
                                                  "SET " + t.getColumnName(j) + " = '" + (String)t.getValueAt(i, j) +
                                                  "' WHERE cid = " + (String)t.getValueAt(i, 0)).executeUpdate();

                        }
                            
                    }
                    listAllUsers();
                    initUserList();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    // list user update combobox if database change
    private void listAllUsers() throws SQLException {
        if(customers == null)
            customers = new JComboBox<>();
        
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Customer");
        
        ResultSet rs = ps.executeQuery();
        rs.next();
        
        int ncustomer = Integer.parseInt(rs.getString(1));
        ResultSet custom = conn.prepareStatement("SELECT * FROM Customer").executeQuery();
        
        customers.removeAllItems();
        
        String customers[] = new String[ncustomer];
        
        for(int i = 0; i < ncustomer; i++) {
            custom.next();
            this.customers.addItem(custom.getString("cid") + ",  " + custom.getString("firstname") + " " + custom.getString("lastname"));
        }
        
    }
    //update table if database change
    private void initUserList() {
        if(model == null) {
            model = new DefaultTableModel();
            
            model.addColumn("cid");
            model.addColumn("firstname");
            model.addColumn("lastname");
            model.addColumn("address");
            model.addColumn("email");
            model.addColumn("phonenumber");
        }
        
        model.setRowCount(0);
        if(t == null)
            t = new JTable(model);
        
        try {
            ResultSet rs = conn.prepareStatement("SELECT * FROM customer").executeQuery();
            
            while(rs.next()) {
                model.addRow(new Object[]{rs.getString("cid"), rs.getString("firstname"),
                                          rs.getString("lastname"), rs.getString("address"),
                                          rs.getString("email"), rs.getString("phonenumber")});
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getValue(JTextField f) {
        if(f.getText().equals(""))
            return "null";
        else
            return "'" + f.getText() + "'";
    }
    
}

public class Portm {
    public static void main(String[] args) {
        try{
        new MyGUI();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}