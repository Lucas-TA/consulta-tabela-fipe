package br.com.alura.projeto.tabelafipe.services;

import java.util.List;

public interface iDataConverter {
    <T> T getData(String json, Class<T> clazz);
    <T> List<T> getList(String json, Class<T> clazz);
}
