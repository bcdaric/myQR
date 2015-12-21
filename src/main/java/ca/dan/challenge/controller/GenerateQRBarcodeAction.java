package ca.dan.challenge.controller;

import ca.dan.challenge.domain.QRBarcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Class responsible for creating the QR Barcode file.
 *
 * @author      Dan Richard
 * @version     %I%, %G%
 * @since       1.0
 */
public class GenerateQRBarcodeAction {

    /**
     * Default public constructor
     */
    public GenerateQRBarcodeAction() {}


    /**
     * Method to generate the QR Barcode file.
     *
     * @param qrBarcode QR Barcode object to convert to a physical file.
     * @return          String representing the path where the image was saved.
     * @throws          IOException
     * @throws          WriterException
     */
    public String generateQRBarcodeAction(QRBarcode qrBarcode) throws IOException, WriterException {

        String filePath = "./";
        File qrFile = null;

        // Generate a unique filename
        qrFile = File.createTempFile("qrcode", ".png", new File(filePath));

        // Defines an error correction (ie. 'damage') level of 7% as per QR barcode standards
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        // Create the raw (encoded) data for the QR barcode image
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrBarcode.getUrl(), BarcodeFormat.QR_CODE, qrBarcode.getSize(), qrBarcode.getSize(), hintMap);

        // Create the image file using the raw barcode image data
        int imgWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(imgWidth, imgWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(qrBarcode.getBgColor());
        graphics.fillRect(0, 0, imgWidth, imgWidth);
        graphics.setColor(qrBarcode.getFgColor());

        // Draw the barcode image
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        // Write the image to disk
        ImageIO.write(image, "PNG", qrFile);

        return qrFile.getAbsolutePath();
    }
}
