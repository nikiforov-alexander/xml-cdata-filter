package com.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFilter {

    // fields

    private Document document;
    private NodeList allNodesList;

    // getters and setters

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public NodeList getAllNodesList() {
        return allNodesList;
    }

    public void setAllNodesList(NodeList allNodesList) {
        this.allNodesList = allNodesList;
    }

    // constructors

    public XmlFilter(Document document) {
       this.document = document;
       this.allNodesList = document.getChildNodes();
    }

    // overrides

    // methods

    public Document filter(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
                System.out.println("CDATA " + node.getNodeName()
                        +  ":" + node.getTextContent());
            }

            if (node.hasChildNodes()) {
                filter(node.getChildNodes());
            }
        }
        return this.document;
    }

    public Document filter() {
        filter(allNodesList);
        return this.document;
    }
}
