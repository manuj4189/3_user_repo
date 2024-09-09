package in.ashokit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.ashokit.dto.LoginFormDTO;
import in.ashokit.dto.QuoteApiResponseDTO;
import in.ashokit.dto.RegisterFormDTO;
import in.ashokit.dto.ResetpwdFormDTO;
import in.ashokit.dto.UserDTO;
import in.ashokit.service.DashboardService;
import in.ashokit.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DashboardService dashboardService;
	
	
	@GetMapping("/register")
	public String loadRegisterpage(Model model) {
		
		Map<Integer,String> countriesmap=userService.getCountries();
		model.addAttribute("countries", countriesmap);
		
		RegisterFormDTO registerFormDTO=new RegisterFormDTO();
		model.addAttribute("registerForm", registerFormDTO);
		
		
		return "register";
	}
		
		
		
		@GetMapping("/states/{countryId}")
		@ResponseBody
		public Map<Integer,String> getStates(@PathVariable Integer countryId) {
			
			Map<Integer,String> statesMap=userService.getStates(countryId);
			//model.addAttribute("states", statesMap);
			return statesMap;
			
		}
		
		@GetMapping("/cities/{stateId}")
		@ResponseBody
		public Map<Integer,String> getCities(@PathVariable Integer stateId) {
			
			Map<Integer,String> citiesMap=userService.getCities(stateId);
			//model.addAttribute("cities", citiesMap);
			return citiesMap;
			
		}
			
	
		
		@PostMapping("/register")
		public String handleRegistration(RegisterFormDTO registerFormDTO,Model model) {
			
			boolean status =userService.duplicateEmailCheck(registerFormDTO.getEmail());
			if(status) {
				model.addAttribute("emsg","Duplicate Email Found");
			}else {
				boolean saveUser=userService.saveUser(registerFormDTO);
				if(saveUser) {
					//user saved
					
					model.addAttribute("smsg","Registration Succeful,pl check your email");
				}
				else {
					//failed to save
					model.addAttribute("emsg", "Registration is failed");
					
				}
			}
			model.addAttribute("countries",userService.getCountries());
			return "register";
			
		}
		
		@GetMapping("/")
		public String index(Model model) {
			LoginFormDTO loginFormDTO=new LoginFormDTO();
			model.addAttribute("loginForm",loginFormDTO);
			return "login";
		}
			
		@PostMapping("/login")
			public String handleUserLogin(LoginFormDTO loginFormDTO,Model model) {
			
			UserDTO userDTO=userService.login(loginFormDTO);
			if(userDTO==null) {
				model.addAttribute("emsg", "Invalid Credentials");
			}else {
				String pwdUpdated=userDTO.getPwdUpdated();
				if("Yes".equals(pwdUpdated)) {
					//display dashboard
					return "redirect:dashboard";
				}
			   else {
				//display rest password page
				   return "redirect:reset-pwd-page?email=" +userDTO.getEmail();
			   }
			}
				
				return "login";
			
			
		}	
			
			
		@GetMapping("/dashboard")
		public String dashboard(Model model) {
			QuoteApiResponseDTO quoteApiResponseDTO=dashboardService.getQuote();
			model.addAttribute("quote",quoteApiResponseDTO);
			return "dashboard";
			
		}
		
		@GetMapping("/reset-pwd-page")
		public String loadResetPwdPage(@RequestParam("email") String email,Model model) {
			
			ResetpwdFormDTO resetpwdFormDTO=new ResetpwdFormDTO();
			resetpwdFormDTO.setEmail(email);
			
			return "resetPwd";
		}
		
			
		@PostMapping("/resetPwd")
		public String handlePwdReset(ResetpwdFormDTO  resetpwdFormDTO,Model model) {
			
			boolean resetPwd=userService.resetPwd(resetpwdFormDTO);
			if(resetPwd) {
				return "redirect:dashboard";
			}
			return "resetPwd";
			
			
		}
			
		
}


		




