package mg.working.service.RH.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceGeneraliser {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    public void cancelDoctype(String sid ,String doctypeName , String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestData = "{" +
                "\"doctype\": \"" + doctypeName +"\"," +
                "\"name\": \"" + name + "\"" +
                "}";
        String url = erpnextUrl + "/api/method/frappe.desk.form.save.cancel";
        HttpEntity<String> request = new HttpEntity<>(requestData, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
