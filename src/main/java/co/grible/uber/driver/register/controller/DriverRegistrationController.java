package co.grible.uber.driver.register.controller;

import co.grible.uber.driver.register.model.AuthorizationResponse;
import co.grible.uber.driver.register.model.UserInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class DriverRegistrationController {

    private static String AUTHORIZATION_URL = "https://login.uber.com/oauth/v2/token";
    private static String PERSONALINFO_URL = "https://api.uber.com/v1/me";

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/register-rider", method = RequestMethod.GET)
    public String displayRegisterRiderForm(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", "Hello dude");
        return "register-driver";
    }

    @RequestMapping(value = "/hookmeup", method = RequestMethod.GET)
    public String registerRider(@RequestParam(value = "code", required = false) String code, Map<String, Object> model) throws IOException {

        AuthorizationResponse authorizationResponse = retrieveAuthorisationConfirmation(code); //oath
        String result = retrieveUserInformation(authorizationResponse.getAccessToken()); // /me

        System.out.println(result);
        return "complete-details";

    }

    private String retrieveUserInformation(final String accessToken) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(PERSONALINFO_URL);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());

        return result;
    }


    private AuthorizationResponse retrieveAuthorisationConfirmation(@RequestParam(value = "code", required = false) String code) throws IOException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("client_id", "wrM46LuAXxFP4CqmeaOX4wlO66g0ZsMI"));
        urlParameters.add(new BasicNameValuePair("client_secret", "1w0-YUL5d4XMZAhY4yhJWX1G6dWg-YvWTAO3f5kX"));
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        urlParameters.add(new BasicNameValuePair("AUTHORIZATION_URL", AUTHORIZATION_URL));
        urlParameters.add(new BasicNameValuePair("redirect_uri", "http://localhost:9000/hookmeup"));
        urlParameters.add(new BasicNameValuePair("code", code));

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(AUTHORIZATION_URL);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        String result = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, AuthorizationResponse.class);
    }


}
