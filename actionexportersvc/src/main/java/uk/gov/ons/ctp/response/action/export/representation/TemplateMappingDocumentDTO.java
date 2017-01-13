package uk.gov.ons.ctp.response.action.export.representation;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representation of TemplateMappingDocument
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TemplateMappingDocumentDTO {

  @NotNull
  private String actionType;
  private String template;
  private String file;
  private Date dateModified;
}
