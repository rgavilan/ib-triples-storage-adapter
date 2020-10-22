package es.um.asio.service.trellis.impl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

import es.um.asio.service.trellis.TrellisCommonOperations;

@Service
public class TrellisCommonOperationsImpl implements TrellisCommonOperations {
	
    /** The authentication enabled. */
    @Value("${app.trellis.authentication.enabled:false}")
    private Boolean authenticationEnabled;
    
    /** The username. */
    @Value("${app.trellis.authentication.username}")
    private String username;
   
    /** The password. */
    @Value("${app.trellis.authentication.password}")
    private String password;
	
	 /**
     * Creates the request specification and adds authentication if is required.
     *
     * @return the request specification
     */
    public RequestSpecification createRequestSpecification() {
        RequestSpecification requestSpecification = RestAssured.given();
        if(authenticationEnabled) {
            requestSpecification.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        }
        return requestSpecification;
    }
}
