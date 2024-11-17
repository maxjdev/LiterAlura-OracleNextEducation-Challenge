package br.com.literalura.main;

import br.com.literalura.domain.author.Author;
import br.com.literalura.domain.book.Book;
import br.com.literalura.domain.book.BookDTO;
import br.com.literalura.domain.results.Responses;
import br.com.literalura.repositories.AuthorRepository;
import br.com.literalura.repositories.BookRepository;
import br.com.literalura.services.ConsumptionAPI;
import br.com.literalura.services.ConvertData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private final ConvertData convertData = new ConvertData();
    private final ConsumptionAPI consumptionApi = new ConsumptionAPI();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    List<Book> books;
    List<Author> authors;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        final var menu = """
                \n\t**** Por favor, selecione uma opção ****
                \t1 - Pesquisar livro por título
                \t2 - Listar livros cadastrados
                \t3 - Listar autores registrados
                \t4 - Listar autores vivos em um determinado ano
                \t5 - Listar livros por idioma
                \n\t0 - Sair
                """;
        var option = -1;
        System.out.println("****************************************");
        while (option != 0) {
            System.out.println(menu);
            System.out.print("Opção: ");
            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listAuthorsAliveInYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.out.println("finalizando aplicação...");
                    break;
                default:
                    System.out.println("Opção inválida, por favor, tente novamente");
                    break;
            }
        }
        System.out.println("****************************************");
    }

    private void searchBookByTitle() {
        System.out.print("Pesquise o livro por título...Por favor, insira o título: ");
        String inTitle = sc.nextLine();

        var json = consumptionApi.getData(inTitle.replace(" ", "%20"));
        var data = convertData.getData(json, Responses.class);

        if (data.results().isEmpty()) {
            System.out.println("Livro não encontrado");
        } else {
            BookDTO bookDTO = data.results().get(0);
            Book book = new Book(bookDTO);
            System.out.println("\n" + book);
            Author author = new Author().getFirstAuthor(bookDTO);
            System.out.println("\n" + author);
            saveData(book, author);
        }
    }

    private void saveData(Book book, Author author) {
        Optional<Book> bookFound = bookRepository.findByTitleContains(book.getTitle());
        if (bookFound.isPresent()) {
            System.out.println("este livro já estava registrado");
        } else {
            try {
                bookRepository.save(book);
            } catch (Exception e) {
                System.out.println("Mensagem de erro: " + e.getMessage());
            }
        }

        Optional<Author> authorFound = authorRepository.findByNameContains(author.getName());
        if (authorFound.isPresent()) {
            System.out.println("este autor já estava cadastrado");
        } else {
            try {
                authorRepository.save(author);
            } catch (Exception e) {
                System.out.println("Mensagem de erro: " + e.getMessage());
            }
        }
    }

    private void listRegisteredBooks() {
        System.out.println("Listar livros cadastrados\n---------------------");
        books = bookRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void listRegisteredAuthors() {
        System.out.println("Listar autores registrados\n-----------------------");
        authors = authorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void listAuthorsAliveInYear() {
        System.out.print("Liste os autores vivos em um determinado ano...Por favor, insira o ano: ");
        Integer year = Integer.valueOf(sc.nextLine());
        authors = authorRepository
                .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        if (authors.isEmpty()) {
            System.out.println("Autores vivos não encontrados");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .forEach(System.out::println);
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Listar livros por idioma\n----------------------");
        System.out.println("""
                \n\t---- Por favor, selecione um idioma ----
                \ten - Inglês
                \tes - Espanhol
                \tfr - Francês
                \tpt - Português
                """);
        String lang = sc.nextLine();
        books = bookRepository.findByLanguageContains(lang);
        if (books.isEmpty()) {
            System.out.println("Livros por idioma selecionado não encontrados");
        } else {
            books.stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .forEach(System.out::println);
        }
    }
}
