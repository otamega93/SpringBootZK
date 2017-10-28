package com.example.services.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.resources.GemResource;
import com.example.services.IGemService;
import com.google.common.collect.Lists;

@Service
public class GemService implements IGemService {
										
	private static final String URL_GEMS = "http://localhost:7890/api/v1/gems";
	
	private static final ParameterizedTypeReference<PagedResources<GemResource>> responseType = new ParameterizedTypeReference<PagedResources<GemResource>>() {};
	
	RestTemplate restTemplateClassic = new RestTemplate();
	
	@Autowired
	RestTemplate restTemplateHalJsonResources;
	
	
	
	@Override
	public List<GemResource> findAll() throws RestClientException, URISyntaxException {

		ResponseEntity<PagedResources<GemResource>> result = restTemplateHalJsonResources.exchange(URL_GEMS, HttpMethod.GET, null/*httpEntity*/, responseType);
		
		List<GemResource> searchResult = Lists.newArrayList(result.getBody().getContent());
		
//		for (GemResource gemResource : searchResult) {
//			System.out.println(gemResource.getName());
//		}

		return searchResult;
		
	}

	@Override
	public GemResource save(GemResource gem) throws RestClientException, URISyntaxException {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GemResource> request = new HttpEntity<>(gem, headers);
		
		GemResource gemResource = restTemplateClassic.exchange(URL_GEMS, HttpMethod.POST, request, GemResource.class).getBody();
		
		return gemResource;
	}

	@Override
	public GemResource update(GemResource gem) throws RestClientException, URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GemResource> request = new HttpEntity<>(gem, headers);
		
		GemResource gemResource = restTemplateClassic.exchange(URL_GEMS + "/" + gem.getIds(), HttpMethod.PUT, request, GemResource.class).getBody();
		
		return gemResource;
	}

	@Override
	public ResponseEntity<?> delete(GemResource gem) throws RestClientException, URISyntaxException {
		
		ResponseEntity<?> responseEntity = restTemplateClassic.exchange(URL_GEMS + "/" + gem.getIds(), HttpMethod.DELETE, null, ResponseEntity.class);
		
		return responseEntity;
	}

}
