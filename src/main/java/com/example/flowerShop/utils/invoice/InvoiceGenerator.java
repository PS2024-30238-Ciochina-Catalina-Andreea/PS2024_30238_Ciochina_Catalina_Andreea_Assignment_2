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

            Font fontTitlu = FontFactory.getFont(FontFactory.TIMES_BOLD, 24, BaseColor.PINK);
            Font fontAntet = FontFactory.getFont(FontFactory.TIMES_BOLD, 16, BaseColor.BLACK);
            Font fontText = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
            Font fontMesaj = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, BaseColor.GRAY);

            Paragraph titlu = new Paragraph("Factura comanda", fontTitlu);
            titlu.setAlignment(Element.ALIGN_CENTER);
            titlu.setSpacingAfter(20);
            document.add(titlu);

            document.add(createParagraf("ID Comanda: " + order.getId(), fontAntet));
            document.add(createParagraf("Data Comenzii: " + order.getOrderDate(), fontText));
            document.add(createParagraf("Metoda de Plata: " + order.getPay(), fontText));
            document.add(createParagraf("Adresa de Livrare: " + order.getAddress(), fontText));
            document.add(createParagraf("Pret Total: " + order.getTotalPrice(), fontAntet));

            document.add(createParagraf("Va multumim ca ati ales floraria noastra!", fontMesaj));

        } finally {
            if (document != null) {
                document.close();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static Paragraph createParagraf(String content, Font font) {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setSpacingAfter(10);
        return paragraph;
    }
}
