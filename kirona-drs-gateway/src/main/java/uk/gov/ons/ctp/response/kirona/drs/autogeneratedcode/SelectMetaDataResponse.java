
package uk.gov.ons.ctp.response.kirona.drs.autogeneratedcode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for selectMetaDataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="selectMetaDataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://autogenerated.OTWebServiceApi.xmbrace.com/}xmbSelectMetaDataResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selectMetaDataResponse", propOrder = {
    "_return"
})
public class SelectMetaDataResponse {

    @XmlElement(name = "return")
    protected XmbSelectMetaDataResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link XmbSelectMetaDataResponse }
     *     
     */
    public XmbSelectMetaDataResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmbSelectMetaDataResponse }
     *     
     */
    public void setReturn(XmbSelectMetaDataResponse value) {
        this._return = value;
    }

}
