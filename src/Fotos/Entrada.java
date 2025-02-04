package Fotos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Entrada {
    /**
     * Classe com as rotinas de entrada e saída do projeto
     * @author Hilario Seibel Junior e Daniel Oliveira Lemos
     */
    public Scanner input;
    /**
     * Construtor da classe InputOutput
     * Se houver um arquivo input.txt, define que o Scanner vai ler deste arquivo.
     * Se o arquivo não existir, define que o Scanner vai ler da entrada padrão (teclado)
     */
    public Entrada() {
        try {
            // Se houver um arquivo input.txt na pasta corrente, o Scanner vai ler dele.
            this.input = new Scanner(new FileInputStream("input.txt"));
            // NAO ALTERE A LOCALICAÇÃO DO ARQUIVO!!
        } catch (FileNotFoundException e) {
            // Caso contrário, vai ler do teclado.
            this.input = new Scanner(System.in);
        }
    }

    /**
     * Faz a leitura de uma linha inteira
     * Ignora linhas começando com #, que vão indicar comentários no arquivo de entrada:
     * @param msg: Mensagem que será exibida ao usuário
     * @return Uma String contendo a linha que foi lida
     */
    private String lerLinha(String msg) {
        // Imprime uma mensagem ao usuário, lê uma e retorna esta linha
        System.out.print(msg);
        String linha = this.input.nextLine();

        // Ignora linhas começando com #, que vão indicar comentários no arquivo de entrada:
        while (linha.charAt(0) == '#') linha = this.input.nextLine();
        return linha;
    }

    /**
     * Faz a leitura de um número inteiro
     * @param msg: Mensagem que será exibida ao usuário
     * @return O número digitado pelo usuário convertido para int
     */
    public int lerInteiro(String msg) {
        // Imprime uma mensagem ao usuário, lê uma linha contendo um inteiro e retorna este inteiro
        String linha = this.lerLinha(msg);
        return Integer.parseInt(linha);
    }

    /**
     * Imprime o menu principal, lê a opção escolhida pelo usuário e retorna a opção selecionada.
     * @return Inteiro contendo a opção escolhida pelo usuário
     */
    public int menu1() {
        // Imprime o menu principal, lê a opção escolhida pelo usuário e retorna a opção selecionada.

        String msg = "*********************\n" +
                "Escolha uma opção:\n" +
                "1) Cadastrar pessoa.\n" +
                "2) Cadastrar empresa.\n" +
                "3) Login.\n" +
                "0) Sair.\n";

        int op = this.lerInteiro(msg);

        while (op < 0 || op > 3) {
            System.out.println("Opção inválida. Tente novamente: ");
            op = this.lerInteiro(msg);
        }

        return op;
    }

    /***************************************************/

    /**
     * Lê os dados de uma nova Pessoa e cadastra-a no sistema.
     * @param s: Um objeto da classe Sistema
     */
    public void cadPessoa(Sistema s) {
        String login = this.lerLinha("Escolha um login: ");

        while (s.buscarUsuario(login) != null) {
            login = this.lerLinha("Usuário já existente. Escolha outro login: ");
        }

        String nome = this.lerLinha("Digite seu nome: ");
        String s1 = this.lerLinha("Digite sua senha: ");
        String cpf = this.lerLinha("Digite seu cpf: ");
        int dia = this.lerInteiro("Digite seu dia de nascimento: ");
        int mes = this.lerInteiro("Digite seu mes de nascimento: ");
        int ano = this.lerInteiro("Digite seu ano de nascimento: ");

        Pessoa p = new Pessoa(login, nome, s1, cpf, dia, mes, ano);

        s.novaPessoa(p);

        System.out.println("*************Pessoa cadastrada com sucesso*************");
    }

    public int menu2(Sistema s, Usuario u){
        String msg = "*********************\n" +
                "Escolha uma opção:\n" +
                "1) Seguir alguém.\n" +
                "2) Fazer uma postagem.\n" +
                "3) Exibir meu feed.\n" +
                "0) Sair.\n";

        int op = this.lerInteiro(msg);

        while (op < 0 || op > 3) {
            System.out.println("Opção inválida. Tente novamente: ");
            op = this.lerInteiro(msg);
        }

        return op;
    }

    public void login(Sistema s){
        String login = this.lerLinha("Login: ");

        if (s.buscarUsuario(login) == null){
            System.out.println("ERRO: Login inexistente");
        }

        Usuario u = s.buscarUsuario(login);

        String senha = this.lerLinha("Senha: ");

        while (!senha.equals(u.senha)){
            senha = this.lerLinha("Senha incorreta. Insira a senha novamente: ");
        }

        System.out.println("******Seja bem-vindo, " + u.nome + "!******");

        int opcao = menu2(s, u);
        System.out.println(opcao);
        while (opcao != 0){
            if (opcao == 1){

                // exibe todos os usuários
                s.listarUsuarios();

                // pergunta qual ele quer seguir
                String escolhido = this.lerLinha("Quem deseja seguir?\n");

                Usuario u2 = s.buscarUsuario(escolhido);

                while (u2 == null){
                    System.out.println("*** Usuario não encontrado. Assegure-se de digitar o LOGIN do Usuario");
                }

                // cria as conexões necessárias
                u.seguir(u2);

                System.out.println("*****Você agora segue " + u2.nome);

                opcao = menu2(s, u);
                System.out.println(opcao);

            }

            else
            {
                if (opcao == 2){
                    cadPostagem(s, u);
                    opcao = menu2(s, u);
                    System.out.println(opcao);
                }
                else{
                    if (opcao == 3){
                        u.feed();
                        opcao = menu2(s, u);
                        System.out.println(opcao);

                    }
                }
            }
        }
    }

    public void cadEmpresa(Sistema s){
        String login = this.lerLinha("Insira o login da empresa: ");
        String nome = this.lerLinha("Insira o nome da empresa: ");
        String senha = this.lerLinha("Insira uma senha: ");
        String cnpj = this.lerLinha("Insira o CNPJ da empresa: ");

        Empresa emp = new Empresa (login, nome, senha, cnpj);

        s.novaEmpresa(emp);

        System.out.println("*************Empresa cadastrada com sucesso*************");
    }

    public void cadPostagem(Sistema s, Usuario u){
        String f = this.lerLinha("Insira a foto: ");
        String leg = this.lerLinha("Digite uma legenda: ");
        int dia = this.lerInteiro("Digite o dia da postagem: ");
        int mes = this.lerInteiro("Digite o mes da postagem: ");
        int ano = this.lerInteiro("Digite o ano da postagem: ");

        // validar senha
        String senha = this.lerLinha("Senha: ");

        while (!senha.equals(s.buscarUsuario(u.login).senha)){
            senha = this.lerLinha("Senha incorreta. Insira a senha novamente: ");
        }

        Data dataPost = new Data(dia, mes, ano);

        u.postar(f, leg, dataPost, senha);

        System.out.println("*************Postagem cadastrada com sucesso*************");
    }
}