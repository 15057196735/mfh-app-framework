/*
 * 文件名称: W3cDomUtils.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-21
 * 修改内容: 
 */
package com.mfh.comn.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mfh.comn.Constants;

/**
 * W3cDom工具类
 * 
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-21
 * @since SHK Framework 1.0
 */
public final class W3cDomUtils {

    /** 默认构造函数 */
    private W3cDomUtils() {
    }

    /**
     * 复制源Document下的根节点的所有子节点到目标Document根节点下
     * 
     * @param from 源Document
     * @param to 目标Document
     * @author LuoJingtian created on 2011-12-21
     * @since SHK Framework 1.0
     */
    public static void copy(Document from, Document to) {
        Element fromRootElem = from.getDocumentElement();
        Element toRootElem = to.getDocumentElement();
        Node childNode, importedNode;
        NodeList toChildNodes = fromRootElem.getChildNodes();
        for (int i = 0; i < toChildNodes.getLength(); i++) {
            childNode = toChildNodes.item(i);
            importedNode = to.importNode(childNode, true);
            toRootElem.appendChild(importedNode);
        }
    }

    /**
     * 将指定的doc转换为Xml字符串
     * 
     * @param doc Document
     * @return Xml字符串
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @author LuoJingtian created on 2011-12-21
     * @since SHK Framework 1.0
     */
    public static String toXmlString(Document doc) throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, TransformerException {
        String xmlString = null;
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        xmlString = sw.toString();
        return xmlString;
    }

    /**
     * 将指定的dom转换为Xml字符串
     * @param node
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @author zhangyz created on 2012-4-18
     */
    public static String toXmlString(Node node) throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, TransformerException {
        String xmlString = null;
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(node);
        trans.transform(source, result);
        xmlString = sw.toString();
        return xmlString;
    }
    
    /**
     * 保存到文件
     * @param doc
     * @param fileOut
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @author zhangyz created on 2012-3-22
     */
    public static void toXmlFile(Document doc, File fileOut, String charset) throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, TransformerException {
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        java.io.OutputStreamWriter writer = null;
        try {
            if (charset == null)
                charset = Constants.defaultCode;
            writer = new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileOut), charset);
            String head = "<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\r\n";
            writer.write(head);
            StreamResult result = new StreamResult(writer);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if (writer != null){
                try {
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将xmlString 转换为 w3c Document 对象
     * 
     * @param xmlString xml String
     * @return Document w3c Document 对象
     */
    public static Document fromXmlString(String xmlString) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            return db.parse(is);
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static XPath path = XPathFactory.newInstance().newXPath();
    public static String getStringValueByXPath(Node node, String xPath) throws XPathExpressionException {
        return path.evaluate(xPath, node);
    } 
    
    /**
     * 获取指定节点的一个子节点
     * @param ele
     * @param tagName
     * @return
     * @author zhangyz created on 2012-3-22
     */
    public static Element getSubElement(Element ele, String tagName){
        NodeList list = ele.getElementsByTagName(tagName);
        if (list.getLength() > 0){
            return (Element)list.item(0);
        }
        else
            return null;
    }
}
