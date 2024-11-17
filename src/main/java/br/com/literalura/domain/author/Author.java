package br.com.literalura.domain.author;

import br.com.literalura.domain.book.Book;
import br.com.literalura.domain.book.BookDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;

    public Author(AuthorDTO authorDTO) {
        this.name = authorDTO.name();
        this.birthYear = Integer.valueOf(authorDTO.birthYear());
        this.deathYear = Integer.valueOf(authorDTO.deathYear());
    }

    public Author getFirstAuthor(BookDTO bookDTO) {
        AuthorDTO authorDTO = bookDTO.author().get(0);
        return new Author(authorDTO);
    }

    @Override
    public String toString() {
        return "\n**** Autor ****" +
                "\n\tNome: " + name +
                "\n\tAno de nascimento: " + birthYear +
                "\n\tAno da morte: " + deathYear;
    }
}