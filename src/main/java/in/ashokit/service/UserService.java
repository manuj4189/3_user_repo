package in.ashokit.service;

import java.util.Map;

import in.ashokit.dto.LoginFormDTO;
import in.ashokit.dto.RegisterFormDTO;
import in.ashokit.dto.ResetpwdFormDTO;
import in.ashokit.dto.UserDTO;

public interface UserService {
	
	public Map<Integer,String> getCountries();

	public Map<Integer,String> getStates(Integer countryId);

	public Map<Integer,String> getCities(Integer stateId);

	public boolean duplicateEmailCheck(String email);

	public boolean saveUser(RegisterFormDTO regFormDTO);

	public UserDTO login(LoginFormDTO loginFormDTO);

	public boolean resetPwd(ResetpwdFormDTO resetPwdDTO);



}
