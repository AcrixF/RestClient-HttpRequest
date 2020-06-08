package org.neoa.restclient.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@EqualsAndHashCode
@Getter
public class HttpRequest<T> {

    private final String url;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final Class<T> responseType;
    private final Object postBody;
    private final Map<String, String> urlVars;

    private static final Predicate<Map.Entry<String, String>>
            CHECK_PARAM_NOT_EMPTY = entry -> isNotBlank(entry.getKey()) && isNotBlank(entry.getValue());

    @ConstructorProperties({"url", "urlVars", "headers", "responseType", "postBody"})
    public HttpRequest(HttpMethod method,
                       String url,
                       Map<String, String> urlVars,
                       HttpHeaders headers,
                       Class<T> responseType,
                       Object postBody) {
        this.method = method;
        this.url = url;
        this.urlVars = urlVars;
        this.headers = headers;
        this.postBody = postBody;
        this.responseType = responseType;

    }
    public static <T> HttpRequestBuilder<T> builder() {
        return new HttpRequestBuilder<>();
    }

    public static class HttpRequestBuilder<T> {

        private HttpMethod method = HttpMethod.GET;
        private String url = "";
        private Map<String, String> urlVars = new HashMap<>();
        private HttpHeaders headers = new HttpHeaders();
        private Class<T> responseType;
        private Object postBody;

        HttpRequestBuilder() {}

        public HttpRequestBuilder<T> url(String url) {
            this.url = url;
            return this;
        }

        public HttpRequestBuilder<T> method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public HttpRequestBuilder<T> post() {
            this.method = HttpMethod.POST;
            return this;
        }

        public HttpRequestBuilder<T> put() {
            this.method = HttpMethod.PUT;
            return this;
        }

        public HttpRequestBuilder<T> urlVars(Map<String, String> urlVars) {
            this.urlVars.putAll(urlVars);
            return this;
        }

        public HttpRequestBuilder<T> urlVar(String name, String value) {
            this.urlVars.put(name, value);
            return this;
        }

        public HttpRequestBuilder<T> headers(Map<String, String> headers) {
            this.headers = new HttpHeaders();
            addHeaders(headers);
            return this;
        }

        public HttpRequestBuilder<T>  addHeaders(Map<String, String> headers) {
            headers.entrySet().stream()
                    .filter(CHECK_PARAM_NOT_EMPTY)
                    .forEach(entry -> {
                        this.headers.add(entry.getKey(), entry.getValue());
                    });
            return this;
        }

        public HttpRequestBuilder<T> responseType(Class<T> responseType) {
            this.responseType = responseType;
            return this;
        }

        public HttpRequestBuilder<T> post(Object postBody) {
            this.method = HttpMethod.POST;
            this.postBody = postBody;
            return this;
        }

        public HttpRequest<T> build() {
            checkArguments();
            return new HttpRequest<T>(method, url, urlVars, headers, responseType, postBody);
        }

        private void checkArguments() {
            //checkArgument(StringUtils.isEmpty(url), "url should not be blank");
            checkNotNull(urlVars, "urlVars should not be null");
            checkNotNull(headers, "headers should not be null");
            checkNotNull(responseType, "ResponseType should be set");
        }

    }


}
