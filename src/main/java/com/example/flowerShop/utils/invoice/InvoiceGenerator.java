package com.example.flowerShop.utils.invoice;

import com.example.flowerShop.entity.Order;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;


import java.io.ByteArrayOutputStream;

@Component
public class InvoiceGenerator {

    public static byte[] generateInvoicePDF(Order order) throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Paragraph idParagraph = new Paragraph("Factura pentru comanda cu ID-ul: " + order.getId(), font);
            Paragraph dateParagraph = new Paragraph("Data comenzii: " + order.getOrderDate(), font);
            Paragraph paymentMethodParagraph = new Paragraph("Metoda plata: " + order.getPay(), font);
            Paragraph addressParagraph = new Paragraph("Adresa de livrare: " + order.getAddress(), font);
            Paragraph totalPriceParagraph = new Paragraph("Total de plata: " + order.getTotalPrice(), font);

            document.add(idParagraph);
            document.add(dateParagraph);
            document.add(paymentMethodParagraph);
            document.add(addressParagraph);
            document.add(totalPriceParagraph);
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
