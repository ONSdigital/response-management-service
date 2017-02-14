package uk.gov.ons.ctp.response.action.export.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import uk.gov.ons.ctp.response.action.export.repository.BaseRepository;

/**
 *
 * Implementation of the BaseReporitory
 *
 * @param <T> the type of the entity to handle.
 * @param <ID> the type of the entity's identifier.
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements BaseRepository<T, ID> {

  private final EntityManager entityManager;

  /**
   * Creates a new SimpleJpaRepository to manage objects of the given
   * JpaEntityInformation.
   *
   * @param entityInformation on entities managed in the persistence context.
   * @param entityManager for the persistence context.
   */
  public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public <S extends T> S persist(S entity) {
    entityManager.persist(entity);
    return entity;
  }

}
