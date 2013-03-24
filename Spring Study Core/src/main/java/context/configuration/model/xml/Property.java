package context.configuration.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Property {

    private String name;
    private String value;
    private String ref;

    public String getName() {
	return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    @XmlAttribute
    public void setValue(String value) {
	this.value = value;
    }

    public String getRef() {
	return ref;
    }

    @XmlAttribute
    public void setRef(String ref) {
	this.ref = ref;
    }

    @Override
    public String toString() {
	return "Property [name=" + name + ", value=" + value + ", ref=" + ref + "]";
    }

}
