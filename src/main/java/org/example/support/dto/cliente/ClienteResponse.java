package org.example.support.dto.cliente;

import org.example.support.domain.entity.Cliente;

public class ClienteResponse {
    public Long id;
    public String nome;
    public String cpf;
    public String email;
    public String telefone;
    public String endereco;
    public boolean ativo;

    public static ClienteResponse from(Cliente c) {
        ClienteResponse r = new ClienteResponse();
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
