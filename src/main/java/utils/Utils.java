package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    public static Integer getIntegerValueOfPrice(String price) {
        String priceValue = price.replaceAll("[^\\d.]", "");
        return Integer.parseInt(priceValue);
    }

    public static String getStringValueOfPrice(Integer price) {
        return NumberFormat.getNumberInstance(Locale.US).format(price);
    }

    public static String readPdfFile(File file) {
        try (PDDocument receiptFile = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String receiptContent = stripper.getText(receiptFile);
            return receiptContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
