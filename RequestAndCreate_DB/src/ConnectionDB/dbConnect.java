package ConnectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class dbConnect {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Scanner scu = new Scanner(System.in);
        Scanner scp = new Scanner(System.in);
        
        System.out.println("Já possui uma conta conosco?");
        System.out.println("(1) Sim.");
        System.out.println("(2) Não.");
        System.out.println("(3) Sair.");
        
        System.out.print("\nSua escolha: ");
        int choice = sc.nextInt();
        
        while (choice != 3) {
            switch (choice) {
                case 1:
                    fazerLogin(scu, scp);
                    break;
                case 2:
                    criarUsuario(scu, scp);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente!");
                    break;
            }
            
            System.out.println("Já possui uma conta conosco?");
            System.out.println("(1) Sim.");
            System.out.println("(2) Não.");
            System.out.println("(3) Sair.");
            
            System.out.print("\nSua escolha: ");
            choice = sc.nextInt();
        }

        sc.close();
        scu.close();
        scp.close();
        System.out.println("\nRespeitamos a sua decisão. Até logo!");
    }

    private static void criarUsuario(Scanner scu, Scanner scp) {
        System.out.print("Crie seu nome de usuário: ");
        String username = scu.nextLine();
        System.out.print("Crie sua senha: ");
        String password = scp.nextLine();

        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            // Conectar ao banco de dados
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("\nConexão com o banco de dados estabelecida com sucesso!");

            // Inserir novo usuário na tabela 'users'
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nUsuário criado com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println("\nErro ao conectar ao banco de dados ou criar usuário: " + e.getMessage());
        } finally {
            // Fechar os recursos
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    private static void fazerLogin(Scanner scu, Scanner scp) {
        System.out.print("Insira o seu usuário: ");
        String username = scu.nextLine();
        System.out.print("Insira a sua senha: ");
        String password = scp.nextLine();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Conectar ao banco de dados
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("\nConexão com o banco de dados estabelecida com sucesso!");

            // Preparar a query para verificar se o username e password correspondem
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            rs = stmt.executeQuery();
            
            // Verificar se há correspondência
            if (rs.next()) {
                System.out.println("\nLogado com sucesso!");
            } else {
                System.out.println("\nUsuário ou senha incorretos!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados ou fazer login: " + e.getMessage());
        } finally {
            // Fechar os recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
