package com.example2.demo.provider;

import com.alibaba.fastjson.JSON;
import com.example2.demo.dto.AccessTokenDTO;
import com.example2.demo.dto.GithubUser;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTPlatform;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws IOException {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
//                .url("https://github.com/login/oauth/access_token?client_id=e4405eacc930c3f68c4e&client_id=19957dbbcb51f3c87c84c11f8cf0fe5038424f01&code="+accessTokenDTO.getCode()+"&redirect_uri=localhost:8080/callback&state=1")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
//            System.out.printf(string); //access_token=3f40c763dfe49392798846dafe43c85c5ce0fb6b&scope=user&token_type=bearer
            String token = string.split("&")[0].split("=")[1];
//            System.out.printf(token);  //28b5f8f53860efee6a8002821d197a21ecbf2f1e
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+ accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
