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

    /**
     * Default constructor, sets up the given {@link #document}
     * to be filtered for bad entities, {@link #allNodesList}
     * because we use it in recursive {@link #filter(NodeList)}
     * function. {@link #badEntitiesReplacementMap} is initialized
     * here as a new {@code HashMap<String,String>} and filled
     * with replacement entities by calling {@link #fillBadEntitiesReplacementMap()}.
     * @param document : Document to be filtered for bad values
     */
    public XmlFilter(Document document) {
       this.document = document;
       this.allNodesList = document.getChildNodes();
       this.badEntitiesReplacementMap = new HashMap<>();
       fillBadEntitiesReplacementMap();
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

    /**
     * Replaces bad entities that are defined as "keys" in
     * {@link #badEntitiesReplacementMap} with good entities
     * that are defined as "values".
     * @param node : Node to be replaced
     * @return Node is returned to be replaced in the original
     * {@link #document}
     */
    Node replaceBadEntities(Node node) {
        for (String badEntity : badEntitiesReplacementMap.keySet()) {
            String oldTextContent = node.getTextContent();
            String newEntity = badEntitiesReplacementMap.get(badEntity);
            node.setTextContent(
                    oldTextContent.replaceAll(badEntity, newEntity)
            );
        }
        return node;
    }

    /**
     * Filters {@link #document} by given {@code nodeList}.
     * This method is <strong>recursive</strong> and goes
     * through all children of given nodes inside {@code nodeList}
     * If he finds &lt;![CDATA[]]&gt; node, then he calls
     * {@link #replaceBadEntities(Node)} method.
     * @param nodeList
     * @return
     */
    public Document filter(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
                node = replaceBadEntities(node);
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
