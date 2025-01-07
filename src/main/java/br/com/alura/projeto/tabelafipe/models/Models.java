package br.com.alura.projeto.tabelafipe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Models(List<Data> modelos) {
}
