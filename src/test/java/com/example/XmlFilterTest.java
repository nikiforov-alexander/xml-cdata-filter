package com.example;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlFilterTest {

    // members

    private Document document;

    private XmlFilter xmlFilter;

    // set up method

    @Before
    public void setUp() throws Exception {
        DocumentBuilderFactory documentBuilderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        File xmlFile = new File("./data/test1.xml");
        document = documentBuilder.parse(xmlFile);
        xmlFilter = new XmlFilter(document);
    }

    // helpful private methods to set up test environment

    private Node getFirstNodeWithBadCharacter() {
        return document.getElementsByTagName("firstName").item(0);
    }

    // tests

    @Test
    public void replaceBadEntities_canReplaceNodeOfTheDocument() throws Exception {
        // Given the Node with bad character
        Node node = getFirstNodeWithBadCharacter();
        assertThat(node.getTextContent()).contains("&apos;");

        // When we call replaceBadEntities with that Node and given Map
        Node changedNode = xmlFilter.replaceBadEntities(node);

        // Then we should get the Node replaced
        assertThat(changedNode.getTextContent()).doesNotContain("&apos;");
    }
}