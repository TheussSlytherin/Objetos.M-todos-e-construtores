package service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class BibliotecaAPIService {

    private final HttpClient client;

    public BibliotecaAPIService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String buscarLivro(String termo) {
        try {
            String termoEncoded = URLEncoder.encode(termo, StandardCharsets.UTF_8);

            // Endpoint da Open Library API limitando a 5 resultados
            String url = "https://openlibrary.org/search.json?q=" + termoEncoded + "&limit=5";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Erro HTTP: " + response.statusCode();
            }

        } catch (Exception e) {
            return "Falha na conexão: " + e.getMessage();
        }
    }
}