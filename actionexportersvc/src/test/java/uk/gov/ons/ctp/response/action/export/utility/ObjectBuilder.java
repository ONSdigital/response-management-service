package uk.gov.ons.ctp.response.action.export.utility;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import uk.gov.ons.ctp.response.action.export.domain.ActionRequestDocument;
import uk.gov.ons.ctp.response.action.export.domain.Address;

/**
 * Utility class to build objects required in tests
 */
public class ObjectBuilder {
  public static List<ActionRequestDocument> buildListOfActionRequestDocuments() {
    List<ActionRequestDocument> result = new ArrayList<>();
    for (int i = 1; i < 51; i++) {
      result.add(buildActionRequestDocument(i));
    }
    return result;
  }

  private static ActionRequestDocument buildActionRequestDocument(int i) {
    ActionRequestDocument result =  new ActionRequestDocument();
    result.setActionId(new BigInteger(new Integer(i).toString()));
    result.setActionType("testActionType");
    result.setIac("testIac");
    result.setAddress(buildActionAddress());
    return result;
  }

  private static Address buildActionAddress() {
    Address address = new Address();
    address.setLine1("1 High Street");
    address.setTownName("Southampton");
    address.setPostcode("SO16 0AS");
    return address;
  }

}
