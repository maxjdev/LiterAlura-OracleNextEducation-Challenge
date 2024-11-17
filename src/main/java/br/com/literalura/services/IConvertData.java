package br.com.literalura.services;

public interface IConvertData {
    <T> T getData(String json, Class<T> tClass);
}
