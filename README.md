<<<<<<< HEAD
# eclipse_hotel
Eclipse Hotel API, um sistema back-end para gerenciamento de reservas de um hotel, usando Java com Spring Boot. O projeto tem como foco o domínio de APIs RESTful, banco de dados e testes com Postman e JUnit.
=======
![Banner para loja online de frete grátis para todo Brasil (4)](https://github.com/user-attachments/assets/c35529af-8934-4cee-b7e2-2a018b2900fb)

# Eclipse Hotel API

🛎️ **Eclipse Hotel API** é um sistema de gerenciamento de hotel desenvolvido em Java usando Spring Boot. Ele permite gerenciar clientes, quartos e reservas de forma eficiente.

## 📋 Sumário
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Consultas SQL](#consultas-sql)
- [Melhorias Futuras](#melhorias-futuras)
- [Contato](#contato)

## 🛠️ Requisitos
- Java 17
- Maven
- Postman (para testar a API)

## 📥 Instalação
1. Clone o repositório:
    ```bash
    git clone <URL_DO_SEU_REPOSITÓRIO>
    ```
2. Navegue até o diretório do projeto:
    ```bash
    cd eclipse-hotel-api
    ```
3. Compile e execute o projeto:
    ```bash
    mvn spring-boot:run
    ```

## 🚀 Uso
&nbsp;

### Endpoints
- **GET /swagger-ui.html** - Acesse a documentação da API no Swagger.
- **GET /h2-console** - Acesse o console do banco de dados H2.
&nbsp;
&nbsp;
### Criar Clientes
```http
POST /customers
Content-Type: application/json

{
  "name": "José Augusto",
  "email": "je.doe@example.com",
  "phone": "123432656"
}
&nbsp;
```
### Criar Quartos
```http
POST /rooms
Content-Type: application/json

{
  "roomNumber": 150,
  "type": "Deluxe2",
  "price": 250.0
}
&nbsp;
```
### Criar Reservas 

```http
POST /reservations
Content-Type: application/json

{
  "customerId": 1,
  "roomId": 1,
  "checkin": "2024-08-01T14:00:00",
  "checkout": "2024-08-10T12:00:00",
  "status": "SCHEDULED"
}
```
&nbsp;
&nbsp;

## 📊 Consultas SQL


### Quantos clientes temos na base ?

SELECT COUNT(*) AS total_clientes FROM customers;

### Quantos quartos temos cadastrados?

SELECT COUNT(*) AS total_quartos FROM rooms;

### Quantas reservas em aberto o hotel possui no momento?

SELECT COUNT(*) AS reservas_em_aberto
FROM reservations
WHERE status = 'SCHEDULED' OR status = 'IN_USE';

### Quantos quartos temos vagos no momento?

SELECT COUNT(*) AS quartos_vagos
FROM rooms r
WHERE NOT EXISTS (
    SELECT 1
    FROM reservations res
    WHERE res.roomId = r.id
    AND res.status = 'IN_USE'
);


### Quantos quartos temos ocupados no momento?

SELECT COUNT(*) AS quartos_ocupados
FROM reservations
WHERE status = 'IN_USE';

### Quantas reservas futuras o hotel possui?

SELECT COUNT(*) AS reservas_futuras
FROM reservations
WHERE checkin > CURRENT_TIMESTAMP;
![Banner](.img/banner.jpg)

### Qual o quarto mais caro do hotel?

SELECT * FROM rooms
ORDER BY price DESC
LIMIT 1;


&nbsp;
&nbsp;


## 🔧 Melhorias Futuras

- Implementar autenticação e autorização.
- Adicionar testes unitários e de integração.
- Melhorar o tratamento de erros e mensagens de resposta.
- Implementar notificações para reservas futuras.
- Adicionar funcionalidades de relatórios gerenciais.

&nbsp;

## 📞 Contato


- Email: [andressa.rodrigues2172@gmail.com](mailto:andressa.rodrigues2172@gmail.com)  
- LinkedIn: [andressarodrigues2172dev](https://www.linkedin.com/in/andressarodrigues2172dev)
&nbsp;
&nbsp;


>>>>>>> 799c54c (Estrutura inicial do backend)
