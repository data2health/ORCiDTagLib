package edu.uiowa.slis.ORCiDTagLib.util;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;

public class NameSpaceEradicator {

    @SuppressWarnings("unchecked")
    public static void removeAllNamespaces(Document doc) {
	Element root = doc.getRootElement();
	removeNamespaces(root.content());
    }

    @SuppressWarnings("unchecked")
    public static void unfixNamespaces(Document doc, Namespace original) {
	Element root = doc.getRootElement();
	if (original != null) {
	    setNamespaces(root.content(), original);
	}
    }

    public static void setNamespace(Element elem, Namespace ns) {
	elem.setQName(QName.get(elem.getName(), ns, elem.getQualifiedName()));
    }

    /**
     * Recursively removes the namespace of the element and all its children:
     * sets to Namespace.NO_NAMESPACE
     */
    public static void removeNamespaces(Element elem) {
	setNamespaces(elem, Namespace.NO_NAMESPACE);
    }

    /**
     * Recursively removes the namespace of the list and all its children: sets
     * to Namespace.NO_NAMESPACE
     */
    public static void removeNamespaces(List<Node> l) {
	setNamespaces(l, Namespace.NO_NAMESPACE);
    }

    /**
     * Recursively sets the namespace of the element and all its children.
     */
    @SuppressWarnings("unchecked")
    public static void setNamespaces(Element elem, Namespace ns) {
	setNamespace(elem, ns);
	setNamespaces(elem.content(), ns);
    }

    /**
     * Recursively sets the namespace of the List and all children if the
     * current namespace is match
     */
    public static void setNamespaces(List<Node> l, Namespace ns) {
	Node n = null;
	for (int i = 0; i < l.size(); i++) {
	    n = l.get(i);

	    // if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
	    // ((Attribute) n).setNamespace(ns);
	    // }
	    if (n.getNodeType() == Node.ELEMENT_NODE) {
		setNamespaces((Element) n, ns);
	    }
	}
    }
}
