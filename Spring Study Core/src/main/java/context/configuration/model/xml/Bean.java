package context.configuration.model.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Bean {

    private String id;
    private String className;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private ArrayList<Property> allProperties;

    public ArrayList<Property> getMyProperties() {
	return allProperties;
    }

    public ArrayList<Property> getProperties() {
	return allProperties;
    }

    public void setProperties(ArrayList<Property> properties) {
	this.allProperties = properties;
    }

    public String getId() {
	return id;
    }

    @XmlAttribute(name="id", required=true)
    public void setId(String id) {
	this.id = id;
    }

    public String getClassName() {
	return className;
    }

    @XmlAttribute(name="class", required=true)
    public void setClassName(String className) {
	this.className = className;
    }

    @Override
    public String toString() {
	return "Bean [id=" + id + ", className=" + className + "]";
    }

}
