package br.com.literalura.domain.book;

import br.com.literalura.domain.author.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record BookDTO(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AuthorDTO> author,
        @JsonAlias("languages") List<String> language,
        @JsonAlias("download_count") Double downloads
) {
}