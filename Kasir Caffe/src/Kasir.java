import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Kasir extends JFrame implements ActionListener {
    private final JTextField tfTotal;
    private final JButton btnSubmit;
    private final JRadioButton rbQRIS;
    private final JRadioButton rbTunai;
    private final JRadioButton rbDebit;
    private double totalAmount = 0.0;

    private final JPanel panelItems;
//constructor
    public Kasir() {

        setTitle("Kasir Caffeku");
        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        tfTotal = new JTextField();
        tfTotal.setEditable(false);

        btnSubmit = new JButton("Submit");


        panelItems = new JPanel();
        panelItems.setLayout(new GridBagLayout());


        // Add items
        addItem("Es Teh", 5000);
        addItem("Mangga", 10000);
        addItem("Alpukat", 12000);
        addItem("Jus Jeruk", 13000);


        setLayout(new BorderLayout());
        add(panelItems, BorderLayout.CENTER);
        add(tfTotal, BorderLayout.NORTH);
        add(btnSubmit, BorderLayout.SOUTH);


        btnSubmit.addActionListener(this);

        rbQRIS = new JRadioButton("QRIS");
        rbTunai = new JRadioButton("Tunai");
        rbDebit = new JRadioButton("Debit");
        // Add action listeners to checkboxes
        ButtonGroup paymentOptions = new ButtonGroup();
        paymentOptions.add(rbQRIS);
        paymentOptions.add(rbTunai);
        paymentOptions.add(rbDebit);


        rbQRIS.addActionListener(this);
        rbTunai.addActionListener(this);
        rbDebit.addActionListener(this);


        JPanel panelPayment = new JPanel();
        panelPayment.setLayout(new FlowLayout());
        panelPayment.add(rbQRIS);
        panelPayment.add(rbTunai);
        panelPayment.add(rbDebit);


        add(panelPayment, BorderLayout.EAST);
    }
//add
    private void addItem(String itemName, double itemPrice) {
        int row = panelItems.getComponentCount(); // Menggunakan baris terakhir yang terisi

        JPanel panelItem = new JPanel();
        panelItem.setLayout(new GridBagLayout());

        JLabel lblItemName = new JLabel(itemName);
        JLabel lblItemPrice = new JLabel(String.format("Rp. %.0f", itemPrice));

        JButton btnAdd = new JButton("+");
        JTextField tfQuantity = new JTextField("0", 3);
        JButton btnSubtract = new JButton("-");

        btnAdd.addActionListener(e -> {
            int quantity = Integer.parseInt(tfQuantity.getText());
            quantity++;
            tfQuantity.setText(String.valueOf(quantity));
            updateTotal();
        });

        btnSubtract.addActionListener(e -> {
            int quantity = Integer.parseInt(tfQuantity.getText());
            if (quantity > 0) {
                quantity--;
                tfQuantity.setText(String.valueOf(quantity));
                updateTotal();
            }
        });

        GridBagConstraints gbcItemName = new GridBagConstraints();
        gbcItemName.gridx = 0;
        gbcItemName.gridy = 0;
        gbcItemName.anchor = GridBagConstraints.WEST;
        gbcItemName.insets = new Insets(0, 10, 0, 10);
        panelItem.add(lblItemName, gbcItemName);

        GridBagConstraints gbcItemPrice = new GridBagConstraints();
        gbcItemPrice.gridx = 1;
        gbcItemPrice.gridy = 0;
        gbcItemPrice.anchor = GridBagConstraints.WEST;
        gbcItemPrice.insets = new Insets(0, 0, 0, 10);
        panelItem.add(lblItemPrice, gbcItemPrice);
        //qty
        GridBagConstraints gbcQuantity = new GridBagConstraints();
        gbcQuantity.gridx = 2;
        gbcQuantity.gridy = 0;
        gbcQuantity.anchor = GridBagConstraints.WEST;
        gbcQuantity.insets = new Insets(0, 10, 0, 10);
        panelItem.add(tfQuantity, gbcQuantity);




        //tambah
        GridBagConstraints gbcAdd = new GridBagConstraints();
        gbcAdd.gridx = 3;
        gbcAdd.anchor = GridBagConstraints.WEST;
        panelItem.add(btnAdd, gbcAdd);

        //kurang
        GridBagConstraints gbcSubtract = new GridBagConstraints();
        gbcSubtract.gridx = 4;
        gbcSubtract.anchor = GridBagConstraints.WEST;
        panelItem.add(btnSubtract, gbcSubtract);

        GridBagConstraints gbcPanelItem = new GridBagConstraints();
        gbcPanelItem.gridx = 0;
        gbcPanelItem.gridy = row;
        gbcPanelItem.anchor = GridBagConstraints.WEST;
        gbcPanelItem.insets = new Insets(5, 10, 5, 10);
        panelItems.add(panelItem, gbcPanelItem);
    }

    private void updateTotal() {
        totalAmount = 0.0;

        for (Component component : panelItems.getComponents()) {
            if (component instanceof JPanel panelItem) {
                String quantityStr = ((JTextField) panelItem.getComponent(2)).getText();
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(((JLabel) panelItem.getComponent(1)).getText().substring(3));
                totalAmount += quantity * price;
            }
        }

        tfTotal.setText(String.format("Total: Rp. %.2f", totalAmount));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            showNota();
        } else if (e.getSource() == rbQRIS || e.getSource() == rbDebit) {
            updateTotal();
        } else if (e.getSource() == rbTunai) {

            String cashAmountStr = JOptionPane.showInputDialog(this, "Masukkan nominal uang tunai:");
            if (cashAmountStr != null && !cashAmountStr.isEmpty()) {
                try {
                    double cashAmount = Double.parseDouble(cashAmountStr);


                    System.out.println("Nominal uang tunai: " + cashAmount);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Format uang tunai tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                    rbTunai.setSelected(false);
                }
            } else {
                rbTunai.setSelected(false);
            }
            updateTotal();
        }
    }



    private void showNota() {
        StringBuilder notaText = new StringBuilder("==========================\n" +
                " \t\t Caffe ku\n" +
                "==========================\n\n" +
                "BUKTI PEMBAYARAN\n");

        double cashAmount = 0.0;
        // waktu
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        notaText.append("Date/Time: ").append(formattedTime).append("\n");
        notaText.append("Kasir: Aditiya Subakti ").append("\n==========================\n\n");

        for (Component component : panelItems.getComponents()) {
            if (component instanceof JPanel panelItem) {
                String itemName = ((JLabel) panelItem.getComponent(0)).getText();
                String quantityStr = ((JTextField) panelItem.getComponent(2)).getText();
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(((JLabel) panelItem.getComponent(1)).getText().substring(3));
                double itemTotal = quantity * price;

                if (quantity > 0) {
                    notaText.append(itemName).append(" - Rp.").append(price).append("0 x ").append(quantity).append(" \t= Rp.").append(itemTotal).append("0\n");
                }
            }
        }

        notaText.append("=========================\nTotal Amount: Rp. ").append(totalAmount).append("0");

        if (rbTunai.isSelected()) {
            String cashAmountStr = JOptionPane.showInputDialog(this, "Masukkan nominal uang tunai:");
            if (cashAmountStr != null && !cashAmountStr.isEmpty()) {
                try {
                    cashAmount = Double.parseDouble(cashAmountStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Format uang tunai tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                    rbTunai.setSelected(false);
                }
            } else {
                rbTunai.setSelected(false);
            }

            double change = cashAmount - totalAmount;
            notaText.append("\nTunai: Rp. ").append(cashAmount).append("0");
            notaText.append("\nKembalian: Rp. ").append(change < 0 ? "0.00" : String.format("%.2f", change));
        }

        notaText.append("\n PPN\t\t\t:0.00");
        notaText.append("\nPayment Method: ");

        if (rbQRIS.isSelected()) {
            notaText.append("QRIS");
        } else if (rbTunai.isSelected()) {
            notaText.append("Tunai");
        } else if (rbDebit.isSelected()) {
            notaText.append("Debit");
        }

        int option = JOptionPane.showOptionDialog(this, notaText.toString(), "Nota", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Print", "OK"}, "OK");

        if (option == JOptionPane.YES_OPTION) {
            printNota(notaText.toString());
        }
    }

    private void printNota(String notaText) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();

        Paper paper = new Paper();
        paper.setSize(58.0 * 72.0 / 25.4, 50.0 * 72.0 / 25.4);
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);

        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                g2d.drawString(notaText, 10, 10);

                return Printable.PAGE_EXISTS;
            }
        }, pageFormat);

        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Error printing nota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
