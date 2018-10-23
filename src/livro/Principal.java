package livro;

import livro.db.LivroDB;
import livro.db.AutorDB;
import livro.model.Livro;
import livro.model.Autor;

public class Principal {

    public static void main(String[] args) {
        LivroDB livroDB = new LivroDB();
        AutorDB autorDB = new AutorDB();
        
        Autor autor1 = new Autor(0, "Alberto dos céus", 1234567891);
        Livro livro1 = new Livro(0, "Um livro a ser lido", 2018, "Editora suprema", autor1);
        
        // não registrado o autor, pois a propria implementação de adicionar um
        // livro ja verifica se existe ou não o autor e o cria caso não exista
        // e da o update caso exista (o autor)
        livroDB.addLivro(livro1);
        
        Autor autor2 = new Autor(0, "Jubileu de ferro", 1987654321);
        Livro livro2 = new Livro(0, "Um livro que não deve ser lido", 2015, "Editora das flores", autor2);
        
        // inserindo o autor e depois o livro manualmente
        autorDB.addAutor(autor2);
        livroDB.addLivro(livro2);
        
        // imprimindo os autores/livros adicionados
        System.out.println("Add Livro/Autor");
        printAutor();
        printLivro();
        
        autor1.setNome("Fernando Kappa");
        autor2.setCpf(1112223334);
        
        livro1.setAutor(autor2);
        livro2.setAutor(autor1);
        
        autorDB.updateAutor(autor1);
        autorDB.updateAutor(autor2);
        
        livroDB.updateLivro(livro1);
        livroDB.updateLivro(livro2);
        
        // imprimindo os autores/livros alterados
        System.out.println("\n\n\n\nUpdate Livro/Autor");
        printAutor();
        printLivro();
        
        livroDB.deleteLivro(livro1);
        livroDB.deleteLivro(livro2);
        
        autorDB.deleteAutor(autor1);
        autorDB.deleteAutor(autor2);
        
        // imprimindo caso algum autor/livros não tenha sido deletado
        System.out.println("\n\n\n\nDelete Livro/Autor");
        printAutor();
        printLivro();
    }
    
    public static void printAutor(){
        System.out.println("===== Imprimindo Autores =====");
        for (Autor autor : new AutorDB().getAutores()) {
            System.out.println(autor);
        }
        System.out.println("==============================");
    }
    
    public static void printLivro(){
        System.out.println("===== Imprimindo  Livros =====");
        for (Livro livro : new LivroDB().getLivros()) {
            System.out.println(livro);
        }
        System.out.println("==============================");
    }
}
