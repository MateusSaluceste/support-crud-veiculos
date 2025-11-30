let credenciais = { usuario: "", senha: "", header: "" };

function atualizarInfoUsuario() {
    const info = document.getElementById("infoUsuario");
    if (credenciais.usuario) {
        info.textContent = `Autenticado como ${credenciais.usuario}`;
    } else {
        info.textContent = "Não autenticado";
    }
}

function selecionarPerfil(valor) {
    if (valor === "admin") {
        document.getElementById("usuario").value = "admin";
        document.getElementById("senha").value = "admin123";
    } else if (valor === "vendedor") {
        document.getElementById("usuario").value = "vendedor";
        document.getElementById("senha").value = "vendedor123";
    }
}

function configurarCredenciais() {
    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value;
    if (!usuario || !senha) {
        atualizarStatus("statusAuth", "Informe usuário e senha", true);
        return;
    }
    const token = btoa(`${usuario}:${senha}`);
    credenciais = { usuario, senha, header: `Basic ${token}` };
    atualizarStatus("statusAuth", "Credenciais configuradas", false);
    atualizarInfoUsuario();
}

function obterHeaders(extra) {
    if (!credenciais.header) {
        throw new Error("Defina as credenciais primeiro");
    }
    return Object.assign({
        "Content-Type": "application/json",
        Authorization: credenciais.header
    }, extra || {});
}

function atualizarStatus(id, mensagem, erro) {
    const barra = document.getElementById(id);
    if (!barra) return;
    barra.textContent = mensagem;
    barra.classList.toggle("erro", !!erro);
}

function trocarAba(alvo) {
    document.querySelectorAll(".aba").forEach(botao => {
        botao.classList.toggle("ativa", botao.dataset.aba === alvo);
    });
    document.querySelectorAll(".painel").forEach(painel => {
        painel.classList.toggle("ativo", painel.id === `painel${alvo.charAt(0).toUpperCase()}${alvo.slice(1)}`);
    });
}

async function carregarVeiculos() {
    try {
        const params = new URLSearchParams();
        const marca = document.getElementById("filtroMarca").value.trim();
        const modelo = document.getElementById("filtroModelo").value.trim();
        const ano = document.getElementById("filtroAno").value.trim();
        const status = document.getElementById("filtroStatus").value.trim();
        if (marca) params.append("marca", marca);
        if (modelo) params.append("modelo", modelo);
        if (ano) params.append("ano", ano);
        if (status) params.append("status", status);
        const resposta = await fetch(`/api/veiculos?${params.toString()}`, {
            headers: obterHeaders()
        });
        if (!resposta.ok) throw new Error("Erro ao carregar veículos");
        const dados = await resposta.json();
        const lista = Array.isArray(dados.content) ? dados.content : dados;
        const corpo = document.getElementById("listaVeiculos");
        corpo.innerHTML = lista.map(item => `
            <tr>
                <td>${item.id}</td>
                <td>${item.marca} ${item.modelo}</td>
                <td>${item.ano}</td>
                <td>${item.placa}</td>
                <td>${item.quantidadeEmEstoque}</td>
                <td>${item.status || ""}</td>
                <td>
                    <div class="linha-botoes">
                        <button class="btn-secundario" onclick="ajustarEstoque(${item.id}, ${item.quantidadeEmEstoque + 1})">Estoque +1</button>
                        <button class="btn-perigo" onclick="removerVeiculo(${item.id})">Excluir</button>
                    </div>
                </td>
            </tr>
        `).join("");
        atualizarStatus("statusVeiculos", "Veículos carregados", false);
    } catch (erro) {
        atualizarStatus("statusVeiculos", erro.message, true);
    }
}

async function criarVeiculo() {
    try {
        const payload = {
            marca: document.getElementById("novoMarca").value.trim(),
            modelo: document.getElementById("novoModelo").value.trim(),
            ano: Number(document.getElementById("novoAno").value),
            placa: document.getElementById("novoPlaca").value.trim(),
            precoVendaSugerido: Number(document.getElementById("novoPreco").value),
            quantidadeEmEstoque: Number(document.getElementById("novoEstoque").value)
        };
        const resposta = await fetch("/api/veiculos", {
            method: "POST",
            headers: obterHeaders(),
            body: JSON.stringify(payload)
        });
        if (!resposta.ok) throw new Error("Não foi possível criar o veículo");
        atualizarStatus("statusVeiculos", "Veículo criado", false);
        carregarVeiculos();
    } catch (erro) {
        atualizarStatus("statusVeiculos", erro.message, true);
    }
}

async function ajustarEstoque(id, quantidade) {
    try {
        const resposta = await fetch(`/api/veiculos/${id}/estoque?quantidade=${quantidade}`, {
            method: "PATCH",
            headers: obterHeaders()
        });
        if (!resposta.ok) throw new Error("Erro ao atualizar estoque");
        atualizarStatus("statusVeiculos", "Estoque atualizado", false);
        carregarVeiculos();
    } catch (erro) {
        atualizarStatus("statusVeiculos", erro.message, true);
    }
}

async function removerVeiculo(id) {
    if (!confirm("Deseja realmente excluir?")) return;
    try {
        const resposta = await fetch(`/api/veiculos/${id}`, {
            method: "DELETE",
            headers: obterHeaders()
        });
        if (!resposta.ok) throw new Error("Erro ao excluir veículo");
        atualizarStatus("statusVeiculos", "Veículo excluído", false);
        carregarVeiculos();
    } catch (erro) {
        atualizarStatus("statusVeiculos", erro.message, true);
    }
}

async function carregarClientes() {
    try {
        const termo = document.getElementById("filtroCliente").value.trim();
        const params = termo ? `?q=${encodeURIComponent(termo)}` : "";
        const resposta = await fetch(`/api/clientes${params}`, {
            headers: obterHeaders()
        });
        if (!resposta.ok) throw new Error("Erro ao carregar clientes");
        const dados = await resposta.json();
        const lista = Array.isArray(dados.content) ? dados.content : dados;
        document.getElementById("listaClientes").innerHTML = lista.map(item => `
            <tr>
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>${item.cpf}</td>
                <td>${item.ativo ? "Sim" : "Não"}</td>
            </tr>
        `).join("");
        atualizarStatus("statusClientes", "Clientes carregados", false);
    } catch (erro) {
        atualizarStatus("statusClientes", erro.message, true);
    }
}

async function criarCliente() {
    try {
        const payload = {
            nome: document.getElementById("clienteNome").value.trim(),
            cpf: document.getElementById("clienteCpf").value.trim(),
            email: document.getElementById("clienteEmail").value.trim(),
            telefone: document.getElementById("clienteTelefone").value.trim(),
            endereco: document.getElementById("clienteEndereco").value.trim()
        };
        const resposta = await fetch("/api/clientes", {
            method: "POST",
            headers: obterHeaders(),
            body: JSON.stringify(payload)
        });
        if (!resposta.ok) throw new Error("Não foi possível criar o cliente");
        atualizarStatus("statusClientes", "Cliente criado", false);
        carregarClientes();
    } catch (erro) {
        atualizarStatus("statusClientes", erro.message, true);
    }
}

async function criarVenda() {
    try {
        const payload = {
            clienteId: Number(document.getElementById("vendaCliente").value),
            formaPagamento: document.getElementById("vendaPagamento").value,
            itens: [
                {
                    veiculoId: Number(document.getElementById("vendaVeiculo").value),
                    quantidade: Number(document.getElementById("vendaQuantidade").value)
                }
            ]
        };
        const resposta = await fetch("/api/vendas", {
            method: "POST",
            headers: obterHeaders(),
            body: JSON.stringify(payload)
        });
        if (!resposta.ok) throw new Error("Erro ao registrar venda");
        atualizarStatus("statusVendas", "Venda registrada", false);
    } catch (erro) {
        atualizarStatus("statusVendas", erro.message, true);
    }
}

async function pagarVenda() {
    const id = document.getElementById("operacaoVendaId").value;
    if (!id) {
        atualizarStatus("statusVendas", "Informe o ID da venda", true);
        return;
    }
    try {
        const resposta = await fetch(`/api/vendas/${id}/pagar`, {
            method: "PATCH",
            headers: obterHeaders()
        });
        if (!resposta.ok) throw new Error("Erro ao pagar venda");
        atualizarStatus("statusVendas", "Venda paga", false);
    } catch (erro) {
        atualizarStatus("statusVendas", erro.message, true);
    }
}

async function cancelarVenda() {
    const id = document.getElementById("operacaoVendaId").value;
    if (!id) {
        atualizarStatus("statusVendas", "Informe o ID da venda", true);
        return;
    }
    try {
        const resposta = await fetch(`/api/vendas/${id}/cancelar`, {
            method: "PATCH",
            headers: obterHeaders()
        });
        if (!resposta.ok) throw new Error("Erro ao cancelar venda");
        atualizarStatus("statusVendas", "Venda cancelada", false);
    } catch (erro) {
        atualizarStatus("statusVendas", erro.message, true);
    }
}

document.getElementById("perfil").addEventListener("change", e => selecionarPerfil(e.target.value));
document.getElementById("btnCredenciais").addEventListener("click", configurarCredenciais);
document.getElementById("btnBuscarVeiculos").addEventListener("click", carregarVeiculos);
document.getElementById("btnCriarVeiculo").addEventListener("click", criarVeiculo);
document.getElementById("btnBuscarClientes").addEventListener("click", carregarClientes);
document.getElementById("btnCriarCliente").addEventListener("click", criarCliente);
document.getElementById("btnCriarVenda").addEventListener("click", criarVenda);
document.getElementById("btnPagarVenda").addEventListener("click", pagarVenda);
document.getElementById("btnCancelarVenda").addEventListener("click", cancelarVenda);
document.querySelectorAll(".aba").forEach(botao => {
    botao.addEventListener("click", () => trocarAba(botao.dataset.aba));
});
