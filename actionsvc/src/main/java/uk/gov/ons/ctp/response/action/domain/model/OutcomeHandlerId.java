package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent;

@Data
@Builder
@Embeddable
@AllArgsConstructor
public class OutcomeHandlerId implements Serializable {
	private static final long serialVersionUID = 3161993644341999008L;
	private ActionEvent outcome;
	private String handler;
}
