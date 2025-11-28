package org.example.support.repository;

import org.example.support.domain.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);

    @Query("select c from Cliente c where c.ativo = true and (:q is null or lower(c.nome) like lower(concat('%', :q, '%')) or c.cpf like concat('%', :q, '%'))")
    Page<Cliente> search(String q, Pageable pageable);
}
