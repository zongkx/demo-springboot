import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.zongkx.feign.FeignService;
import com.zongkx.okhttp3.OkHttp3Interceptor;
import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author zongkxc
 */
public class FeignTests {


    @Test
    @SneakyThrows
    public void  ah1(){
        // 使用 OkHttpClient 作为 feign的http客户端
        OkHttpClient okHttpClient = new OkHttpClient();
        // 且改OkHttpClient客户端使用了拦截器
        OkHttpClient build = okHttpClient.newBuilder().addInterceptor(new OkHttp3Interceptor(okHttpClient)).build();
        ObjectMapper objectMapper = new ObjectMapper();
        FeignService target = Feign.builder()
                .errorDecoder(new ExceptionDecoder())//异常解析器
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .decoder(new JacksonDecoder(objectMapper))// jackson
                .encoder(new JacksonEncoder(objectMapper))// jackson
                .client(new feign.okhttp.OkHttpClient(build))// client指定
                .target(FeignService.class, "http://localhost:8080");
        String list = target.list(objectMapper.writeValueAsString(ImmutableMap.of("k", "v")), new HashMap<>());
        System.out.println(list);
    }

    public static class ExceptionDecoder extends ErrorDecoder.Default {
        @Override
        public Exception decode(String methodKey, Response response) {
            Exception decode = super.decode(methodKey, response);
            return new RuntimeException(decode);
        }
    }
}
