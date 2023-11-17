package com.trust.ashram.res;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trust")
public class TrustAdminController {

	private final static Logger LOG = LoggerFactory.getLogger(TrustAdminController.class);

	@GetMapping("/index")
	public String showIndexPage(Model model) {
		LOG.info("Showing index page");
		return "DisplayIndexPage";
	}
	
	
		
		
	}
