package com.afkl.airfare.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.afkl.airfare.service.AirfareService;

@RestController
public class AirFareController {
	@Autowired
    private AirfareService airfareService;
	@RequestMapping(value = "/airports", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }) 
    public ResponseEntity<List<String>> listAllAirports() {

    	List<String> airports = airfareService.retrieveAirport();
    	
        return new ResponseEntity<List<String>>(airports, HttpStatus.OK);
    }
	@RequestMapping(value = "/airports/{orgin}/{destination}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<String>> retrieveFareDetails(@PathVariable("orgin") String origin,
			@PathVariable("destination") String destination) throws InterruptedException, ExecutionException {
		
    	CompletableFuture<ResponseEntity<String>> airOrgin = airfareService.retrieveOrgin(origin);
    	CompletableFuture<ResponseEntity<String>> airDestination = airfareService.retrieveDestination(destination);
    	
    	CompletableFuture<ResponseEntity<String>> airFareValue = airfareService.retrieveFare(destination, destination);
    	String orginValue = airOrgin.get().toString();
    	String destinationValue = airDestination.get().toString();
    	String fareValue = airFareValue.get().toString();
    	
    	List<String> fareDetails = new ArrayList<String>();
    	fareDetails.add(orginValue);
    	fareDetails.add(destinationValue);
    	fareDetails.add(fareValue);
        return new ResponseEntity<List<String>>(fareDetails, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }) 
    public ResponseEntity<Map<String, Object>> getStatistics() {
		
    	Map<String, Object> responseValue = airfareService.retrieveStats();
    	
        return new ResponseEntity<Map<String, Object>>(responseValue, HttpStatus.OK);
    }
}
