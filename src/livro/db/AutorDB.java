/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livro.db;

import livro.model.Livro;
import livro.model.Autor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nathan Not
 */
public class AutorDB {

    private static final String URL = "jdbc:sqlite:livro.db";
    private static final String TABLE = "autor";
    private Connection conn;

    private PreparedStatement selectTodos;
    private PreparedStatement selectOne;
    private PreparedStatement insertNovo;
    private PreparedStatement update;
    private PreparedStatement delete;
    private PreparedStatement deleteAll;

    public AutorDB() {
        try {
            conn = DriverManager.getConnection(URL);

            createTable();

            selectTodos = conn.prepareStatement("SELECT * FROM " + TABLE);
            selectOne = conn.prepareStatement("SELECT * FROM " + TABLE + " WHERE id=?");
            insertNovo = conn.prepareStatement("INSERT INTO " + TABLE + " (nome, cpf) VALUES (?,?)");
            update = conn.prepareStatement("UPDATE " + TABLE + " SET nome=?, cpf=? WHERE id=?");
            delete = conn.prepareStatement("DELETE FROM " + TABLE + " WHERE id=?");
            deleteAll = conn.prepareStatement("DELETE FROM " + TABLE);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE
                + "  (id            INTEGER,"
                + "   nome          VARCHAR(255),"
                + "   cpf           LONG,"
                + "   PRIMARY KEY (id))";
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
    }

    private Autor getAutorFromRs(ResultSet rs) throws SQLException {
        return new Autor(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getLong("cpf")
        );
    }

    public Autor getAutor(int id) {
        Autor resultado = null;
        ResultSet rs = null;

        try {
            selectOne.setInt(1, id);

            rs = selectOne.executeQuery();

            while (rs.next()) {
                resultado = getAutorFromRs(rs);
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

    // obtém todas as pessoas
    public List<Autor> getAutores() {
        List<Autor> resultado = null;
        ResultSet rs = null;

        try {
            rs = selectTodos.executeQuery();
            resultado = new ArrayList<>();

            while (rs.next()) {
                resultado.add(getAutorFromRs(rs));
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

    public int addAutor(Autor c) {
        int resultado = addAutor(c.getNome(), c.getCpf());

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

    // adiciona uma pessoa
    public int addAutor(String nome, long cpf) {
        int resultado = 0;

        try {
            insertNovo.setString(1, nome);
            insertNovo.setLong(2, cpf);

            // insere e retorna o numero de linhas atualizadas
            resultado = insertNovo.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int updateAutor(Autor c) {
        return updateAutor(c.getId(), c.getNome(), c.getCpf());
    }

    public int updateAutor(int id, String nome, long cpf) {
        int resultado = 0;

        try {
            update.setString(1, nome);
            update.setLong(2, cpf);

            update.setInt(3, id);

            // retorna o numero de linhas atualizadas
            resultado = update.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return resultado;
    }

    public int deleteAutor(Autor c) {
        return deleteAutor(c.getId());
    }

    public int deleteAutor(int id) {
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
