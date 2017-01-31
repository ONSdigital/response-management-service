package uk.gov.ons.ctp.response.action.export.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import uk.gov.ons.ctp.response.action.export.domain.FileRowCount;
import uk.gov.ons.ctp.response.action.export.repository.FileRowCountRepository;
import uk.gov.ons.ctp.response.action.export.service.FileRowCountService;

/**
 * The implementation of FileRowCountService
 */
@Named
public class FileRowCountServiceImpl implements FileRowCountService {

  @Inject
  private FileRowCountRepository fileRowCountRepo;

  @Override
  public FileRowCount save(FileRowCount fileRowCount) {
    return fileRowCountRepo.save(fileRowCount);
  }

}
