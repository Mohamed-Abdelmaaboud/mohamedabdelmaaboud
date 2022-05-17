package com.company.invoicegenerator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;


public class InvoiceGenenrator extends JFrame implements ActionListener {
    //Jpanel right side
    private JPanel rightMainPanel;
    private JPanel rightSub1Panel;
    private JPanel rightSub2Panel;
    private JPanel rightSub3Panel;
    private JPanel rightSub4Panel;
    private JPanel rightTablePanel;


    //Jpanel left Side Panel
    private JPanel leftSidePanel;
    private JPanel panelButtonRight;
    private JPanel panelButtonLeft;


    //menu fields
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem loadFile;
    private JMenuItem saveFile;

    //Jtext field
    private JTextField invoiceDate;
    private JTextField customerName;
    // private  JTextField

    //Buttons fields
    private JButton createNewVoicesBtn;
    private JButton deleteInvoiceBtn;
    private JButton SaveBtn;
    private JButton CancelBtn;

    // table fields
    private JTable invoicesTable;
    private JTable invoiceItemsTable;
    // private JLabel table1Label;
    // private JLabel table2Label;
    private String[][] InvoiceTableData = new String[3][4];
    private String[] colInvoiceTable = {"No.", "Date", "Customer", "Total"};
    //private String [][] InvoiceTableData = {{"1","22-11-2020","Ali"},{"2","13-10-2021","Salah"}};

    private String[] colInvoiceItemsTable = {"No.", "itemName", "itemPrice", "Count", "itemTotal"};

    private String[][] ItemsTableData = new String[3][5];

   /* private String [][]ItemsTableData = {{"1","Mobile","3200","1"},
            {"1","Cover","20","2"},
            {"1","Headphone","130","1"}};*/

    public InvoiceGenenrator() {
        super("SIG APP");


        setLayout(new BorderLayout());


        // menu bar
        menuBar = new JMenuBar();

        //load file item

        loadFile = new JMenuItem("loadFile", 'l');
        loadFile.addActionListener(this);
        loadFile.setActionCommand("load");
        saveFile = new JMenuItem("saveFile", 's');
        saveFile.addActionListener(this);
        saveFile.setActionCommand("saveFile");

        // file menu
        fileMenu = new JMenu("File");

        //add item menu to file menu
        fileMenu.add(loadFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);

        // add file menu to menu bar
        menuBar.add(fileMenu);

        //add menu bar to frame
        setJMenuBar(menuBar);


        //create invoice table
        leftSidePanel = new JPanel();
        leftSidePanel.setLayout(new BorderLayout());
        invoicesTable = new JTable(InvoiceTableData, colInvoiceTable);
        JScrollPane scrollPane1 = new JScrollPane(invoicesTable);
        JPanel labelPanel = new JPanel();
        JLabel invoiceTable = new JLabel("Invoices Table");
        labelPanel.add(invoiceTable);
        leftSidePanel.add(labelPanel, BorderLayout.PAGE_START);
        leftSidePanel.add(scrollPane1, BorderLayout.WEST);
        add(leftSidePanel, "West");

        // add button to left side
        panelButtonLeft = new JPanel();
        panelButtonLeft.setLayout(new FlowLayout());
        createNewVoicesBtn = new JButton("Create New Invoices");
        panelButtonLeft.add(createNewVoicesBtn);
        createNewVoicesBtn.addActionListener(this);
        createNewVoicesBtn.setActionCommand("create");
        deleteInvoiceBtn = new JButton("Delete Invoices");
        panelButtonLeft.add(deleteInvoiceBtn);
        deleteInvoiceBtn.addActionListener(this);
        deleteInvoiceBtn.setActionCommand("delete");
        leftSidePanel.add(panelButtonLeft, "South");


        // fields panels
        rightMainPanel = new JPanel();
        rightMainPanel.setLayout(new GridLayout(3, 3));
        //invoice number filed
        JPanel rightSubPanel = new JPanel();
        GridLayout layout = new GridLayout(6, 5);
        layout.setHgap(10);
        layout.setVgap(-3);
        rightSubPanel.setLayout(layout);
        rightSubPanel.add(new JLabel("Invoice Number"));

        //invoice data field

        rightSubPanel.add(new JLabel("Invoice Date"));
        invoiceDate = new JTextField(10);
        //rightSub2Panel.add(invoiceDate);
        rightSubPanel.add(invoiceDate);

        //customer name fields

        rightSubPanel.add(new JLabel("Customer Name"));
        customerName = new JTextField(10);

        rightSubPanel.add(customerName);

        rightSubPanel.add(new JLabel("Invoice Total"));

        //add sub panels to main panel
        rightMainPanel.add(rightSubPanel);
        invoiceItemsTable = new JTable(ItemsTableData, colInvoiceItemsTable);
        JScrollPane scrollPane2 = new JScrollPane(invoiceItemsTable);
        rightMainPanel.add(scrollPane2);

        // add button to right side
        panelButtonRight = new JPanel();
        panelButtonRight.setLayout(new FlowLayout());
        SaveBtn = new JButton("Save");
        panelButtonRight.add(SaveBtn);
        SaveBtn.addActionListener(this);
        SaveBtn.setActionCommand("Save");
        CancelBtn = new JButton("Cancel");
        panelButtonRight.add(CancelBtn);
        CancelBtn.addActionListener(this);
        CancelBtn.setActionCommand("cancel");
        rightMainPanel.add(panelButtonRight, "South");
        pack();

        // add main panel to frame
        add(rightMainPanel, "East");


        setSize(925, 500);
        setLocation(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand() == "load"){
            loadFile();
        }else if(e.getActionCommand() == "saveFile"){
            saveFile();
        }else if(e.getActionCommand() == "create"){
            createBtn();

        }else if(e.getActionCommand()=="delete"){
            deleteBtn();

        }else if(e.getActionCommand() == "save"){
            saveBtn();

        }else if(e.getActionCommand()=="cancel"){
            cancelBtn();

        }

    }


    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();

            FileInputStream fis = null;

            try {
                fis = new FileInputStream(path);

                /*int size = fis.available();
                byte[] bytes = new byte[size];
                fis.read(bytes);*/
                DefaultTableModel csv_data = new DefaultTableModel();

                try {
                    int start = 0;
                    InputStreamReader inputStreamReader =
                            new InputStreamReader(fis);
                    org.apache.commons.csv.CSVParser csvParser =
                            CSVFormat.DEFAULT.parse(inputStreamReader);

                    for (CSVRecord csvRecord : csvParser) {
                        if (start == 0) {
                            start = 1;
                            csv_data.addColumn(csvRecord.get(0));
                            csv_data.addColumn(csvRecord.get(1));
                            csv_data.addColumn(csvRecord.get(2));
                            csv_data.addColumn(csvRecord.get(3));
                        } else {
                            Vector row = new Vector();
                            row.add(csvRecord.get(0));
                            row.add(csvRecord.get(1));
                            row.add(csvRecord.get(2));
                            row.add(csvRecord.get(3));
                            row.add(csvRecord.get(4));
                            csv_data.addRow(row);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                invoicesTable.setModel(csv_data);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {

            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        }
    private void saveFile() {


           JFileChooser fc = new JFileChooser();
           int result = fc.showOpenDialog(this);
           if(result == JFileChooser.APPROVE_OPTION){
            String path = fc.getSelectedFile().getPath();

            JTable table = null;
            try {

                TableModel model = table.getModel();
                FileWriter csv = new FileWriter(new File(path));

                for (int i = 0; i < model.getColumnCount(); i++) {
                    csv.write(model.getColumnName(i) + ",");
                }

                csv.write("\n");

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        csv.write(model.getValueAt(i, j).toString() + ",");
                    }
                    csv.write("\n");
                }

                csv.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void saveBtn(){

    }

    private void deleteBtn(){
        invoicesTable.setVisible(false);
        //invoiceItemsTable.dispose();
        invoicesTable = null;



    }

    private void cancelBtn(){
        customerName.setText("");
        invoiceDate.setText("");
        invoiceItemsTable.setVisible(false);
        invoiceItemsTable=null;


    }
    private void createBtn(){
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            String path = fc.getSelectedFile().getPath();

            JTable table = null;
            try {

                TableModel model = table.getModel();
                FileWriter invoice = new FileWriter(new File(path));

                for (int i = 0; i < model.getColumnCount(); i++) {
                    invoice.write(model.getColumnName(i) + ",");
                }

                invoice.write("\n");

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        invoice.write(model.getValueAt(i, j).toString() + ",");
                    }
                    invoice.write("\n");
                }

                invoice.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}










