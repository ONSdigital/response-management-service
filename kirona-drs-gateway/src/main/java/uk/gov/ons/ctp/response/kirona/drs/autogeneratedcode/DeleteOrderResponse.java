
package uk.gov.ons.ctp.response.kirona.drs.autogeneratedcode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteOrderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteOrderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://autogenerated.OTWebServiceApi.xmbrace.com/}xmbDeleteOrderResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteOrderResponse", propOrder = {
    "_return"
})
public class DeleteOrderResponse {

    @XmlElement(name = "return")
    protected XmbDeleteOrderResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link XmbDeleteOrderResponse }
     *     
     */
    public XmbDeleteOrderResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmbDeleteOrderResponse }
     *     
     */
    public void setReturn(XmbDeleteOrderResponse value) {
        this._return = value;
    }

}
