package co.grible.uber.driver.register.controller;


import co.grible.uber.driver.register.model.UserInformation;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * for quickly testing the firebase api.
 */

@RestController
public class UserdataController {

    private static String FIREBASEURL = "https://whodrivesme-registrations.firebaseio.com/users/";

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/storeUser", method = RequestMethod.POST)
    public String storeUser(Map<String, Object> model) {

        UserInformation userInformation = new UserInformation();
        userInformation.setEmail("arthur@codedvalue.it");
        userInformation.setFirstName("Arthur");
        userInformation.setLastName("Arts");
        userInformation.setMobileVerified(true);
        userInformation.setUuid("91d81273-46c2-4b57-8124-d0165f8240c0");
        userInformation.setPhone("0625611009");
        userInformation.setPersonalInformation("Ask me about photography and old Nintendo games!");
        HttpEntity<UserInformation> request = new HttpEntity<>(userInformation);
        String result =  restTemplate.postForObject(FIREBASEURL+userInformation.getPhone()+".json", request, String.class);

        model.put("result", result);
        return "store-success";
    }

    @RequestMapping(value="/getUser/{phone}", method=RequestMethod.GET)
    public String getUser(@PathVariable(value="phone") String phone) {

        String url = FIREBASEURL+phone+".json";
//        System.out.println(url);
        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }

}
