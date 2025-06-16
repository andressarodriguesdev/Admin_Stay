

# 🛎️ AdminStay API

**AdminStay API** é um sistema **back‑end** em **Java 17 + Spring Boot** para gerenciamento de hotéis (clientes, quartos e reservas). O projeto prioriza boas práticas REST, documentação automática via Swagger e testes.

> **Status:** 🚧 *em desenvolvimento*

---

## ✨ Principais recursos

| Funcionalidade | Descrição |
| -------------- | --------- |
| CRUD de Clientes | Cadastro, listagem, atualização e exclusão de hóspedes |
| CRUD de Quartos  | Controle completo dos quartos (tipo, preço, disponibilidade) |
| CRUD de Reservas | Reservas com check‑in/out, status e regras de conflito |
| Dashboard (futuro) | Métricas de ocupação, receitas e check‑ins |


## ⚙️ Stack & Ferramentas

| Camada | Tecnologias |
| ------ | ----------- |
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 3 (Web, Data JPA, Validation) |
| **Banco** | H2 (memória & file‑based) para dev/teste |
| **Build** | Maven |
| **Documentação** | Swagger UI / OpenAPI 3 |
| **Testes** | JUnit 5, Mockito & Postman |
| **Futuro Front‑end** | React + Vite + Tailwind (repo a ser criado) |


## 📋 Pré‑requisitos

* JDK 17+
* Maven 3.8+

> Verifique: `java -version` e `mvn -version`.


## 🚀 Como executar

```bash
# 1. Clone o repositório
$ git clone https://github.com/SEU_USUARIO/adminstay-api.git
$ cd adminstay-api

# 2. Execute a aplicação
$ mvn spring-boot:run
```

A API iniciará em `http://localhost:8080`.

### Swagger
* UI: `http://localhost:8080/swagger-ui.html`
* Spec: `http://localhost:8080/v3/api-docs`

### Console H2 (opcional)
* `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:adminstay`


## 🧪 Testes

```bash
# Executar todos os testes de unidade e integração
$ mvn test
```

Coleção Postman disponível em `docs/postman/` para chamadas manuais.




## 🤝 Contribuindo

1. Fork este repositório
2. Crie sua feature branch: `git checkout -b feature/minha-feature`
3. Commit suas mudanças: `git commit -m 'feat: Minha nova feature'`
4. Push para a branch: `git push origin feature/minha-feature`
5. Abra um Pull Request


## 📄 Licença

Este projeto está licenciado sob a licença **MIT** – veja o arquivo `LICENSE` para detalhes.


## 📞 Contato

| | |
| - | - |
| **E‑mail** | [andressa.rodrigues2172@gmail.com](mailto:andressa.rodrigues2172@gmail.com) |
| **LinkedIn** | [andressarodrigues2172dev](https://www.linkedin.com/in/andressarodrigues2172dev) |
