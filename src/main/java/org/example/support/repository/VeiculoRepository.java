package org.example.support.repository;

import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.enums.StatusVeiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Optional<Veiculo> findByPlaca(String placa);

    @Query("select v from Veiculo v where v.ativo = true and (:marca is null or lower(v.marca) like lower(concat('%', :marca, '%'))) " +
            "and (:modelo is null or lower(v.modelo) like lower(concat('%', :modelo, '%'))) " +
            "and (:ano is null or v.ano = :ano) " +
            "and (:status is null or v.status = :status)")
    Page<Veiculo> search(String marca, String modelo, Integer ano, StatusVeiculo status, Pageable pageable);
}
