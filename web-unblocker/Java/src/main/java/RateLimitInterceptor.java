import okhttp3.Interceptor;
import okhttp3.Response;
import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;

public class RateLimitInterceptor implements Interceptor {
    private RateLimiter rateLimiter = RateLimiter.create(Settings.REQUESTS_RATE);

    @Override
    public Response intercept(Chain chain) throws IOException {
        rateLimiter.acquire(1);

        return chain.proceed(chain.request());
    }
}