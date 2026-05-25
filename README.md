# 🍺 Walnut Brewery - Projeto da Faculdade

Site e sistema de reservas de chopp feito para o trabalho da faculdade.
O backend foi construído em **Java com Spring Boot**, e os dados dos pedidos ficam salvos no banco de dados H2.

## 🛠️ Tecnologias que usei no projeto
- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.2.5
- **Banco de Dados**: H2 (salva num arquivo pra não perder os dados)
- **Frontend Admin**: Thymeleaf + HTML/CSS
- **Frontend Principal**: HTML, CSS e JS puros
- **Gerenciador de Dependências**: Maven

---

## 🏗️ Estrutura do Projeto

```text
walnut-brewery-java/
├── pom.xml (Arquivo de configuração de dependências Maven)
├── run.cmd (Script utilitário de execução facilitada no Windows)
├── db/
│   └── walnutdb.mv.db (Arquivo de banco de dados H2 gerado automaticamente)
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── walnut/
        │           └── brewery/
        │               ├── WalnutBreweryApplication.java (Classe de Inicialização)
        │               ├── model/
        │               │   └── Pedido.java (Entidade JPA mapeada no banco)
        │               ├── repository/
        │               │   └── PedidoRepository.java (Interface JPA CRUD)
        │               └── controller/
        │                   ├── PedidoApiController.java (API REST de pedidos: salvar, listar e deletar)
        │                   └── AdminController.java (Controller Thymeleaf do Painel Administrativo)
        └── resources/
            ├── application.properties (Parâmetros de configuração H2, JPA e logging)
            ├── templates/
            │   └── admin.html (Painel administrativo do professor em Thymeleaf)
            └── static/
                ├── index.html (Página principal premium do site com seção de pedidos reativa)
                ├── style.css (Estilos CSS unificados do site e painel de pedidos)
                ├── script.js (Comportamento de scroll, parallax, carrossel e Fetch API)
                └── assets/ (Imagens oficiais dos rótulos de cerveja da Walnut Brewery)
```

---

## 🚀 Como Executar o Projeto

Para rodar este projeto, o único requisito é ter o **Java JDK 21** (ou superior) instalado na máquina. 

### Opção 1: Rodar pela IDE (Recomendado)
1. Abra a sua IDE Java de preferência (ex: **IntelliJ IDEA**, **Eclipse** ou **Visual Studio Code**).
2. Vá em `File > Open...` (Abrir...) e selecione esta pasta raiz `walnut-brewery-java` (a IDE reconhecerá o arquivo `pom.xml` como um projeto Maven e fará a importação e download automático das bibliotecas necessárias).
3. Localize e abra o arquivo `src/main/java/com/walnut/brewery/WalnutBreweryApplication.java`.
4. Clique com o botão direito sobre o arquivo ou aperte no botão **Run / Executar**.
5. O servidor inicializará no Tomcat embarcado na porta **8080**.

### Opção 2: Rodar pelo Prompt do Windows
1. Na pasta raiz do projeto, dê dois cliques sobre o arquivo executável `run.cmd` criado especialmente para facilitar a inicialização.

---

## 🔗 Endpoints e Rotas Úteis

Quando a aplicação estiver rodando com sucesso, você poderá abrir o seu navegador de internet e acessar os seguintes links locais:

### 1. 🌐 Site Principal da Cervejaria
* **Link**: [http://localhost:8080](http://localhost:8080)
* **O que testar**: 
  - Navegue e veja as animações.
  - Veja o carrossel de garrafas na seção **Nossa Coleção**.
  - Vá na seção **Fazer Pedido** (ou no rodapé), preencha o formulário completo de reserva de barril.
  - Perceba que o **Resumo da Reserva** (no painel à direita) se atualiza reativamente em tempo real com base no nome do cliente, tamanho de barril escolhido, rota de entrega e rótulo!
  - Clique em **Finalizar Reserva**. Os dados serão enviados via JSON assíncrono (AJAX) para o backend Java, que processará o cálculo com segurança e salvará no banco de dados. Uma tela de confirmação de sucesso aparecerá sem precisar recarregar o site!

### 2. 📊 Painel Administrativo de Controle
1. Acesse o painel pelo navegador: [http://localhost:8080/admin](http://localhost:8080/admin)
2. Lá tem a lista com os pedidos e os totais calculados.
3. Dá pra excluir os testes que a gente faz no site.
  - Ele possui cards estatísticos dinâmicos calculando em tempo real:
    - **Faturamento Estimado** acumulado de todos os pedidos.
    - **Total de Reservas** cadastradas.
    - Contadores específicos de volumetria de barris (**30L** e **50L**).
  - Possui uma tabela elegante listando todos os pedidos registrados de forma cronológica (com ID, Data do Envio, Nome, CPF, WhatsApp, Rótulo, Período de Locação do Chopp, Tipo de Frete e Endereço).

### 3. 💾 Console de Gerenciamento do Banco H2
* **Link**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
* **Parâmetros de Acesso**:
  - **JDBC URL**: `jdbc:h2:file:./db/walnutdb`
  - **User Name**: `sa`
  - **Password**: (Deixar em branco)
* **O que testar**: Clique em **Connect** para visualizar a estrutura das tabelas SQL geradas automaticamente pelo Hibernate JPA e rodar queries SQL de teste.

---

## 📌 Diferenciais Técnicos e Acadêmicos Avaliados
- **Segurança no Backend**: O cálculo de valores de barris e frete é revalidado e efetuado diretamente no backend (`PedidoApiController.java`), evitando falsificação de preços por requisições de frontend alteradas.
- **Banco Persistente**: Utilização de arquivo local (`./db/walnutdb`) para o banco de dados. Isso garante que, mesmo que você reinicie a aplicação ou desligue a máquina, os pedidos criados não sumirão!
- **Data Auditoring**: Uso de `@PrePersist` para carimbar de forma automática e imutável a data e horário exato em que o pedido foi originado pelo cliente.
- **Uso do Lombok**: Clean code na modelagem das tabelas do banco, economizando centenas de linhas de boilerplate desnecessário.
