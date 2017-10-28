package com.example.services;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.example.resources.GemResource;

public interface IGemService {
	
	public List<GemResource> findAll() throws RestClientException, URISyntaxException;
	
	public GemResource save(GemResource gem) throws RestClientException, URISyntaxException;
	
	public GemResource update(GemResource gem) throws RestClientException, URISyntaxException;
	
	public ResponseEntity<?> delete(GemResource gem) throws RestClientException, URISyntaxException;

}
