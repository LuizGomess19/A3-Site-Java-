# 🍺 Walnut Brewery - Sistema de Reservas e Gestão de Chopp

Trabalho Prático - Unidade Curricular: Modelos, Métodos e Técnicas de Engenharia de Software (UniBH)
**Professor:** Lucas Goulart Silva
**Integrantes:** Pedro Picinin Velloso Vieira - 12410337
Luiz Antônio Gomes Vicente -12313884
Leonardo Alves Silva - 124221849
Sabrina Abade Fernandes Ribeiro - 124222032
Rafael Moura Souza - 124222140

---

## 1. Definição do Problema
O problema identificado pertence ao contexto comercial e operacional de uma cervejaria artesanal local. Atualmente, muitos estabelecimentos desse porte gerenciam reservas de barris de chopp de forma manual (via WhatsApp ou planilhas). Isso gera gargalos no atendimento, erros de cálculo de frete ou valores de barris, e perda de dados importantes.

**Nossa Solução:** Desenvolvemos uma plataforma web (Front-end) integrada a uma API REST (Back-end Java) que automatiza o processo de reserva. O sistema calcula automaticamente os valores com base no rótulo, volume e tipo de entrega, registrando tudo de forma segura em um banco de dados persistente e fornecendo um painel administrativo para a gestão do negócio.

## 2. Levantamento e Análise de Requisitos
Optamos por uma **abordagem ágil** para a elicitação de requisitos, utilizando Histórias de Usuário (User Stories):

* **US01:** Como cliente, eu quero preencher um formulário de reserva escolhendo o tamanho do barril (30L ou 50L) e o rótulo da cerveja, para garantir meu chopp para um evento de forma autônoma.
* **US02:** Como cliente, eu quero ver o resumo da minha reserva (valores dos produtos e do frete) atualizado reativamente em tempo real na tela, para saber exatamente quanto vou pagar antes de confirmar o pedido.
* **US03:** Como administrador da cervejaria, eu quero acessar um painel restrito que me mostre o faturamento estimado, a volumetria de barris vendidos e uma lista cronológica de todas as reservas, para gerenciar as entregas e a operação diária.

## 3. Desenvolvimento da Solução e Arquitetura

O sistema foi desenvolvido utilizando as seguintes tecnologias:
- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.2.5 (Web, Data JPA)
- **Banco de Dados**: H2 (persistido em arquivo local `./db/walnutdb`)
- **Frontend**: HTML, CSS, JS puros (Cliente) e Thymeleaf (Admin)

### Aplicação dos Princípios SOLID
O projeto foi refatorado para garantir aderência aos princípios SOLID, com destaque para o **Single Responsibility Principle (SRP)**:
- Inicialmente, a regra de negócio (cálculo de preços e frete) estava acoplada ao `PedidoApiController`.
- Refatoramos a arquitetura criando a camada `PedidoService`. Agora, o *Controller* é responsável apenas por gerenciar o tráfego HTTP (requisições e respostas da API), enquanto o *Service* concentra toda a lógica comercial de cálculo e validação. O *Repository* cuida exclusivamente do contrato com o banco de dados.

### Padrões de Projeto (Design Patterns)
Utilizamos padrões arquiteturais e de projeto nativos do ecossistema Spring:
- **Injeção de Dependência (Dependency Injection):** Utilizado via `@Autowired` para desacoplar a criação de objetos. O ciclo de vida do `PedidoRepository` e do `PedidoService` é gerenciado pelo contêiner do Spring.
- **Singleton:** As classes anotadas com `@Service` e `@RestController` são instanciadas como Singletons pelo Spring, garantindo que apenas uma instância exista em memória durante a execução para otimizar recursos.

### Testes Unitários
Foram implementados testes unitários utilizando **JUnit 5** e **Mockito**. A classe `PedidoServiceTest` valida as regras de cálculo de valores (com e sem frete) e as validações de exceção da camada de serviço, realizando o *mock* do repositório para isolar o teste do banco de dados real.

## 4. Modelagem da Solução

Abaixo apresentamos o Diagrama de Classes focando no Back-end, evidenciando as Entidades, Repositórios, Serviços e Controladores:

![Diagrama de Classes](diagrama-classes.png)

## 5. Como Executar a Aplicação

Para rodar este projeto, o único requisito é ter o **Java JDK 21** (ou superior) instalado na máquina. 

**Passo a passo:**
1. Faça o clone deste repositório.
2. Abra o terminal na raiz do projeto e execute o comando usando o Maven Wrapper embutido:
   * No Windows: `.\mvnw spring-boot:run`
   * No Linux/Mac: `./mvnw spring-boot:run`
3. O servidor inicializará no Tomcat embarcado na porta **8080**.

**Acessando as Interfaces:**
* **Site do Cliente:** [http://localhost:8080](http://localhost:8080)
* **Painel Administrativo:** [http://localhost:8080/admin](http://localhost:8080/admin)
* **Console H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:file:./db/walnutdb`, User: `sa`, Senha em branco).