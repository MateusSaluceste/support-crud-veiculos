package org.example.support.dto.cliente;

import org.example.support.domain.entity.Cliente;

public class ClienteSaida {
    public Long id;
    public String nome;
    public String cpf;
    public String email;
    public String telefone;
    public String endereco;
    public boolean ativo;

    public static ClienteSaida from(Cliente c) {
        ClienteSaida r = new ClienteSaida();
        r.id = c.getId();
        r.nome = c.getNome();
        r.cpf = c.getCpf();
        r.email = c.getEmail();
        r.telefone = c.getTelefone();
        r.endereco = c.getEndereco();
        r.ativo = c.isAtivo();
        return r;
    }
}
