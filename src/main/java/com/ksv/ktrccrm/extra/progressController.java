package com.ksv.ktrccrm.extra;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class progressController {

	
	@GetMapping("/progressBar")
	public String testPregressBar() {
		System.out.println("Inside Progress Bar Method");
		return "dashboard/progressbar";

	}

	@GetMapping("/popupBox")
	public String testPopUpBox() {
		System.out.println("Inside Progress Bar Method");
		return "dashboard/popupBox";

	}
	
	
	@GetMapping("/location")
	public String getLocation() {
		System.out.println("Inside method location");
		return "checkincheckout/newLocation";

	}
	
}
