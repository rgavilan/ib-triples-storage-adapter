package es.um.asio.service.trellis;

import com.jayway.restassured.specification.RequestSpecification;

public interface TrellisCommonOperations {

	RequestSpecification createRequestSpecification();
}
