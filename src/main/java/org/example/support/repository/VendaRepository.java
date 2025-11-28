package org.example.support.repository;

import org.example.support.domain.entity.Venda;
import org.example.support.domain.enums.VendaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    @Query("select v from Venda v where (:status is null or v.status = :status)")
    Page<Venda> search(VendaStatus status, Pageable pageable);
}
