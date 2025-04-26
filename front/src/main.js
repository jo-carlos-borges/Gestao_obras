// Estado da aplicação
let obras = [];
let gastos = [];
let obraSelecionada = null;
let obraParaFinalizar = null;

// Formatador de moeda
const formatarMoeda = (valor) => {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
};

// Formatador de moeda para input
const formatarMoedaInput = (valor) => {
    valor = valor.replace(/\D/g, '');
    valor = (parseInt(valor) / 100).toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
    });
    return valor;
};

// Função para extrair o valor numérico do input formatado
const extrairValorNumerico = (valorFormatado) => {
    if (!valorFormatado) return 0;
    return Number(valorFormatado.replace(/\D/g, '')) / 100;
};

// Formatador de data
const formatarData = (data) => {
    return new Date(data).toLocaleDateString();
};

// Gerenciamento de abas
const tabs = document.querySelectorAll('.tab-button');
const contents = document.querySelectorAll('.tab-content');

tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        tabs.forEach(t => t.classList.remove('active'));
        contents.forEach(c => c.classList.remove('active'));

        tab.classList.add('active');
        document.getElementById(tab.dataset.tab).classList.add('active');
    });
});

// Cálculos
const calcularTotalGastos = (obraId) => {
    return gastos
        .filter(gasto => gasto.obraId === obraId)
        .reduce((total, gasto) => total + gasto.valor, 0);
};

const calcularSaldo = (obra) => {
    const totalGastos = calcularTotalGastos(obra.id);
    return obra.valorTotal - totalGastos;
};

// Gerenciamento de modais
const abrirModal = (modalId) => {
    document.getElementById(modalId).style.display = 'block';
    document.body.style.overflow = 'hidden';
};

const fecharModal = (modalId) => {
    document.getElementById(modalId).style.display = 'none';
    document.body.style.overflow = 'auto';
};

// Funções de gerenciamento
const gerenciarObra = (id) => {
    const obra = obras.find(o => o.id === id);
    if (obra) {
        obraSelecionada = id;
        document.getElementById('obra-selecionada-titulo').textContent = obra.nome;
        abrirModal('conteudo-gerenciamento');
        atualizarSaldoDisponivel();
        renderizarGastos();
    }
};

const fecharGerenciamento = () => {
    fecharModal('conteudo-gerenciamento');
    obraSelecionada = null;
};

const iniciarFinalizarObra = (id) => {
    obraParaFinalizar = id;
    abrirModal('modal-confirmar');
};

const fecharModalConfirmar = () => {
    fecharModal('modal-confirmar');
    obraParaFinalizar = null;
};

const confirmarFinalizarObra = () => {
    if (obraParaFinalizar) {
        const obra = obras.find(o => o.id === obraParaFinalizar);
        if (obra) {
            obra.status = 'Concluída';
            renderizarObras();
            renderizarRelatorios();

            if (obraParaFinalizar === obraSelecionada) {
                atualizarSaldoDisponivel();
            }
        }
        fecharModalConfirmar();
    }
};

// Renderização das obras
const renderizarObras = () => {
    const listaObras = document.getElementById('lista-obras');
    listaObras.innerHTML = '';
    
    // Criar tabela
    const tabela = document.createElement('table');
    tabela.className = 'obras-lista';
    
    // Cabeçalho da tabela
    const cabecalho = document.createElement('thead');
    cabecalho.innerHTML = `
        <tr>
            <th>Nome da Obra</th>
            <th>Data de Início</th>
            <th>Status</th>
            <th>Valor Total</th>
            <th>Total Gastos</th>
            <th>Saldo</th>
            <th>Ações</th>
        </tr>
    `;
    tabela.appendChild(cabecalho);
    
    // Corpo da tabela
    const corpo = document.createElement('tbody');
    
    obras.forEach(obra => {
        const totalGastos = calcularTotalGastos(obra.id);
        const saldo = calcularSaldo(obra);
        
        const linha = document.createElement('tr');
        linha.innerHTML = `
            <td class="obra-nome">${obra.nome}</td>
            <td>${formatarData(obra.dataInicio)}</td>
            <td><span class="obra-status status-${obra.status.toLowerCase().replace(' ', '-')}">${obra.status}</span></td>
            <td>${formatarMoeda(obra.valorTotal)}</td>
            <td>${formatarMoeda(totalGastos)}</td>
            <td class="${saldo < 0 ? 'saldo-negativo' : 'saldo-positivo'}">${formatarMoeda(saldo)}</td>
            <td>
                <div class="acoes-container">
                    <button class="acao-btn" onclick="gerenciarObra('${obra.id}')" title="Gerenciar Gastos">
                        <i class="fas fa-money-bill-wave"></i>
                    </button>
                    ${obra.status === 'Em Andamento' ? `
                    <button class="acao-btn acao-btn-finalizar" onclick="iniciarFinalizarObra('${obra.id}')" title="Finalizar Obra">
                        <i class="fas fa-check-circle"></i>
                    </button>
                    ` : ''}
                </div>
            </td>
        `;
        
        corpo.appendChild(linha);
    });
    
    tabela.appendChild(corpo);
    listaObras.appendChild(tabela);
};

// Atualização do saldo disponível
const atualizarSaldoDisponivel = () => {
    if (obraSelecionada) {
        const obra = obras.find(o => o.id === obraSelecionada);
        const saldo = calcularSaldo(obra);
        const saldoElement = document.querySelector('#saldo-disponivel .saldo');
        saldoElement.textContent = formatarMoeda(saldo);
        saldoElement.className = `saldo ${saldo < 0 ? 'saldo-negativo' : 'saldo-positivo'}`;

        // Atualizar visibilidade do formulário de gastos baseado no status da obra
        const formGasto = document.getElementById('form-gasto');
        const msgObraConcluida = document.getElementById('msg-obra-concluida');

        if (obra.status === 'Concluída') {
            formGasto.style.display = 'none';
            msgObraConcluida.style.display = 'block';
        } else {
            formGasto.style.display = 'block';
            msgObraConcluida.style.display = 'none';
        }
    }
};

// Renderização de gastos
const renderizarGastos = () => {
    const listaGastos = document.getElementById('lista-gastos');
    listaGastos.innerHTML = '';

    gastos
        .filter(gasto => gasto.obraId === obraSelecionada)
        .sort((a, b) => new Date(b.data) - new Date(a.data))
        .forEach(gasto => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${formatarData(gasto.data)}</td>
                <td>${formatarMoeda(gasto.valor)}</td>
                <td>${gasto.descricao}</td>
            `;
            listaGastos.appendChild(tr);
        });
};

// Função para calcular o lucro de uma obra
const calcularLucro = (obra) => {
    const totalGastos = calcularTotalGastos(obra.id);
    // Lucro = Valor Total - Total Gastos
    return obra.valorTotal - totalGastos;
};

// Função para calcular o lucro mensal de uma obra
const calcularLucroMensal = (obra, mes, ano) => {
    // Calcular total de gastos no mês
    const gastosMes = calcularTotalGastosMes(obra.id, mes, ano);

    // Para simplificar, vamos considerar que o lucro mensal é proporcional aos gastos do mês
    // em relação ao total de gastos da obra
    const totalGastos = calcularTotalGastos(obra.id);

    if (totalGastos === 0) return 0; // Evitar divisão por zero

    const lucroTotal = calcularLucro(obra);
    const proporcaoGastosMes = gastosMes / totalGastos;

    // Lucro mensal proporcional aos gastos do mês
    return lucroTotal * proporcaoGastosMes;
};

// Função para filtrar gastos por mês e ano
const filtrarGastosPorMes = (mes, ano) => {
    return gastos.filter(gasto => {
        const data = new Date(gasto.data);
        return data.getMonth() === mes && data.getFullYear() === ano;
    });
};

// Função para calcular total de gastos de uma obra em um mês específico
const calcularTotalGastosMes = (obraId, mes, ano) => {
    return gastos
        .filter(gasto => {
            const data = new Date(gasto.data);
            return gasto.obraId === obraId &&
                data.getMonth() === mes &&
                data.getFullYear() === ano;
        })
        .reduce((total, gasto) => total + gasto.valor, 0);
};

// Modificar a função renderizarRelatorios para incluir lucros e filtrar por mês
const renderizarRelatorios = () => {
    const resumoObrasEmAndamento = document.getElementById('resumo-obras-em-andamento');
    const resumoObrasConcluidas = document.getElementById('resumo-obras-concluidas');
    const totalEmAndamento = document.getElementById('total-em-andamento');
    const totalConcluidas = document.getElementById('total-concluidas');

    // Limpar conteúdo anterior
    resumoObrasEmAndamento.innerHTML = '';
    resumoObrasConcluidas.innerHTML = '';

    // Obter mês e ano selecionados
    const mesRelatorio = parseInt(document.getElementById('mes-relatorio').value);
    const anoRelatorio = parseInt(document.getElementById('ano-relatorio').value);

    // Atualizar o nome do mês no título
    const meses = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
        'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
    document.getElementById('mes-atual-relatorio').textContent = `${meses[mesRelatorio]} de ${anoRelatorio}`;

    // Filtrar obras por status
    const obrasEmAndamento = obras.filter(obra => obra.status === 'Em Andamento');
    const obrasConcluidas = obras.filter(obra => obra.status === 'Concluída');

    let somaEmAndamento = 0;
    let somaConcluidas = 0;

    // Renderizar obras em andamento
    obrasEmAndamento.forEach(obra => {
        somaEmAndamento++;
        const totalGasto = calcularTotalGastos(obra.id);
        const gastoMes = calcularTotalGastosMes(obra.id, mesRelatorio, anoRelatorio);
        const saldo = calcularSaldo(obra);
        const lucro = calcularLucro(obra);

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${obra.nome}</td>
            <td>${formatarMoeda(obra.valorTotal)}</td>
            <td>${formatarMoeda(totalGasto)} </td>
            <td class="${saldo < 0 ? 'saldo-negativo' : 'saldo-positivo'}">${formatarMoeda(saldo)}</td>
            <td class="${lucro < 0 ? 'saldo-negativo' : 'saldo-positivo'}">${formatarMoeda(lucro)}</td>
        `;
        resumoObrasEmAndamento.appendChild(tr);
    });

    // Renderizar obras concluídas
    obrasConcluidas.forEach(obra => {
        somaConcluidas++;
        const totalGasto = calcularTotalGastos(obra.id);
        const gastoMes = calcularTotalGastosMes(obra.id, mesRelatorio, anoRelatorio);
        const saldo = calcularSaldo(obra);
        const lucro = calcularLucro(obra);

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${obra.nome}</td>
            <td>${formatarMoeda(obra.valorTotal)}</td>
            <td>${formatarMoeda(totalGasto)} </td>
            <td class="${saldo < 0 ? 'saldo-negativo' : 'saldo-positivo'}">${formatarMoeda(saldo)}</td>
            <td class="${lucro < 0 ? 'saldo-negativo' : 'saldo-positivo'}">${formatarMoeda(lucro)}</td>
        `;
        resumoObrasConcluidas.appendChild(tr);
    });

    // Atualizar contadores
    totalEmAndamento.textContent = somaEmAndamento;
    totalConcluidas.textContent = somaConcluidas;

    // Adicionar resumo financeiro do mês se não existir
    adicionarResumoFinanceiroMensal(mesRelatorio, anoRelatorio);
};

// Função para adicionar um resumo financeiro mensal à página de relatórios
const adicionarResumoFinanceiroMensal = (mes, ano) => {
    // Verificar se já existe um resumo financeiro e removê-lo
    const resumoExistente = document.getElementById('resumo-financeiro-mensal');
    if (resumoExistente) {
        resumoExistente.remove();
    }

    // Calcular totais do mês
    let totalGastosMes = 0;
    let totalLucroMes = 0;

    // Processar todas as obras para calcular totais
    obras.forEach(obra => {
        const gastosMes = calcularTotalGastosMes(obra.id, mes, ano);
        totalGastosMes += gastosMes;

        // Calcular lucro proporcional do mês
        const lucroMes = calcularLucroMensal(obra, mes, ano);
        totalLucroMes += lucroMes;
    });

    // Criar elemento de resumo financeiro
    const resumoFinanceiro = document.createElement('div');
    resumoFinanceiro.id = 'resumo-financeiro-mensal';
    resumoFinanceiro.className = 'relatorio-secao resumo-financeiro';

    // Obter nome do mês
    const meses = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
        'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];

    resumoFinanceiro.innerHTML = `
        <h3>Resumo Financeiro - ${meses[mes]}/${ano}</h3>
        <div class="resumo-cards">
            <div class="resumo-card">
                <h4>Total de Gastos no Mês</h4>
                <p>${formatarMoeda(totalGastosMes)}</p>
            </div>
            <div class="resumo-card ${totalLucroMes < 0 ? 'saldo-negativo' : 'saldo-positivo'}">
                <h4>Lucro Estimado no Mês</h4>
                <p>${formatarMoeda(totalLucroMes)}</p>
            </div>
        </div>
    `;

    // Adicionar à página após os filtros
    const filtrosRelatorio = document.querySelector('.relatorio-filtros');

    // Verificar se o elemento existe antes de tentar inserir
    if (filtrosRelatorio) {
        // Verificar se o elemento pai existe
        if (filtrosRelatorio.parentNode) {
            filtrosRelatorio.parentNode.insertBefore(resumoFinanceiro, filtrosRelatorio.nextSibling);
        } else {
            console.error('Elemento pai de .relatorio-filtros não encontrado');

            // Alternativa: adicionar ao elemento #relatorios
            const relatoriosDiv = document.getElementById('relatorios');
            if (relatoriosDiv) {
                relatoriosDiv.appendChild(resumoFinanceiro);
            } else {
                console.error('Elemento #relatorios não encontrado');
            }
        }
    } else {
        console.error('Elemento .relatorio-filtros não encontrado');

        // Alternativa: adicionar ao elemento #relatorios
        const relatoriosDiv = document.getElementById('relatorios');
        if (relatoriosDiv) {
            relatoriosDiv.appendChild(resumoFinanceiro);
        } else {
            console.error('Elemento #relatorios não encontrado');
        }
    }

    // Adicionar logs para depuração
    console.log('Resumo financeiro criado:', resumoFinanceiro);
    console.log('Total de gastos no mês:', totalGastosMes);
    console.log('Lucro estimado no mês:', totalLucroMes);
};

// Event listener único para DOMContentLoaded
document.addEventListener('DOMContentLoaded', () => {
    // Configurar seletores de mês e ano
    const mesRelatorio = document.getElementById('mes-relatorio');
    const anoRelatorio = document.getElementById('ano-relatorio');
    
    // Definir mês e ano atuais como padrão
    const dataAtual = new Date();
    mesRelatorio.value = dataAtual.getMonth();
    anoRelatorio.value = dataAtual.getFullYear();
    
    // Adicionar event listeners para os seletores
    mesRelatorio.addEventListener('change', renderizarRelatorios);
    anoRelatorio.addEventListener('change', renderizarRelatorios);
    
    // Configurar abas de relatórios
    const relatorioTabs = document.querySelectorAll('.relatorio-tab-button');
    const relatorioContents = document.querySelectorAll('.relatorio-tab-content');
    
    relatorioTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // Remover classe active de todas as abas e conteúdos
            relatorioTabs.forEach(t => t.classList.remove('active'));
            relatorioContents.forEach(c => c.classList.remove('active'));
            
            // Adicionar classe active à aba clicada e seu conteúdo correspondente
            tab.classList.add('active');
            const tabId = tab.dataset.relatorioTab;
            document.getElementById(`relatorio-${tabId}`).classList.add('active');
        });
    });
    
    // Renderizar relatórios iniciais
    renderizarRelatorios();
});
// Event Listeners
document.getElementById('form-obra').addEventListener('submit', (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const valorTotalInput = e.target.querySelector('[name="valorTotal"]');
    const valorTotal = extrairValorNumerico(valorTotalInput.value);

    const obra = {
        id: Math.random().toString(36).substr(2, 9),
        nome: formData.get('nome'),
        descricao: formData.get('descricao'),
        dataInicio: formData.get('dataInicio'),
        valorTotal: valorTotal,
        status: 'Em Andamento'
    };

    obras.push(obra);
    renderizarObras();
    renderizarRelatorios();
    e.target.reset();
    valorTotalInput.value = '';

    // Mudar para a aba de gerenciamento após criar a obra
    const gerenciamentoTab = document.querySelector('[data-tab="gerenciamento"]');
    gerenciamentoTab.click();
});

document.getElementById('form-gasto').addEventListener('submit', (e) => {
    e.preventDefault();
    if (obraSelecionada) {
        const formData = new FormData(e.target);
        const valorInput = e.target.querySelector('[name="valor"]');
        const valor = extrairValorNumerico(valorInput.value);

        const gasto = {
            id: Math.random().toString(36).substr(2, 9),
            obraId: obraSelecionada,
            data: formData.get('data'),
            valor: valor,
            descricao: formData.get('descricao')
        };

        gastos.push(gasto);
        renderizarGastos();
        renderizarObras();
        renderizarRelatorios();
        atualizarSaldoDisponivel();
        e.target.reset();
        valorInput.value = '';
    }
});

// Adicionar formatação de moeda aos inputs de valor
document.querySelectorAll('input[name="valorTotal"], input[name="valor"]').forEach(input => {
    input.addEventListener('input', (e) => {
        let valor = e.target.value.replace(/\D/g, '');
        if (valor === '') {
            e.target.value = '';
            return;
        }
        e.target.value = formatarMoedaInput(valor);
    });

    input.addEventListener('focus', () => {
        if (!input.value) {
            input.value = 'R$ 0,00';
        }
    });

    input.addEventListener('blur', () => {
        if (input.value === 'R$ 0,00') {
            input.value = '';
        }
    });
});

// Expor funções para o HTML
window.gerenciarObra = gerenciarObra;
window.fecharGerenciamento = fecharGerenciamento;
window.iniciarFinalizarObra = iniciarFinalizarObra;
window.fecharModalConfirmar = fecharModalConfirmar;
window.confirmarFinalizarObra = confirmarFinalizarObra;