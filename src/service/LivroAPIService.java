package service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LivroAPIService {

    public static String buscarNaAPI(String termo) {
        try {
            String termoFormatado = URLEncoder.encode(termo.trim(), StandardCharsets.UTF_8);
            String url = "https://openlibrary.org/search.json?q=" + termoFormatado;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (Exception e) {
            System.out.println("Erro na comunicação com a API: " + e.getMessage());
        }
        return null;
    }
}