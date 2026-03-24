package iuh.fit;

import iuh.fit.adapter.JsonService;
import iuh.fit.adapter.JsonToXmlAdapter;
import iuh.fit.adapter.XmlService;
import iuh.fit.adapter.XmlToJsonAdapter;

public class Main {
    public static void main(String[] args) {
        // Hệ thống XML cũ
        XmlService xmlService = new XmlService();

        // Adapter để dùng Json
        JsonService adapter = new XmlToJsonAdapter(xmlService);

        System.out.println("JSON -> XML");
        adapter.processJson("{name : 'Vũ'}");

        // ngược lại
        System.out.println("\n XML -> JSON");
        JsonToXmlAdapter reverseAdapter = new JsonToXmlAdapter();
        String json  = reverseAdapter.convertXmlToJson("<name>Vũ</name>");

        System.out.println("Kết quả JSON: " + json);
    }
}
