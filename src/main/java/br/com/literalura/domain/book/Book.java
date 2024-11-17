package br.com.literalura.domain.book;

import br.com.literalura.domain.author.Author;
import br.com.literalura.domain.author.AuthorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String title;
    private String author;
    private String language;
    private Double downloads;

    public Book(BookDTO bookDTO) {
        this.title = bookDTO.title();
        this.author = getFirstAuthor(bookDTO).getName();
        this.language = getFirstLanguage(bookDTO);
        this.downloads = bookDTO.downloads();
    }

    public Author getFirstAuthor(BookDTO bookDTO) {
        AuthorDTO authorDTO = bookDTO.author().get(0);
        return new Author(authorDTO);
    }

    public String getFirstLanguage(BookDTO bookDTO) {
        return bookDTO.language().get(0);
    }

    @Override
    public String toString() {
        return "\n**** Livro ****" +
                "\n\tTítulo: " + title +
                "\n\tAutor: " + author +
                "\n\tLingua: " + language +
                "\n\tDownloads: " + downloads;
    }
}