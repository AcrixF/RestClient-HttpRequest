package org.neoa.restclient.services;

import org.neoa.restclient.model.Quote;
import org.neoa.restclient.utils.HttpRequest;
import org.neoa.restclient.utils.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

    @Autowired
    private RestClient restClient;

    public ResponseEntity<Quote> searchQuote() {

        HttpRequest<Quote> response = HttpRequest.<Quote>builder()
                .method(HttpMethod.GET)
                .url("https://gturnquist-quoters.cfapps.io/api/random")
                .responseType(Quote.class)
                .build();

        ResponseEntity<Quote> quote = restClient.execute(response);

        if (quote.getStatusCodeValue() == 200) {
            return quote;
        }

        return null;
    }

}
