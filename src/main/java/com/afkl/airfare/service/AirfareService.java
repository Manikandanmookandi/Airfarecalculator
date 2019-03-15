package com.afkl.airfare.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

public interface AirfareService {

	List<String> retrieveAirport(); 
	CompletableFuture<ResponseEntity<String>> retrieveOrgin(String orgin);
	CompletableFuture<ResponseEntity<String>> retrieveDestination(String destination);
	CompletableFuture<ResponseEntity<String>> retrieveFare(String orgin, String destination);
	Map<String, Object> retrieveStats();

}
