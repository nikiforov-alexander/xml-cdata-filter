package com.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class XmlFilter {

    // fields

    private Document document;
    private NodeList allNodesList;
    private Map<String, String> badEntitiesReplacementMap;

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

    public Map<String, String> getBadEntitiesReplacementMap() {
        return badEntitiesReplacementMap;
    }

    public void setBadEntitiesReplacementMap(Map<String, String> badEntitiesReplacementMap) {
        this.badEntitiesReplacementMap = badEntitiesReplacementMap;
    }


    // constructors

    public XmlFilter(Document document) {
       this.document = document;
       this.allNodesList = document.getChildNodes();
       this.badEntitiesReplacementMap = new HashMap<>();
    }

    // overrides

    // methods

    /**
     * This method fills {@link #badEntitiesReplacementMap}
     * with values which will be replaced in &lt;![CDATA[]]&gt;
     * section. For now only {@literal &apos; -> '} is there.
     * {@link #badEntitiesReplacementMap} is filled in constructors.
     */
    void fillBadEntitiesReplacementMap() {
        badEntitiesReplacementMap.put("&apos;", "'");
    }

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
