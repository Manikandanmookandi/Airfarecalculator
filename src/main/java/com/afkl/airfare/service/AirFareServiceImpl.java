package com.afkl.airfare.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import com.afkl.airfare.authentication.ConfigureAuthentication;

@Configuration
@PropertySource("classpath:application.properties")
@Service
public class AirFareServiceImpl implements AirfareService {

	@Autowired
	private ConfigureAuthentication configAuth;
	private RestTemplate restTemplate;
	static int requestCount=0;
	static int statusCode =0;
	static int clientError =0;
	static int serverError =0;
	static List<Long> resTime = new ArrayList<Long>();
	
	@Value( "${api.url}" )
	private String apiUrl;
	
	@Override
	public List<String> retrieveAirport() {
		Instant startTime = Instant.now();
		requestCount++;
		restTemplate=configAuth.oAuthRestTemplate();
		ResponseEntity<List> response = restTemplate.getForEntity(apiUrl + "/airports", List.class);
		Instant endTime = Instant.now();
		
		long diff = Duration.between(startTime, endTime).toMillis();
		resTime.add(diff);
		if (response.getStatusCode().is2xxSuccessful()) {
			statusCode++;
				
		}else
			if (response.getStatusCode().is4xxClientError())
			{
				clientError++;
			} else
				if (response.getStatusCode().is5xxServerError()) {
					serverError++;
				}

		return (List<String>) response;
	}

	@Async("threadPoolTaskExecutor")
	public CompletableFuture<ResponseEntity<String>> retrieveOrgin(String orgin) {
		Instant startTime = Instant.now();
		requestCount++;
		restTemplate=configAuth.oAuthRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl+"/airports/{orgin}", String.class);
		Instant endTime = Instant.now();
		long diff = Duration.between(startTime, endTime).toMillis();
		
		resTime.add(diff);
		if (response.getStatusCode().is2xxSuccessful()) {
			statusCode++;
				
		}else
			if (response.getStatusCode().is4xxClientError())
			{
				clientError++;
			} else
				if (response.getStatusCode().is5xxServerError()) {
					serverError++;
				}	
		return CompletableFuture.completedFuture(response);
	}

	@Async("threadPoolTaskExecutor")
	public CompletableFuture<ResponseEntity<String>> retrieveDestination(String destination) {
		Instant startTime = Instant.now();
		restTemplate=configAuth.oAuthRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl+"/airports/{destination}", String.class);
		Instant endTime = Instant.now();
		long diff = Duration.between(startTime, endTime).toMillis();
		resTime.add(diff);
		if (response.getStatusCode().is2xxSuccessful()) {
			statusCode++;
				
		}else
			if (response.getStatusCode().is4xxClientError())
			{
				clientError++;
			} else
				if (response.getStatusCode().is5xxServerError()) {
					serverError++;
				}	
		return CompletableFuture.completedFuture(response);
	}

	public CompletableFuture<ResponseEntity<String>> retrieveFare(String orgin, String destination) {
		Instant startTime = Instant.now();
		restTemplate=configAuth.oAuthRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl+"/fares/{origin}/{destination}", String.class);
		Instant endTime = Instant.now();
		long diff = Duration.between(startTime, endTime).toMillis();
		resTime.add(diff);
		if (response.getStatusCode().is2xxSuccessful()) {
			statusCode++;
				
		}else
			if (response.getStatusCode().is4xxClientError())
			{
				clientError++;
			} else
				if (response.getStatusCode().is5xxServerError()) {
					serverError++;
				}	
		return CompletableFuture.completedFuture(response);
	}
	public Map<String, Object> retrieveStats(){
		LongSummaryStatistics stats = resTime.stream().mapToLong(Long::new).summaryStatistics();
		Map<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("Total_Request", requestCount);
		responseMap.put("Success", statusCode);
		responseMap.put("Client_Error", clientError);
		responseMap.put("Server_Error", serverError);
		responseMap.put("Avg_Response", stats.getAverage());
		responseMap.put("Min_Response", stats.getMin());
		responseMap.put("Max_Response", stats.getMax());
		return responseMap;	
	}

}
