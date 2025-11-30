package org.example.support.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClienteEntrada {
    @NotBlank
    public String nome;
    @NotBlank
    @Pattern(regexp = "\\d{11}")
    public String cpf;
    @Email
    public String email;
    public String telefone;
    public String endereco;
}
