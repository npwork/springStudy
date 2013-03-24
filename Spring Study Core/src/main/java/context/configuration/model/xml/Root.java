package context.configuration.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
public class Root {

    @XmlElementWrapper(name = "beans")
    @XmlElement(name = "bean")
    private ArrayList<Bean> allBeans;

    public ArrayList<Bean> getBeans() {
	return allBeans;
    }

    public void setBeans(ArrayList<Bean> beans) {
	this.allBeans = beans;
    }

}
