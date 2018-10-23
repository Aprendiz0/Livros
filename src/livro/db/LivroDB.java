/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livro.db;

import livro.model.Livro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import livro.model.Autor;

/**
 *
 * @author Nathan Not
 */
public class LivroDB {

    private static final String URL = "jdbc:sqlite:livro.db";
    private static final String TABLE = "livro";
    private Connection conn;

    private PreparedStatement selectTodos;
    private PreparedStatement insertNovo;
    private PreparedStatement update;
    private PreparedStatement delete;
    private PreparedStatement deleteAll;

    public LivroDB() {
        try {
            conn = DriverManager.getConnection(URL);

            createTable();

            selectTodos = conn.prepareStatement("SELECT * FROM " + TABLE);
            insertNovo = conn.prepareStatement("INSERT INTO " + TABLE + " (titulo, anoPublicacao, editora, id_autor) VALUES (?,?,?,?)");
            update = conn.prepareStatement("UPDATE " + TABLE + " SET titulo=?, anoPublicacao=?, editora=?, id_autor=? WHERE id=?");
            delete = conn.prepareStatement("DELETE FROM " + TABLE + " WHERE id=?");
            deleteAll = conn.prepareStatement("DELETE FROM " + TABLE + "");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE
                + "  (id              INTEGER,"
                + "   titulo          VARCHAR(255),"
                + "   anoPublicacao   INTEGER,"
                + "   editora         VARCHAR(255),"
                + "   id_autor        INTEGER,"
                + "   FOREIGN KEY (id_autor) REFERENCES autor(id),"
                + "   PRIMARY KEY (id))";

        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
    }

    private Livro getLivroFromRs(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getInt("anoPublicacao"),
                rs.getString("editora"),
                new AutorDB().getAutor(rs.getInt("id_autor"))
        );
    }

    // obtém todas as livros
    public List<Livro> getLivros() {
        List<Livro> resultado = null;
        ResultSet rs = null;

        try {
            rs = selectTodos.executeQuery();
            resultado = new ArrayList<>();

            while (rs.next()) {
                resultado.add(getLivroFromRs(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                close();
            }
        }
        return resultado;
    }

    public int addLivro(Livro c) {
        int resultado = addLivro(c.getTitulo(), c.getAnoPublicacao(), c.getEditora(), c.getAutor());

        try {

            ResultSet generatedKeys = insertNovo.getGeneratedKeys();

            if (generatedKeys.next()) {
                c.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating client failed, no ID obtained.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    // adiciona uma livro
    public int addLivro(String titulo, int anoPublicacao, String editora, Autor autor) {
        int resultado = 0;

        try {
            if (new AutorDB().getAutor(autor.getId()) == null) {
                new AutorDB().addAutor(autor);
            } else {
                new AutorDB().updateAutor(autor);
            }
            insertNovo.setString(1, titulo);
            insertNovo.setInt(2, anoPublicacao);
            insertNovo.setString(3, editora);
            insertNovo.setInt(4, autor.getId());

            // insere e retorna o numero de linhas atualizadas
            resultado = insertNovo.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int updateLivro(Livro c) {
        return updateLivro(c.getId(), c.getTitulo(), c.getAnoPublicacao(), c.getEditora(), c.getAutor());
    }

    public int updateLivro(int id, String titulo, int anoPublicacao, String editora, Autor autor) {
        int resultado = 0;

        try {
            if (new AutorDB().getAutor(autor.getId()) == null) {
                new AutorDB().addAutor(autor);
            } else {
                new AutorDB().updateAutor(autor);
            }
            update.setString(1, titulo);
            update.setInt(2, anoPublicacao);
            update.setString(3, editora);
            update.setInt(4, autor.getId());

            update.setInt(5, id);

            // retorna o numero de linhas atualizadas
            resultado = update.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int deleteLivro(Livro c) {
        return deleteLivro(c.getId());
    }

    public int deleteLivro(int id) {
        int resultado = 0;

        try {
            delete.setInt(1, id);
            // deleta e retorna o numero de linhas atualizadas
            resultado = delete.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int deleteAllEntries() {
        int resultado = 0;

        try {
            // deleta e retorna o numero de linhas atualizadas
            resultado = deleteAll.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
