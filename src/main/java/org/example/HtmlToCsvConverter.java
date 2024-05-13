package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

public class HtmlToCsvConverter {
    public static void test() {
        try {
            // Parse HTML File
            String htmlFile = "C:\\Users\\KanikaPathak\\Downloads\\My_Activity_Full.html";
            Document doc = Jsoup.parse(new java.io.File(htmlFile), "UTF-8");
            String bodyHtml = doc.body().html();
            System.out.println("HTML parsing successful");

            // Get all <p class="mdl-typography--title"> elements
            Elements titleElements = doc.select(".mdl-typography--title");

            // Write data to CSV file
            String csvFile = "output.csv";
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
                // Write header
                writer.writeNext(new String[]{"Title", "Action", "Amount", "TxnDetails", "Date", "Status", "OtherDetails"});

                // Iterate over title elements and extract data
                for (Element titleElement : titleElements) {

                    String date = null;
                    String txnDetails = "";
                    try {
                        String title = titleElement.text();
                        Element parentDiv1 = titleElement.parent();
                        Element parentDiv = parentDiv1.parent();
                        Node contentNode = parentDiv.childNodes().get(1); /*Content node */

                        // extract action and Amount from content --
                        String content = contentNode.childNode(0) != null ? String.valueOf(contentNode.childNode(0)) : null;

                        String[] contentData = content.split(" ");
                        String action = contentData[0];
                        String amount = contentData[1];

                        date = contentNode.childNode(2) != null ? String.valueOf(contentNode.childNode(2)) : null;
                        date = date.replace(" ", "");
                        for (int i = 3; i < contentData.length; i++) {
                            txnDetails += contentData[i] + " ";
                        }


                        Node otherElements = parentDiv.childNodes().get(3);

                        String otherDetails = otherElements.childNodes().get(6) != null ? otherElements.childNodes().get(6).toString() : null;
                        otherDetails = otherDetails.replace(" ", "");
                        String status = otherElements.childNodes().get(8) != null ? otherElements.childNodes().get(8).toString() : null;
                        status = status.replace(" ", "");

                        // Write data
                        writer.writeNext(new String[]{title, action, amount, txnDetails.trim(), date.trim(), status, otherDetails});
                    }
                    catch(Exception e) {
                        System.out.println("Exception in node iteration for : "+ txnDetails + date);
                        e.printStackTrace();
                    }

                    }

                System.out.println("CSV file has been created successfully!");

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}



