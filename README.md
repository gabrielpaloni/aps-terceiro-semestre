# Universidade Amazônia - APS

Este projeto é uma aplicação desktop desenvolvida em **Java** como parte da Atividade Prática Supervisionada (APS). O sistema foi projetado para gerenciar portais de diferentes perfis de usuários (Alunos, Professores e Administradores), utilizando uma arquitetura organizada e escalável.

## 🏗️ Arquitetura do Projeto
O sistema segue o padrão **MVC (Model-View-Controller)**, garantindo a separação de responsabilidades:

* **Model:** Contém as classes de entidade (ex: `Aluno.java`) e a lógica de persistência (`Conexao.java`).
* **View:** Interface do usuário, composta por múltiplas telas de login e portais específicos para cada nível de acesso.
* **Controller:** Gerencia o fluxo de dados entre a View e o Model (ex: `AlunoController.java`, `ProfessorController.java`).

## 🚀 Funcionalidades Principais
* **Autenticação Multi-nível:** Telas de login customizadas para Administradores, Alunos e Professores.
* **Portal do Aluno/Professor/Adm:** Interfaces distintas com funcionalidades específicas para cada tipo de usuário.
* **Gerenciamento de Dados:** Controlador centralizado para manipulação de informações acadêmicas.
* **Conexão com Banco de Dados:** Classe dedicada para gerenciar o estado da conexão.

## 🛠️ Tecnologias Utilizadas
* **Linguagem:** Java
* **Ambiente de Desenvolvimento:** IntelliJ IDEA (arquivos `.idea`, `.iml`)
* **Controle de Versão:** Git

## 📂 Como Executar o Projeto
1. Clone este repositório.
2. Abra o projeto em sua IDE de preferência (recomendado: IntelliJ IDEA).
3. Certifique-se de que o SDK do Java esteja configurado corretamente.
4. Execute a classe `Main.java` localizada na raiz do diretório `src`.

---
*Projeto desenvolvido por Gabriel Paloni para o curso de Ciência da Computação.*
