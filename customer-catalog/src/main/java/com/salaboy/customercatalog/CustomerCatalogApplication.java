package com.salaboy.customercatalog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;

@SpringBootApplication
@RestController
public class CustomerCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerCatalogApplication.class, args);
	}

	@Value("${STATE_STORE_NAME:kvstore}")
	private String STATE_STORE_NAME;

	private String CUSTOMER_KEY = "customer-";

	@PostMapping
	public ResponseEntity<Customer> storeCustomer(@RequestBody Customer customer) {
		try (DaprClient client = new DaprClientBuilder().build()) {
			client.saveState(STATE_STORE_NAME, CUSTOMER_KEY + customer.id(), customer).block();
		} catch (Exception ex) {
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(customer);
	}

	@GetMapping
	public ResponseEntity<Customer> getCustomerById(@RequestParam String customerId) {
		try (DaprClient client = new DaprClientBuilder().build()) {
			State<Customer> customerState = client.getState(STATE_STORE_NAME, CUSTOMER_KEY + customerId, Customer.class).block();	
			if( customerState.getValue() == null){
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(customerState.getValue());
		} catch (Exception ex) {
			return ResponseEntity.internalServerError().build();
		}
	}

	public record Customer(String id, String name, String email) {}
}
