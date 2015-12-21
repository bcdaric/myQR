package ca.dan.challenge.domain;

import java.awt.*;

/**
 * Domain object representing a QRBarcode.
 *
 * @author      Dan Richard
 * @version     %I%, %G%
 * @since       1.0
 */
public class QRBarcode {

    private String url;
    private int size;
    private Color bgColor;
    private Color fgColor;


    /**
     * Constructor used by Command Line where no colours are provided.
     *
     * @param url   URL to encode in the QR Barcode
     * @param size  Dimension (in pixels) of the QR Barcode
     */
    public QRBarcode(String url, int size) {
        this.url = url;
        this.size = size;
        this.bgColor = Color.WHITE;
        this.fgColor = Color.BLACK;
    }


    /**
     * Constructor used by Swing UI where colours are provided.
     *
     * @param url       URL to encode in the QR Barcode
     * @param size      Dimension (in pixels) of the QR Barcode
     * @param bgColor   Color for the QR Barcode background
     * @param fgColor   Color for the QR Barcode contents
     */
    public QRBarcode(String url, int size, Color bgColor, Color fgColor) {
        this.url = url;
        this.size = size;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
    }


    // Public Accessors and Modifiers.
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getFgColor() {
        return fgColor;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }


    /**
     * String output of the object for debugging.
     *
     * @return  String object representing the QRBarcode object.
     */
    public String toString(){
        return "url: " + url + ", " +
                "size: " + size + ", " +
                "fgColor: " + fgColor.toString() + ", " +
                "bgColor: " + bgColor.toString();
    }
}
