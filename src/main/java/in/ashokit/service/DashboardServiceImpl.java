package in.ashokit.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import in.ashokit.dto.QuoteApiResponseDTO;

public class DashboardServiceImpl implements DashboardService {
	private String quoteApiURL="https://dummyjson.com/quotes/random";

	@Override
	public 	QuoteApiResponseDTO getQuote() {
		
		RestTemplate rt=new RestTemplate();
		ResponseEntity<QuoteApiResponseDTO> forEntity=rt.getForEntity(quoteApiURL, QuoteApiResponseDTO.class);
		
		QuoteApiResponseDTO body=forEntity.getBody();
		return body;
	}
 
}
