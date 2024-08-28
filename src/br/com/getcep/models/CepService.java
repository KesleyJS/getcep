import br.com.getcep.models.Address;
import br.com.getcep.models.AddressViaCep;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class GetAddress {
    private final String clientUrl = "https://viacep.com.br/ws/";

    public Address getAddress(String cep) throws IOException, InterruptedException {
        String encodedGet = URLEncoder.encode(cep, StandardCharsets.UTF_8);
        String urlGet = clientUrl + encodedGet + "/json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlGet))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        // Handle potential errors (e.g., empty response, invalid JSON)
        if (json.isEmpty()) {
            System.out.println("Erro ao buscar CEP. Verifique a conexão.");
            return null;
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        AddressViaCep addressViaCep = gson.fromJson(json, AddressViaCep.class);
        return new Address(addressViaCep);
    }
}