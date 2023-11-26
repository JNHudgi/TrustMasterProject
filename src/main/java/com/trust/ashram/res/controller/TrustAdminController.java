package com.trust.ashram.res.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.trust.ashram.res.model.AdminDetails;
import com.trust.ashram.res.model.DonationDetails;
import com.trust.ashram.res.model.EventDetails;
import com.trust.ashram.res.model.GuruMalaDetails;
import com.trust.ashram.res.model.LingaDetails;
import com.trust.ashram.res.model.NightStayDetails;
import com.trust.ashram.res.model.PictureDetails;
import com.trust.ashram.res.model.VisitorDetails;
import com.trust.ashram.res.model.VolunteerDetails;
import com.trust.ashram.res.model.YogaDetails;
import com.trust.ashram.res.service.ITrustAdminService;
import com.trust.util.ImageUtil;

@Controller
@RequestMapping("/trust")
public class TrustAdminController {

	private final static Logger LOG = LoggerFactory.getLogger(TrustAdminController.class);

	Integer visitorId = null;
	Integer yogaId = null;
	Integer eventId=null;
	Integer nightStayId=null;
	Integer LingabhishekaId=null;
	
	String fp=null;
	AdminDetails opt = null;
	
	@Autowired
	private ITrustAdminService service;

	@GetMapping("/index")
	public String showIndexPage(Model model) {
		LOG.info("Showing index page");
		return "DisplayIndexPage";
	}
	@GetMapping("/loginPage")
	public String showLoginPage(Model model) {
		LOG.info("Showing Login page");
		LOG.info(System.getProperty("user.home"));
		return "DisplayEmployeeLoginPage";
	}
	@PostMapping("/trustlogin")
	public String VerifiyLoginCredn(@RequestParam String username,@RequestParam String password, Model model,Principal p) {
		LOG.info("Controller : Started Verifing the Login Info");
		String loginid =null;
		String loginpassword =null;
		String status =null;
		try {
			
			Optional<AdminDetails> oadmd = null;
			oadmd = service.getAdminDetailsByEmailID(username);
			if (oadmd != null) 
				opt = oadmd.get();
			LOG.info("Controller : returned from services");
			if (opt != null) {
				loginid= opt.getAemail().toString().trim();
				loginpassword = opt.getApassword().toString().trim();
				
				if(username!=null && !username.equalsIgnoreCase("") && loginid!=null && !loginid.equalsIgnoreCase("") 
				&& password!= null && !password.equalsIgnoreCase("") && loginpassword!= null && !loginpassword.equalsIgnoreCase("")) {
					if(loginid.equalsIgnoreCase(username) && loginpassword.equalsIgnoreCase(password)) {
						LOG.info("Controller : Login Verification successfully : Calling Employee Info");
						status = "EmployeeWelcomePage";
					} else {
						LOG.info("Controller : Login Verification failed : Username & password is invalied");
						status ="DisplayEmployeeLoginPage";
						model.addAttribute("msg", "USERNAME / PASSWORD INVALID");
					}
				} else {
					LOG.info("Controller : Login Verification failed : Username or password BLNAK OR NULL");
					model.addAttribute("msg", "USERNAME / PASSWORD INVALID");
					status ="DisplayEmployeeLoginPage";
				}
				
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			} else {
				LOG.info("Controller : Login Verification failed : adminDetails recvied empty Data from DB ");
				model.addAttribute("msg", "USERNAME / PASSWORD INVALID");
				status ="DisplayEmployeeLoginPage";
			} 
		} catch (Exception e) {
			status ="DisplayEmployeeLoginPage";
			model.addAttribute("msg", "USERNAME / PASSWORD INVALID");
			LOG.info("Controller : Login Verification failed found issue in execption : "+e.getMessage().toString());
			e.printStackTrace();
		}
		return status;
	}
	
	@GetMapping("/ashramwelcomepage")
	public String getWelcomePage(Principal p, Model model) {
		LOG.info("Showing Employee Welcome page");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return "DisplayAshramWelcomePage";
	}

	@GetMapping("/employeewelcomepage")
	public String employeeWelcomePage(Principal p, Model model) {
		LOG.info("Showing Employee Welcome page");
		//model = getLoggerInfo(p,model);
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		
		return "EmployeeWelcomePage";
		
	}

	
	//-------------------------- Started Visitor Information ------------------
	
	
	@GetMapping("/onevisitordetailspage")
	public String OneVisitorDetails(Model model, Principal p) {
		LOG.info("Display one visitor details Form ");

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		return "SingleVisitorSearchForm";
	}

	@GetMapping("/employeeloginpage")
	public String employeeLoginPage() {
		LOG.info("Showing Employee login page");
		return "DisplayEmployeeLoginPage";
	}

	@GetMapping("/accessdeniedpage")
	public String AccessDeniedPage() {
		LOG.info("Showing Access denied page");
		return "AccessDeniedPage";
	}

	@PostMapping("/visitorSearchWithDate") // 2022-08-20	->	2022-08-20
	public String visitorSearchWithDate(@RequestParam String visitorsearchwitdate, Model model, Principal p) {
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		List<Object[]> vd = null;
		vd = service.visitorListByDate(visitorsearchwitdate);

		if (vd.size() >= 1) {
			LOG.info("retrived the visitor Search With Date deatils Done" + vd.size());
			model.addAttribute("vdlist", vd);
			return "VisitorListInformation";
		} else {
			LOG.info("failed to retrive visitor Search With Date info");
			model.addAttribute("usernotfound",
					visitorsearchwitdate + " :  Visitor profile not found.. Please try again or Please Contact Adminstartor ");
			return "VisitorListInformation";
		}

	}
	
	@PostMapping("/visitorSearchWithOccuption")
	public String visitorSearchWithOccuption(@RequestParam String visitorsearchwitOccupation, Model model, Principal p) {

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		List<Object[]> vd = null;
		vd = service.visitorListByOccpation(visitorsearchwitOccupation);

		if (vd.size() >= 1) {
			LOG.info("retrived the visitor search wit Occupation deatils Done" + vd.size());
			model.addAttribute("vdlist", vd);
			return "VisitorListInformation";
		} else {
			LOG.info("failed to retrive visitor search wit Occupation info");
			model.addAttribute("usernotfound",
					visitorsearchwitOccupation + " :  Visitor profile not found.. Please try again or Please Contact Adminstartor ");
			return "VisitorListInformation";
		}

	}
	// ----------------------------------- testing
	// ----------------------------------------

/*	@RequestMapping("/student/{pageNo}/{pageSize}")
	@ResponseBody
	public List<VisitorDetails> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize) {
		return service.findPaginated(pageNo, pageSize);
	}

	@RequestMapping("/home")
	public String homeFun(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Long l = service.getVisitorDetaislCount();
		System.out.println(l);
		session.setAttribute("totalStudent", l);// ;repo.count());
		return "VisitorInfo";
	}

	@GetMapping("/checkEmail")
	public @ResponseBody String validateEmail(@RequestParam String email) {
		String message = "";
		if (service.isEmployeeExistByEmail(email)) {
			message = email + " Already exist";
		}
		return message;
	}
*/
	// --this visitor edit form

	@GetMapping("/editvisitor")
	public String getVisitorEditPage(@RequestParam Integer vid, Model model, Principal p) {
		LOG.info("Get request Edit page  for visitor from controller");

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		Optional<VisitorDetails> opt = null;
		opt = service.getViewByVisitorID(vid);
		if (opt != null) {
			VisitorDetails evd = null;
			evd = opt.get();
			model.addAttribute("edit", evd);

		} else {
			model.addAttribute("noeditpage", "Issue with Edit visitors, Please Contact Adminstartor ");
		}
		LOG.info("Get rsponse Edit page for visitor from controller");

		return "VisitorEditFrom";
	}
	
	// view single visitors details
	@GetMapping("/viewvisitor")
	public String getViewVisitortPage(@RequestParam Integer vid, Model model, Principal p) {
		LOG.info("Get request view page  for visitor from controller");
		visitorId = vid;

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		Optional<VisitorDetails> opt = null;
		opt = service.getViewByVisitorID(vid);
		if (opt != null) {
			VisitorDetails evd = null;
			evd = opt.get();
			model.addAttribute("edit", evd);
			model.addAttribute("imgUtil", new ImageUtil());
		} else {
			model.addAttribute("noeditpage", "Issue with view visiotor, Please Contact Adminstartor ");
		}
		LOG.info("Get rsponse view page for Visitor from controller");
		return "SingleVisitorsDetails";
	}

	@GetMapping("/getimagefilename")
	public @ResponseBody VisitorDetails visitorImage() {
		LOG.info("Ajax calling with id :" + visitorId);

		Optional<VisitorDetails> opt = null;
		VisitorDetails visitorViewDetails = null;
		opt = service.getViewByVisitorID(visitorId);
		if (opt != null) {
			visitorViewDetails = opt.get();
			LOG.info("Visitor Data from DB: "+visitorViewDetails.getVfname());
		}
		// LOG.info(visitorViewDetails.toString());
		LOG.info("Ajax calling done");
		return visitorViewDetails;

	}

	// -------------Validation ---------------------
	// Email validation
	@GetMapping("/checkUserName")
	public @ResponseBody String checkLoginUserName(@RequestParam String aemail) {

		String message = "";
		message = service.getValidateUsedLoginIdAsEmail(aemail);

		return message;
	}

	// Pan number validation
	@GetMapping("/validatePanNumber")
	public @ResponseBody String validatePanNumber(@RequestParam String vpannumber) {

		String message = "";
		if (vpannumber.length() == 10)
			message = service.getValidatePanNumber(vpannumber);
		else
			message = "Pan Card Number Is Incorrect.";
		return message;
	}

	// Aadhar number validation
	@GetMapping("/validateAadharNumber")
	public @ResponseBody String validateAadharNumber(@RequestParam String vaadher) {

		LOG.info("Ajax calling...");
		String message = "";
		if (vaadher.length() == 12)
			message = service.getValidateAadharNumber(vaadher);
		else
			message = "Aadhar Card Number Is Incorrect.";
		return message;
	}

	// Phone number validation
	@GetMapping("/validatePhoneNumber")
	public @ResponseBody String validatePhoneNumber(@RequestParam String vphonenumber) {

		String message = "";
		if (vphonenumber.length() == 10)
			message = service.getValidatePhoneNumber(vphonenumber);
		else
			message = "Phone Card Number Is Incorrect.";
		return message;
	}

	// pin code number validation
	@GetMapping("/validatePinCodeNumber")
	public @ResponseBody String validatePinCodeNumber(@RequestParam String vpincode) {

		String message = "";
		if (vpincode.length() == 6)
			message = "";
		else
			message = "Pin Code Number Is Incorrect.";
		return message;
	}

	// Email id validation
	@GetMapping("/getemailcount")
	public @ResponseBody String getAdminEmailIDCount(@RequestParam String aemail) {
		String message = "";
		boolean b = service.getAdminEmailCount(aemail);

		if (b == false)
			message = "Incorrect Email Id.";
		return message;
	}

	// ----------------------------------------------------------------------
	// Password change function
	@GetMapping("/passwordchangeform")
	public String passwordChangeForm() {
		LOG.info("Display Pasword change form");
		return "DisplayEmployeePasswordForm";
	}

	@PostMapping("/passwordchangeformope")
	public String passwordChangeFunction(@ModelAttribute AdminDetails empl, Model model) {
		LOG.info("Started changing password from controller");

		AdminDetails opt = null;
		Optional<AdminDetails> oadm =null;
		oadm = service.getAdminDetailsByEmailID(empl.getAemail());
		if(oadm!=null)
			opt=oadm.get();
		
		if (opt != null) {
			opt.setApassword(empl.getApassword());
			service.registerAdminInformation(opt);
			model.addAttribute("msg", opt.getAname() + " Your Password Has Been Changed.");
		} else {
			model.addAttribute("msg", " Issue With Changing Your Password, Please try Again..");
		}
		LOG.info("Controller done with chnaging password ");

		return "DisplayEmployeePasswordForm";
	}

	
	@GetMapping("/employeeprofileupdate")
	public String employeeprofileedit(@RequestParam Integer aid, Model model, Principal p) {

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		AdminDetails profile = null;
		profile = service.getEmployeeByAID(aid);

		if (profile != null) {
			LOG.info("One employee profile have got retrived Done");
			model.addAttribute("empinfo", profile);
		} else {
			LOG.info("failed to retrive single the employee info");
			model.addAttribute("msg", "Filed to Retrive single The employee Info.. Please try again ");
		}

		return "AdminFunctionOprnPage";
	}

	@PostMapping("/employeeupdateoperation")
	public String getemployeeUpdate(@ModelAttribute AdminDetails detailsfromUi, Model model, Principal p) {

		LOG.info("udateing Employee deatils to DB");

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		
		Optional<AdminDetails> opt = null;
		AdminDetails detailsfromDb = null;

		opt = service.getViewByEmployeeEmail(detailsfromUi.getAemail());
		if (opt != null) {
			detailsfromDb = opt.get();

			SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy, hh:mm:ss aa, z");// new
																								// SimpleDateFormat("E,
																								// dd MMM yyyy HH:mm:ss
																								// z");
			detailsfromUi.setAvisitieddatetime(formatter.format(new Date()));

			if (detailsfromUi.getAid() == null)
				detailsfromUi.setAid(detailsfromDb.getAid());
			if (detailsfromUi.getAname() == null)
				detailsfromUi.setAname(detailsfromDb.getAname());
			if (detailsfromUi.getApassword() == null)
				detailsfromUi.setApassword(detailsfromDb.getApassword());
			if (detailsfromUi.getAgender() == null)
				detailsfromUi.setAgender(detailsfromDb.getAgender());
			if (detailsfromUi.getArole() == null)
				detailsfromUi.setArole(detailsfromDb.getArole());
			if (detailsfromUi.getAemail() == null)
				detailsfromUi.setAemail(detailsfromDb.getAemail());
			if (detailsfromUi.getAphone() == null)
				detailsfromUi.setAphone(detailsfromDb.getAphone());
			if (detailsfromUi.getAaddress() == null)
				detailsfromUi.setAaddress(detailsfromDb.getAaddress());

			AdminDetails ad = service.registerAdminInformation(detailsfromUi);
			if (ad != null) {
				model.addAttribute("msg",
						"'" + ad.getAname().toUpperCase() + " Profile Has Been Updated Successfuly");
			} else {
				model.addAttribute("msg", "Updation Filed.. Please try again ");
			}
		}

		List<AdminDetails> emplinfo = null;
		emplinfo = service.getAllEmployeeList();
		if (emplinfo != null) {
			LOG.info("retrived the all employee deatils Done");
			emplinfo.stream().forEach(e -> {
				e.setApassword("*******");
			});
			model.addAttribute("empinfolist", emplinfo);
		} else {
			LOG.info("failed to retrive all the employee info");
			model.addAttribute("removemsg", "Filed to Retrive all The employee Info.. Please try again ");
		}

		return "AdminEmployeeInfoList";
	}

	@GetMapping("/visitorreportgen")
	public String visitorReportGene(Model model, Principal p) {

		LOG.info("Showing Admin access page");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		List<AdminDetails> emplinfo = null;
		emplinfo = service.getAllEmployeeList();
		if (emplinfo != null) {
			LOG.info("retrived the all employee deatils Done");
			emplinfo.stream().forEach(s -> {
				s.setApassword("**********");
			});

			model.addAttribute("empinfolist", emplinfo);
			model.addAttribute("removemsg", "Report Registeration is Under Process will update soon ...");
		} else {
			LOG.info("failed to retrive all the employee info");
			model.addAttribute("removemsg", "Report Registeration is Under Process will update soon ...");
		}

		return "AdminFunctionPage";
	}

	

	/*@GetMapping("/listofyogapeopleinfo") //not in use
	public @ResponseBody List<YogaDetails> getYogaList() {
		LOG.info("Ajax calling with All yoga people");

		LOG.info("Retriving all yoga deatils from  DB");
		List<Object[]> yd = null; yd = service.allYogaInformation();
		if (yd != null) {
			LOG.info("retrived the all yoga deatils Done");
		} else {
			LOG.info("failed to retrive all the yoga info");
		}
		LOG.info("Ajax calling done");
		return null;//return yd; //not in use

	} */

	@GetMapping("/yogaparticipentprofilelink")
	public String getParticipentProfilelinking(@RequestParam Integer yid, Model model) {

		LOG.info("Calling to link yoga id" + yid);
		yogaId = yid;
		LOG.info("Sucess to link yoga id" + yid);

		return "YogaParticipantentInfo";
	}

	@GetMapping("/yogaparticipentprofilelinkInfo")
	@ResponseBody
	public YogaDetails getParticipentProfile() {

		LOG.info("Calling to yoga get participent info" + yogaId);

		Optional<YogaDetails> opt = null;
		YogaDetails yogaViewDetails = null;
		opt = service.getViewByYogaID(yogaId);
		if (opt != null) {
			yogaViewDetails = opt.get();
			
			/*Map<String,PictureDetails> mapPicture=null;
			LOG.info("Calling to get all Picture info");
			mapPicture =service.getAllPictures();
			if(mapPicture!=null) {
				PictureDetails appaji8Picture=mapPicture.get("appaji8");
				PictureDetails logoPicture=mapPicture.get("logo");
				LOG.info("Calling to Assigne logo and Picture to yoga form");
				yogaViewDetails.setYimagedataappaji(appaji8Picture.getPimagedata());
				yogaViewDetails.setYimagedatalogo(logoPicture.getPimagedata());
			} */
			/*
			 * String imgName = yogaViewDetails.getYimagedata().toString();
			 * LOG.info(imgName);
			 */
		}
		LOG.info("calling done and posting html");
		return yogaViewDetails;
	}
	
	@GetMapping("/searchyoga")
	public String searchYogaform() {
		LOG.info("Showing yoga page");
		return "YogaSearchForm";
	}
	
	@PostMapping("/searchyogaids")
	public String getOneOrAllYogaIdsSearch(@RequestParam String yogaoption, Model model, Principal p) {

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		List<Object[]>  vd = null;
		vd = service.getOneOrAllYogaInformation(yogaoption);

		if (vd.size() >= 1) {
			LOG.info("retrived the Yoga deatils Done" + vd.size());
			model.addAttribute("vdlist", vd);
			return "AllYogaInformationList";
		} else {
			LOG.info("failed to retrive all the yoga info");
			model.addAttribute("removemsg",
					yogaoption + " :  Yoga profile not found.. Please try again or Please Contact Adminstartor ");
			return "AllYogaInformationList";
		}
	
	}
	

/*	@GetMapping("/listofyogapeople")
	public String viewYogaPage(Model model) {
		LOG.info("Calling Paginationa dna sorting to get all the yoga detials");
		return viewAllYogaPage(1, "yidcard", "asc", model);		
	}
	
	@GetMapping("/page/{pageNo}")
	public String viewAllYogaPage(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		LOG.info("Started Retireving the all Yoga deatils with Paginationa dna sorting");
		int pageSize = 10;
		Page<YogaDetails> page = service.viewAllYogaPage(pageNo, pageSize, sortField, sortDir);
		List<YogaDetails> listEmployees = page.getContent();
		if(listEmployees!=null && listEmployees.size()>0) {
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("imgUtil", new ImageUtil());
			model.addAttribute("listEmployees", listEmployees);
			model.addAttribute("yogapbatch", null);
			model.addAttribute("yogapoccupation", null);
			model.addAttribute("yogapname", null);
			model.addAttribute("yogapidcard", null);
		} else {
			model.addAttribute("removemsg"," Yoga profile not found.. Please try again or Please Contact Adminstartor ");
		}
		LOG.info("Ended Retireving the all Yoga deatils with Paginationa dna sorting, Posting to html");
		return "YogaList";
	}
*/
	
		
	@PostMapping("/yogaSearchByBatch")
	public String getYogaSearchByBatch(@RequestParam String yogasearchbybatch, Model model, Principal p) {
		LOG.info("Calling Paginationa and sorting to get all the Yoga Search By Batch deatils");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return viewYogaSearchByBatch(1, "yidcard", "asc",yogasearchbybatch, model);		
	}
	
	@GetMapping("/yogapagibatch/{pageNo}")
	public String viewYogaSearchByBatch(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("batch") String batch,
			Model model) {
		LOG.info("Started Retireving the all Yoga view Yoga Search By batch with Paginationa and sorting");
		int pageSize = 10;
		Page<YogaDetails> page = service.getYogaSearchByBatchWitPagination(pageNo, pageSize, sortField, sortDir, batch);
		List<YogaDetails> listEmployees = page.getContent();
		if(listEmployees!=null && listEmployees.size()>0) {
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("imgUtil", new ImageUtil());
			model.addAttribute("yogasearchbybatch",batch);
			model.addAttribute("yogapbatch", listEmployees);
			model.addAttribute("listEmployees",null);
			model.addAttribute("yogapoccupation", null);
			model.addAttribute("yogapname", null);
			model.addAttribute("yogapidcard", null);
			LOG.info("Ended Retireving the all Yoga Search By batch with Paginationa and sorting, and finlly Posting to html");
		} else {
			model.addAttribute("yogasearchbybatch",batch);
			model.addAttribute("removemsg"," Yoga profile not found.. Please try again or Please Contact Adminstartor ");
		}
		return "YogaList";
	}
	
	@PostMapping("/yogaSearchByOccuption")
	public String getYogaSearchByOccuption(@RequestParam String yogasearchbyoccupation, Model model, Principal p) {
		//List<Object[]>  vd = null;	vd = service.getYogaSearchByOccuption(yogasearchbyoccupation);
		LOG.info("Calling Paginationa and sorting to get all the Yoga Search By occupation deatils");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return viewYogaSearchByOccupation(1,"yidcard", "asc",yogasearchbyoccupation, model);		
	}
	
	@GetMapping("/yogapoccupation/{pageNo}")
	public String viewYogaSearchByOccupation(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("occupation") String occupation,
			Model model) {
		LOG.info("Started Retireving the all Yoga view Yoga Search By occupation with Paginationa and sorting");
		int pageSize = 10;
		Page<YogaDetails> page = service.getYogaSearchByOccupationWitPagination(pageNo, pageSize, sortField, sortDir, occupation);
		List<YogaDetails> listEmployees = page.getContent();
		if(listEmployees!=null && listEmployees.size()>0) {
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("imgUtil", new ImageUtil());
			model.addAttribute("yogasearchbyoccupation", occupation);
			model.addAttribute("yogapoccupation", listEmployees);
			model.addAttribute("listEmployees", null);
			model.addAttribute("yogapbatch", null);
			model.addAttribute("yogapname", null);
			model.addAttribute("yogapidcard", null);
		} else {
			model.addAttribute("yogasearchbyoccupation", occupation);
			model.addAttribute("removemsg"," Yoga profile not found.. Please try again or Please Contact Adminstartor ");
		}
		
		LOG.info("Ended Retireving the all Yoga Search By occupation with Paginationa and sorting, and finlly Posting to html");
		return "YogaList";
	}
	
	@PostMapping("/yogaSearchByName")
	public String getYogaSearchByName(@RequestParam String yogasearchbyname, Model model, Principal p) {
		//List<Object[]>  vd = null;	vd = service.getYogaSearchByName(yogasearchbyname);
		LOG.info("Calling Paginationa and sorting to get all the Yoga Search By name deatils");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return viewYogaSearchByName(1,"yidcard", "asc",yogasearchbyname, model);	
	}
	
	@GetMapping("/yogapname/{pageNo}")
	public String viewYogaSearchByName(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("name") String name,
			Model model) {
		LOG.info("Started Retireving the all Yoga view Yoga Search By name with Paginationa and sorting");
		int pageSize = 10;
		Page<YogaDetails> page = service.getYogaSearchByNameWitPagination(pageNo, pageSize, sortField, sortDir, name);
		List<YogaDetails> listEmployees = page.getContent();
		if(listEmployees!=null && listEmployees.size()>0) {
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("imgUtil", new ImageUtil());
			model.addAttribute("yogasearchbyname", name);
			model.addAttribute("yogapname", listEmployees);
			model.addAttribute("listEmployees", null);
			model.addAttribute("yogapbatch", null);
			model.addAttribute("yogapoccupation", null);
			model.addAttribute("yogapidcard", null);
		} else {
			model.addAttribute("yogasearchbyname", name);
			model.addAttribute("removemsg"," Yoga profile not found.. Please try again or Please Contact Adminstartor ");
		}
		
		LOG.info("Ended Retireving the all Yoga Search By name with Paginationa and sorting, and finlly Posting to html");
		return "YogaList";
	}
	
	@PostMapping("/yogaSearchByIdcard")
	public String getYogaSearchByIdcard(@RequestParam String yogasearchbyidcard, Model model, Principal p) {
		//List<Object[]>  vd = null;	vd = service.getYogaSearchByName(yogasearchbyname);
		LOG.info("Calling Paginationa and sorting to get all the Yoga Search By id card deatils");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return viewYogaSearchByIdcard(1,"yidcard", "asc",yogasearchbyidcard, model);		
	}
	
	@GetMapping("/yogapidcard/{pageNo}")
	public String viewYogaSearchByIdcard(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("idcard") String idcard,
			Model model) {
		LOG.info("Started Retireving the all Yoga view Yoga Search By idcard with Paginationa and sorting");
		int pageSize = 10;
		Page<YogaDetails> page = service.getYogaSearchByIdcardWitPagination(pageNo, pageSize, sortField, sortDir, idcard);
		List<YogaDetails> listEmployees = page.getContent();
		if(listEmployees!=null && listEmployees.size()>0) {
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			model.addAttribute("imgUtil", new ImageUtil());
			model.addAttribute("yogasearchbyidcard", idcard);
			model.addAttribute("yogapidcard", listEmployees);
			model.addAttribute("listEmployees", null);
			model.addAttribute("yogapbatch", null);
			model.addAttribute("yogapoccupation", null);
			model.addAttribute("yogapname", null);
		} else {
			model.addAttribute("yogasearchbyidcard", idcard);
			model.addAttribute("removemsg"," Yoga profile not found.. Please try again or Please Contact Adminstartor ");
		}
		
		LOG.info("Ended Retireving the all Yoga Search By idcard with Paginationa and sorting, and finlly Posting to html");
		return "YogaList";
	}
	
	@GetMapping("/viewyoga")
	public String getYogaViewPage(@RequestParam Integer yid, Model model, Principal p) {
		LOG.info("Get request view page  for Yoga from controller");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		Optional<YogaDetails> opt = null;
		opt = service.getViewByYogaID(yid);
		if (opt != null) {
			YogaDetails evd = null;
			evd = opt.get();
			model.addAttribute("edit", evd);
			model.addAttribute("imgUtil", new ImageUtil());
		} else {
			model.addAttribute("noeditpage", "Issue with view yoga, Please Contact Adminstartor ");
		}
		LOG.info("Get rsponse view page for yoga from controller");
		return "YogaViewProfile";
	}
	
	
	
	@GetMapping("/checkYogaIdCardCount")
	public @ResponseBody String checkYogaIdCardCount(@RequestParam String yidcard, @RequestParam String ybatch)
	{
		LOG.info("Ajax calling with Yoga id : "+yidcard+" , and Batch :" + ybatch);
		String message = null;
		message = service.getCheckYogaIdCardCount(yidcard,ybatch);
		LOG.info("Ended Ajax calling POsting back to resptiov html");
		if(message!=null)
			return message;
		else
			return null;
	}

	
	// ---------------Finished Yoga details --------
	// ---------------Started storing appaji pictures --------
	
	@GetMapping("/picture")
	public String showPicturePage(Model model) {
		LOG.info("Showing picture page");
		return "DisplayPictureRegistration";
	}
	
	@PostMapping("/pictureregistrationdetails")
	public String pictureRegistringDetails(@RequestParam("imageFile") MultipartFile imageFile,
			@ModelAttribute PictureDetails picture, Model model, Principal p) {

		LOG.info("registering Picture deatils to DB");

		try {

			byte[] data = null;
			data = imageFile.getBytes();
			if(data.length>1) {
				/* LOG.info("Image data : " + data.toString()); */
				picture.setPimagedata(data);
			} else {
				LOG.info("Image data Not found in : ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PictureDetails pd = null;
		pd = service.registerPictureInformation(picture);

		if (pd != null) {
			LOG.info("registering picture deatils Done");
			model.addAttribute("msg", "Picture has been Stored Successfully.");
		} else {
			LOG.info("failed to register");
			model.addAttribute("msg", "Registration Filed.. Please try again ");
		}

		return "DisplayPictureRegistration";
	}
	
	// ---------------End storing appaji pictures --------
	
	//-----------Get login employee name:
	public Model getLoggerInfo(Principal p, Model model2) {
		String emailAsUserid=null;
		try {
			emailAsUserid=p.getName();
		} catch (NullPointerException e) {
			LOG.info("Its null Pointer execption, because of Employee haven't logged in");
		}
		if(emailAsUserid==null) {
			emailAsUserid="No-Login";
		}  else {
			AdminDetails ad = null;
			Optional<AdminDetails> oadm =null;
			oadm = service.getAdminDetailsByEmailID(emailAsUserid);
			if(oadm!=null)
				ad=oadm.get();
			if (ad != null) {
				emailAsUserid= ad.getAname();
				model2.addAttribute("ad",ad);
				LOG.info("This trust login by : "+ad.getAname());
			}
		}
		return model2;
	}


	
	public String getLoginEmpName(Principal name) {
		String emailAsUserid = null;
		try {
			emailAsUserid=name.getName();
		} catch (NullPointerException e) {
			LOG.info("Its null Pointer execption, because of Employee haven't logged in");
		}
		if(emailAsUserid==null) {
			emailAsUserid="No_Name";
		}  else {
			AdminDetails ad = null;
			Optional<AdminDetails> oadm =null;
			
			oadm = service.getAdminDetailsByEmailID(emailAsUserid);
			if(oadm!=null)
				ad=oadm.get();
			
			if (ad != null) {
				emailAsUserid= ad.getAname();
			}
		}
		
		return emailAsUserid;
	}
	//-----------End login employee name:
	
	//------------ started Night Stay Things--------------
	
	@GetMapping("/nightstay")
	public String openNightStayWelcomePage(Model model) {
		LOG.info("Controller : Calling to Open Night Stay Welcome page");
		return "NightStayWelcomePage";
	}
	
	@GetMapping("/nightstayregister")
	public String openNightStayRegisterPage(Model model) {
		LOG.info("Controller : Calling to Open Night Stay Page");
		return "NightStayRegisterPage";
	}
	
	@PostMapping("/nightstayregistrationdetails")
	public String opreation(@RequestParam("familyImage") MultipartFile familyImage, @ModelAttribute NightStayDetails ns, Model model, Principal p) {
		LOG.info("Controller : Started Night Stay registration process");
		try {
			LOG.info("Controller : Started to setting the family and aadhar image to object");
			byte[] familydata = null;
			
			//New Logic to resize the image
			byte[] bytes1 = null ;
			bytes1 =(familyImage!=null ? familyImage.getBytes() : null);
			if(bytes1!=null && bytes1.length>0) {
				LOG.info("Controller : night stay  Before IMAGE : " +bytes1.length); 
				BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(scaledImg, "jpg", baos); 
				familydata = baos.toByteArray();
				LOG.info("Controller : night stay After IMAGE : " +familydata.length); 
			}//end
			
			if(familydata!=null && familydata.length>0) {
				/* LOG.info("Family Image data : " + familydata.toString()); */
				ns.setNsfamilyimagedata(familydata);
			}
			/*byte[] aadhardata = null;
			aadhardata = aadharImage.getBytes();
			if(aadhardata.length>1) {
				ns.setNsaadharimagedata(aadhardata);
			}*/
			LOG.info("Controller : Finished, family and aadhar image set to object");
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed to set family and aadhar image to object");
			e.printStackTrace();
		}
		ns.setNsprofilecode("NIGHT STAY");
		NightStayDetails nsd = null;
		LOG.info("Controller :Calling the Service to register the night stay object");
		nsd = service.registerNightStayInformation(ns);
		
		if (nsd != null) {
			LOG.info("Controller :  night stay registration Successfully to Completed and posting to HTML ");
			model.addAttribute("msg", "'Profile has been created Successfully.");
			model.addAttribute("visitorid",nsd.getNsid());
			model.addAttribute("visitorname",nsd.getNsfullname());
		} else {
			LOG.info("Controller : Got exception, Failed to night stay registration process");
			model.addAttribute("msg", "Registration Filed.. Please try again ");
		}
		return "NightStayRegisterPage";
	}
	
	@GetMapping("/nightstaylist")
	public String AllNightStayPeopleList(Model model, Principal p) {
		
		try {
			LOG.info("Retriving all Nihgt stay deatils from  DB");
			List<Object[]> yd = null; yd = service.allNightSatyInformation();
			if (yd!= null) {
				ArrayList<String[]> vistlist=new ArrayList<>();
				//handling Sno ..	nsid,nsfullname,nsmobilenumber,nsdays,nsfulladdress,nsfamilyimagedata
				//Logic for imges  in the list
				HashMap<String, byte[]> imgs=new HashMap<>();
				int sno=1;
				for (Object[] obj : yd) {
					String[] sNo=null;
					int idno = (Integer)obj[0];
					sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
					sno++;
					vistlist.add(sNo);
					imgs.put(Integer.toString(idno),(byte[]) obj[5]);
				}
				//End handling Sno ..	
				model.addAttribute("vdlist", vistlist);
				model.addAttribute("imgUtil", new ImageUtil());
				model.addAttribute("imgs",imgs);
			} else {
				LOG.info("failed to retrive all the night stay info");
				model.addAttribute("removemsg", " night stay profile list are not found..");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "NightStayListInformation";
		
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * if(opt!=null) { model.addAttribute("name", opt.getAname());
		 * model.addAttribute("admin", opt); model.addAttribute("imgUtil", new
		 * ImageUtil()); }
		 * LOG.info("Controller : Started to fetch list of Night Stay process");
		 * LOG.info("Controller :Calling the Service to get all the night stay object");
		 * List<Object[]> nsd = null; nsd = service.allNightSatyInformation();
		 * 
		 * if (nsd!= null) { LOG.
		 * info("Controller : Successfully to retirve all the Night stay people and posting to HTML "
		 * ); model.addAttribute("vdlist", nsd); return "NightStayListInformation"; }
		 * else { LOG.
		 * info("Controller : Got exception, Failed  to retrive all the night stay list info"
		 * ); model.addAttribute("usernotfound",
		 * " Night Stay profile's list are not found.. Please try again or Please Contact Adminstartor "
		 * ); return "NightStayListInformation"; }
		 */
	}
	
	@GetMapping("/nightstaydelete")
	public String nightstaydelete(@RequestParam Integer nsid, Model model, Principal p) {
		try {

			LOG.info("Controller : Started to remove the of prfile");

			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}

			LOG.info("Controller :Calling the Service to remove one the night stay object");
			service.getRemoveNightStayById(nsid);
			model.addAttribute("removed", "Night Stay profil Has been Removed");
			LOG.info("Controller : removed one night Stay profile deatils from  DB");


			List<Object[]> yd = null; yd = service.allNightSatyInformation();
			if (yd!= null) {
				ArrayList<String[]> vistlist=new ArrayList<>();
				//handling Sno ..
				//Logic for imges  in the list
				HashMap<String, byte[]> imgs=new HashMap<>();

				int sno=1;
				for (Object[] obj : yd) {
					String[] sNo=null;
					int idno = (Integer)obj[0];
					sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
					sno++;
					vistlist.add(sNo);
					imgs.put(Integer.toString(idno),(byte[]) obj[5]);
				}
				//End handling Sno ..	
				model.addAttribute("vdlist", vistlist);
				model.addAttribute("imgUtil", new ImageUtil());
				model.addAttribute("imgs",imgs);
			} else {
				LOG.info("failed to retrive all the night stay info");
				model.addAttribute("removemsg", " night stay profile list are not found..");
			}
		}catch (Exception e) {
			LOG.info("COntroller : Execption recevied in Night stay Deleting process : "+e.getMessage());
		}
			return "NightStayListInformation";
	}
	
	@GetMapping("/nightstayview")
	public String getNightstayview(@RequestParam Integer nsid, Model model) {
		LOG.info("Controller : Calling to link NIght stay id" + nsid);
		nightStayId = nsid;
		LOG.info("Controller : Sucess to link NIght stay id" + nsid +" : and ready to call ajax by calling new HTML");
		return "NightStayViewLink";
	}

	@GetMapping("/nightstayviewlinkinfo")
	@ResponseBody
	public NightStayDetails getNightStayViewLinkInfo() {
		LOG.info("Controller : Calling ajax to night stay get info" + nightStayId);
		Optional<NightStayDetails> opt = null;
		NightStayDetails ViewDetails = null;
		opt = service.getViewByNightStayID(nightStayId);
		if (opt != null) {
			ViewDetails = opt.get();
		}
		LOG.info("Controller : calling done and posting html");
		return ViewDetails;
	}
	
	@GetMapping("/nightstaysearch")
	public String getNightStaySearch(Principal p, Model model) {
		LOG.info("Controller : Calling to Open Night Stay Search page");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return "NightStaySearchPage";
	}


	@PostMapping("/nightshiftsearchvalues")
	public String getNightStaySearchValues(@RequestParam String nightstayoption, Principal p, Model model) {
		LOG.info("Controller : Calling service with Open Night Stay Search values");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		List<Object[]> nsd = null; 
		nsd = service.getOneOrAllNightStayInformation(nightstayoption);
		
		if (nsd.size()>=1) {
			LOG.info("Controller : Successfully to retirve one or all the Night stay people and posting to HTML ");
			model.addAttribute("vdlist", nsd);
			return "NightStayListInformation";
		} else {
			LOG.info("Controller : Got exception, Failed  to retrive all the night stay list info");
			model.addAttribute("msg", nightstayoption +" : Night stay profile's list are not found.. Please try again or Please Contact Adminstartor");
			
			LOG.info("Controller :Calling the Service to get all the night stay details object, after the search");
			List<Object[]> eds = null; 
			eds = service.allNightSatyInformation();
			
			if (eds!= null) {
				LOG.info("Controller : Successfully to retirve all the Night stay details after the seach and posting to HTML ");
				model.addAttribute("vdlist", eds);
				return "NightStayListInformation";
			} else {
				LOG.info("Controller : Got exception, Failed  to retrive all the Night stay  details list info, after the search");
				model.addAttribute("msg", " Night stay  details profile's list are not found.. Please try again or Please Contact Adminstartor ");
				return "NightStayListInformation";
			}
			
		}
	}
	
	@GetMapping("/nightstayedit")
	public String getNightStayEditPage(@RequestParam Integer nsid, Model model, Principal p) {
		LOG.info("Controller : Calling service with Open Night Stay Edit profile values");
		try {
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}

			Optional<NightStayDetails> opt = null;
			opt = service.getViewByNightStayID(nsid);
			if (opt != null) {
				NightStayDetails evd = null;
				evd = opt.get();
				if(evd!=null) {
					model.addAttribute("edit", evd);
					if(evd!=null && evd.getNsfamilyimagedata()!=null)
						model.addAttribute("imgUtil", new ImageUtil());
				}
			} else {
				model.addAttribute("msg", "Couldn't not found night stay Details, Please Contact Adminstartor ");
			}

			LOG.info("night stay View and Edit Operation Ended and Data posting to HTML..");
		} catch (Exception e) {
		e.printStackTrace();
		LOG.info("Controller Error : got execption night stay View and Edit Operation ");
	}

		return "NightStayEditFrom";
	}
	
	@PostMapping("/editnightstayOpe")
	public String nightStaOpreation(@RequestParam("imageFile") MultipartFile imageFile, 
			@ModelAttribute NightStayDetails ns, Model model, Principal p) {
		LOG.info("Controller : Started Night Stay Edit updation process");
		try {
			LOG.info("Controller : Started to setting the family and aadhar image to object");
			byte[] familydata = null;

			//New Logic to resize the image
			byte[] bytes1 = null ;
			bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
			if(bytes1!=null && bytes1.length>0) {
				LOG.info("Controller : night stay  Before IMAGE : " +bytes1.length); 
				BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(scaledImg, "jpg", baos); 
				familydata = baos.toByteArray();
				LOG.info("Controller : night stay After IMAGE : " +familydata.length); 
			}//end

			if(familydata.length>=1) {
				/* LOG.info("Family Image data found with size : " + familydata.toString()); */
				ns.setNsfamilyimagedata(familydata);
			}
			ns.setNsprofilecode("NIGHT STAY");
			
			LOG.info("Controller : Finished, family and aadhar image set to object");
			
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed to set family and aadhar image to object");
			e.printStackTrace();
		}

		String yd = null;
		yd = service.registerNightStayEditInformation(ns);

		if (yd != null) {
			LOG.info("registering Night stay deatils Done");
			model.addAttribute("update", "'" + ns.getNsfullname().toUpperCase()
					+ "' Profile has been UPDATED Successfully.");
		} else {
			LOG.info("failed to register");
			model.addAttribute("usernotfound", "Registration Filed.. Please try again ");
		}
	
		Optional<NightStayDetails> opt = null;
		opt = service.getViewByNightStayID(ns.getNsid());
		if (opt != null) {
			NightStayDetails evd = null;
			evd = opt.get();
			if(evd!=null) {
				model.addAttribute("edit", evd);
				model.addAttribute("msg", yd.toUpperCase()+" Has been updated.");
				if(evd!=null && evd.getNsfamilyimagedata()!=null)
					model.addAttribute("imgUtil", new ImageUtil());
				LOG.info("Controller :Success to get the night stay profile from service, and posting to HTML");
			}
		} else {
			model.addAttribute("noeditpage", "Issue with Edit NIght Stay, Please Contact Adminstartor ");
		}

		return "NightStayEditFrom";
	}
	
	//------------ Ended Night Stay Things----------------
	
	
	//------------ Started Dynamic search program Things---------------
	
	@GetMapping("/dynamicsearch")
	public String getDynamicSearchList(Model model, Principal p) {
		
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		
		LOG.info("Controller : entered and Started to get all the Dynamic search process");
		
		//Yoga Search Info
		try {
			LOG.info("Controller : Started dynamic search for Yoga Information");
			List<Object[]> yogalist=null;
			yogalist=service.geDynamicYogaInformation();
			if(yogalist!=null)
				model.addAttribute("ylist", yogalist);
			LOG.info("Controller : Successfull, dynamic search for yoga Information, and Posting to HTML .. "+ yogalist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info Yoga Details");
		}
		
		//Night Stay Info
		try {
			LOG.info("Controller : Started dynamic search for night stay Information");
			List<Object[]> nightstaylist=null;
			nightstaylist=service.geDynamicNightStayInformation();
			if(nightstaylist!=null)
				model.addAttribute("nslist", nightstaylist);
			LOG.info("Controller : Successfull, dynamic search for night stay Information, and Posting to HTML .. "+ nightstaylist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info night stay Details");
		}
		
		//Event Program Info
		try {
			LOG.info("Controller : Started dynamic search for Event Information");
			List<Object[]> eventlist=null;
			eventlist=service.geDynamicEventInformation();
			if(eventlist!=null)
				model.addAttribute("elist", eventlist);
			LOG.info("Controller : Successfull, dynamic search for event Information, and Posting to HTML .. "+ eventlist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info event list  Details");
		}
		
		//Visitor Search Info
		try {
			LOG.info("Controller : Started dynamic search for visitor Information");
			List<Object[]> visitorlist=null;
			visitorlist=service.geDynamicVisitorInformation();
			if(visitorlist!=null)
				model.addAttribute("vlist", visitorlist);
				LOG.info("Controller : Successfull, dynamic search for visitor Information, and Posting to HTML .. "+ visitorlist.size());
		} catch (Exception e) {
				LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info visitor Details");
		}
			
		return "DynamicSearchListInformation";
	}
	
	@GetMapping("/vipyoga")
	public String getYogaMoveToShivapur(@RequestParam Integer yid, Model model) {
		LOG.info("Controller : Calling Service to Move yoga Id to Shivapur : " +yid);
		try {
			Integer result =null;
			result=service.yogaProMoveToShivapur(yid);
			if(result==1) {
				LOG.info("Controller : Successfull to Move Night Stay Id to Shivapur : " +yid);
				model.addAttribute("msg","Profile Moved to Shivapur Successfully, Please verify it from Admin panel");
			} else {
				LOG.info("Controller : Issues with Moving Night Stay Id to Shivapur : " +yid);
				model.addAttribute("issue","Profile not Moved to Shivapur, Please Contact Admin");
			}
		}catch (Exception e) {
			LOG.info("Controller : got execption While Move yoga Id to Shivapur : " +yid);
		}
		return "redirect:dynamicsearch";
	}
	
	@GetMapping("/vipnightstay")
	public String getNightStayMoveToShivapur(@RequestParam Integer nsid, Model model) {
		LOG.info("Controller : Calling Service to Move Night Stay Id to Shivapur : " +nsid);
		try {
			Integer result =null;
			result=service.nightStayProMoveToShivapur(nsid);
			if(result!=null) {
				LOG.info("Controller : Successfull to Move Night Stay Id to Shivapur : " +nsid);
				model.addAttribute("msg","Profile Moved to Shivapur Successfully, Please verify it from Admin panel");
			} else {
				LOG.info("Controller : Issues with Moving Night Stay Id to Shivapur : " +nsid);
				model.addAttribute("issue","Profile not Moved to Shivapur, Please Contact Admin");
			}
		}catch (Exception e) {
			LOG.info("Controller : got execption While Move Night Stay Id to Shivapur : " +nsid);
		}
		return "redirect:dynamicsearch";
	}
	
	@GetMapping("/vipevent")
	public String getEventMoveToShivapur(@RequestParam Integer eid, Model model) {
		LOG.info("Controller : Calling Service to Move event Id to Shivapur : " +eid);
		try {
			Integer result =null;
			result=service.eventProMoveToShivapur(eid);
			if(result!=null) {
				LOG.info("Controller : Successfull to Move event Id to Shivapur : " +eid);
				model.addAttribute("msg","Profile Moved to Shivapur Successfully, Please verify it from Admin panel");
			} else {
				LOG.info("Controller : Issues with Moving event Id to Shivapur : " +eid);
				model.addAttribute("issue","Profile not Moved to Shivapur, Please Contact Admin");
			}
		}catch (Exception e) {
			LOG.info("Controller : got execption While Move event Id to Shivapur : " +eid);
		}
		return "redirect:dynamicsearch";
	}
	
	@GetMapping("/vipvisitore")
	public String getVisitorMoveToShivapur(@RequestParam Integer vid, Model model) {
		LOG.info("Controller : Calling Service to Move Visitor Id to Shivapur : " +vid);
		try {
			Integer result =null;
			result=service.visitorProMoveToShivapur(vid);
			if(result!=null) {
				LOG.info("Controller : Successfull to Move Visitor Id to Shivapur : " +vid);
				model.addAttribute("msg","Profile Moved to Shivapur Successfully, Please verify it from Admin panel");
			} else {
				LOG.info("Controller : Issues with Moving Visitor Id to Shivapur : " +vid);
				model.addAttribute("issue","Profile not Moved to Shivapur, Please Contact Admin");
			}
		}catch (Exception e) {
			LOG.info("Controller : got execption While Move Visitor Id to Shivapur : " +vid);
		}
		return "redirect:dynamicsearch";
	}
	
	@PostMapping("/dynamicsearchvalues")
	public String getDynamicSearchValues(@RequestParam String dynamicoption, Principal p, Model model) {
		LOG.info("Controller : Calling service with dynamic Search Option values");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		//Yoga Search Info
				try {
					List<Object[]> yogalist=null;
					yogalist=service.geDynamicSearchOptionYogaInformation(dynamicoption);
					if(yogalist!=null)
						model.addAttribute("ylist", yogalist);
					LOG.info("Controller : Successfull, dynamic Search Option for yoga Information, and Posting to HTML .. "+ yogalist.size());
				} catch (Exception e) {
					LOG.info("Controller : Got exception, Failed  to retrive all the dynamic Search Option Info Yoga Details");
				}
				
				//Night Stay Info
				try {
					LOG.info("Controller : Started dynamic Search Option for night stay Information");
					List<Object[]> nightstaylist=null;
					nightstaylist=service.geDynamicSearchOptionNightStayInformation(dynamicoption);
					if(nightstaylist!=null)
						model.addAttribute("nslist", nightstaylist);
					LOG.info("Controller : Successfull, dynamic Search Option for night stay Information, and Posting to HTML .. "+ nightstaylist.size());
				} catch (Exception e) {
					LOG.info("Controller : Got exception, Failed  to retrive all the dynamic Search Option Info night stay Details");
				}
				
				//Event Program Info
			try {
					LOG.info("Controller : Started dynamic Search Option for Event Information");
					List<Object[]> eventlist=null;
					eventlist=service.geDynamicSearchOptionEventInformation(dynamicoption);
					if(eventlist!=null)
						model.addAttribute("elist", eventlist);
					LOG.info("Controller : Successfull, dynamic Search Option for event Information, and Posting to HTML .. "+ eventlist.size());
				} catch (Exception e) {
					LOG.info("Controller : Got exception, Failed  to retrive all the dynamic Search Option Info event list  Details");
				}
				
				//Visitor Search Info
				try {
					LOG.info("Controller : Started dynamic Search Option for visitor Information");
					List<Object[]> visitorlist=null;
					visitorlist=service.geDynamicSearchOptionVisitorInformation(dynamicoption);
					if(visitorlist!=null)
						model.addAttribute("vlist", visitorlist);
						LOG.info("Controller : Successfull, dynamic Search Option for visitor Information, and Posting to HTML .. "+ visitorlist.size());
				} catch (Exception e) {
						LOG.info("Controller : Got exception, Failed  to retrive all the dynamic Search Option Info visitor Details");
				}
		
		return "DynamicSearchListInformation";
		
	}
	
	//------------ Ended Dynamic search Things---------------
	
	//---------------- Shivapur started ----------------------
	@GetMapping("/shivapurList")
	public String getShivapurList(Model model, Principal p) {
		
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		
		//yoga Shivapur
		LOG.info("Controller : entered and Started to get all the Shivapur Search");
		try {
			LOG.info("Controller : Started  Shivapur search for Yoga Information");
			List<Object[]> yogalist=null;
			yogalist=service.getShivapurYogaInformation();
			if(yogalist!=null)
				model.addAttribute("ylist", yogalist);
			LOG.info("Controller : Successfull, dynamic search for yoga Information, and Posting to HTML .. "+ yogalist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info Yoga Details");
		}
		
		//night stay Shivapur
		try {
			LOG.info("Controller : Started  Shivapur search for Night stay Information");
			List<Object[]> nslist=null;
			nslist=service.getShivapurNightStayInformation();
			if(nslist!=null)
				model.addAttribute("nslist", nslist);
			LOG.info("Controller : Successfull, dynamic search for Night stay Information, and Posting to HTML .. "+ nslist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info Night stay Details");
		}
		
		//Event stay Shivapur
		try {
			LOG.info("Controller : Started  Shivapur search for Event Information");
			List<Object[]> elist=null;
			elist=service.getShivapurEventInformation();
			if(elist!=null)
				model.addAttribute("elist", elist);
			LOG.info("Controller : Successfull, dynamic search for Event Information, and Posting to HTML .. "+ elist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info Event Details");
		}
		
		//Visitor  Shivapur
		try {
			LOG.info("Controller : Started  Shivapur search for Visitor Information");
			List<Object[]> vlist=null;
			vlist=service.getShivapurVisitorInformation();
			if(vlist!=null)
				model.addAttribute("vlist", vlist);
			LOG.info("Controller : Successfull, dynamic search for Visitor Information, and Posting to HTML .. "+ vlist.size());
		} catch (Exception e) {
			LOG.info("Controller : Got exception, Failed  to retrive all the dynamic search Info Visitor Details");
		}
		
		return "ShivapurListInformation";
	}
	//----------------- ended Shivapur ----------------------
	//----------- Admin function Started -----------------
	
	@GetMapping("/adminfunction")
	public String adminfunctionpage(Principal p, Model model) {
		LOG.info("Showing Admin access page");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return "AdminFunctionPage";
	}
	@GetMapping("/ashramemppage")
	public String ashramemppage(Principal p, Model model) {
		LOG.info("Showing ashram employee page");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		return "AshramEmployeePage";
	}
	
	@GetMapping("/adminempinfo")
	public String getAdminEmployeeInfo(Principal p, Model model) {
		LOG.info("Controller : getting all employee infor from aasharm");
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}

		List<AdminDetails> emplinfo = null;
		emplinfo = service.getAllEmployeeList();
		if (emplinfo != null) {
			LOG.info("retrived the all employee deatils Done");
			
			  emplinfo.stream().forEach(s -> { s.setApassword("**********"); });
			 
			model.addAttribute("empinfolist", emplinfo);
			model.addAttribute("imgUtil", new ImageUtil());
		} else {
			LOG.info("failed to retrive all the employee info");
			model.addAttribute("msg", "Filed to Retrive all The employee Info.. Please try again ");
		}

		return "AdminEmployeeInfoList";
	}
	
	
	@GetMapping("/employeeregisterpage")
	public String showAdminRegisterForm() {
		LOG.info("Showing Employee register page");
		return "DisplayEmployeeRegisterForm";
	}

	@PostMapping("/employeeregister")
	public String registerAdminDetails(@RequestParam("employeeImage") MultipartFile employeeImage,@ModelAttribute AdminDetails adm, Model model, Principal p) {
		LOG.info(" Conntroller : started processing registering Ashram EMPLOYEE deatils to DB");
		try {

			byte[] data = null;
			data = employeeImage.getBytes();
			if(data.length>1) {
				adm.setAimage(data);
			}  else {
				LOG.info("Image data Not found in : Aahram empl registering");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(" Controller Error : got execption while setting the Ashram employee pictire deatils to DB");
		}
		
		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		
		AdminDetails ad = null;

		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy, hh:mm:ss aa, z");// new SimpleDateFormat("E,
																							// dd MMM yyyy HH:mm:ss z");
		adm.setAvisitieddatetime(formatter.format(new Date()));

		ad = service.registerAdminInformation(adm);
		if (ad != null) {
			model.addAttribute("msg", "'" + ad.getAname().toUpperCase() + "' Registration Successfuly as '"
					+ ad.getArole().toString().toUpperCase() + "' role and Login ID : '" + ad.getAemail() + "'");
		} else {
			model.addAttribute("msg", "Registration Filed.. Please try again ");
		}
		return "DisplayEmployeeRegisterForm";
	}
	
	@GetMapping("/employeeprofileremove")
	public String employeeprofileremoval(@RequestParam Integer aid, Model model, Principal p) {

		if(opt!=null) {
			model.addAttribute("name", opt.getAname());
			model.addAttribute("admin", opt);
			model.addAttribute("imgUtil", new ImageUtil());
		}
		
		service.getRemoveEmployeeByEmail(aid);
		model.addAttribute("removed", "Employee Has been Removed");
		LOG.info("removed one employee deatils from  DB");

		List<AdminDetails> emplinfo = null;
		emplinfo = service.getAllEmployeeList();
		if (emplinfo != null) {
			LOG.info("retrived the all employee deatils Done");
			
			  emplinfo.stream().forEach(s -> { s.setApassword("**********"); });
			 
			model.addAttribute("empinfolist", emplinfo);
			model.addAttribute("imgUtil", new ImageUtil());
		} else {
			LOG.info("failed to retrive all the employee info");
			model.addAttribute("msg", "Filed to Retrive all The employee Info.. Please try again ");
		}

		return "AdminEmployeeInfoList";
	}

	
	//----------------ended Admin Function -----------------------


	//--------------  Volunteer Info Started---------------

	//volunteer register
	@PostMapping("/voluneetrregistrationdetails")
	public String yogaVolunteerRegistringDetails(@RequestParam("imageFile") MultipartFile imageFile,
			@ModelAttribute VolunteerDetails yogaVolunteer, Model model, Principal p) {

		LOG.info(" Conntroller : started processing registering YOGA Volunteer deatils to DB");

		try {
			byte[] data = null;
			data = imageFile.getBytes();
			if(data.length>1) 
				yogaVolunteer.setVimagedata(data);
			else 
				LOG.info("Image data Not found in : yoga Volunteer registering");

			VolunteerDetails yvd = null;
			yvd = service.registerYogaVolunteerInformation(yogaVolunteer);

			if (yvd != null) {
				LOG.info("registering yoga Volunteer deatils Done");
				model.addAttribute("msg", "'" + yvd.getVfullname().toUpperCase()+ "' Profile created Successfully'");
			} else {
				LOG.info("failed to register");
				model.addAttribute("msg", "Registration Filed.. Please try again ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(" Controller Error : got execption while setting the YOGA Volunteer pictire deatils to DB");
		}
		return "DisplayYogaVolunteerRegistrationForm";
	}
		
	//Yoga volunteer list
		@GetMapping("/volunteerlist")
		public String getYogaAllVolunteerList(Model model, Principal p) {
			
			try {
				if(opt!=null) {
					model.addAttribute("name", opt.getAname());
					model.addAttribute("admin", opt);
					model.addAttribute("imgUtil", new ImageUtil());
				}
				
				LOG.info("Retriving all yoga volunteer deatils from  DB");
				List<Object[]> yd = null; 
				yd = service.getAllYogaVolunteerList();
				
				ArrayList<String[]> vistlist=new ArrayList<>();
				//handling Sno .. vid,vidcard,vbatch,vfullname,vprofilecode,vmobilenumber,vpermanentaddress,vimagedata
				HashMap<String, byte[]> imgs=new HashMap<>();

				int sno=1;
				for (Object[] obj : yd) {
					String[] sNo=null;
					int idno = (Integer)obj[0];
					sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5],(String) obj[6]};
					sno++;
					vistlist.add(sNo);
					imgs.put(Integer.toString(idno),(byte[]) obj[7]);
				}
				//End handling Sno ..	
				
				if (yd!= null) {
					LOG.info("retrived the Yoga volunteer deatils Done" + yd.size());
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the yoga info");
					model.addAttribute("usernotfound", " Yoga profile list are not found.. Please try again or Please Contact Adminstartor ");
				}
			} catch (Exception e) {
				LOG.info("Retriving execption while retriving all volunteer deatils from  DB");
				e.printStackTrace();
			}
			
			return "YogaVolunteerInformationList";
		}
		
		//Yoga Volunteer Edit & view
		@GetMapping("/editviewyogavolunteer")
		public String getYogaVolunteerEditViewPage(@RequestParam Integer yvid, Model model, Principal p) {
			LOG.info("Get request Edit & view page for Yoga volunteer from controller");
		 Optional<VolunteerDetails> opt = null;
			opt = service.getViewByYogaVolunteerID(yvid);
			if (opt != null) {
				VolunteerDetails evd = null;
				evd = opt.get();
				model.addAttribute("edit", evd);
				model.addAttribute("imgUtil", new ImageUtil());
			} else {
				model.addAttribute("noeditpage", "Issue with Edit and view yoga volunteer, Please Contact Adminstartor ");
			}
			LOG.info("Get response Edit and view page for yoga volunteer from controller");
			return "YogaVolunteerEditViewFrom";
		}

		//Yoga Volunteer Edit & view Operation
		@PostMapping("/editviewyogavlounterOperation")
		public String getEditviewyogavolunteerUpdate(@RequestParam("imageFile") MultipartFile imageFile,@ModelAttribute VolunteerDetails editform, Model model, Principal p) {
			LOG.info("updating Edit view yoga vlounter deatils to DB");
			try {
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : volunteer Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : volunteer After IMAGE : " +data.length); 
				}//end
				
				if(data!=null && data.length>0) 
					editform.setVimagedata(data);
				else
					LOG.info("Image data Not found in : Edit view yoga vlounter registering");
				editform.setVprofilecode("YOGA_VOLUNTEER");
				String yd = null;
				yd = service.registerYogaVolunteerEditViewInformation(editform);
				if (yd != null) {
					LOG.info("Updating edit view yoga vlounter deatils Done");
					model.addAttribute("msg", "'" + editform.getVfullname().toUpperCase()+ "' Profile has been UPDATED Successfully.");
				} else {
					LOG.info("failed to Update");
					model.addAttribute("msg", "Update Filed.. Please try again ");
				}
				
				Optional<VolunteerDetails> opt = null;
				opt = service.getViewByYogaVolunteerID(editform.getVid());
				if (opt != null) {
					VolunteerDetails evd = null;
					evd = opt.get();
					model.addAttribute("edit", evd);
					model.addAttribute("imgUtil", new ImageUtil());
					
				} else {
					model.addAttribute("noeditpage", "Issue with Edit view yoga volunteer, Please Contact Adminstartor ");
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info(" Controller Error : got execption while setting the YOGA volunteer Editing & view deatils to DB");
			}
			return "YogaVolunteerEditViewFrom";
		}
		
		//Yoga Volunteer Delete
		@GetMapping("/deleteyogavolunteer")
		public String deleteYogaVolunteerParticipent(@RequestParam Integer yvid, Model model, Principal p) {
			try {

				LOG.info("Yoga volunteer Delete Operation Started..");
				service.getRemoveYogaVolunteerById(yvid);
				model.addAttribute("removemsg", "YOGA volunteer profile Removed.");
				
				List<Object[]> yd = null; 
				yd = service.getAllYogaVolunteerList();
				
				ArrayList<String[]> vistlist=new ArrayList<>();
				//handling Sno .. vid,vidcard,vbatch,vfullname,vprofilecode,vmobilenumber,vpermanentaddress,vimagedata
				HashMap<String, byte[]> imgs=new HashMap<>();

				int sno=1;
				for (Object[] obj : yd) {
					String[] sNo=null;
					int idno = (Integer)obj[0];
					sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5],(String) obj[6]};
					sno++;
					vistlist.add(sNo);
					imgs.put(Integer.toString(idno),(byte[]) obj[7]);
				}
				//End handling Sno ..	
				
				if (yd!= null) {
					LOG.info("retrived the Yoga volunteer deatils Done" + yd.size());
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the yoga info");
					model.addAttribute("usernotfound", " Yoga profile list are not found.. Please try again or Please Contact Adminstartor ");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
				return "YogaVolunteerInformationList";
		}
		
		@PostMapping("/yogaVolunteerSearchByBatch")
		public String getYogaVolunteerSearchByBatch(@RequestParam String yogavolunteersearchbybatch, Model model, Principal p) {
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}

			List<Object[]>  vd = null;
			vd = service.getYogaVolunteerSearchByBatch(yogavolunteersearchbybatch);

			if (vd.size() >= 1) {
				LOG.info("retrived the Yoga Volunteer Search By Batch deatils Done" + vd.size());
				model.addAttribute("vdlist", vd);
				return "YogaVolunteerInformationList";
			} else {
				LOG.info("failed to retrive all the yoga info");
				model.addAttribute("removemsg",
						yogavolunteersearchbybatch + " :  Yoga profile not found.. Please try again or Please Contact Adminstartor ");
				return "YogaVolunteerInformationList";
			}
		
		}
		
		@PostMapping("/yogaVolunteerSearchByOccuption")
		public String getYogaVolunteerSearchByOccuption(@RequestParam String yogavolunteersearchbyoccupation, Model model, Principal p) {

			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}

			List<Object[]>  vd = null;
			vd = service.getYogaVolunteerSearchByOccuption(yogavolunteersearchbyoccupation);

			if (vd.size() >= 1) {
				LOG.info("retrived the Yoga volunteer Search By occupation deatils Done" + vd.size());
				model.addAttribute("vdlist", vd);
				return "YogaVolunteerInformationList";
			} else {
				LOG.info("failed to retrive all the yoga info");
				model.addAttribute("removemsg",
						yogavolunteersearchbyoccupation + " :  Yoga profile not found.. Please try again or Please Contact Adminstartor ");
				return "YogaVolunteerInformationList";
			}
		
		}
		
		@PostMapping("/yogaVolunteerSearchByIds")
		public String getYogaVolunteerSearchByIds(@RequestParam String yogavolunteerids, Model model, Principal p) {

			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}

			List<Object[]>  vd = null;
			vd = service.getYogaVolunteerSearchByIds(yogavolunteerids);

			if (vd.size() >= 1) {
				LOG.info("retrived the Yoga volunteer Search By ids deatils Done" + vd.size());
				model.addAttribute("vdlist", vd);
				return "YogaVolunteerInformationList";
			} else {
				LOG.info("failed to retrive all the yoga info");
				model.addAttribute("removemsg",
						yogavolunteerids + " :  Yoga profile not found.. Please try again or Please Contact Adminstartor ");
				return "YogaVolunteerInformationList";
			}
		
		}
		
		@PostMapping("/yogaVolunteerSearchByName")
		public String getYogaVolunteerSearchByName(@RequestParam String yogavolunteersearchbyname, Model model, Principal p) {

			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}

			List<Object[]>  vd = null;
			vd = service.getYogaVolunteerSearchByName(yogavolunteersearchbyname);

			if (vd.size() >= 1) {
				LOG.info("retrived the Yoga volunteer Search By name deatils Done" + vd.size());
				model.addAttribute("vdlist", vd);
				return "YogaVolunteerInformationList";
			} else {
				LOG.info("failed to retrive all the yoga info");
				model.addAttribute("removemsg",
						yogavolunteersearchbyname + " :  Yoga profile not found.. Please try again or Please Contact Adminstartor ");
				return "YogaVolunteerInformationList";
			}
		
		}
		
		//-------------- Yoga Volunteer Info Ended--------------
		//--------------  Volunteer Info Started--------------
		@GetMapping("/volunteer")
		public String showVolunteerHomePage(Model model, Principal p) {
			LOG.info("Showing volunteer Home page");
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}
			return "VolunteerWelcomePage";
		}
		
		//yoga Volunteer
		@GetMapping("/yogavolunteer")
		public String showYogaVolunteerWelcomePage(Model model, Principal p) {
			LOG.info("Showing Yoga VolunteerHome page");
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}
			return "DisplayYogaVolunteerHomePage";//DisplayYogaVolunteerRegistrationForm";
		}
		
		@GetMapping("/volunteerregipage")
		public String showYogaVolunteerRegistrationPage(Model model, Principal p) {
			LOG.info("Showing Yoga VolunteerHome page");
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}
			return "DisplayYogaVolunteerRegistrationForm";
		}
		
		//Ashram Volunteer
		@GetMapping("/ashramvolunteer")
		public String showAshramVolunteerWelcomePage(Model model, Principal p) {
			LOG.info("Showing Yoga VolunteerHome page");
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}
			return "AshramVolunteerHomePage";
		}
		
		@GetMapping("/ashramvolunteerregipage")
		public String showAshramVolunteerRegistrationPage(Model model, Principal p) {
			LOG.info("Showing Yoga VolunteerHome page");
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}
			return "AshramVolunteerRegistrationForm";
		}

		@PostMapping("/ashramvoluneetrregistrationdetails")
		public String ashramVolunteerRegistringDetails(@RequestParam("imageFile") MultipartFile imageFile,
				@ModelAttribute YogaDetails yoga, Model model, Principal p) {
			LOG.info(" Conntroller : started processing registering ASHRAM Volunteer deatils to DB");
			try {
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : ashram volunteer Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : ashram volunteer After IMAGE : " +data.length); 
				}//end
				
				if(data!=null && data.length>0)
					yoga.setYimagedata(data);
				else 
					LOG.info("Image data Not found in : ASHRAM Volunteer registering");
				yoga.setYprofilecode("ASHRAM_VOLUNTEER");
				YogaDetails yd = null;
				yd = service.registerYogaInformation(yoga);
				if (yd != null) {
					LOG.info("registering ASHRAM Volunteer deatils Done");
					model.addAttribute("msg", "'" + yd.getYfullname().toUpperCase()
							+ "' Profile created Successfully'");
				} else {
					LOG.info("failed to register");
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info(" Controller Error : got execption while inserting the ASHRAM Volunteer deatils to DB");
			}
			return "AshramVolunteerRegistrationForm";
		}
		
		@GetMapping("/ashramvolunteerlist")
		public String getAllAshramVolunteerList(Model model, Principal p) {
			if(opt!=null) {
				model.addAttribute("name", opt.getAname());
				model.addAttribute("admin", opt);
				model.addAttribute("imgUtil", new ImageUtil());
			}
			LOG.info("Retriving all  Ashram deatils from  DB");
			List<Object[]> yd = null; yd = service.getAllAshramVolunteerList();
			if (yd!= null) {
				LOG.info("retrived the ashram volunteer deatils Done" + yd.size());
				model.addAttribute("vdlist", yd);
				return "AshramVolunteerInformationList";
			} else {
				LOG.info("failed to retrive all the ashram volunteer info");
				model.addAttribute("usernotfound", " ashram volunteer profile list are not found..");
				return "AshramVolunteerInformationList";
			}
		}
		//--------------  Volunteer Info Ended--------------
		
		//-------------- Guru Mala info Started -------------------
		@GetMapping("/gurumalainfo")
		public String showGuruMalaHomePage(Model model, Principal p) {
			LOG.info("Showing Guru Mala Home page");
			return "GurumalaWelcomePage";
		}
		
		@GetMapping("/gurumalaregistrationpage")
		public String showGuruMalaRegistrationPage(Model model, Principal p) {
			LOG.info("Showing Guru Mala register page");
			return "GuruMalaRegistrationForm";
		}
		
		@PostMapping("/gurumalaregistrationdetails")
		public String gurumalaRegistringDetails(@RequestParam("imageFile") MultipartFile imageFile,
				@ModelAttribute GuruMalaDetails gurumala, Model model, Principal p) {
			LOG.info(" Conntroller : started processing registering gurumala deatils to DB");
			try {
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : guru mala Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : gurumala After IMAGE : " +data.length); 
				}//end
				
				if(data!=null && data.length>0)
					gurumala.setGmimagedata(data);
				else 
					LOG.info("Image data Not found in : gurumala registering");
				gurumala.setGmprofilecode("GURUMALA");
				Calendar calendar = Calendar.getInstance();
				int month =calendar.get(Calendar.MONTH) +1;
				gurumala.setGmdateofmonth(month+" "+calendar.get(Calendar.YEAR));
				GuruMalaDetails guruMalaDetails = null;
				guruMalaDetails = service.registerGuruMalaInformation(gurumala);
				if (guruMalaDetails != null) {
					LOG.info("registering gurumala deatils Done");
					model.addAttribute("msg", "'" + guruMalaDetails.getGmfullname().toUpperCase()
							+ "' Profile created Successfully'");
				} else {
					LOG.info("failed to register");
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info(" Controller Error : got execption while inserting the DONATION deatils to DB");
			}
			return "GuruMalaRegistrationForm";
		}
		
		@GetMapping("/gurumallist")
		public String getGuruMalaList(Model model, Principal p) {
			try {
				LOG.info("Retriving all Gurumala deatils from  DB");
				List<Object[]> yd = null; yd = service.getAllGuruMalaList();
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..gmid,gmfullname,gmmobilenumber,gmoccupation,gmpresentaddress,gmimagedata
					//Logic for imges  in the list
					HashMap<String, byte[]> imgs=new HashMap<>();
					int sno=1;
					for (Object[] obj : yd) {	
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(sNo);
						imgs.put(Integer.toString(idno),(byte[]) obj[5]);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the Gurumala info");
					model.addAttribute("removemsg", " Gurumala profile list are not found..");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "GuruMalaInformationList";
		}
		
		@GetMapping("/vieweditgurumala")
		public String getGuruMalViewEditPage(@RequestParam Integer gmid, Model model, Principal p) {
			try {
				LOG.info("Guru Mala View and Edit info Started from controller..");
				Optional<GuruMalaDetails> opt = null;
				opt = service.getViewEditByGurumalID(gmid);
				if (opt != null) {
					GuruMalaDetails evd = null;
					evd = opt.get();
					if(evd!=null) {
						model.addAttribute("edit", evd);
						if(evd!=null && evd.getGmimagedata()!=null)
							model.addAttribute("imgUtil", new ImageUtil());
					}
				} else {
					model.addAttribute("msg", "Couldn't not found Guru Mala Details, Please Contact Adminstartor ");
				}
				LOG.info("Guru Mala View and Edit info Ended and Data posting to HTML..");
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption Guru Mala View and Edit info");
			}
			return "GurumalViewEditInfo";
		}
		
		@PostMapping("/vieweditoperation")
		public String getGuruMalViewEditOpreation(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute GuruMalaDetails editform, Model model, Principal p) {
			try {
				LOG.info("Guru Mala View and Edit Opreation info Started from controller..");
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : guru mala Edit & view Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : gurumala Edit & view After IMAGE : " +data.length); 
				}//end
				
				if(data!=null && data.length>0) 
					editform.setGmimagedata(data);
				else
					LOG.info("Image data Not found in : Guru mala Edit registering");
				editform.setGmprofilecode("GURUMALA");
				Calendar calendar = Calendar.getInstance();
				editform.setGmdateofmonth(calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.YEAR));
				String yd = null;
				yd = service.getUpdateGuruMala(editform);
				if (yd != null) {
					model.addAttribute("msg", "UPDATED Successfully.");
				} else {
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			
				Optional<GuruMalaDetails> opt = null;
				opt = service.getViewEditByGurumalID(editform.getGmid());
				if (opt != null) {
					GuruMalaDetails evd = null;
					evd = opt.get();
					if(evd!=null) {
						model.addAttribute("edit", evd);
						if(evd!=null && evd.getGmimagedata()!=null)
							model.addAttribute("imgUtil", new ImageUtil());
					}
				} else {
					model.addAttribute("msg", "Couldn't not found Guru Mala Details, Please Contact Adminstartor ");
				}
				
				LOG.info("Guru Mala View and Edit Operation Ended and Data posting to HTML..");
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption Guru Mala View and Edit Operation ");
			}
			return "GurumalViewEditInfo";
		}
		
		@GetMapping("/deletegurumala")
		public String getDeleteGurumala(@RequestParam Integer gmid, Model model, Principal p) {
			try {
				LOG.info("Guru Mala Delete Operation Started..");
				service.getRemoveGuruMalaById(gmid);
				model.addAttribute("removemsg", "Guru Mala profile Removed.");
				
				LOG.info("Retriving all Gurumala deatils from  DB");
				List<Object[]> yd = null; yd = service.getAllGuruMalaList();
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					//Logic for imges  in the list
					HashMap<String, byte[]> imgs=new HashMap<>();

					int sno=1;
					for (Object[] obj : yd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(sNo);
						imgs.put(Integer.toString(idno),(byte[]) obj[5]);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the Gurumala info");
					model.addAttribute("removemsg", " Gurumala profile list are not found..");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption Guru Mala delete Operation ");
			}
			return "GuruMalaInformationList";
		}
		
		@PostMapping("/guruMalaSearchByName")
		public String getGuruMalaSearchByName(@RequestParam String gurumalasearchbyname, Model model, Principal p) {

			try {
				List<Object[]>  yd = null;
				yd = service.getGuruMalaSearchByName(gurumalasearchbyname);
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : yd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(sNo);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
				} else {
					LOG.info("failed to retrive all the Gurumala info");
					model.addAttribute("removemsg", " Gurumala profile list are not found..");
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			return "GuruMalaInformationList";
		}
		
		//PDF Creating Started ..
		@PostMapping("/gurumalapdfmonthlywise")
		public String getGuruMalaPDFMonthlyWise(@RequestParam String pdfdocmonthlywise, Model model, Principal p) {

			try {
				
				List<Object[]>  nsd = null;
				nsd = service.getGuruMalaPDFMonthlyWise(pdfdocmonthlywise);
		
				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaPDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaPDF\\GuruMalaMonthWise.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(sNo);

						//get Image Each Vid
						if (obj[5]!= null && flag==true) {
							byte [] data = null;
							data=(byte[]) obj[5];
							if(data!=null && data.length>0) {
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; 
								bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaImage\\output.jpg"));

								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+(String) obj[1]).add("PHONE NO :: "+(String) obj[2]).add("ADDRESS :: "+(String) obj[4]));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("gmpdf", "GURUMALA PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "PdfGenaretore";
		} 
		
		
		@GetMapping("/gurumalalistpdf")
		public String getGurumalaListPDFDocument(Model model) {

			try {
				LOG.info("Controller : Started to fetch list of Visitor process");
				LOG.info("Controller :Calling the Service to get all the Visitor object");
				List<Object[]> nsd = null; nsd = service.getAllGuruMalaList();

				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaPDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaPDF\\GuruMalaList.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(sNo);

						//get Image Each Vid
						Optional<GuruMalaDetails> opt = null;
						GuruMalaDetails details = null;
						opt = service.getViewEditByGurumalID((Integer)obj[0]);

						if (opt != null && flag==true) {
							details = opt.get();
							if(details!=null && details.getGmimagedata()!=null) {
								byte [] data = null;
								data=details.getGmimagedata();
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaImage\\output.jpg"));
								
								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\GuruMalaImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+details.getGmfullname()).add("PHONE NO :: "+details.getGmmobilenumber()).add("ADDRESS :: "+details.getGmpresentaddress()));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("gmpdf", "GURUMALA PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "PdfGenaretore";
			//return "GuruMalaInformationList";
		} 
		
		//-------------- Guru Mala info Ended ---------------------
		//------------ Started Event program Things---------------
		@GetMapping("/event")
		public String openEventProgramWelcomePage(Model model) {
			return "EventWelcomePage";
		}
		
		@GetMapping("/eventregister")
		public String openEventProgramRegisterPage(Model model) {
			return "EventRegisterPage";
		}
		
		@PostMapping("/evnetbookingregister")
		public String getEvenetBookingRegister(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute EventDetails ed, Model model, Principal p) {
			LOG.info("Controller : Started Event registration process");
			
			try {
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : event Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : event After IMAGE : " +data.length); 
				}//end
				
				if(data!=null && data.length>0)
					ed.setEpersonsimagedata(data);
				else 
					LOG.info("Image data Not found in : event registering");
				ed.setEprofilecode("EVENT");
				Calendar calendar = Calendar.getInstance();
				int month =calendar.get(Calendar.MONTH) +1;
				ed.setEdateofmonth(month+" "+calendar.get(Calendar.YEAR));
				EventDetails eds = null;
				eds = service.registerEventDetailsInformation(ed);
				if (eds!=null) {
					LOG.info("registering gurumala deatils Done");
					model.addAttribute("msg", "'" + eds.getEpersonnames().toUpperCase() +"' Event created Successfully'");
				} else {
					LOG.info("failed to register");
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			} catch (Exception e) {
				LOG.info("Controller : Got exception, Failed");
				e.printStackTrace();
			}
			return "EventRegisterPage";
		}
		
		@GetMapping("/eventlist")
		public String getEventDetailsList(Model model, Principal p) {
			try {
				LOG.info("Retriving all Gurumala deatils from  DB");
				List<Object[]> yd = null; yd = service.allEventDetailsInformation();
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno .. eid,edatetime,evenue,ename,epersonnames,epersonmobiles,epersonsimagedata
					//Logic for imges  in the list
					HashMap<String, byte[]> imgs=new HashMap<>();
					int sno=1;
					for (Object[] obj : yd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(sNo);
						imgs.put(Integer.toString(idno),(byte[]) obj[6]);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the Gurumala info");
					model.addAttribute("removemsg", " Gurumala profile list are not found..");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "EventListInformation";
		}
		
		@PostMapping("/eventsearchvalues")
		public String getEventDetailsSearchValues(@RequestParam String eventoption, Principal p, Model model) {
			LOG.info("Controller : Calling service with Open Event Search values");
	
			try {
				List<Object[]>  yd = null;
				yd = service.getOneOrAllEventDetailInformation(eventoption);
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : yd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(sNo);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
				} else {
					LOG.info("failed to retrive all the event info");
					model.addAttribute("removemsg", " Event profile list are not found..");
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			return "EventListInformation";
		}
		
		@GetMapping("/eventdelete")
		public String eventDeatilsdelete(@RequestParam Integer eid, Model model, Principal p) {
			try {
				LOG.info("Event Delete Operation Started..");
				service.getRemoveEventDetailsById(eid);
				model.addAttribute("removemsg", "EVENT profile Removed.");
				
				LOG.info("Retriving all Evcent deatils from  DB");
				List<Object[]> yd = null; yd = service.allEventDetailsInformation();
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					//Logic for imges  in the list
					HashMap<String, byte[]> imgs=new HashMap<>();

					int sno=1;
					for (Object[] obj : yd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(sNo);
						imgs.put(Integer.toString(idno),(byte[]) obj[6]);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the Gurumala info");
					model.addAttribute("removemsg", " Gurumala profile list are not found..");
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "EventListInformation";
		}
		
		@GetMapping("/eventviewedit")
		public String getEventviewedit(@RequestParam Integer eid, Model model, Principal p) {
			try {
				LOG.info("Event View and Edit info Started from controller..");
				Optional<EventDetails> opt = null;
				opt = service.getViewByEventID(eid);
				if (opt != null) {
					EventDetails evd = null;
					evd = opt.get();
					if(evd!=null) {
						model.addAttribute("edit", evd);
						if(evd!=null && evd.getEpersonsimagedata()!=null)
							model.addAttribute("imgUtil", new ImageUtil());
					}
				} else {
					model.addAttribute("msg", "Couldn't not found Event Details, Please Contact Adminstartor ");
				}
				LOG.info("Event View and Edit info Ended and Data posting to HTML..");
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption Guru Mala View and Edit info");
			}
			return "EventViewEditInfo";
		}
		
		@PostMapping("/eventvieweditoperation")
		public String getEventViewEditOpreation(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute EventDetails editform, Model model, Principal p) {
			try {
				LOG.info("event View and Edit Opreation info Started from controller..");
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : event Edit & view Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : event Edit & view After IMAGE : " +data.length); 
				}//end
				
				if(data!=null && data.length>0) 
					editform.setEpersonsimagedata(data);
				else
					LOG.info("Image data Not found in : Event Edit registering");
				editform.setEprofilecode("EVENT");
				Calendar calendar = Calendar.getInstance();
				editform.setEdateofmonth(calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.YEAR));
				String yd = null;
				yd = service.getUpdateEvent(editform);
				if (yd != null) {
					model.addAttribute("msg", "UPDATED Successfully.");
				} else {
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			
				Optional<EventDetails> opt = null;
				opt = service.getViewByEventID(editform.getEid());
				if (opt != null) {
					EventDetails evd = null;
					evd = opt.get();
					if(evd!=null) {
						model.addAttribute("edit", evd);
						if(evd!=null && evd.getEpersonsimagedata()!=null)
							model.addAttribute("imgUtil", new ImageUtil());
					}
				} else {
					model.addAttribute("msg", "Couldn't not found event Details, Please Contact Adminstartor ");
				}
				
				LOG.info("Edit View and Edit Operation Ended and Data posting to HTML..");
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption Event View and Edit Operation ");
			}
			return "EventViewEditInfo";
		}
		
				//PDF Creating Started ..
				@PostMapping("/eventpdfmonthlywise")
				public String getEventPDFMonthlyWise(@RequestParam String pdfdocmonthlywise, Model model, Principal p) {

					try {
						List<Object[]>  nsd = null;
						nsd = service.getEventPDFMonthlyWise(pdfdocmonthlywise);
				
						File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
						File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventPDF");  
						f2.mkdir(); f3.mkdir();
						
						File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventImage");  
						boolean flag=f1.mkdir();
						if (nsd!= null) {
							PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventPDF\\EventMonthWise.pdf");
							PdfDocument pdfdoc=new PdfDocument(writer);
							pdfdoc.addNewPage();
							Document document=new Document(pdfdoc);

							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno ..
							int sno=1;
							for (Object[] obj : nsd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);

								//get Image Each Vid
								if (obj[6]!= null && flag==true) {
									byte [] data = null;
									data=(byte[]) obj[6];
									if(data!=null && data.length>0) {
										ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
										BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
										ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventImage\\output.jpg"));

										ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventImage\\output.jpg");
										Image img = new Image(imageData);
										img.setHeight(450.0f);
										img.setWidth(500.0f);
										Table table = new Table(1);
										table.addCell(new Cell().add(img).add("Name : "+(String) obj[1]).add("PHONE NO :: "+(String) obj[2]).add("ADDRESS :: "+(String) obj[4]));
										document.add(table);
									}
								}
							}
							document.close();
							for (File subfile : f1.listFiles()) {
					            subfile.delete();
					        }
							f1.delete();
							
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("epdf", "Event PDF Downloaded.. ");
						}	
					}catch (Exception e) {
						e.printStackTrace();
					}
					//return "EventListInformation";
					return "PdfGenaretore";
				} 
							
		@GetMapping("/eventlistpdf")
		public String getEventListPDFDocument(Model model) {

			try {
				LOG.info("Controller :Calling the Service to get all the Event object");
				List<Object[]> nsd = null; nsd = service.allEventDetailsInformation();

				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventPDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventPDF\\EventList.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(sNo);
						
						//get Image Each Vid
						Optional<EventDetails> opt = null;
						EventDetails details = null;
						opt = service.getViewByEventID((Integer)obj[0]);

						if (opt != null && flag==true) {
							details = opt.get();
							if(details!=null && details.getEpersonsimagedata()!=null) {
								byte [] data = null;
								data=details.getEpersonsimagedata();
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventImage\\output.jpg"));
								
								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\EventImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+details.getEpersonnames()).add("PHONE NO :: "+details.getEpersonmobiles()).add("ADDRESS :: "+details.getEpersonaddress()));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("epdf", "EVENT PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			//return "EventListInformation";
			return "PdfGenaretore";
		} 
		
		//------------ Ended Event program Things---------------
		//-----------------STarted Lingabisheka object----------
				@GetMapping("/linga")
				public String openLingaProgramWelcomePage(Model model) {
					LOG.info("Controller : Calling to Open linga Details Welcome page");
					return "LingaWelcomePage";
				}
				
				@GetMapping("/lingaregister")
				public String openLingaProgramRegisterPage(Model model) {
					LOG.info("Controller : Calling to Open Linga Program Page");
					return "LingaRegisterPage";
				}
				
				@PostMapping("/lingabookingregister")
				public String getLingaBookingRegister(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute LingaDetails ed, Model model, Principal p) {
					try {
						LOG.info("Controller : Started lingabhisheka registration process");
						byte[] data = null;
						
						//New Logic to resize the image
						byte[] bytes1 = null ;
						bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
						if(bytes1!=null && bytes1.length>0) {

							LOG.info("Controller : linga Before IMAGE : " +bytes1.length); 
							BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(scaledImg, "jpg", baos); 
							data = baos.toByteArray();
							LOG.info("Controller : linga After IMAGE : " +data.length); 
						}//end
						
						if(data!=null && data.length>0)
							ed.setLpersonimagedata(data);
						else 
							LOG.info("Image data Not found in : event registering");
						ed.setLprofilecode("LINGABHISHEKA");
						Calendar calendar = Calendar.getInstance();
						int month =calendar.get(Calendar.MONTH) +1;
						ed.setLdateofmonth(month+" "+calendar.get(Calendar.YEAR));
						LingaDetails eds = null;
						eds = service.registerLingaDetailsInformation(ed);
						if (eds!=null) {
							LOG.info("registering Linagbishake deatils Done");
							model.addAttribute("msg", "'" + eds.getLfullname().toUpperCase() +"' Event created Successfully'");
						} else {
							LOG.info("failed to register");
							model.addAttribute("msg", "Registration Filed.. Please try again ");
						}
					} catch (Exception e) {
						LOG.info("Controller : Got exception, Failed to set family and aadhar image to object");
						e.printStackTrace();
					}
					return "LingaRegisterPage";
				}
			
				@GetMapping("/lingalist")
				public String getLingaDetailsList(Model model, Principal p) {
					try {
						LOG.info("Retriving all Lingabishake deatils from  DB");
						List<Object[]> yd = null; yd = service.allLingaDetailsInformation();
						if (yd!= null) {
							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno .. lid,ldatetime,lfullname,lphonenumber,loccupation,laddress,lpersonimagedata
							//Logic for imges  in the list
							HashMap<String, byte[]> imgs=new HashMap<>();
							int sno=1;
							for (Object[] obj : yd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);
								imgs.put(Integer.toString(idno),(byte[]) obj[6]);
							}
							//End handling Sno ..	
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("imgUtil", new ImageUtil());
							model.addAttribute("imgs",imgs);
						} else {
							LOG.info("failed to retrive all the Gurumala info");
							model.addAttribute("removemsg", " Gurumala profile list are not found..");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return "LingaListInformation";
				}
					
				@PostMapping("/lingasearchvalues")
				public String getLingaDetailsSearchValues(@RequestParam String lingaoption, Principal p, Model model) {
					
					try {
						List<Object[]>  yd = null;
						yd = service.getOneOrAllLingaDetailInformation(lingaoption);
						if (yd!= null) {
							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno .. lid,ldatetime,lfullname,lphonenumber,loccupation,laddress
							int sno=1;
							for (Object[] obj : yd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);
							}
							//End handling Sno ..	
							model.addAttribute("vdlist", vistlist);
						} else {
							LOG.info("failed to retrive all the Linga info");
							model.addAttribute("removemsg", " linga profile list are not found..");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return "LingaListInformation";
				}
				
				@GetMapping("/lingadelete")
				public String lingaDeatilsdelete(@RequestParam Integer lid, Model model, Principal p) {
					try {

						LOG.info("Linga Delete Operation Started..");
						service.getRemoveLingaDetailsById(lid);
						model.addAttribute("removemsg", "LINGABISHAKE profile Removed.");
						
						LOG.info("Retriving all Linga deatils from  DB");
						List<Object[]> yd = null; yd = service.allLingaDetailsInformation();
						if (yd!= null) {
							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno ..
							//Logic for imges  in the list
							HashMap<String, byte[]> imgs=new HashMap<>();

							int sno=1;
							for (Object[] obj : yd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);
								imgs.put(Integer.toString(idno),(byte[]) obj[6]);
							}
							//End handling Sno ..	
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("imgUtil", new ImageUtil());
							model.addAttribute("imgs",imgs);
						} else {
							LOG.info("failed to retrive all the Linga info");
							model.addAttribute("removemsg", " linga profile list are not found..");
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
						return "LingaListInformation";
				}
				
				
				@GetMapping("/lingaviewedit")
				public String getLingaviewedit(@RequestParam Integer lid, Model model, Principal p) {
					try {
						LOG.info("Linga View and Edit info Started from controller..");
						Optional<LingaDetails> opt = null;
						opt = service.getViewByLingaID(lid);
						if (opt != null) {
							LingaDetails evd = null;
							evd = opt.get();
							if(evd!=null) {
								model.addAttribute("edit", evd);
								if(evd!=null && evd.getLpersonimagedata()!=null)
									model.addAttribute("imgUtil", new ImageUtil());
							}
						} else {
							model.addAttribute("msg", "Couldn't not found Linga Details, Please Contact Adminstartor ");
						}
						LOG.info("Linga View and Edit info Ended and Data posting to HTML..");
					} catch (Exception e) {
						e.printStackTrace();
						LOG.info("Controller Error : got execption Guru Mala View and Edit info");
					}
					return "LingaEditFrom";
				}
				
				@PostMapping("/lingavieweditoperation")
				public String getLingaViewEditOpreation(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute LingaDetails editform, Model model, Principal p) {
					try {
						LOG.info("Linga View and Edit Opreation info Started from controller..");
						byte[] data = null;
						
						//New Logic to resize the image
						byte[] bytes1 = null ;
						bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
						if(bytes1!=null && bytes1.length>0) {
							LOG.info("Controller : linga edit and view Before IMAGE : " +bytes1.length); 
							BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(scaledImg, "jpg", baos); 
							data = baos.toByteArray();
							LOG.info("Controller : linga edit and view After IMAGE : " +data.length); 
						}//end
						
						if(data!=null && data.length>0) 
							editform.setLpersonimagedata(data);
						else
							LOG.info("Image data Not found in : Linga Edit registering");
						editform.setLprofilecode("LINGABHISHEKA");
						Calendar calendar = Calendar.getInstance();
						editform.setLdateofmonth(calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.YEAR));
						String yd = null;
						yd = service.getUpdateLingabishake(editform);
						if (yd != null) {
							model.addAttribute("msg", "UPDATED Successfully.");
						} else {
							model.addAttribute("msg", "Registration Filed.. Please try again ");
						}
					
						Optional<LingaDetails> opt = null;
						opt = service.getViewByLingaID(editform.getLid());
						if (opt != null) {
							LingaDetails evd = null;
							evd = opt.get();
							if(evd!=null) {
								model.addAttribute("edit", evd);
								if(evd!=null && evd.getLpersonimagedata()!=null)
									model.addAttribute("imgUtil", new ImageUtil());
							}
						} else {
							model.addAttribute("msg", "Couldn't not found Linga Details, Please Contact Adminstartor ");
						}
						
						LOG.info("Linga View and Edit Operation Ended and Data posting to HTML..");
					} catch (Exception e) {
						e.printStackTrace();
						LOG.info("Controller Error : got execption Linga View and Edit Operation ");
					}
					return "LingaEditFrom";
				}
				
				//PDF Creating Started ..
				@PostMapping("/lingapdfmonthlywise")
				public String getLingaPDFMonthlyWise(@RequestParam String pdfdocmonthlywise, Model model, Principal p) {

					try {
						List<Object[]>  nsd = null;
						nsd = service.getLingaPDFMonthlyWise(pdfdocmonthlywise);
				
						File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
						File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingabishakePDF");  
						f2.mkdir(); f3.mkdir();
						
						File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingaImage");  
						boolean flag=f1.mkdir();
						if (nsd!= null) {
							PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingabishakePDF\\LingabishakeMonthWise.pdf");
							PdfDocument pdfdoc=new PdfDocument(writer);
							pdfdoc.addNewPage();
							Document document=new Document(pdfdoc);

							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno ..
							int sno=1;
							for (Object[] obj : nsd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);

								//get Image Each Vid
								if (obj[6]!= null && flag==true) {
									byte [] data = null;
									data=(byte[]) obj[6];
									if(data!=null && data.length>0) {
										ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
										BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
										ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingaImage\\output.jpg"));

										ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingaImage\\output.jpg");
										Image img = new Image(imageData);
										img.setHeight(450.0f);
										img.setWidth(500.0f);
										Table table = new Table(1);
										table.addCell(new Cell().add(img).add("Name : "+(String) obj[2]).add("PHONE NO :: "+(String) obj[3]).add("ADDRESS :: "+(String) obj[4]));
										document.add(table);
									}
								}
							}
							document.close();
							for (File subfile : f1.listFiles()) {
					            subfile.delete();
					        }
							f1.delete();
							
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("lpdf", "Lingabishake PDF Downloaded.. ");
						}	
					}catch (Exception e) {
						e.printStackTrace();
					}
					//return "LingaListInformation";
					return "PdfGenaretore";
				} 
							
		@GetMapping("/lingalistpdf")
		public String getLingaListPDFDocument(Model model) {

			try {
				LOG.info("Controller :Calling the Service to get all the Event object");
				List<Object[]> nsd = null; nsd = service.allLingaDetailsInformation();

				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingabishakePDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingaImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingabishakePDF\\LingabishakeList.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(sNo);
						
						//get Image Each Vid
						Optional<LingaDetails> opt = null;
						LingaDetails details = null;
						opt = service.getViewByLingaID((Integer)obj[0]);

						if (opt != null && flag==true) {
							details = opt.get();
							if(details!=null && details.getLpersonimagedata()!=null) {
								byte [] data = null;
								data=details.getLpersonimagedata();
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingaImage\\output.jpg"));
								
								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\LingaImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+details.getLfullname()).add("PHONE NO :: "+details.getLphonenumber()).add("ADDRESS :: "+details.getLaddress()));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("lpdf", "LINGABISHAKE PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			//return "LingaListInformation";
			return "PdfGenaretore";
		} 
		
		//------------ Ended Linga program Things---------------
		//-------------- Donation info Started -------------------
				@GetMapping("/donation")
				public String showDonationHomePage(Model model, Principal p) {
					return "DonationWelcomePage";
				}
				
				@GetMapping("/donationregistrationpage")
				public String showDonationRegistrationPage(Model model, Principal p) {
					return "DonationRegistrationForm";
				}
				
				@PostMapping("/donationregistrationdetails")
				public String donationRegistringDetails(@RequestParam("imageFile") MultipartFile imageFile,
						@ModelAttribute DonationDetails ed, Model model, Principal p) {
					LOG.info(" Conntroller : started processing registering DONATION deatils to DB");
					try {
						
						LOG.info("Controller : Started Donation registration process");
						byte[] data = null;
						
						//New Logic to resize the image
						byte[] bytes1 = null ;
						bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
						if(bytes1!=null && bytes1.length>0) {

							LOG.info("Controller : donation Before IMAGE : " +bytes1.length); 
							BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(scaledImg, "jpg", baos); 
							data = baos.toByteArray();
							LOG.info("Controller : donation After IMAGE : " +data.length); 
						}//end
						
						if(data!=null && data.length>0)
							ed.setDimagedata(data);
						else 
							LOG.info("Image data Not found in : event registering");
						ed.setDprofilecode("DONATION");
						Calendar calendar = Calendar.getInstance();
						int month =calendar.get(Calendar.MONTH) +1;
						ed.setDdateofmonth(month+" "+calendar.get(Calendar.YEAR));
						DonationDetails eds = null;
						eds = service.registerDonationInformation(ed);
						if (eds!=null) {
							LOG.info("registering Linagbishake deatils Done");
							model.addAttribute("msg", "'" + eds.getDfullname().toUpperCase() +"' Donation created Successfully'");
						} else {
							LOG.info("failed to register");
							model.addAttribute("msg", "Registration Filed.. Please try again ");
						}
					} catch (Exception e) {
						e.printStackTrace();
						LOG.info(" Controller Error : got execption while inserting the DONATION deatils to DB");
					}
					return "DonationRegistrationForm";
				}
				
				@GetMapping("/donationlist")
				public String getDonationList(Model model, Principal p) {
					try {
						LOG.info("Retriving all Donation deatils from  DB");
						List<Object[]> yd = null; yd = service.getAllDonationList();
						if (yd!= null) {
							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno .. did,ditems,dfullname,dmobilenumber,doccupation,dpresentaddress,dimagedata
							//Logic for imges  in the list
							HashMap<String, byte[]> imgs=new HashMap<>();
							int sno=1;
							for (Object[] obj : yd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);
								imgs.put(Integer.toString(idno),(byte[]) obj[6]);
							}
							//End handling Sno ..	
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("imgUtil", new ImageUtil());
							model.addAttribute("imgs",imgs);
						} else {
							LOG.info("failed to retrive all the Donation info");
							model.addAttribute("removemsg", " Donation profile list are not found..");
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					return "DonationInformationList";
				}
					
				@PostMapping("/donationsearchvalues")
				public String getDonationDetailsSearchValues(@RequestParam String donationoption, Principal p, Model model) {
					
					try {
						List<Object[]>  yd = null;
						yd = service.getOneOrAllDonationDetailInformation(donationoption);
						if (yd!= null) {
							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno .. did,ditems,dfullname,dmobilenumber,doccupation,dpresentaddress
							int sno=1;
							for (Object[] obj : yd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);
							}
							//End handling Sno ..	
							model.addAttribute("vdlist", vistlist);
						} else {
							LOG.info("failed to retrive all the Linga info");
							model.addAttribute("removemsg", " linga profile list are not found..");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return "DonationInformationList";
				}
				
				@GetMapping("/donationdelete")
				public String getDonationdelete(@RequestParam Integer did, Model model, Principal p) {
					try {

						LOG.info("Linga Delete Operation Started..");
						service.getRemoveDonationDetailsById(did);
						model.addAttribute("removemsg", "DONATION profile Removed.");
						
						LOG.info("Retriving all Linga deatils from  DB");
						List<Object[]> yd = null; yd = service.getAllDonationList();
						if (yd!= null) {
							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno .. did,ditems,dfullname,dmobilenumber,doccupation,dpresentaddress
							//Logic for imges  in the list
							HashMap<String, byte[]> imgs=new HashMap<>();

							int sno=1;
							for (Object[] obj : yd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);
								imgs.put(Integer.toString(idno),(byte[]) obj[6]);
							}
							//End handling Sno ..	
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("imgUtil", new ImageUtil());
							model.addAttribute("imgs",imgs);
						} else {
							LOG.info("failed to retrive all the donation info");
							model.addAttribute("removemsg", " Donation profile list are not found..");
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
						return "DonationInformationList";
				}
				
				
				@GetMapping("/donationviewedit")
				public String getDonationviewedit(@RequestParam Integer did, Model model, Principal p) {
					try {
						LOG.info("Donation View and Edit info Started from controller..");
						Optional<DonationDetails> opt = null;
						opt = service.getViewByDonationID(did);
						if (opt != null) {
							DonationDetails evd = null;
							evd = opt.get();
							if(evd!=null) {
								model.addAttribute("edit", evd);
								if(evd!=null && evd.getDimagedata()!=null)
									model.addAttribute("imgUtil", new ImageUtil());
							}
						} else {
							model.addAttribute("msg", "Couldn't not found Donation Details, Please Contact Adminstartor ");
						}
						LOG.info("Donation View and Edit info Ended and Data posting to HTML..");
					} catch (Exception e) {
						e.printStackTrace();
						LOG.info("Controller Error : got execption Donation View and Edit info");
					}
					return "DonationViewEditInfo";
				}
				
				@PostMapping("/donationvieweditoperation")
				public String getDonationViewEditOpreation(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute DonationDetails editform, Model model, Principal p) {
					try {
						LOG.info("Donation View and Edit Opreation info Started from controller..");
						byte[] data = null;
						
						//New Logic to resize the image
						byte[] bytes1 = null ;
						bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
						if(bytes1!=null && bytes1.length>0) {
							LOG.info("Controller : donation edit & view Before IMAGE : " +bytes1.length); 
							BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(scaledImg, "jpg", baos); 
							data = baos.toByteArray();
							LOG.info("Controller : donation edit & view After IMAGE : " +data.length); 
						}
						//end
						
						if(data!=null && data.length>0) 
							editform.setDimagedata(data);
						else
							LOG.info("Image data Not found in : Donation Edit registering");
						editform.setDprofilecode("DONATION");
						Calendar calendar = Calendar.getInstance();
						editform.setDdateofmonth(calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.YEAR));
						String yd = null;
						yd = service.getUpdateDonation(editform);
						if (yd != null) {
							model.addAttribute("msg", "UPDATED Successfully.");
						} else {
							model.addAttribute("msg", "Registration Filed.. Please try again ");
						}
					
						Optional<DonationDetails> opt = null;
						opt = service.getViewByDonationID(editform.getDid());
						if (opt != null) {
							DonationDetails evd = null;
							evd = opt.get();
							if(evd!=null) {
								model.addAttribute("edit", evd);
								if(evd!=null && evd.getDimagedata()!=null)
									model.addAttribute("imgUtil", new ImageUtil());
							}
						} else {
							model.addAttribute("msg", "Couldn't not found Donation Details, Please Contact Adminstartor ");
						}
						
						LOG.info("Donation View and Edit Operation Ended and Data posting to HTML..");
					} catch (Exception e) {
						e.printStackTrace();
						LOG.info("Controller Error : got execption Donation View and Edit Operation ");
					}
					return "DonationViewEditInfo";
				}
				
				//PDF Creating Started ..
				@PostMapping("/donationapdfmonthlywise")
				public String getDonationPDFMonthlyWise(@RequestParam String pdfdocmonthlywise, Model model, Principal p) {

					try {
						List<Object[]>  nsd = null;
						nsd = service.getDonationPDFMonthlyWise(pdfdocmonthlywise);
				
						File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
						File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationPDF");  
						f2.mkdir(); f3.mkdir();
						
						File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationImage");  
						boolean flag=f1.mkdir();
						if (nsd!= null) {
							PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationPDF\\DonationMonthWise.pdf");
							PdfDocument pdfdoc=new PdfDocument(writer);
							pdfdoc.addNewPage();
							Document document=new Document(pdfdoc);

							ArrayList<String[]> vistlist=new ArrayList<>();
							//handling Sno ..
							int sno=1;
							for (Object[] obj : nsd) {
								String[] sNo=null;
								int idno = (Integer)obj[0];
								sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
								sno++;
								vistlist.add(sNo);

								//get Image Each Vid
								if (obj[6]!= null && flag==true) {
									byte [] data = null;
									data=(byte[]) obj[6];
									if(data!=null && data.length>0) {
										ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
										BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
										ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationImage\\output.jpg"));

										ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationImage\\output.jpg");
										Image img = new Image(imageData);
										img.setHeight(450.0f);
										img.setWidth(500.0f);
										Table table = new Table(1);
										table.addCell(new Cell().add(img).add("Name : "+(String) obj[2]).add("PHONE NO :: "+(String) obj[3]).add("ADDRESS :: "+(String) obj[4]));
										document.add(table);
									}
								}
							}
							document.close();
							for (File subfile : f1.listFiles()) {
					            subfile.delete();
					        }
							f1.delete();
							
							model.addAttribute("vdlist", vistlist);
							model.addAttribute("dpdf", "Donation PDF Downloaded.. ");
						}	
					}catch (Exception e) {
						e.printStackTrace();
					}
					//return "DonationInformationList";
					return "PdfGenaretore";
				} 
							
		@GetMapping("/donationlistpdf")
		public String getDonationListPDFDocument(Model model) {

			try {
				LOG.info("Controller :Calling the Service to get all the Event object");
				List<Object[]> nsd = null; nsd = service.getAllDonationList();

				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationPDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationPDF\\DonationList.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(sNo);
						
						//get Image Each Vid
						Optional<DonationDetails> opt = null;
						DonationDetails details = null;
						opt = service.getViewByDonationID((Integer)obj[0]);

						if (opt != null && flag==true) {
							details = opt.get();
							if(details!=null && details.getDimagedata()!=null) {
								byte [] data = null;
								data=details.getDimagedata();
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationImage\\output.jpg"));
								
								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\DonationImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+details.getDfullname()).add("PHONE NO :: "+details.getDmobilenumber()).add("ADDRESS :: "+details.getDpresentaddress()));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("dpdf", "DONATION PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "PdfGenaretore";//return "DonationInformationList";
		} 
		//------------ Ended Donation program Things---------------
		//------------ Started Visitor code---------------
		@GetMapping("/visitor")
		public String getVisitorPage(Principal p, Model model) {
			LOG.info("Showing visitor Welcome page");
			return "DisplayVisitorWelcomePage";
		}

		@GetMapping("/visitorsregisterpage")
		public String visitorsregisterpage(Principal p, Model model) {
			LOG.info("Showing visitors register page");
			return "DisplayVisitorsRegisterForm";
		}

		@PostMapping("/visitorregister")
		public String VisitorRegister(@RequestParam("visitorimage") MultipartFile visitorimage,
				@ModelAttribute VisitorDetails stu, Model model, Principal p) {

			LOG.info("Controller : Started Visitor registration process");
			try {
				LOG.info("Controller : Started to setting the family and aadhar image to object");
				byte[] familydata = null;

				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(visitorimage!=null ? visitorimage.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {
					LOG.info("Controller : Visitore Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					familydata = baos.toByteArray();
					LOG.info("Controller : Visitore After IMAGE : " +familydata.length); 
					//end
				}
				
				if(familydata!=null && familydata.length>0) {
					stu.setVimagedata(familydata);
				}  else {
					LOG.info("Image data Not found in : Visitor Registering");
				}
				stu.setVprofilecode("VISITOR");

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // new SimpleDateFormat("E,
				stu.setVvistedDateTime(formatter.format(new Date()));

				Calendar calendar = Calendar.getInstance();
				int month =calendar.get(Calendar.MONTH) +1;
				stu.setVdateofmonth(month+" "+calendar.get(Calendar.YEAR));

				VisitorDetails vd = null;
				LOG.info("Controller :Calling the Service to register the Visitor object");
				vd = service.registerVisitorInformation(stu);

				if (vd != null) {
					LOG.info("Controller :  Visitor registration Successfully to Completed and posting to HTML ");
					//model.addAttribute("msg","'" + vd.getVfname().toUpperCase() + "' Profile has been created Successfully AS VISITOR.");
					model.addAttribute("msg","'Profile created Successfully'.");
					model.addAttribute("visitorid",vd.getVid());
					model.addAttribute("visitorname",vd.getVfname());
				} else {
					LOG.info("Controller : Got exception, Failed to Visitor registration process");
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			} catch (Exception e) {
				LOG.info("Controller : Got exception, Failed to set family and aadhar image to object");
				e.printStackTrace();
			}
			return "DisplayVisitorsRegisterForm";
		}

		
		@GetMapping("/allvisitordetails")
		public String AllVisitorDetails(Model model, Principal p) {
			try{
				LOG.info("Controller :Calling the Service to get all the Visitor object");
				List<Object[]> nsd = null; 
				nsd = service.allVisitorInformation();
				
				if (nsd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//Logic for imges  in the list
					HashMap<String, byte[]> imgs=new HashMap<>();
					int sno=1;
					for (Object[] obj : nsd) {	//handling Sno . vid,vfname,vphonenumber,voccupation,vdistcity,vimagedata
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(visiNo);
						imgs.put(Integer.toString(i),(byte[]) obj[5]);
					}
					//End handling Sno ..
				LOG.info("Controller : Successfully to retirve all the Visitor people and posting to HTML ");
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("Controller : Got exception, Failed  to retrive all the Visitor list info");
					model.addAttribute("usernotfound", " Visitor profile's list are not found.. Please try again or Please Contact Adminstartor ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "VisitorListInformation";
		}
		
		@PostMapping("/onevisitorsearch")
		public String oneVisitorSearch(@RequestParam String visitoroption, Model model, Principal p) {

			try {
				List<Object[]>  yd = null;
				yd = service.singleVisitorInformation(visitoroption);
				if (yd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno .. vid,v.vfname,v.vphonenumber,v.voccupation,v.vdistcity
					int sno=1;
					for (Object[] obj : yd) {
						String[] sNo=null;
						int idno = (Integer)obj[0];
						sNo = new String[] {Integer.toString(idno),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(sNo);
					}
					//End handling Sno ..	
					model.addAttribute("vdlist", vistlist);
				} else {
					LOG.info("failed to retrive all the Linga info");
					model.addAttribute("removemsg", " linga profile list are not found..");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "VisitorListInformation";	
		}
	
		@GetMapping("/deletvisitor")
		public String getVisitordelete(@RequestParam Integer vid, Model model, Principal p) {
			try {

				LOG.info("Visitor Delete Operation Started..");
				service.getRemoveVisitorById(vid);
				model.addAttribute("removemsg", "Visitor profile Removed.");
				
				List<Object[]> nsd = null; nsd = service.allVisitorInformation();
				if (nsd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//Logic for imges  in the list
					HashMap<String, byte[]> imgs=new HashMap<>();
					int sno=1;
					for (Object[] obj : nsd) {	//handling Sno . vid,vfname,vphonenumber,voccupation,vdistcity,vimagedata
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(visiNo);
						imgs.put(Integer.toString(i),(byte[]) obj[5]);
					}
					//End handling Sno ..
				LOG.info("Controller : Successfully to retirve all the Visitor people and posting to HTML ");
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the donation info");
					model.addAttribute("removemsg", " Donation profile list are not found..");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
				return "VisitorListInformation";
		}
		
		@GetMapping("/vieweditvisitor")
		public String getVisitorviewedit(@RequestParam Integer vid, Model model, Principal p) {
			try {
				LOG.info("Visitor View and Edit info Started from controller..");
				Optional<VisitorDetails> opt = null;
				opt = service.getViewByVisitorID(vid);
				if (opt != null) {
					VisitorDetails evd = null;
					evd = opt.get();
					if(evd!=null) {
						model.addAttribute("edit", evd);
						if(evd!=null && evd.getVimagedata()!=null)
							model.addAttribute("imgUtil", new ImageUtil());
					}
				} else {
					model.addAttribute("msg", "Couldn't not found VIsitor Details, Please Contact Adminstartor ");
				}
				LOG.info("Visitor View and Edit info Ended and Data posting to HTML..");
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption VIsitor View and Edit info");
			}
			return "VisitorEditFrom";
		}
		
		@PostMapping("/visitorvieweditoperation")
		public String getVisitorViewEditOpreation(@RequestParam("imageFile") MultipartFile imageFile, @ModelAttribute VisitorDetails editform, Model model, Principal p) {
			try {
				LOG.info("Visitor View and Edit Opreation info Started from controller..");
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {
					LOG.info("Controller : Visitore Edit & view Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : Visitore Edit & view After IMAGE : " +data.length); 
				}
				//end
				
				if(data!=null && data.length>0) 
					editform.setVimagedata(data);
				else
					LOG.info("Image data Not found in : Donation Edit registering");
				editform.setVprofilecode("VISITOR");
				Calendar calendar = Calendar.getInstance();
				editform.setVdateofmonth(calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.YEAR));
				String yd = null;
				yd = service.getUpdateVisitor(editform);
				if (yd != null) {
					model.addAttribute("msg", "UPDATED Successfully.");
				} else {
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			
				Optional<VisitorDetails> opt = null;
				opt = service.getViewByVisitorID(editform.getVid());
				if (opt != null) {
					VisitorDetails evd = null;
					evd = opt.get();
					if(evd!=null) {
						model.addAttribute("edit", evd);
						if(evd!=null && evd.getVimagedata()!=null)
							model.addAttribute("imgUtil", new ImageUtil());
					}
				} else {
					model.addAttribute("msg", "Couldn't not found VIsitor Details, Please Contact Adminstartor ");
				}
				
				LOG.info("VIsitor View and Edit Operation Ended and Data posting to HTML..");
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Controller Error : got execption VIsitor View and Edit Operation ");
			}
			return "VisitorEditFrom";
		}
		//PDF Creating Started ..
		@PostMapping("/visitorpdfmonthlywise")
		public String getVisitorPDFMonthlyWise(@RequestParam String pdfdocmonthlywise, Model model, Principal p) {

			try {
				List<Object[]>  nsd = null;
				nsd = service.getVistiorPDFMonthlyWise(pdfdocmonthlywise);
		
				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorPDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorPDF\\VisitorMonthWise.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(visiNo);

						//get Image Each Vid
						if (obj[5]!= null && flag==true) {
							byte [] data = null;
							data=(byte[]) obj[5];
							if(data!=null && data.length>0) {
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorImage\\output.jpg"));

								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+(String) obj[1]).add("PHONE NO :: "+(String) obj[2]).add("ADDRESS :: "+(String) obj[3]));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("vpdf", "Visitor PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "PdfGenaretore"; //return "VisitorListInformation";
		} 
					
		@GetMapping("/visitorlistpdf")
		public String getVisitorListPDFDocument(Model model) {

			try {
				LOG.info("Controller :Calling the Service to get all the Event object");
				List<Object[]> nsd = null; nsd = service.allVisitorInformation();

				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorPDF");  
				f2.mkdir(); f3.mkdir();

				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorPDF\\VisitoreList.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno ..
					int sno=1;
					for (Object[] obj : nsd) {
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4]};
						sno++;
						vistlist.add(visiNo);

						//get Image Each Vid
						Optional<VisitorDetails> opt = null;
						VisitorDetails details = null;
						opt = service.getViewByVisitorID((Integer)obj[0]);

						if (opt != null && flag==true) {
							details = opt.get();
							if(details!=null && details.getVimagedata()!=null) {
								byte [] data = null;
								data=details.getVimagedata();
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorImage\\output.jpg"));

								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\VisitorImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+details.getVfname()).add("PHONE NO :: "+details.getVphonenumber()).add("ADDRESS :: "+details.getVdistcity()));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
						subfile.delete();
					}
					f1.delete();

					model.addAttribute("vdlist", vistlist);
					model.addAttribute("vpdf", "VISITORE PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "PdfGenaretore";
			//return "VisitorListInformation";
		} 
		//------------ Ended Vistore code---------------
		
		// -------------- YOGA Details starts -------------------
		@GetMapping("/yoga")
		public String showYogaHomePage(Model model, Principal p) {
			LOG.info("Showing Yoga Home page");
			return "DisplayYogaHomePage";
		}

		@GetMapping("/yogaregistrationpage")
		public String showYogaRegistrationPage(Model model) {
			LOG.info("Showing Yoga Home page before : "+ fp);
			fp=null;
			model.addAttribute("fp",fp);
			LOG.info("Showing Yoga Home page after : "+ fp);
			return "DisplayYogaRegistrationForm";
		}
		
		@PostMapping("/yogaregistrationdetails")		//2021-10-14T15:01
		public String yogaRegistringDetails(@RequestParam("imageFile") MultipartFile imageFile,	
				@ModelAttribute YogaDetails yoga, Model model, Principal p) {

			LOG.info(" Conntroller : started processing registering YOGA deatils to DB");
			try {
				byte[] familydata = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : yoga Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					familydata = baos.toByteArray();
					LOG.info("Controller : yoga After IMAGE : " +familydata.length); 
				}//end
				
				if(familydata!=null && familydata.length>0) {
					yoga.setYimagedata(familydata);
				}  else {
					LOG.info("Image data Not found in : Yoga Registering");
				}
				yoga.setYprofilecode("YOGA");
				yoga.setYattendeesign(yoga.getYfullname()!=null ?yoga.getYfullname() : "");
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // new SimpleDateFormat("E,
				Calendar calendar = Calendar.getInstance();
				int month =calendar.get(Calendar.MONTH) +1;
				yoga.setYdateofmonth(month+" "+calendar.get(Calendar.YEAR));
				
				YogaDetails yd = null;
				LOG.info("Controller :Calling the Service to register the Visitor object before : "+fp);
				yd = service.registerYogaInformation(yoga);
				fp=null;
				if (yd != null) {
					model.addAttribute("fp",fp);
					model.addAttribute("msg", "'Profile created Successfully :'");
					LOG.info("registering yoga deatils Done after :" +fp);
				} else {
					LOG.info("failed to register");
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
				return "DisplayYogaRegistrationForm";
		}

		@GetMapping("/listofyogapeople")
		public String AllYogaPeopleList(Model model, Principal p) {
			try{
				LOG.info("Controller :Calling the Service to get all the Yoga object");
				List<Object[]> nsd = null; 
				nsd = service.allYogaInformation();
				
				if (nsd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno . yid,yidcard,ybatch,yfullname,ymobilenumber,ypresentaddress
					int sno=1;
					HashMap<String, byte[]> imgs=new HashMap<>();
					for (Object[] obj : nsd) {
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(visiNo);
						imgs.put(Integer.toString(i),(byte[]) obj[6]);
					}
					//End handling Sno ..
				LOG.info("Controller : Successfully to retirve all the Yoga people and posting to HTML ");
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the yoga info");
					model.addAttribute("usernotfound", " Yoga profile list are not found.. Please try again or Please Contact Adminstartor ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "AllYogaInformationList";
		}
		

		@GetMapping("/deleteyoga")
		public String deleteYogaParticipent(@RequestParam Integer yid, Model model, Principal p) {
			try {

				LOG.info("Yoga Delete Operation Started..");
				service.getRemoveYogaById(yid);
				model.addAttribute("removemsg", "YOGA profile Removed.");
				
				LOG.info("Controller :Calling the Service to get all the Yoga object");
				List<Object[]> nsd = null; 
				nsd = service.allYogaInformation();
				
				if (nsd!= null) {
					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno . yid,yidcard,ybatch,yfullname,ymobilenumber,ypresentaddress
					int sno=1;
					HashMap<String, byte[]> imgs=new HashMap<>();
					for (Object[] obj : nsd) {
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(visiNo);
						imgs.put(Integer.toString(i),(byte[]) obj[6]);
					}
					//End handling Sno ..
				LOG.info("Controller : Successfully to retirve all the Yoga people and posting to HTML ");
					model.addAttribute("vdlist", vistlist);
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("imgs",imgs);
				} else {
					LOG.info("failed to retrive all the yoga info");
					model.addAttribute("usernotfound", " Yoga profile list are not found.. Please try again or Please Contact Adminstartor ");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
				return "AllYogaInformationList";
		}
		
		@PostMapping("/yogaSearchByBatchAndIds")
		public String getYogaSearchByBatchAndIds(@RequestParam String yogasearchbybatch,@RequestParam String yogasearchbyids, Model model, Principal p) {
			try {
			LOG.info("Retriving yoga Search By Batch And Ids deatils from DB strated : yoga id : "+yogasearchbyids+" batch : "+yogasearchbybatch);
			
			List<Object[]> yd = null; 
			yd = service.yogaSearchByBatchAndIds(yogasearchbyids, yogasearchbybatch);
			
			if (yd!= null) {
				ArrayList<String[]> vistlist=new ArrayList<>();
				//handling Sno . yid,yidcard,ybatch,yfullname,ymobilenumber,ypresentaddress
				int sno=1;
				HashMap<String, byte[]> imgs=new HashMap<>();
				for (Object[] obj : yd) {
					String[] visiNo=null;
					int i = (Integer)obj[0];
					visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
					sno++;
					vistlist.add(visiNo);
					imgs.put(Integer.toString(i),(byte[]) obj[6]);
				}
				//End handling Sno ..
			LOG.info("Controller : Successfully to retirve all the Yoga people and posting to HTML ");
				model.addAttribute("vdlist", vistlist);
				model.addAttribute("imgUtil", new ImageUtil());
				model.addAttribute("imgs",imgs);
			} else {
				LOG.info("failed to retrive all the yoga info");
				model.addAttribute("usernotfound", " Yoga profile list are not found.. Please try again or Please Contact Adminstartor ");
			}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
			return "AllYogaInformationList";
	}
		
		@GetMapping("/edityoga")
		public String getYogaEditPage(@RequestParam Integer yid, Model model, Principal p) {
			LOG.info("Get request Edit page  for Yoga from controller");
		 Optional<YogaDetails> opt = null;
			opt = service.getViewByYogaID(yid);
			if (opt != null) {
				YogaDetails evd = null;
				evd = opt.get();
				model.addAttribute("edit", evd);
				model.addAttribute("imgUtil", new ImageUtil());
			} else {
				model.addAttribute("noeditpage", "Issue with Edit yoga, Please Contact Adminstartor ");
			}
			LOG.info("Get rsponse Edit page for yoga from controller");
			return "YogaEditFrom";
		}

		@PostMapping("/edityogaOpe")
		public String getVisitorUpdate(@RequestParam("imageFile") MultipartFile imageFile,@ModelAttribute YogaDetails editform, Model model, Principal p) {
			LOG.info("udateing Yoga deatils to DB");
			try {
				byte[] data = null;
				
				//New Logic to resize the image
				byte[] bytes1 = null ;
				bytes1 =(imageFile!=null ? imageFile.getBytes() : null);
				if(bytes1!=null && bytes1.length>0) {

					LOG.info("Controller : yoga Edit & view Before IMAGE : " +bytes1.length); 
					BufferedImage scaledImg = Scalr.resize(ImageIO.read(new ByteArrayInputStream(bytes1)), Method.QUALITY, 1280, 960);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(scaledImg, "jpg", baos); 
					data = baos.toByteArray();
					LOG.info("Controller : yoga Edit & view After IMAGE : " +data.length); 
				}//end
				if(data!=null && data.length>0) 
					editform.setYimagedata(data);
				else
					LOG.info("Image data Not found in : yoga Edit registering");
				editform.setYprofilecode("YOGA");
				String yd = null;
				LOG.info(editform.getYnameopt());
				yd = service.registerYogaEditInformation(editform);
				if (yd != null) {
					LOG.info("registering yoga edit deatils Done");
					model.addAttribute("msg", "'" + editform.getYfullname().toUpperCase()+ "' Profile has been UPDATED Successfully.");
				} else {
					LOG.info("failed to register");
					model.addAttribute("msg", "Registration Filed.. Please try again ");
				}
				
				Optional<YogaDetails> opt = null;
				opt = service.getViewByYogaID(editform.getYid());
				if (opt != null) {
					YogaDetails evd = null;
					evd = opt.get();
					model.addAttribute("edit", evd);
					model.addAttribute("imgUtil", new ImageUtil());
					
				} else {
					model.addAttribute("noeditpage", "Issue with Edit yoga, Please Contact Adminstartor ");
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info(" Controller Error : got execption while setting the YOGA Editing deatils to DB");
			}
			return "YogaEditFrom";
		}
		@PostMapping("/yogapdfbatchwise")
		public String getYogaPDFBatchWise(@RequestParam String pdfdocbatchywise, Model model, Principal p) {

			try {
				List<Object[]>  nsd = null;
				nsd = service.getYogaPDFBatchWise(pdfdocbatchywise);
		
				File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF");
				File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\YogaPDF");  
				f2.mkdir(); f3.mkdir();
				
				File f1 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\YogaImage");  
				boolean flag=f1.mkdir();
				if (nsd!= null) {
					PdfWriter writer=new PdfWriter("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\YogaPDF\\YogaBatchWise.pdf");
					PdfDocument pdfdoc=new PdfDocument(writer);
					pdfdoc.addNewPage();
					Document document=new Document(pdfdoc);

					ArrayList<String[]> vistlist=new ArrayList<>();
					//handling Sno .. yid,yidcard,ybatch,yfullname,ymobilenumber,ypresentaddress,yimagedata 
					int sno=1;
					for (Object[] obj : nsd) {
						String[] visiNo=null;
						int i = (Integer)obj[0];
						visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
						sno++;
						vistlist.add(visiNo);

						//get Image Each Vid
						if (obj[6]!= null && flag==true) {
							byte [] data = null;
							data=(byte[]) obj[6];
							if(data!=null && data.length>0) {
								ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
								BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
								ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\YogaImage\\output.jpg"));

								ImageData imageData = ImageDataFactory.create("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\PDF\\YogaImage\\output.jpg");
								Image img = new Image(imageData);
								img.setHeight(450.0f);
								img.setWidth(500.0f);
								Table table = new Table(1);
								table.addCell(new Cell().add(img).add("Name : "+(String) obj[3]).add("PHONE NO :: "+(String) obj[4]).add("ADDRESS :: "+(String) obj[5]));
								document.add(table);
							}
						}
					}
					document.close();
					for (File subfile : f1.listFiles()) {
			            subfile.delete();
			        }
					f1.delete();
					
					LOG.info("Controller :Calling the Service to get all the Yoga object");
					nsd = null; 
					nsd = service.allYogaInformation();
					
					if (nsd!= null) {
						vistlist=null;
						vistlist=new ArrayList<>();
						//handling Sno . yid,yidcard,ybatch,yfullname,ymobilenumber,ypresentaddress
						//Logic for imges  in the list
						HashMap<String, byte[]> imgs=new HashMap<>();
						sno=1;
						for (Object[] obj : nsd) {
							String[] visiNo=null;
							int i = (Integer)obj[0];
							visiNo = new String[] {Integer.toString(i),Integer.toString(sno),(String) obj[1], (String) obj[2],(String) obj[3], (String) obj[4],(String) obj[5]};
							sno++;
							vistlist.add(visiNo);
							imgs.put(Integer.toString(i),(byte[]) obj[6]);
						}
						//End handling Sno ..
					LOG.info("Controller : Successfully to retirve all the Yoga people and posting to HTML ");
						model.addAttribute("vdlist", vistlist);
						model.addAttribute("imgUtil", new ImageUtil());
						model.addAttribute("imgs",imgs);
					} else {
						LOG.info("failed to retrive all the yoga info");
						model.addAttribute("usernotfound", " Yoga profile list are not found.. Please try again or Please Contact Adminstartor ");
					}
					model.addAttribute("ypdf", "YOGA PDF Downloaded.. ");
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "PdfGenaretore";
			//return "AllYogaInformationList";
		} 
		//-----------Yoga Ended -----------------
		//----Started Database Backup ------------
		@GetMapping("/completedbBackup")
		public String getDatabaseBackUpInfo(Model model, Principal p) {
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String backDbDate=formatter.format(new Date());
			File f2 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp");
			f2.mkdir(); 
			File f3 = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate);  
			f3.mkdir();
			String status ="";
			FileWriter visitor =null;
			
			//visitor DB Back Up	-- 	vid,  vfname,vphonenumber,voccupation,vdistcity,vdateofmonth, vprofilecode,   -->vimagedata
			try{
				LOG.info("Controller : Visitore Data Base Back Up Operation Started.. Please Wait");
				File visitorInfo = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\VisitoreInfo");
				visitorInfo.mkdir(); 
				File visitoreImg = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\VisitoreInfo\\VisitoreImage");
				visitoreImg.mkdir();
				File visitoreDetails = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\VisitoreInfo\\VisitoreDetails");
				visitoreDetails.mkdir();

				File file = null; file=new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\VisitoreInfo\\VisitoreDetails\\VISITOR_DETAILS.CSV");
				visitor = new FileWriter(file);
				
				
				List<Object[]> info = null; 
				info = service.getVisitorBackUpInfo();
				if(info!=null && info.size()>0) {
					for (Object[] obj : info) {
						if(obj!=null && obj.length>0) {
							try {
								visitor.append(Integer.toString((Integer)obj[0]));		visitor.append(',');
								
								visitor.append((String) obj[1]!=null ? ((String) obj[1]).replaceAll(","," ") : "");		visitor.append(',');
								visitor.append((String) obj[2]!=null ? ((String) obj[2]).replaceAll(","," ") : ""); 	visitor.append(',');
								visitor.append((String) obj[3]!=null ? ((String) obj[3]).replaceAll(","," ") : ""); 	visitor.append(',');		
								visitor.append((String) obj[4]!=null ? ((String) obj[4]).replaceAll(","," ") : ""); 	visitor.append(',');
								visitor.append((String) obj[5]!=null ? ((String) obj[5]).replaceAll(","," ") : ""); 	visitor.append(',');		

								visitor.append((String) obj[6]!=null ? ((String) obj[6]).replaceAll(","," ") : ""); 	visitor.append(',');
								
								visitor.append('\n');
							} catch (IOException e) {
						        e.printStackTrace();
						    } 
							
							try {
								byte [] data = null;
								data=(byte[]) obj[7];
								if(data!=null && data.length>0) {
									ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
									BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
									ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\VisitoreInfo\\VisitoreImage\\VISITOR_"+obj[0]+".jpg"));
								}
							} catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					}
					status="Visitor,";
				}
				LOG.info("Controller : Visitore Data Base Back Up Operation Ended.. Please Check");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					visitor.flush();
					visitor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }	
			
			//Yoga DB Back Up	-- 	yid, yindate,youtdate,yidcard,ybatch,yfullname, yfatherhusbandname,ydateofbirth,ygender,ymaritualstatus,ybloodgroup,  yoccupation,ypresentaddress,ydisease,yaadharnumber,ymobilenumber, 
			//						,yemergencyno,yweight,ybp,ypulserate,ysugar,  ydateofmonth,yprofilecode, --> yimagedata
			FileWriter yoga =null;
			try{
				LOG.info("Controller : Yoga Data Base Back Up Operation Started.. Please Wait");
				File yogaInfo = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\YogaInfo");
				yogaInfo.mkdir(); 
				File yogaImg = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\YogaInfo\\YogaImage");
				yogaImg.mkdir();
				File yogaDetails = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\YogaInfo\\YogaDetails");
				yogaDetails.mkdir();

				File file = null; file=new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\YogaInfo\\YogaDetails\\YOGA_DETAILS.CSV");
				yoga = new FileWriter(file);
				
				List<Object[]> info = null; 
				info = service.getYogaBackUpInfo();
				if(info!=null && info.size()>0) {
					for (Object[] obj : info) {
						if(obj!=null && obj.length>0) {
							try {
								yoga.append(Integer.toString((Integer)obj[0]));			yoga.append(',');
								
								yoga.append((String) obj[1]!=null ? ((String) obj[1]).replaceAll(","," ") : "");	yoga.append(',');
								yoga.append((String) obj[2]!=null ? ((String) obj[2]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[3]!=null ? ((String) obj[3]).replaceAll(","," ") : ""); 	yoga.append(',');		
								yoga.append((String) obj[4]!=null ? ((String) obj[4]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[5]!=null ? ((String) obj[5]).replaceAll(","," ") : ""); 	yoga.append(',');	
								                           
								yoga.append((String) obj[6]!=null ? ((String) obj[6]).replaceAll(","," ") : "");	yoga.append(',');
								yoga.append((String) obj[7]!=null ? ((String) obj[7]).replaceAll(","," ") : "");	yoga.append(',');
								yoga.append((String) obj[8]!=null ? ((String) obj[8]).replaceAll(","," ") : "");	yoga.append(',');		
								yoga.append((String) obj[9]!=null ? ((String) obj[9]).replaceAll(","," ") : "");	yoga.append(',');
								yoga.append((String) obj[10]!=null ? ((String) obj[10]).replaceAll(","," ") : ""); 	yoga.append(',');	

								yoga.append((String) obj[11]!=null ? ((String) obj[11]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[12]!=null ? ((String) obj[12]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[13]!=null ? ((String) obj[13]).replaceAll(","," ") : ""); 	yoga.append(',');		
								yoga.append((String) obj[14]!=null ? ((String) obj[14]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[15]!=null ? ((String) obj[15]).replaceAll(","," ") : ""); 	yoga.append(',');	
														
								yoga.append((String) obj[16]!=null ? ((String) obj[16]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[17]!=null ? ((String) obj[17]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[18]!=null ? ((String) obj[18]).replaceAll(","," ") : ""); 	yoga.append(',');		
								yoga.append((String) obj[19]!=null ? ((String) obj[19]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[20]!=null ? ((String) obj[20]).replaceAll(","," ") : ""); 	yoga.append(',');	
														
								yoga.append((String) obj[21]!=null ? ((String) obj[21]).replaceAll(","," ") : ""); 	yoga.append(',');
								yoga.append((String) obj[22]!=null ? ((String) obj[22]).replaceAll(","," ") : ""); 	yoga.append(',');

								yoga.append('\n');
							} catch (IOException e) {
						        e.printStackTrace();
						    } 
							
							try {
								byte [] data = null;
								data=(byte[]) obj[23];
								if(data!=null && data.length>0) {
									ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
									BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
									ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\YogaInfo\\YogaImage\\YOGA_"+obj[0]+".jpg"));
								}
							} catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					}
					status=status+" Yoga,";
				}
				LOG.info("Controller : Yoga Data Base Back Up Operation Ended.. Please Check");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					yoga.flush();
					yoga.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }	
			
			//GuruMal DB Back Up	-- 	gmid, gmindate,gmfullname,gmfatherhusbandname,gmgender,gmmobilenumber,  gmemergencyno,gmoccupation,gmpresentaddress,gmdateofmonth,gmprofilecode,  --> gmimagedata
			FileWriter guruMala =null;
			try{
				LOG.info("Controller : GuruMala Data Base Back Up Operation Started.. Please Wait");
				File guruMalaInfo = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\GuruMalaInfo");
				guruMalaInfo.mkdir(); 
				File guruMalaImg = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\GuruMalaInfo\\GuruMalaImage");
				guruMalaImg.mkdir();
				File guruMalaDetails = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\GuruMalaInfo\\GuruMalaDetails");
				guruMalaDetails.mkdir();

				File file = null; file=new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\GuruMalaInfo\\GuruMalaDetails\\GURUMALA_DETAILS.CSV");
				guruMala = new FileWriter(file);
				
				List<Object[]> info = null; 
				info = service.getGuruMalaBackUpInfo();
				if(info!=null && info.size()>0) {
					for (Object[] obj : info) {
						if(obj!=null && obj.length>0) {
							try {
								guruMala.append(Integer.toString((Integer)obj[0]));			guruMala.append(',');		
								
								guruMala.append((String) obj[1]!=null ? ((String) obj[1]).replaceAll(","," ") : "");	guruMala.append(',');
								guruMala.append((String) obj[2]!=null ? ((String) obj[2]).replaceAll(","," ") : ""); 	guruMala.append(',');
								guruMala.append((String) obj[3]!=null ? ((String) obj[3]).replaceAll(","," ") : ""); 	guruMala.append(',');		
								guruMala.append((String) obj[4]!=null ? ((String) obj[4]).replaceAll(","," ") : ""); 	guruMala.append(',');
								guruMala.append((String) obj[5]!=null ? ((String) obj[5]).replaceAll(","," ") : ""); 	guruMala.append(',');
								                             
								guruMala.append((String) obj[6]!=null ? ((String) obj[6]).replaceAll(","," ") : "");	guruMala.append(',');
								guruMala.append((String) obj[7]!=null ? ((String) obj[7]).replaceAll(","," ") : ""); 	guruMala.append(',');
								guruMala.append((String) obj[8]!=null ? ((String) obj[8]).replaceAll(","," ") : ""); 	guruMala.append(',');		
								guruMala.append((String) obj[9]!=null ? ((String) obj[9]).replaceAll(","," ") : ""); 	guruMala.append(',');	
								guruMala.append((String) obj[10]!=null ? ((String) obj[10]).replaceAll(","," ") : ""); 	guruMala.append(',');	
	
								guruMala.append('\n');
							} catch (IOException e) {
						        e.printStackTrace();
						    } 
							
							try {
								//Image Back up
								byte [] data = null;
								data=(byte[]) obj[11];
								if(data!=null && data.length>0) {
									ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
									BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
									ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\GuruMalaInfo\\GuruMalaImage\\GURUMALA_"+obj[0]+".jpg"));
								}
							} catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					}
					status=status+" GuruMala,";
				}
				LOG.info("Controller : GuruMala Data Base Back Up Operation Ended.. Please Check");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					guruMala.flush();
					guruMala.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }	
			
			//Donation DB Back Up	-- 	did, dindate,ditems,dfullname,dfatherhusbandname,dmobilenumber,  doccupation,dpresentaddress,dprofilecode,ddateofmonth,  -->dimagedata
			FileWriter donation =null;
			try{
				LOG.info("Controller : Donation Data Base Back Up Operation Started.. Please Wait");
				File donationInfo = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\DonationInfo");
				donationInfo.mkdir(); 
				File donationImg = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\DonationInfo\\DonationImage");
				donationImg.mkdir();
				File donationDetails = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\DonationInfo\\DonationDetails");
				donationDetails.mkdir();

				File file = null; file=new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\DonationInfo\\DonationDetails\\DONATION_DETAILS.CSV");
				donation = new FileWriter(file);
				
				List<Object[]> info = null; 
				info = service.getDonationBackUpInfo();
				if(info!=null && info.size()>0) {
					for (Object[] obj : info) {
						if(obj!=null && obj.length>0) {
							try {
								donation.append(Integer.toString((Integer)obj[0]));			donation.append(',');		
								
								donation.append((String) obj[1]!=null ? ((String) obj[1]).replaceAll(","," ") : "");	donation.append(',');
								donation.append((String) obj[2]!=null ? ((String) obj[2]).replaceAll(","," ") : ""); 	donation.append(',');
								donation.append((String) obj[3]!=null ? ((String) obj[3]).replaceAll(","," ") : ""); 	donation.append(',');		
								donation.append((String) obj[4]!=null ? ((String) obj[4]).replaceAll(","," ") : ""); 	donation.append(',');
								donation.append((String) obj[5]!=null ? ((String) obj[5]).replaceAll(","," ") : ""); 	donation.append(',');
								                             
								donation.append((String) obj[6]!=null ? ((String) obj[6]).replaceAll(","," ") : "");	donation.append(',');
								donation.append((String) obj[7]!=null ? ((String) obj[7]).replaceAll(","," ") : ""); 	donation.append(',');
								donation.append((String) obj[8]!=null ? ((String) obj[8]).replaceAll(","," ") : ""); 	donation.append(',');		
								donation.append((String) obj[9]!=null ? ((String) obj[9]).replaceAll(","," ") : ""); 	donation.append(',');	
	
								donation.append('\n');
							} catch (IOException e) {
						        e.printStackTrace();
						    } 
							
							try {
								byte [] data = null;
								data=(byte[]) obj[10];
								if(data!=null && data.length>0) {
									ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
									BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
									ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\DonationInfo\\DonationImage\\DONATION_"+obj[0]+".jpg"));
								}
							} catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					}
					status=status+" Donation,";
				}
				LOG.info("Controller : Donation Data Base Back Up Operation Ended.. Please Check");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					donation.flush();
					donation.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }	
			
			//Lingabishake DB Back Up	-- 	lid,  ldatetime,lfullname,lfamilyname,lphonenumber,loccupation, laddress,lprofilecode,ldateofmonth,   -->lpersonimagedata 
			FileWriter linga =null;
			try{
				LOG.info("Controller : lingabishakeInfo Data Base Back Up Operation Started.. Please Wait");
				File lingabishakeInfo = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\LingabishakeInfo");
				lingabishakeInfo.mkdir(); 
				File lingabishakeImg = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\LingabishakeInfo\\LingabishakeImage");
				lingabishakeImg.mkdir();
				File lingabishakeDetails = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\LingabishakeInfo\\LingabishakeDetails");
				lingabishakeDetails.mkdir();

				File file = null; file=new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\LingabishakeInfo\\LingabishakeDetails\\LINGABISHAKE_DETAILS.CSV");
				linga = new FileWriter(file);
				
				List<Object[]> info = null; 
				info = service.getLingaBackUpInfo();
				if(info!=null && info.size()>0) {
					for (Object[] obj : info) {
						if(obj!=null && obj.length>0) {
							try {
								linga.append(Integer.toString((Integer)obj[0]));		linga.append(',');		
								
								linga.append((String) obj[1]!=null ? ((String) obj[1]).replaceAll(","," ") : "");	linga.append(',');
								linga.append((String) obj[2]!=null ? ((String) obj[2]).replaceAll(","," ") : ""); 	linga.append(',');
								linga.append((String) obj[3]!=null ? ((String) obj[3]).replaceAll(","," ") : ""); 	linga.append(',');		
								linga.append((String) obj[4]!=null ? ((String) obj[4]).replaceAll(","," ") : ""); 	linga.append(',');
								linga.append((String) obj[5]!=null ? ((String) obj[5]).replaceAll(","," ") : ""); 	linga.append(',');
								                             
								linga.append((String) obj[6]!=null ? ((String) obj[6]).replaceAll(","," ") : "");	linga.append(',');
								linga.append((String) obj[7]!=null ? ((String) obj[7]).replaceAll(","," ") : ""); 	linga.append(',');
								linga.append((String) obj[8]!=null ? ((String) obj[8]).replaceAll(","," ") : ""); 	linga.append(',');
	
								linga.append('\n');
							} catch (IOException e) {
						        e.printStackTrace();
						    } 
							
							try {
								byte [] data = null;
								data=(byte[]) obj[9];
								if(data!=null && data.length>0) {
									ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
									BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
									ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\LingabishakeInfo\\LingabishakeImage\\LINGABISHAKE_"+obj[0]+".jpg"));
								}
							} catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					}
					status=status+" Lingabishake,";
				}
				LOG.info("Controller : Lingabishake Data Base Back Up Operation Ended.. Please Check");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					linga.flush();
					linga.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }	
			
			//Event DB Back Up	-- eid,   edatetime,evenue,ename,epersonnames,epersonmobiles,  eoccupation,epersonaddress,eprofilecode,edateofmonth,   -->epersonsimagedata
			FileWriter eventW =null;
			try{
				LOG.info("Controller : Event Data Base Back Up Operation Started.. Please Wait");
				File eventInfo = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\EventInfo");
				eventInfo.mkdir(); 
				File eventImg = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\EventInfo\\EventImage");
				eventImg.mkdir();
				File eventDetails = new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\EventInfo\\EventInfo");
				eventDetails.mkdir();

				File file = null; file=new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\EventInfo\\EventInfo\\EVENT_DETAILS.CSV");
				eventW = new FileWriter(file);
				
				List<Object[]> info = null; 
				info = service.getEventBackUpInfo();
				if(info!=null && info.size()>0) {
					for (Object[] obj : info) {
						if(obj!=null && obj.length>0) {
							try {
								eventW.append(Integer.toString((Integer)obj[0]));		eventW.append(',');		
								
								eventW.append((String) obj[1]!=null ? ((String) obj[1]).replaceAll(","," ") : "");	eventW.append(',');
								eventW.append((String) obj[2]!=null ? ((String) obj[2]).replaceAll(","," ") : ""); 	eventW.append(',');
								eventW.append((String) obj[3]!=null ? ((String) obj[3]).replaceAll(","," ") : ""); 	eventW.append(',');		
								eventW.append((String) obj[4]!=null ? ((String) obj[4]).replaceAll(","," ") : ""); 	eventW.append(',');
								eventW.append((String) obj[5]!=null ? ((String) obj[5]).replaceAll(","," ") : ""); 	eventW.append(',');
								                             
								eventW.append((String) obj[6]!=null ? ((String) obj[6]).replaceAll(","," ") : "");	eventW.append(',');
								eventW.append((String) obj[7]!=null ? ((String) obj[7]).replaceAll(","," ") : ""); 	eventW.append(',');
								eventW.append((String) obj[8]!=null ? ((String) obj[8]).replaceAll(","," ") : ""); 	eventW.append(',');		
								eventW.append((String) obj[9]!=null ? ((String) obj[9]).replaceAll(","," ") : ""); 	eventW.append(',');
	
								eventW.append('\n');
							} catch (IOException e) {
						        e.printStackTrace();
						    } 
							
							try {
								byte [] data = null;
								data=(byte[]) obj[10];
								if(data!=null && data.length>0) {
									ByteArrayInputStream bis = null; bis= new ByteArrayInputStream(data);
									BufferedImage bImage2 = null; bImage2=ImageIO.read(bis);
									ImageIO.write(bImage2, "jpg", new File("D:\\Sri_Basavaling_Avadhoota_Ashram_Trust\\Project_DBBackUp\\"+backDbDate+"\\EventInfo\\EventImage\\EVENT_"+obj[0]+".jpg"));
								}
							} catch (Exception e) {
						        e.printStackTrace();
						    }
						}
					}
					status=status+" & Event";
				}
				LOG.info("Controller : Event Data Base Back Up Operation Ended.. Please Check");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					eventW.flush();
					eventW.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    } 	
			
			if(status!=null && !status.equalsIgnoreCase(""))
				status=status+" DataBase Back Up Successfull Completed : "+backDbDate;
			model.addAttribute("status", status);
			return "EmployeeWelcomePage";
		}	
		
		//----Ended Database Backup ------------
		
		
		//-------------------Finger Print -------------
		@GetMapping("/getfingerprints")
		public String getfingerprints(Model model, Principal p) {
			try{
				LOG.info("Controller :Calling the Service to get all the Finger print object : ");
				int min = 1; // Minimum value of range
				int max = 10; // Maximum value of range
				int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
				model.addAttribute("tmb",Integer.toString(random_int));
				LOG.info("Controller :Calling the Service to get all the Finger print End + "+fp);
			} catch (Exception e) {
				LOG.info("Controller : Got exception, Failed to set family and aadhar image to object");
				e.printStackTrace();
			}
			return "DisplayYogaRegistrationForm";
		}
		
		
		//------------------- finger Print End 
		//-------------- Pdf Genaretore tool ------------
		
		@GetMapping("/pdf")
		public String getpdfpage(Model model, Principal p) {
				LOG.info("Showing Pdf page");
				return "PdfGenaretore";
			}
		
		//------ En Pdf 
		
		
	}
