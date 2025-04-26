package com.obras.repositories;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

//repositories/ObraRepositoryCustom.java
public interface ObraRepositoryCustom {
 Page<Obra> buscarComFiltros(ObraFiltro filtro, Pageable pageable);
}

//repositories/ObraRepositoryImpl.java
@RequiredArgsConstructor
public class ObraRepositoryImpl implements ObraRepositoryCustom {
 
 private final EntityManager em;

 @Override
 public Page<Obra> buscarComFiltros(ObraFiltro filtro, Pageable pageable) {
     CriteriaBuilder cb = em.getCriteriaBuilder();
     CriteriaQuery<Obra> cq = cb.createQuery(Obra.class);
     Root<Obra> obra = cq.from(Obra.class);
     
     List<Predicate> predicates = new ArrayList<>();
     
     if (filtro.nome() != null) {
         predicates.add(cb.like(obra.get("nome"), "%" + filtro.nome() + "%"));
     }
     
     // Adicione outros filtros
     
     cq.where(predicates.toArray(new Predicate[0]));
     
     TypedQuery<Obra> query = em.createQuery(cq);
     query.setFirstResult((int) pageable.getOffset());
     query.setMaxResults(pageable.getPageSize());
     
     return new PageImpl<>(query.getResultList(), pageable, getTotalCount(filtro));
 }
 
 private long getTotalCount(ObraFiltro filtro) {
     // Implementação da contagem total
 }
}
