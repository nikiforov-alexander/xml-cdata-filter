package com.example;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private Node getNodeWithElementAtIndex(String elementName, int index) {
        return document.getElementsByTagName(elementName).item(index);
    }

    // tests

    @Test
    public void replaceBadEntities_canReplaceNodeOfTheDocument() throws Exception {
        // Given the Node with bad character
        Node node = getNodeWithElementAtIndex("firstName", 0);
        assertThat(node.getTextContent()).contains("&apos;");

        // When we call replaceBadEntities with that Node and given Map
        Node changedNode = xmlFilter.replaceBadEntities(node);

        // Then we should get the Node replaced
        assertThat(changedNode.getTextContent()).doesNotContain("&apos;");
    }

    // integration test

    @Test
    public void filter_replacesTheBadNodesAndReturnsCorrectDocument() throws Exception {
        // Given the Document with 4 bad CDATA groups
        List<Node> badNodes = Arrays.asList(
                getNodeWithElementAtIndex("firstName", 0),
                getNodeWithElementAtIndex("lastName", 0),
                getNodeWithElementAtIndex("nickname", 0),
                getNodeWithElementAtIndex("salary", 0)
        );
        for (Node node: badNodes) {
            assertThat(node.getTextContent()).contains("&apos;");
        }

        // When we call filter()
        xmlFilter.filter();

        // Then 4 nodes should not contain bad characters
        badNodes = Arrays.asList(
            getNodeWithElementAtIndex("firstName", 0),
            getNodeWithElementAtIndex("lastName", 0),
            getNodeWithElementAtIndex("nickname", 0),
            getNodeWithElementAtIndex("salary", 0)
        );
        for (Node node: badNodes) {
            assertThat(node.getTextContent()).doesNotContain("&apos;");
        }
    }
}