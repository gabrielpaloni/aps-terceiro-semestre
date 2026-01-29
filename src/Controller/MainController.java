package Controller;

import View.TelaEscolhaLogin;
import View.TelaLoginAdm;
import View.TelaLoginAluno;
import View.TelaLoginProfessor;

public class MainController {

    public void iniciarSistema() {
        new TelaEscolhaLogin().mostrar(this::painelLogin);
    }

    private void painelLogin(String tipo) {
        switch (tipo) {
            case "Administrador" -> new TelaLoginAdm().mostrar();
            case "Aluno" -> new TelaLoginAluno().mostrar("", "");
            case "Professor" -> new TelaLoginProfessor().mostrar();
        }
    }
}