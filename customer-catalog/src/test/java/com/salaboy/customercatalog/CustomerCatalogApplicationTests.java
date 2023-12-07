package com.salaboy.customercatalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.salaboy.customercatalog.CustomerCatalogApplication.Customer;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.UUID;

@SpringBootTest(classes=CustomerCatalogTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@Testcontainers
class CustomerCatalogApplicationTests {

	@Test
	void storeAndRetrieveCustomerTest() {
		String customerId = UUID.randomUUID().toString();
		with()
			.body(new Customer(customerId, "salaboy", "salaboy@mail.com"))
			.contentType(ContentType.JSON)
		.when()
		.request("POST", "/")
		.then()
		.assertThat().statusCode(200);

		with().param("customerId", customerId)
		.when()
		.request("GET", "/")
		.then()
		.assertThat().statusCode(200)
		.body("name", equalTo("salaboy"))
		.body("email", equalTo("salaboy@mail.com"));
		
	}

}
