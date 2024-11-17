package br.com.literalura.domain.results;

import br.com.literalura.domain.book.BookDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record Responses(
        @JsonAlias("results") List<BookDTO> results
) {}