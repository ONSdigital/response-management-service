
package uk.gov.ons.ctp.response.kirona.drs.autogeneratedcode;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xmbCheckAvailabilityResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xmbCheckAvailabilityResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://autogenerated.OTWebServiceApi.xmbrace.com/}commandResponse">
 *       &lt;sequence>
 *         &lt;element name="theOtherOrders" type="{http://autogenerated.OTWebServiceApi.xmbrace.com/}order" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="theResourceIDContracts" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="theResourceIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="theSlots" type="{http://autogenerated.OTWebServiceApi.xmbrace.com/}daySlotsInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmbCheckAvailabilityResponse", propOrder = {
    "theOtherOrders",
    "theResourceIDContracts",
    "theResourceIDs",
    "theSlots"
})
public class XmbCheckAvailabilityResponse
    extends CommandResponse
{

    protected List<Order> theOtherOrders;
    protected List<String> theResourceIDContracts;
    protected List<String> theResourceIDs;
    protected List<DaySlotsInfo> theSlots;

    /**
     * Gets the value of the theOtherOrders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the theOtherOrders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTheOtherOrders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Order }
     * 
     * 
     */
    public List<Order> getTheOtherOrders() {
        if (theOtherOrders == null) {
            theOtherOrders = new ArrayList<Order>();
        }
        return this.theOtherOrders;
    }

    /**
     * Gets the value of the theResourceIDContracts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the theResourceIDContracts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTheResourceIDContracts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTheResourceIDContracts() {
        if (theResourceIDContracts == null) {
            theResourceIDContracts = new ArrayList<String>();
        }
        return this.theResourceIDContracts;
    }

    /**
     * Gets the value of the theResourceIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the theResourceIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTheResourceIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTheResourceIDs() {
        if (theResourceIDs == null) {
            theResourceIDs = new ArrayList<String>();
        }
        return this.theResourceIDs;
    }

    /**
     * Gets the value of the theSlots property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the theSlots property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTheSlots().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DaySlotsInfo }
     * 
     * 
     */
    public List<DaySlotsInfo> getTheSlots() {
        if (theSlots == null) {
            theSlots = new ArrayList<DaySlotsInfo>();
        }
        return this.theSlots;
    }

}
