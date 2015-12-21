package ca.dan.challenge.view;

import ca.dan.challenge.controller.GenerateQRBarcodeAction;
import ca.dan.challenge.domain.QRBarcode;
import com.google.zxing.WriterException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Swing UI form to gather data and generate the QR Barcode.
 *
 * @author      Dan Richard
 * @version     %I%, %G%
 * @since       1.0
 */
public class QRSwingUI extends JFrame {

    private JPanel pnlMain;
    private JLabel lblTitle;
    private JButton btnGenerateQR;
    private JLabel lblUrl;
    private JTextField txtUrl;
    private JLabel lblMessage;
    private JLabel lblSize;
    private JSlider sldSize;
    private JComboBox cboBGColour;
    private JLabel lblFGColour;
    private JComboBox cboFGColour;
    private JLabel lblPath;
    private JLabel lblSelectedSize;

    // Enumeration of colours to pick from.
    private enum Hue {
        Black(Color.black), White(Color.white), Cyan(Color.cyan), Magenta(Color.magenta),
        Yellow(Color.yellow), Red(Color.red), Green(Color.green), Blue(Color.blue);

        private final Color color;

        Hue(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    /**
     * Default constructor - Generates the Swing UI.
     */
    public QRSwingUI() {

        setTitle("QR Coderator 3000");
        getRootPane().setDefaultButton(btnGenerateQR);
        setContentPane(pnlMain);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        sldSize.addChangeListener(changeListener);
        sldSize.setValue(250);

        lblSelectedSize.setText("Current Size: " + sldSize.getValue() + "x" + sldSize.getValue() + " px");
        lblMessage.setText(" ");
        lblPath.setText(" ");

        // Set a pre-defined list of colours to choose from, defaulting to black for the FG.
        for (Hue h : Hue.values()) {
            cboFGColour.addItem(h);
        }
        cboFGColour.setSelectedIndex(0);

        // Set a pre-defined list of colours to choose from, defaulting to white for the BG and black for the FG.
        for (Hue h : Hue.values()) {
            cboBGColour.addItem(h);
        }
        cboBGColour.setSelectedIndex(1);

        // Action listener for the button press.
        ActionListener generateButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Only proceed if the data is valid.
                if(validateForm()) {
                    // Create the QR Barcode.
                    generateQRCode();
                }
            }
        };
        btnGenerateQR.addActionListener(generateButtonListener);
    }


    // Listener responsible for updating the JLabel on QR Size (JSlider) movement.
    private ChangeListener changeListener = new ChangeListener()
    {
        public void stateChanged(ChangeEvent ce)
        {
            JSlider slider = (JSlider) ce.getSource();
            lblSelectedSize.setText("Current Size: " + slider.getValue() + "x" + slider.getValue() + " px");
        }
    };


    /**
     * Validate the form elements whose business constraints are not enforceable by the Swing attributes.
     *
     * @return  Status of the form validity
     */
    private boolean validateForm() {
        boolean bValidForm = true;
        // Clear any previous errors
        lblMessage.setText(" ");

        // Validate URL
        final Pattern pattern = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");

        if (txtUrl.getText().length() > 0) {
            if (!pattern.matcher(txtUrl.getText()).matches()) {
                bValidForm = false;
                lblMessage.setText("C'mon now, get the URL right man! ");
                System.out.println("ERROR: Invalid URL format");
            }
        } else {
            bValidForm = false;
            lblMessage.setText("At least try... enter a URL buddy! ");
            System.out.println("ERROR: Empty URL");
        }

        return bValidForm;
    }


    /**
     * Generate the actual QRBarcode file.
     */
    public void generateQRCode() {

        String errorWriteMsg = "\nOOPS! We had an issue writing the QR Barcode file to disk. Email matt.mackenzie@medavie.bluecross.ca to get ya sorted.";
        String errorQRBarcodeMsg = "\nOOPS! We had an issue generating the QR Barcode. Email matt.mackenzie@medavie.bluecross.ca to get ya sorted.";
        String successMsg = "\nWoohoo! You've now got yourself a fancy QR Code! Grab it here:";

        Color bgColor = Hue.valueOf(cboBGColour.getSelectedItem().toString()).getColor();
        Color fgColor = Hue.valueOf(cboFGColour.getSelectedItem().toString()).getColor();

        QRBarcode qrBarcode = new QRBarcode(txtUrl.getText(), sldSize.getValue(), bgColor, fgColor);

        // Generate the QRBarcode file.
        GenerateQRBarcodeAction generateQRBarcodeAction = new GenerateQRBarcodeAction();
        try {
            String qrPath = generateQRBarcodeAction.generateQRBarcodeAction(qrBarcode);
            lblMessage.setForeground(new Color(0, 94, 133));
            lblMessage.setText(successMsg);
            lblPath.setForeground(new Color(0, 94, 133));
            lblPath.setText(qrPath.replace("./", ""));
        } catch(IOException e) {
            lblMessage.setForeground(new Color(255, 0, 0));
            lblMessage.setText(errorWriteMsg);
        } catch(WriterException e) {
            lblMessage.setForeground(new Color(255, 0, 0));
            lblMessage.setText(errorQRBarcodeMsg);
        }
    }
}