import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import okio.GzipSource;
import okio.Okio;

import java.io.IOException;

class GzipInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return decompress(response);
    }


    private Response decompress(final Response response) throws IOException {
        if (response.body() == null) {
            return response;
        }

        String encoding = response.headers().get("Content-Encoding");

        if (encoding == null || !encoding.equals("gzip")) {
            return response;
        }

        GzipSource responseBody = new GzipSource(response.body().source());
        Headers strippedHeaders = response.headers().newBuilder().build();
        String responseContent = response.body().contentType().toString();
        return response
                .newBuilder()
                .headers(strippedHeaders)
                .body(
                        new RealResponseBody(
                                responseContent,
                                response.body().contentLength(),
                                Okio.buffer(responseBody)
                        ))
                .build();
    }
}