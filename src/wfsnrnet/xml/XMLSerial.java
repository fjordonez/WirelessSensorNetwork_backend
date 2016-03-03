package wfsnrnet.xml;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.util.List;
import java.util.LinkedList;

/*! Used for XML serialization, able to load, save, print, read and
 *  write XML data. It uses internally the XPath library. See \ref
 *  XMLSerialTest for example use.
 */
public class XMLSerial
{
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document=null;
    private TransformerFactory transformerFactory;
    private Transformer transformer=null;

    /*! Constructor 
     */  
    public XMLSerial()
    {      	
    }
    
     /*! Returns a new Document object. The first call constructs the
     *  object, subsequent calls reuse it.
     */ 
    public Document document()
    {
	if (document==null)
	{	    
	    document=documentBuilder().newDocument();
	}
	return document;
    }

    /*! Returns the DocumentBuilder object for creating new
     *  documents. The first call constructs the object, subsequent
     *  calls reuse it.
     */ 
    private DocumentBuilder documentBuilder()
    {
	if (documentBuilder==null)
	{
	    documentBuilderFactory=DocumentBuilderFactory.newInstance();
	    documentBuilderFactory.setIgnoringElementContentWhitespace(true);
	    try 
	    {
		documentBuilder=documentBuilderFactory.newDocumentBuilder();	    
	    }
	    catch (ParserConfigurationException e) 
	    {
	    	System.out.println("XMLSerial::documentBuilder Couldn't create 'documentBuilder': "+e);	    
	    }
	}
	return documentBuilder;
    }

    /*! Returns the Transformer object for saving and printing. The first call constructs the
     *  object, subsequent calls reuse it.
     */ 
    private Transformer transformer()
    {
	if (transformer==null)
	{
	    transformerFactory=TransformerFactory.newInstance();
	    try 
	    {
		transformerFactory.setAttribute("indent-number",2);
		transformer=transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	    }
	    catch (TransformerConfigurationException e) 
	    {
	    	System.out.println("XMLSerial::transformer Couldn't create 'transformer': "+e);
	    }	
	}
	return transformer;
    }

    /*! Converts a node to String and returns it.
     */ 
    public String toString(Node node)
    {
	ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
	StreamResult result = new StreamResult(byteArrayOutputStream);
	transform(node,result);
	return byteArrayOutputStream.toString();
    }

    /*! Saves a node to file.
     */
    public void save(Node node,String filename)
    {
	File xmlOutputFile = new File(filename);
	FileOutputStream fileOutputStream=null;
	try 
	{
	    fileOutputStream = new FileOutputStream(xmlOutputFile);
	    StreamResult result = new StreamResult(fileOutputStream);
	    transform(node,result);
	}
	catch (FileNotFoundException e)
	{
		System.out.println("XMLSerial::save Couldn't open file '"+filename+"' for writing: "+e);
	}
    }

    private void transform(Node node,StreamResult result)
    {	
	DOMSource source = new DOMSource(node);
	try 
	{
	    transformer().transform(source,result);
	}
	catch (TransformerException e) 
	{
		System.out.println("XMLSerial::transform Couldn't transform xml document: "+e);
	}
    }

    /*! Returns the node after loading it from file.
     */
    public Node load(String filename)
    {
	File sourceFile = new File(filename);
	try 
	{	   
	    document=documentBuilder().parse(sourceFile);
	}
	catch (SAXException e)
	{
		System.out.println("XMLSerial::load Couldn't read XML structure in file '"+filename+"': "+e);
	}
	catch (IOException e) 
	{
		System.out.println("XMLSerial::load Couldn't open file '"+filename+"' for reading: "+e);
	}
	return document;
    }

    /*! Returns the named child node or 'null' if it isn't found.
     */
    public Node getChild(Node node,String name)
    {
	return getChild(node,name,false);
    }

    /*! Returns the named child node or 'null' if it isn't found. If
     *  error is 'true' it exits if no child is found with that name.
     */
    public Node getChild(Node node,String name,Boolean error)
    {
	NodeList children = node.getChildNodes();
	Node ret=null;
	for (int i = 0; i < children.getLength(); i++)
	{
	    Node child=children.item(i);
	    if (child.getNodeName().equals(name))
	    {
		ret=child;
		break;
	    }
	}
	if (ret==null && error)
	{	    
		System.out.println("XMLSerial::getChild Couldn't find attribute '"+name+"' in: "+toString(node));
	    System.exit(1);
	}
	return ret;
    }

    /*! Returns a list of nodes with the specified name.
     */
    public List<Node> getChildren(Node node,String name)
    {
	List<Node> nodes=new LinkedList<Node>();
	NodeList children = node.getChildNodes();
	for (int i = 0; i < children.getLength(); i++)
	{
	    Node child=children.item(i);
	    if (child.getNodeName().equals(name))
	    {
		nodes.add(child);
	    }
	}	
	return nodes;
    }
    
    /*! Returns a value of the specified attribute or 'null' if it isn't found.
     */
    public String getAttribute(Node node,String name)
    {
	return getAttribute(node,name,false);
    }

    /*! Returns the value of the specified attribute or 'null' if it isn't found. If
     *  error is 'true' it exits if no child is found with that name.
     */
    public String getAttribute(Node node,String name,Boolean error)
    {
	String s=null;
	NamedNodeMap attributes=node.getAttributes();
	if (attributes!=null)
	{
	    for (int i = 0; i < attributes.getLength(); i++)
	    {
		Node attribute = attributes.item(i);
		if (attribute.getNodeName().equals(name))
		    return attribute.getNodeValue();
	    }
	}
	if (s==null && error)
	{	    
		System.out.println("XMLSerial::getAttribute Couldn't find attribute '"+name+"' in: "+toString(node));
	    System.exit(1);
	}
	return s;
    }

}

