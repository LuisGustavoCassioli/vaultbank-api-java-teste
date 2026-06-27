# VaultBank 🏦

VaultBank é um sistema de Internet Banking Fullstack, moderno e seguro, desenvolvido com **Spring Boot 3** no backend e **React (Vite)** no frontend.

O projeto tem como objetivo simular operações financeiras do mundo real, incluindo cadastro de usuários, autenticação segura (JWT), gestão de contas, transferências via PIX/TED e um painel de administração restrito.

---

## 🛠 Tecnologias Utilizadas

### Backend (`/vaultbank-api`)
- **Java 21**
- **Spring Boot 3.5** (Web, Security, Data JPA, Validation)
- **Spring Security + JWT** para autenticação e controle de acesso
- **PostgreSQL** como banco de dados em produção
- **H2 Database** como banco de dados em memória para testes (JUnit / Mockito)
- **Lombok** para redução de boilerplate
- **Swagger / OpenAPI** para documentação da API

### Frontend (`/vaultbank-frontend`)
- **React 18** com **Vite** para máxima velocidade e performance
- **Vanilla CSS** moderno (Glassmorphism, Dark Mode, animações dinâmicas)
- **React Router Dom** para navegação de páginas (SPA)
- **Lucide React** para iconografia

---

## 🚀 Arquitetura e Funcionalidades

O projeto possui uma arquitetura limpa e segmentada:

1. **Autenticação:** Login e registro protegidos por senhas criptografadas (BCrypt) e emissão de tokens JWT.
2. **Gerenciamento de Contas:** Criação automática de conta bancária para cada novo usuário com geração de número sequencial único.
3. **Transações:** Motor transacional para transferências seguras. Suporte a tipagem de transferências (Ex: PIX, TED), validando saldo e lançando o extrato do remetente e do destinatário de forma atômica.
4. **Painel Admin:** Acesso restrito via Role (`ROLE_ADMIN`) para visualizar todos os usuários e bloquear/desbloquear acessos.
5. **Integração de Testes:** Suíte E2E e unitária isolada usando H2 e MockMvc para atestar estabilidade das lógicas principais de serviço e de controlador.

---

## ⚙️ Como Executar o Projeto

Para rodar este projeto na sua máquina local, você precisará de:
- [Java 21+](https://adoptium.net/)
- [Node.js](https://nodejs.org/) (v18+)
- [PostgreSQL](https://www.postgresql.org/)

### 1. Clonando o repositório
```bash
git clone https://github.com/seu-usuario/vaultbank.git
cd vaultbank
```

### 2. Configurando e Rodando o Backend (API)
Navegue até a pasta da API:
```bash
cd vaultbank-api
```
Certifique-se de configurar o acesso ao seu PostgreSQL local alterando o arquivo `application.properties` com suas credenciais ou usando variáveis de ambiente.

Para rodar a aplicação:
```bash
# No Windows
.\mvnw.cmd spring-boot:run

# No Mac/Linux
./mvnw spring-boot:run
```
A API iniciará na porta `8080`.

### 3. Configurando e Rodando o Frontend
Em um novo terminal, navegue até a pasta do frontend:
```bash
cd vaultbank-frontend
```

Instale as dependências:
```bash
npm install
```

Inicie o servidor de desenvolvimento:
```bash
npm run dev
```
O painel visual iniciará na porta `5173` (ou a próxima disponível apontada pelo Vite). Acesse através do seu navegador: `http://localhost:5173`.

---

## 🧪 Rodando os Testes
Para garantir que as regras de negócio estão sólidas, a API possui testes automatizados. Na pasta `vaultbank-api`, execute:

```bash
.\mvnw.cmd test
```

## 📝 Documentação da API
Após ligar o servidor Spring Boot, a documentação Swagger da API pode ser encontrada em:
`http://localhost:8080/swagger-ui/index.html`

---
*Feito com 💡 e ☕.*
