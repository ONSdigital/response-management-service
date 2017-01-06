package uk.gov.ons.ctp.response.iac.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.casesvc.representation.CaseDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseTypeDTO;
import uk.gov.ons.ctp.response.iac.crypto.InternetAccessCodeGenerator;
import uk.gov.ons.ctp.response.iac.domain.model.InternetAccessCode;
import uk.gov.ons.ctp.response.iac.domain.model.InternetAccessCodeCaseContext;
import uk.gov.ons.ctp.response.iac.domain.repository.InternetAccessCodeRepository;
import uk.gov.ons.ctp.response.iac.service.CaseSvcClientService;
import uk.gov.ons.ctp.response.iac.service.InternetAccessCodeService;

/**
 * Accept feedback from handlers
 */
@Slf4j
@Named
public class InternetAccessCodeServiceImpl implements InternetAccessCodeService {

  private final static int GENERATE_TIMEOUT = 30;
  private final static int UPDATE_TIMEOUT = 10;

  @Inject
  private InternetAccessCodeRepository internetAccessCodeRepository;

  @Inject
  private InternetAccessCodeGenerator internetAccessCodeGenerator;

  @Inject
  private CaseSvcClientService caseSvcClientService;

  @Override
  public InternetAccessCodeCaseContext getCaseContext(String iac) throws CTPException {
    InternetAccessCodeCaseContext iacCaseContext = new InternetAccessCodeCaseContext();

    InternetAccessCode code = findInternetAccessCode(iac);
    if (code == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "IAC not found for iac %s", iac);
    }

    CaseDTO caseDTO = caseSvcClientService.getCase(iac);
    if (caseDTO == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Case not found for iac %d",
          iac);
    }

    CaseTypeDTO caseTypeDTO = caseSvcClientService.getCaseType(caseDTO.getCaseTypeId());
    if (caseTypeDTO == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Case Type not found for casetypeid %d",
          caseDTO.getCaseTypeId());
    }

    code.setLastUsedDateTime(DateTimeUtil.nowUTC());
    code.setUpdatedBy("SYSTEM");
    internetAccessCodeRepository.save(code);
    
    iacCaseContext.setIac(iac);
    iacCaseContext.setCaseId(caseDTO.getCaseId());
    iacCaseContext.setCaseRef(caseDTO.getCaseRef());
    iacCaseContext.setActive(code.getActive());
    iacCaseContext.setLastUsedDateTime(DateTimeUtil.nowUTC());

    iacCaseContext.setQuestionSet(caseTypeDTO.getQuestionSet());

    return iacCaseContext;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = UPDATE_TIMEOUT)
  public InternetAccessCode deactivateCode(String iac, String updatedBy) {
    log.debug("Entered deativateCode with code %s by %s", iac, updatedBy);
    InternetAccessCode code = internetAccessCodeRepository.findOne(iac);
    InternetAccessCode updatedCode = null;
    if (code != null) {
      code.setActive(false);
      code.setUpdatedBy(updatedBy);
      code.setUpdatedDateTime(DateTimeUtil.nowUTC());
      updatedCode = internetAccessCodeRepository.save(code);
    }
    return updatedCode;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = GENERATE_TIMEOUT)
  @Override
  public List<InternetAccessCode> generateCodes(int count, String createdBy) {
    InternetAccessCode iac = null;

    List<InternetAccessCode> codes = new ArrayList<>(count);
    for (int n = 0; n < count; n++) {
      String code = null;
      do {
        code = internetAccessCodeGenerator.generateIAC();
        iac = internetAccessCodeRepository.findOne(code);
      } while (iac != null);

      iac = InternetAccessCode
          .builder()
          .active(true)
          .code(code)
          .createdBy(createdBy)
          .createdDateTime(DateTimeUtil.nowUTC())
          .build();
      internetAccessCodeRepository.save(iac);
      codes.add(iac);
    }
    return codes;
  }

  @Override
  public InternetAccessCode findInternetAccessCode(String iac) {
    log.debug("Entering findInternetAccessCode with iac %s", iac);
    return internetAccessCodeRepository.findOne(iac);
  }

}
