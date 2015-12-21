package ca.dan.challenge.view;

import ca.dan.challenge.Main;
import ca.dan.challenge.controller.GenerateQRBarcodeAction;
import ca.dan.challenge.domain.QRBarcode;
import com.google.zxing.WriterException;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command line interface to gather data and generate the QR Barcode file.
 *
 * @author      Dan Richard
 * @version     %I%, %G%
 * @since       1.0
 */
public class QRCli {

    private static Options options = new Options();

    /**
     * Default constructor - Processes command line arguments for URL and Size.
     *
     * @param args  Command line arguments
     */
    public QRCli(String[] args) {
        Logger log = Logger.getLogger(Main.class.getName());

        String errorWriteMsg = "\nOOPS! We had an issue writing the QR Barcode file to disk. Email matt.mackenzie@medavie.bluecross.ca to get ya sorted.";
        String errorQRBarcodeMsg = "\nOOPS! We had an issue generating the QR Barcode. Email matt.mackenzie@medavie.bluecross.ca to get ya sorted.";
        String successMsg = "\nWoohoo! You've got yourself a QR Code! Grab it here: ";

        String qrUrl = "http://www.google.ca";
        int qrSize = 250;

        Option optUrl = Option.builder("u")
                .argName("u")
                .hasArg()
                .required(false)
                .desc("URL to embed in QR Code")
                .build();

        Option optSize = Option.builder("s")
                .argName("s")
                .hasArg()
                .required(false)
                .desc("Size (width in pixels) of the QR Code image")
                .type(Number.class)
                .build();

        options.addOption("h", "help", false, "Show help");
        options.addOption(optUrl);
        options.addOption(optSize);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        // Parse the command line properties.
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                CliHelp();
            }
            else {
                if (cmd.hasOption("u")) {
                    log.log(Level.INFO, "Using cli argument -u = " + cmd.getOptionValue("u"));
                    qrUrl = cmd.getOptionValue("u");
                }
                if (cmd.hasOption("s")) {
                    log.log(Level.INFO, "Using cli argument -s = " + cmd.getOptionValue("s"));
                    qrSize = Integer.parseInt(cmd.getOptionValue("s"));
                }

                if(qrUrl != null || qrSize == 0) {
                    QRBarcode qrBarcode = new QRBarcode(qrUrl, qrSize);

                    // Generate the QRBarcode file.
                    GenerateQRBarcodeAction generateQRBarcodeAction = new GenerateQRBarcodeAction();
                    try {
                        String qrPath = generateQRBarcodeAction.generateQRBarcodeAction(qrBarcode);
                        System.out.println(successMsg + qrPath.replace("./",""));
                    } catch(IOException e) {
                        System.out.println(errorWriteMsg);
                    } catch(WriterException e) {
                        System.out.println(errorQRBarcodeMsg);
                    }
                } else {
                    log.log(Level.SEVERE, "One or more command line arguments were not provided");
                }
            }
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            CliHelp();
        }
    }


    /**
     * Display command line help.
     */
    public static void CliHelp() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", options);
    }
}
