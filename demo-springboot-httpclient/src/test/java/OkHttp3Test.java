import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.zongkx.okhttp3.OkHttp3Interceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;

/**
 * @author zongkxc
 */
@Slf4j
public class OkHttp3Test {

    // 简单的get请求
    @Test
    public void ah1() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url("http://localhost:8080/get?name=123").build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            log.info(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 简单的post请求  FormBody formBody = new FormBody.Builder().build();
    @Test
    public void ah2() throws JsonProcessingException {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), new ObjectMapper().writeValueAsString(ImmutableMap.of("k","v")));
        Request request = new Request.Builder().addHeader("Content-Type", "application/json").addHeader("token","test")
                .post(requestBody).url("http://localhost:8080/post").build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            log.info(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //含有拦截器的用法
    @Test
    @SneakyThrows
    public void ah3() {
        OkHttpClient okHttpClient= new OkHttpClient();
        OkHttpClient my =  okHttpClient.newBuilder().addInterceptor(new OkHttp3Interceptor(okHttpClient)).build();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), new ObjectMapper().writeValueAsString(ImmutableMap.of("k","v")));
        Request request = new Request.Builder().post(requestBody).url("http://localhost:8080/post").build();
        try (Response response = my.newCall(request).execute()) {
            log.info(response.body().string());
        }
    }

}
