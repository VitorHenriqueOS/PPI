# Projeto Final: Sistema de Gerenciamento de Hotel
> Disciplina: Programação para Internet

Este projeto consiste em um sistema Web para o gerenciamento de operações hoteleiras, permitindo o controle de hóspedes, reservas, quartos e funcionários. O sistema foi desenvolvido seguindo a arquitetura Java Servlet, com persistência em banco de dados relacional e interface em HTML.

---

## Modelagem do Banco de Dados

O banco de dados foi modelado para suportar o fluxo principal de um hotel, desde a categorização dos quartos até a limpeza realizada pelos funcionários.

![Modelagem do Banco de Dados](DiagramaER.png)

### Descrição das Entidades
* **Hóspede:** Armazena as informações cadastrais dos clientes (CPF, Nome, Email, Telefone e Data de Nascimento).
* **Quarto:**  Representa as unidades físicas (Número e Andar), o seu status de ocupação e a categoria à qual pertencem.
* **Categoria:**  Define os tipos de quartos disponíveis, estabelecendo o preço, capacidade de pessoas e o tipo de cama.
* **Reserva:** Entidade associativa que gerencia a estadia de um hóspede em um quarto específico.
* **Funcionário:** Gerencia os dados dos colaboradores e os registros de limpeza dos quartos.

---

## Requisitos do Projeto

O projeto atende aos seguintes critérios obrigatórios:

### Tecnologias Utilizadas
* **Frontend:** HTML5 e CSS3.
* **Backend:** Java Servlets.
* **Banco de Dados:** SQL (MySQL).

### Estrutura de Dados
* **4+ Tabelas:** Hóspede, Quarto, Categoria, Funcionário, Reserva.
* **Relacionamentos:**
    * Hóspede registra Reserva.
    * Quarto pertence a uma Categoria.
    * Funcionário limpa Quarto.

### Funcionalidades (CRUD)
O sistema permite realizar as seguintes operações para as entidades principais:
1.  **Cadastro:** Inclusão de novos hóspedes, quartos e reservas.
2.  **Consulta:** Listagem geral de quartos disponíveis ou busca específica de reservas por ID.
3.  **Alteração:** Atualização de dados cadastrais de funcionários ou mudança de status de limpeza do quarto.
4.  **Remoção:** Exclusão de registros de reservas ou cancelamento de cadastros.

---

## Status do Projeto
O projeto encontra-se em fase inicial de desenvolvimento. 
*Nota: A modelagem e os requisitos podem sofrer alterações conforme o avanço da implementação.*
