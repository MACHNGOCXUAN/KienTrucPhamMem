package iuh.fit.adapter;

public class XmlToJsonAdapter implements JsonService {
    private XmlService xmlService;

    public XmlToJsonAdapter(XmlService xmlService) {
        this.xmlService = xmlService;
    }

    @Override
    public void processJson(String jsonData) {
        // convert JSON => XML (giả lâp)
        String xmlData = convertJsonToXml(jsonData);

        System.out.println("Convert JSON -> XML");
        xmlService.processXml(xmlData);
    }

    private String convertJsonToXml(String json) {
        return "<data>" + json + "</data>";
    }
}
