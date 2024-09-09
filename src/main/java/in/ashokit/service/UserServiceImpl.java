package in.ashokit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.dto.LoginFormDTO;
import in.ashokit.dto.RegisterFormDTO;
import in.ashokit.dto.ResetpwdFormDTO;
import in.ashokit.dto.UserDTO;
import in.ashokit.entities.CityEntity;
import in.ashokit.entities.CountryEntity;
import in.ashokit.entities.StateEntity;
import in.ashokit.entities.UserEntity;
import in.ashokit.repo.CityRepo;
import in.ashokit.repo.CountryRepo;
import in.ashokit.repo.StateRepo;
import in.ashokit.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private CountryRepo countryRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private CityRepo cityRepo;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailService emailService;
	@Override
	public Map<Integer, String> getCountries() {
		
		Map<Integer,String> countryMap=new HashMap<>();
		
		List<CountryEntity> countriesList=countryRepo.findAll();
		countriesList.stream().forEach(c->{
			countryMap.put(c.getCountryId(), c.getCountryName());
		});
		return countryMap ;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer,String> statesMap=new HashMap<>();
		
		List<StateEntity> statesList=stateRepo.findByCountryId(countryId);
		statesList.forEach(s->{
		statesMap.put(s.getStateId(), s.getStateName());
		
		});
		
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		
		Map<Integer,String> citiesMap=new HashMap<>();
		
		List<CityEntity> citiesList=cityRepo.findByStateId(stateId);
		citiesList.forEach(c->{
		citiesMap.put(c.getCityId(), c.getCityName());
		});
		return citiesMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
	UserEntity byEmail=userRepo.findByEmail(email);
	
	if(byEmail!=null) {
		return true;
	}else
		return false;
	}

	@Override
	public boolean saveUser(RegisterFormDTO regFormDTO) {
		UserEntity userEntity=new UserEntity();
		BeanUtils.copyProperties(regFormDTO, userEntity);
		
		CountryEntity country=countryRepo.findById(regFormDTO.getCountryId()).orElse(null);
		userEntity.setCountry(country);
		
		StateEntity state=stateRepo.findById(regFormDTO.getStateId()).orElse(null);
		userEntity.setState(state);
		
		CityEntity city=cityRepo.findById(regFormDTO.getCityId()).orElse(null);
		userEntity.setCity(city);
		String randomPwd=generateRamdomPwd() ;
		
		userEntity.setPwd(randomPwd);
		userEntity.setPwdUpdated("No");
		
		UserEntity savedUser=userRepo.save(userEntity);
		if(null!=savedUser.getUserId()) {
			String subject="your account created";
			String body="your password To  login:" +randomPwd;
			String to=regFormDTO.getEmail();
			emailService.sendEmail(subject, body, to);
			return true;
		}
		
		return false;
	}
	private String generateRamdomPwd() {
		
		
      String upperCaseLetters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String lowerCaseLetters="abcdefghijklmnopqrstuvwxyz";
      
      String alphabets= upperCaseLetters+lowerCaseLetters;
      
      Random random=new Random();
      
      StringBuffer generatedPwd=new StringBuffer();
      
      for(int i=0;i<5;i++) {
    	  int randomIndex=random.nextInt(alphabets.length());
    	  
    	  generatedPwd.append(alphabets.charAt(randomIndex));
      }
      
		return generatedPwd.toString();
		
	}

	@Override
	public UserDTO login(LoginFormDTO loginFormDTO) {
	     UserEntity userEntity=userRepo.findByEmailAndPwd(loginFormDTO.getEmail(), loginFormDTO.getPwd());
	     
	     if(userEntity!=null) {
	    	 UserDTO userDTO=new UserDTO();
	    	 BeanUtils.copyProperties(userEntity, userDTO);
	    	 return userDTO;
	     }
		return null;
	}

	@Override
	public boolean resetPwd(ResetpwdFormDTO resetPwdDTO) {
		String email=resetPwdDTO.getEmail();
		    
		UserEntity entity=userRepo.findByEmail(email);
		
		entity.setPwd(resetPwdDTO.getNewpwd());
		entity.setPwdUpdated("yes");
		userRepo.save(entity);//upsert
		
		return true;
		
	}

}
