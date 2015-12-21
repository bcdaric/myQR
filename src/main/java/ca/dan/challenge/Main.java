package ca.dan.challenge;

import ca.dan.challenge.view.QRCli;
import ca.dan.challenge.view.QRSwingUI;

/**
 * Application entry point.
 *
 * @author      Dan Richard
 * @version     %I%, %G%
 * @since       1.0
 */
public class Main {

    /**
     * Application main method invoked on startup.
     *
     * @param args  Arguments provided to the program on startup
     */
    public static void main(String[] args) {

        // Initialize the Swing UI if no program arguments are provided.
        if(args.length == 0) {
            QRSwingUI mainFrame = new QRSwingUI();
            mainFrame.setVisible(true);
        } else {
            // Command line options, for the geekier of us...
            // If args are provided, proceed with command line processing.
            if(args.length > 0) {
                QRCli qrCli = new QRCli(args);
            }
        }
    }
}
