let clientes = [];
let quartos = [];
let reservas = [];

// Função utilitária para renderizar listas
function renderLista(lista, ulId, tipo) {
    const ul = document.getElementById(ulId);
    ul.innerHTML = '';
    lista.forEach((item, index) => {
        const li = document.createElement('li');
        li.textContent = `${index + 1}. ${Object.values(item).join(' - ')}`;
        const editBtn = document.createElement('button');
        editBtn.textContent = '✏️';
        editBtn.onclick = () => editarItem(tipo, index);
        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = '🗑️';
        deleteBtn.onclick = () => deletarItem(tipo, index);
        li.appendChild(editBtn);
        li.appendChild(deleteBtn);
        ul.appendChild(li);
    });
}

function editarItem(tipo, index) {
    const item = tipo === 'cliente' ? clientes[index] : tipo === 'quarto' ? quartos[index] : reservas[index];
    Object.keys(item).forEach(chave => {
        document.getElementById(`${chave}${tipo.charAt(0).toUpperCase() + tipo.slice(1)}`).value = item[chave];
    });
    document.getElementById(`${tipo}Id`).value = index;
    document.getElementById(`cancelar${tipo.charAt(0).toUpperCase() + tipo.slice(1)}`).classList.remove('hidden');
}

function deletarItem(tipo, index) {
    if (confirm('Tem certeza que deseja excluir?')) {
        if (tipo === 'cliente') clientes.splice(index, 1);
        else if (tipo === 'quarto') quartos.splice(index, 1);
        else reservas.splice(index, 1);
        renderTudo();
    }
}

function salvarItem(event, tipo) {
    event.preventDefault();
    const idField = document.getElementById(`${tipo}Id`);
    const index = idField.value;
    const novoItem = {};
    Array.from(event.target.elements).forEach(input => {
        if (input.id && input.id !== `${tipo}Id` && input.type !== 'submit' && input.type !== 'button') {
            const campo = input.id.replace(`${tipo.charAt(0).toUpperCase() + tipo.slice(1)}`, '');
            novoItem[campo] = input.value;
        }
    });
    if (index) {
        if (tipo === 'cliente') clientes[index] = novoItem;
        else if (tipo === 'quarto') quartos[index] = novoItem;
        else reservas[index] = novoItem;
    } else {
        if (tipo === 'cliente') clientes.push(novoItem);
        else if (tipo === 'quarto') quartos.push(novoItem);
        else reservas.push(novoItem);
    }
    idField.value = '';
    event.target.reset();
    document.getElementById(`cancelar${tipo.charAt(0).toUpperCase() + tipo.slice(1)}`).classList.add('hidden');
    renderTudo();
}

function renderTudo() {
    renderLista(clientes, 'listaClientes', 'cliente');
    renderLista(quartos, 'listaQuartos', 'quarto');
    renderLista(reservas, 'listaReservas', 'reserva');
}

document.getElementById('clienteForm').addEventListener('submit', e => salvarItem(e, 'cliente'));
document.getElementById('quartoForm').addEventListener('submit', e => salvarItem(e, 'quarto'));
document.getElementById('reservaForm').addEventListener('submit', e => salvarItem(e, 'reserva'));

document.getElementById('cancelarCliente').addEventListener('click', () => document.getElementById('clienteForm').reset());
document.getElementById('cancelarQuarto').addEventListener('click', () => document.getElementById('quartoForm').reset());
document.getElementById('cancelarReserva').addEventListener('click', () => document.getElementById('reservaForm').reset());