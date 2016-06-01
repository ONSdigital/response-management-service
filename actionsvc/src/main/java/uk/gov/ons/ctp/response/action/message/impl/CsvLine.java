package uk.gov.ons.ctp.response.action.message.impl;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;

/**
 * Each line in the ingested CSV is initially mapped to this POJO, whose primary
 * purpose is to perform the up front validation of each field. The fields are
 * deliberately all String type - the csv loader will try and convert "" into
 * Integer and throw an InvalidFormatException otherwise, and we want to
 * validate the fields with more finesse using the javax.validation framework
 */
@Data
@Getter
public class CsvLine {
  private final static String POSTCODE_RE = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";
  private final static String NON_BLANK_INTEGER_NUMERIC_RE = "[\\d]+";
  private final static String NON_BLANK_FLOAT_NUMERIC_RE = "[\\d]+\\.[\\d]+";
  private final static String ACTION_TYPE_RE = "\\D*";
  private final static String PRIORITY_RE = "[1-5]";
  private final static String ADDRESS_TYPE_RE = "|HH|CE";
  private final static String INSTRUCTION_TYPE_RE = "Request|Cancel";
  private final static String HANDLER_TYPE_RE = "HotelSurvey|HHSurvey|Printer|Field|CensusSupport";

  @Pattern(regexp = HANDLER_TYPE_RE)
  private String handler;

  @Pattern(regexp = ACTION_TYPE_RE)
  private String actionType;

  @Pattern(regexp = INSTRUCTION_TYPE_RE)
  private String instructionType;

  @Pattern(regexp = ADDRESS_TYPE_RE)
  private String addressType;

  @Size(min = 0, max = 6)
  private String estabType;

  @Size(min = 0, max = 35)
  private String locality;

  @Size(min = 0, max = 60)
  private String organisationName;

  @Size(min = 0, max = 20)
  private String category;

  @Size(min = 0, max = 60)
  private String line1;

  @Size(min = 0, max = 60)
  private String line2;

  @Size(min = 0, max = 30)
  private String townName;

  @Pattern(regexp = POSTCODE_RE)
  private String postcode;

  @Pattern(regexp = NON_BLANK_FLOAT_NUMERIC_RE)
  private String latitude;

  @Pattern(regexp = NON_BLANK_FLOAT_NUMERIC_RE)
  private String longitude;

  @Pattern(regexp = NON_BLANK_INTEGER_NUMERIC_RE)
  private String uprn;

  private String contactName;

  @Pattern(regexp = NON_BLANK_INTEGER_NUMERIC_RE)
  private String caseId;

  @Pattern(regexp = NON_BLANK_INTEGER_NUMERIC_RE)
  private String questionnaireId;

  @Pattern(regexp = PRIORITY_RE)
  private String priority;

  @Pattern(regexp = NON_BLANK_INTEGER_NUMERIC_RE)
  private String iac;

  private String events;
}
