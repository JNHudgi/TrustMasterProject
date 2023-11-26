package com.trust.ashram.res.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
import com.trust.ashram.res.repository.TrustAdminDetailsRepository;
import com.trust.ashram.res.repository.TrustDonationDetailsRepository;
import com.trust.ashram.res.repository.TrustEventDetailsRepository;
import com.trust.ashram.res.repository.TrustGuruMalaDetailsRepository;
import com.trust.ashram.res.repository.TrustLingaDetailsRepository;
import com.trust.ashram.res.repository.TrustNightStayDetailsRepository;
import com.trust.ashram.res.repository.TrustPictureDetailsRepository;
import com.trust.ashram.res.repository.TrustVisitorDetailsRepository;
import com.trust.ashram.res.repository.TrustVolunteerDetailsRepository;
import com.trust.ashram.res.repository.TrustYogaDetailsRepository;
import com.trust.ashram.res.service.ITrustAdminService;

@Service
public class TrustAdminServiceImpl implements ITrustAdminService, UserDetailsService {

	private final static Logger LOG=LoggerFactory.getLogger(TrustAdminServiceImpl.class);

	@Autowired
	private TrustAdminDetailsRepository adminDetailsRepo;

	@Autowired
	private TrustVisitorDetailsRepository visitorDetailsRepo;

	@Autowired 
	private BCryptPasswordEncoder encode;
	
	@Autowired
	private TrustYogaDetailsRepository yogaDetailsRepo;  
	
	@Autowired
	private TrustVolunteerDetailsRepository volunteerDetailsRepo; 
	
	@Autowired
	private TrustDonationDetailsRepository donationDetailsRepo; 
	
	@Autowired
	private TrustGuruMalaDetailsRepository guruMalaDetailsRepo; 
	
	@Autowired
	private TrustPictureDetailsRepository pictureDetailsRepo;
	
	@Autowired
	private TrustNightStayDetailsRepository nightStayDetailsRepo;
	
	@Autowired
	private TrustEventDetailsRepository eventdetailsRepo;
	
	@Autowired
	private TrustLingaDetailsRepository lingadetailsRepo;
	
	@Override
	public AdminDetails registerAdminInformation(AdminDetails info) {
		LOG.info("Started Registering Info...");
		AdminDetails ad=null;

		//BCryptPasswordEncoder encode =new BCryptPasswordEncoder(); 
		//String encodepass=encode.encode(info.getApassword()); 
		//info.setApassword(encodepass);
		ad=adminDetailsRepo.save(info);
		LOG.info("Finish Registering Process");
		return ad;
	}


	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<AdminDetails> opt=adminDetailsRepo.findByAemail(username);
		if(opt.isPresent()) {
			AdminDetails ad=opt.get();
			Set<String> roles=ad.getArole();
			Set<GrantedAuthority> authorities = new HashSet<>();
			for (String role:roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			return new User(username, ad.getApassword(), authorities);
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	@Override
	public AdminDetails getEmployeeByAID(Integer aid) {
		
		  Optional<AdminDetails> opt=adminDetailsRepo.findById(aid); 
		  AdminDetails ad=null; 
		  if(opt.isPresent()) 
			 ad=opt.get();
		  
		  if(ad!=null)
		 return ad;
		return ad;
		  
	}
	@Override
	@Transactional
	public Optional<AdminDetails> getAdminDetailsByEmailID(String email) {
		LOG.info("Services : Started calling DB to get logger info..");
		return adminDetailsRepo.findByAemail(email);
	}

	@Override
	public VisitorDetails registerVisitorInformation(VisitorDetails vdetails) {
		LOG.info("Started visitor Registering Info...");
		
		VisitorDetails vd=null;
		vd=visitorDetailsRepo.save(vdetails);

		LOG.info("Finish visitor Registering Process");
		return vd;
	}
	
	@Override
	public List<Object[]> allVisitorInformation() {
		LOG.info("Started fetching all visitor Info...");
		List<Object[]> vd=null;
		vd=visitorDetailsRepo.getAllVisitorSpecficColList();
		LOG.info("Finished fetching all visitor Info...");
		return vd;
	}
	
	@Override
	public List<Object[]> allVisitoreDetailsInformation() {
		List<Object[]> visitorlist=null;
		try {
			LOG.info("Service : Started to get all the dynamic search process for visitor Details");
			visitorlist=visitorDetailsRepo.getAllVisitorSpecficColList();
			LOG.info("Service : completed, Finished fetching all dynamic search Info visitor Details, and back to controller..");
		} catch (Exception e) {
			LOG.info("Service : Got exception, Failed  to retrive all the dynamic search Info visitor Details");
		}
		return visitorlist;
	}
	
	@Override
	@Transactional
	public List<Object[]> visitorListByDate(String name) {
		List<Object[]> empinfo=null;
		try {
			empinfo=visitorDetailsRepo.getVisitorListByDate(name);
			if(empinfo.size()==0)
				LOG.info("Visitore found with :" +empinfo.size());
		} catch (Exception e) {
			LOG.info("Visitore found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return empinfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> visitorListByOccpation(String name) {
		List<Object[]> empinfo=null;
		try {
			empinfo=visitorDetailsRepo.getVisitorListByOccpation(name);
			if(empinfo.size()==0)
				LOG.info("Visitore found with :" +empinfo.size());
		} catch (Exception e) {
			LOG.info("Visitore found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return empinfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> singleVisitorInformation(String name) {
		
		List<Object[]> empinfo=null;
		try {
			empinfo=visitorDetailsRepo.getOneVisitorDetails(name);
			if(empinfo.size()>=1)
				LOG.info("Visitore found with :" +empinfo.size());
		} catch (Exception e) {
			LOG.info("Visitore found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return empinfo;
		
		/*Optional<List<VisitorDetails>> vdinfo=null;
		List<VisitorDetails> vd=null;
		
		vdinfo =visitorDetailsRepo.findByVfname(name);
		vd=vdinfo.get();
		if(vd.isEmpty()==false) {
				LOG.info("Visitor found by :  name");
				return vd;
		} else {
			vdinfo=null; vd=null;
			vdinfo =visitorDetailsRepo.findByVaadher(name);
			vd=vdinfo.get();
			if(vd.isEmpty()==false) {
					LOG.info("Visitor found by :  Aadher");
					return vd;
			} else {
				vdinfo=null; vd=null;
				vdinfo =visitorDetailsRepo.findByVpannumber(name);
				vd=vdinfo.get();
				if(vd.isEmpty()==false) {
						LOG.info("Visitor found by :  Pan");
						return vd;
				} else {
					vdinfo=null; vd=null;
					vdinfo =visitorDetailsRepo.findByVphonenumber(name);
					vd=vdinfo.get();
					if(vd.isEmpty()==false) {
							LOG.info("Visitor found by :  Phone");
							return vd;
						}else {
							vdinfo=null; vd=null;
							vdinfo =visitorDetailsRepo.findByVoccupation(name);
							vd=vdinfo.get();
							if(vd.isEmpty()==false) {
								LOG.info("Visitor found by : Occuption");
								return vd;
							}
						}	
				}
			}
		}
		
		return null;*/
	}
	
	@Override
	public void getRemoveVisitorById(Integer id) {
		visitorDetailsRepo.deleteById(id);
	}

	//----------------------------testing 
	@Override
	public List<VisitorDetails> findPaginated(int pageNo, int pageSize) {
		Pageable paging=PageRequest.of(pageNo, pageSize) ;
		Page<VisitorDetails> pagedResult = visitorDetailsRepo.findAll(paging);
		LOG.info(pagedResult.getSize() +" :: "+ paging.getPageSize() + " :: "+ paging.getPageNumber());
		return pagedResult.toList();
	}
	
	@Override
	public Long getVisitorDetaislCount() {
		return visitorDetailsRepo.count();
	}


	@Override
	public boolean isEmployeeExistByEmail(String email) {
		return adminDetailsRepo.getEmailCount(email)>0;
	}


	@Override
	public Optional<VisitorDetails> getViewByVisitorID(Integer vid) {
			return visitorDetailsRepo.findById(vid);
	}

	@Override
	public String getValidateUsedLoginIdAsEmail(String email) {
		
		Integer i= adminDetailsRepo.getEmailCount(email);
		if(i>=1)
		return "Email is alreday Registered.";
		else
			return "";
	}


	@Override
	public String getValidatePanNumber(String vpannumber) {
		Integer i= visitorDetailsRepo.getPanNumberCount(vpannumber);
		if(i>=1) {
			LOG.info(" from Service Pan number exists");
		return "Pan Number Is Alreday Existed.";}
		else
			return "";
	}


	@Override
	public String getValidateAadharNumber(String vaadher) {
		Integer i= visitorDetailsRepo.getAadharNumberCount(vaadher);
		if(i>=1)
		return "Aadhar Number Is Alreday Existed.";
		else
			return "";
	}


	@Override
	public String getValidatePhoneNumber(String vphonenumber) {
		Integer i= visitorDetailsRepo.getPhoneNumberCount(vphonenumber);
		if(i>=1)
		return "Phone Number Is Alreday Existed.";
		else
			return "";
	}

	@Override
	public boolean getAdminEmailCount(String email) {
		
		Integer i= adminDetailsRepo.getEmailCount(email);
		if(i==1)
			return true;
		else
			return false;
	}


	@Override
	public List<AdminDetails> getAllEmployeeList() {
		LOG.info("Started fetching all employee Info...");
		List<AdminDetails> empinfo=null;
		empinfo=adminDetailsRepo.findAll();
		LOG.info("Finished fetching all employee Info...");
		return empinfo;
	}


	@Override
	public String getRemoveEmployeeByEmail(Integer aid) {
		
		/*
		 * Optional<AdminDetails> opt=adminDetailsRepo.findById(aid); AdminDetails
		 * ad=null; if(opt.isPresent()) ad=opt.get();
		 * 
		 * if(ad!=null)
		 */
			adminDetailsRepo.deleteById(aid);
		
		return "Employee has been removed.";
	}


	@Override
	public Optional<AdminDetails> getViewByEmployeeEmail(String aemail) {
		Optional<AdminDetails> opt=null;
		opt=adminDetailsRepo.findByAemail(aemail);
		return opt;
	}

	//----------Started YOGA volunteer Started---------------
	@Override
	@Transactional
	public VolunteerDetails registerYogaVolunteerInformation(VolunteerDetails yogaVolunteer) {
		LOG.info("Started YOGA volunteer Registering Info...");
		VolunteerDetails yd=null;
		yd=volunteerDetailsRepo.save(yogaVolunteer);
		LOG.info("Finish Yoga volunteer Registering Process");
		return yd;
	}
	
	@Override
	public List<Object[]> getAllYogaVolunteerList() {	
		LOG.info("Started fetching all Yoga Volunteer Info...");
		List<Object[]> list=null;
		list=volunteerDetailsRepo.getAllYogaVolunteerList();
		LOG.info("Finished fetching all yoga Volunteer Info...");
		return list;
	}
	
	@Override
	@Transactional
	public String registerYogaVolunteerEditViewInformation(VolunteerDetails editform) {
		LOG.info("Started Updateing "+editform.getVid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getVimagedata()!=null)
			ydimg=volunteerDetailsRepo.getUpdateYogaVolunterImageDetails(editform.getVimagedata(),editform.getVid());
		
			yd=volunteerDetailsRepo.getUpdateYogaVolunteerDetails(editform.getVindate(),editform.getVoutdate(),editform.getVbatch(),editform.getVfullname(),
											editform.getVfatherhusbandname(),editform.getVage(),editform.getVgender(),
											editform.getVmaritualstatus(),editform.getVoccupation(),editform.getVpermanentaddress(),
											editform.getVmobilenumber(),editform.getVemergencvno(),editform.getVprofilecode(),editform.getVidcard(),editform.getVid());
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getVid() +" : " +value+" :: yoga Volunter edit and View 	Image count :"+img);
			return "DONE Updateing "+editform.getVid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getVid() +" : " +value);
			return "Not DONE Updateing";
		}
	}

	@Override
	public void getRemoveYogaVolunteerById(Integer id) {
		volunteerDetailsRepo.deleteById(id);
		
	}
	
	
	//----------End YOGA Voluneer---------------
	
	

	//----------Started YOGA Started---------------
	@Override
	@Transactional
	public YogaDetails registerYogaInformation(YogaDetails yoga) {
		LOG.info("Started YOGA Registering Info...");
		
		YogaDetails yd=null;
		yd=yogaDetailsRepo.save(yoga);

		LOG.info("Finish YOga Registering Process");
		return yd;
	}

	@Override
	public List<Object[]> allYogaInformation() {	
		LOG.info("Started fetching all Yoga Info...");
		List<Object[]> list=null;
		list=yogaDetailsRepo.getAllYogaSpecficColList();
		LOG.info("Finished fetching all yoga Info...");
		return list;
	}
	
	

	@Override
	public Page<YogaDetails> viewAllYogaPage(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
				Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
				return yogaDetailsRepo.findAll(pageable);
	}
	
	@Override
	public String getCheckYogaIdCardCount(String yidcard, String ybatch) {
		LOG.info("Started fetching Yoga Check Yoga IdCard Count Started...");
		int count = 0;
		count =yogaDetailsRepo.getCheckYogaIdCardCount(ybatch, yidcard);
		LOG.info("Started fetching Yoga Check Yoga IdCard Count : "+count);
		if(count>=1)
			return "YOGA ID ALREADY IS EXIST";
		else
			return null;
	}

	@Override
	@Transactional
	public Page<YogaDetails> getYogaSearchByBatchWitPagination(int pageNo, int pageSize, String sortField, String sortDirection, String batch) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
				Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
				return yogaDetailsRepo.findByYbatch(batch, pageable);
	}
	
	@Override
	@Transactional
	public Page<YogaDetails> getYogaSearchByOccupationWitPagination(int pageNo, int pageSize, String sortField, String sortDirection, String occupation) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
				Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
				return yogaDetailsRepo.findByYoccupation(occupation, pageable);
	}
	
	@Override
	@Transactional
	public Page<YogaDetails> getYogaSearchByIdcardWitPagination(int pageNo, int pageSize, String sortField, String sortDirection, String idcard) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
				Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
				return yogaDetailsRepo.findByYidcard(idcard, pageable);
	}
	
	@Override
	@Transactional
	public Page<YogaDetails> getYogaSearchByNameWitPagination(int pageNo, int pageSize, String sortField, String sortDirection, String name) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
				Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
				return yogaDetailsRepo.findByYfullname(name, pageable);
	}
	
	
	
	@Override
	public Optional<YogaDetails> getViewByYogaID(Integer yid) {
		return yogaDetailsRepo.findById(yid);
	}

	@Override
	public Optional<VolunteerDetails> getViewByYogaVolunteerID(Integer yvid) {
		return volunteerDetailsRepo.findById(yvid);
	}

	@Override
	public PictureDetails registerPictureInformation(PictureDetails picture) {
		LOG.info("Started Pictire Registering Info...");
		
		PictureDetails pd=null;
		pd=pictureDetailsRepo.save(picture);

		LOG.info("Finish Picture Registering Process");
		return pd;
	}


	@Override
	public Map<String, PictureDetails> getAllPictures() {
		LOG.info("Started fetching all Picture Info...");
		Map<String, PictureDetails> mapPicture=null;
		List<PictureDetails> pd=null;
		pd=pictureDetailsRepo.findAll();
		if(pd!=null) {
			mapPicture=new HashMap<>();
			for (PictureDetails pictureDetails : pd) {
				mapPicture.put(pictureDetails.getPname(), pictureDetails);
			}
			LOG.info("Finished Covertion from list to Map ...");
			
		}
		LOG.info("Finished fetching all Picture Info...");
		return mapPicture;
		
	}

	@Override
	@Transactional
	public List<Object[]> getOneOrAllYogaInformation(String name) {
		List<Object[]> yogainfo=null;
		try {
			//yogainfo=yogaDetailsRepo.getOneOrAllYogaDetails(name);
			yogainfo=yogaDetailsRepo.getOneYogaDetailsUsingYogaId(name);
			if(yogainfo.size()>=1)
				LOG.info("Yoga found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getYogaVolunteerSearchByBatch(String name) {
		List<Object[]> yogainfo=null;
		try {
			String code="VOLUNTEER";
			yogainfo=yogaDetailsRepo.getYogaVolunteerSearchByBatch(name,code);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Volunteer Search By Batch found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Volunteer Search By Batch found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getYogaVolunteerSearchByOccuption(String name) {
		List<Object[]> yogainfo=null;
		try {
			String code="VOLUNTEER";
			yogainfo=yogaDetailsRepo.getYogaVolunteerSearchByOccupation(name,code);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Volunteer Search By Occupation found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Volunteer Search By Occupation found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getYogaVolunteerSearchByIds(String name) {
		List<Object[]> yogainfo=null;
		try {
			String code="VOLUNTEER";
			yogainfo=yogaDetailsRepo.getYogaVolunteerSearchByIds(name,code);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Volunteer Search By ids found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Volunteer Search By ids found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getYogaVolunteerSearchByName(String name) {
		List<Object[]> yogainfo=null;
		try {
			String code="VOLUNTEER";
			yogainfo=yogaDetailsRepo.getYogaVolunteerSearchByName(name,code);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Volunteer Search By name found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Volunteer Search By name found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	
	
	@Override
	@Transactional
	public List<Object[]> getYogaSearchByBatch(String name) {
		List<Object[]> yogainfo=null;
		try {
			yogainfo=yogaDetailsRepo.getYogaSearchByBatch(name);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Search By Batch found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Search By Batch found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getYogaSearchByOccuption(String name) {
		List<Object[]> yogainfo=null;
		try {
			yogainfo=yogaDetailsRepo.getYogaSearchByOccuption(name);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Search By Occuption found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Search By Occuption found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getYogaSearchByName(String name) {
		List<Object[]> yogainfo=null;
		try {
			yogainfo=yogaDetailsRepo.getYogaSearchByName(name);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Search By Name found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Search By name found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
		//----------Started YOGA Started---------------


	
	@Override
	@Transactional
	public List<Object[]> yogaSearchByBatchAndIds(String yogasearchbyids, String yogasearchbybatch) {
		List<Object[]> yogainfo=null;
		try {
			yogainfo=yogaDetailsRepo.getYogaSearchByBatchAndIds(yogasearchbybatch, yogasearchbyids);
			if(yogainfo.size()>=1)
				LOG.info("Yoga Search By Name found with :" + yogainfo.size());
		} catch (Exception e) {
			LOG.info("Yoga Search By name found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return yogainfo;
	}
	
	@Override
	public Optional<YogaDetails> getViewByYogaIdCard(String yogaidcard) {
		return yogaDetailsRepo.findByYidcard(yogaidcard);
	}


	@Override
	@Transactional
	public String registerYogaEditInformation(YogaDetails editform) {
	
		LOG.info("Started Updateing "+editform.getYid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getYimagedata()!=null)
			ydimg=yogaDetailsRepo.getUpdateYogaImageDetails(editform.getYimagedata(),editform.getYid());
		
			yd=yogaDetailsRepo.getUpdateYogaDetails(editform.getYindate(),editform.getYoutdate(),editform.getYbatch(),editform.getYfullname(),
											editform.getYfatherhusbandname(),editform.getYdateofbirth(),editform.getYage(),editform.getYgender(),
											editform.getYmaritualstatus(),editform.getYbloodgroup(),editform.getYnationality(),editform.getYoccupation(),
											editform.getYpresentaddress(),editform.getYpermanentaddress(),editform.getYdisease(),editform.getYaadharnumber(),
											editform.getYmobilenumber(),editform.getYemergencyno(),editform.getYweight(),editform.getYbp(),editform.getYpulserate(),
											editform.getYsugar(),editform.getYprofilecode(),editform.getYidcard(),editform.getYinchargesign(),editform.getYattendeesign(),editform.getYnameopt(),editform.getYid());
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getYid() +" : " +value+" :: yoga Image count :"+img);
			return "DONE Updateing "+editform.getYid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getYid() +" : " +value);
			return "Not DONE Updateing";
		}
	}
	
	@Override
	public void getRemoveYogaById(Integer id) {
		yogaDetailsRepo.deleteById(id);
	}
	
	
	//--------------------- Yoga object ends------------------------------

	//--------------------- Started Night Stay object---------------------
	@Override
	public NightStayDetails registerNightStayInformation(NightStayDetails ns) {
		LOG.info("Service : entered and Started Night Stay registration process");
		LOG.info("Service : Calling to repository to complete night stay registration process");
		NightStayDetails nsd=null;
		nsd=nightStayDetailsRepo.save(ns);
		LOG.info("Service : completed, night stay registration process and back to controller");
		return nsd;
	}
	
	@Override
	public List<Object[]> allNightSatyInformation() {
		LOG.info("Service : entered and Started to get all the night stay process");
		List<Object[]> list=null;
		list=nightStayDetailsRepo.getAllNightShiftSpecficColList();
		LOG.info("Service : completed, Finished fetching all night stay Info, and back to controller..");
		return list;
	}
	
	@Override
	public void getRemoveNightStayById(Integer nsid) {
		LOG.info("Service : entered and Started to remove one night stay process");
		nightStayDetailsRepo.deleteById(nsid);
		LOG.info("Service : completed, Successfull to remove one night stay profile, and back to controller..");
	}
	
	
	@Override
	public Optional<NightStayDetails> getViewByNightStayID(Integer nightStayId) {
		LOG.info("Service : entered and Started to calling to DAO to one night stay profile using id");
		return nightStayDetailsRepo.findById(nightStayId);
	}
	
	@Override
	@Transactional
	public List<Object[]> getOneOrAllNightStayInformation(String name) {
		LOG.info("Service : entered and Started to get one the night stay profile");
		List<Object[]> nsinfo=null;
		try {
			nsinfo=nightStayDetailsRepo.getOneOrAllNightStayDetails(name);
			
			if(nsinfo.size()==0)
				LOG.info("Night Stay found with :" + nsinfo.size());
		} catch (Exception e) {
			LOG.info("Yoga found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return nsinfo;
	}
	
	@Override
	@Transactional
	public String registerNightStayEditInformation(NightStayDetails ns) {
		LOG.info("Service : entered and started Updating Night stay profile: "+ ns.getNsfullname());
		String result="";
		
		LOG.info("Service : calling DAO to only details update");
		int onlydetails= 0;
		onlydetails=nightStayDetailsRepo.getUpdateNightStayOnlyDetails(ns.getNsidcard(),ns.getNsfullname(),ns.getNsfamilynames(),ns.getNsaadharnumber(),ns.getNsmobilenumber(),
				ns.getNsalternetnumber(),ns.getNsindate(),ns.getNsoutdate(),ns.getNsdays(),ns.getNsfulladdress(),ns.getNscode(),ns.getNsid());
		if(onlydetails==1) {
			LOG.info("Service : Successfull to update Only details." +ns.getNsfullname());
			result=result+"Only Datails,";
		}
		
		
		LOG.info("Service : calling DAO to Family details update");
		if(ns.getNsfamilyimagedata()!=null) {
			LOG.info("Service : calling DAO to only Family image update");
			int family= 0;
			family=nightStayDetailsRepo.getUpdateNightStayFamilyDetails(ns.getNsfamilyimagedata(), ns.getNsid());
			if(family==1) {
				LOG.info("Service : Successfull to update Family image details.");
				result=result+"Family Image Datails,";
			}
		}
		
		LOG.info("Service : calling DAO to Aadhar details update");
		if(ns.getNsaadharimagedata()!=null) {
			LOG.info("Service : calling DAO to only Aadhar image update");
			int aadhar= 0;
			aadhar=nightStayDetailsRepo.getUpdateNightStayAadharDetails(ns.getNsaadharimagedata(), ns.getNsid());
			if(aadhar==1) {
				LOG.info("Service : Successfull to update aadhar image details.");
				result=result+"aadhar Image Datails,";
			}
		}
		return result;
	}
	//--------------------- Ended Night Stay object---------------------
	//--------------------- Started Event details object---------------------
	
	@Override
	@Transactional
	public EventDetails registerEventDetailsInformation(EventDetails eds) {
		LOG.info("Service : entered and Started event details registration process");
		LOG.info("Service : Calling to repository to complete event details registration process");
		EventDetails nsd=null;
		nsd=eventdetailsRepo.save(eds);
		LOG.info("Service : completed, event details registration process and back to controller");
		return nsd;
	}
	
	@Override
	public List<Object[]> allEventDetailsInformation() {
		LOG.info("Service : entered and Started to get all the event details process");
		List<Object[]> list=null;
		list=eventdetailsRepo.getAllEventDeatilsSpecficColList();
		LOG.info("Service : completed, Finished fetching all event details Info, and back to controller..");
		return list;
	}
	
	@Override
	@Transactional
	public List<Object[]> getOneOrAllEventDetailInformation(String eventoption) {
		LOG.info("Service : entered and Started to get one the event details profile");
		List<Object[]> info=null;
		try {
			info=eventdetailsRepo.getOneOrAllEventDetails(eventoption);
			if(info.size()==0)
				LOG.info("Event Deatils found with :" + info.size());
		} catch (Exception e) {
			LOG.info("event details found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}
	@Override
	public void getRemoveEventDetailsById(Integer eid) {
		LOG.info("Service : entered and Started to remove one event details process");
		eventdetailsRepo.deleteById(eid);
		LOG.info("Service : completed, Successfull to remove one event details profile, and back to controller..");
	}
	
	@Override
	public Optional<EventDetails> getViewByEventID(Integer eventId) {
		LOG.info("Service : entered and Started to calling to DAO to one Event details profile using id : "+ eventId);
		return eventdetailsRepo.findById(eventId);
	}
	//--------------------- Ended Event details object---------------------

	//--------------------- Started Dynamic Search details object---------------------
	@Override
	@Transactional
	public List<Object[]> geDynamicYogaInformation() {
		LOG.info("Service : Started, Dynamic search for yoga details");
		List<Object[]> info=null;
		List<Object[]> fnlInfo=null;
		try {
			info=yogaDetailsRepo.getDynamicYogaDeatils();
			if(info!=null) {
				LOG.info("Service : Successfull, Dynamic search for yoga details : "+info.size());
				fnlInfo=new ArrayList<Object[]>();
				for (Object[] objects : info) {
					fnlInfo.add(objects);
					/*if(objects[5]==null) {	
						fnlInfo.add(objects);
					} else {
						if(!objects[5].toString().equalsIgnoreCase("VIP")) {
							fnlInfo.add(objects);
						}
					}*/
				}
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for yoga details");
			LOG.info(e.getMessage().toString());
		}
		return fnlInfo;
	}


	@Override
	@Transactional
	public List<Object[]> geDynamicNightStayInformation() {
		LOG.info("Service : Started, Dynamic search for night stay details");
		List<Object[]> info=null;
		List<Object[]> fnlInfo=null;
		try {
			info=nightStayDetailsRepo.getDynamicNightStayDeatils();
			if(info!=null) {
				LOG.info("Service : Successfull, Dynamic search for Night Stay details : "+info.size());
				fnlInfo=new ArrayList<Object[]>();
				for (Object[] objects : info) {
					fnlInfo.add(objects);
					/*
					 * if(objects[5]==null) { fnlInfo.add(objects); } else {
					 * if(!objects[5].toString().equalsIgnoreCase("VIP")) { fnlInfo.add(objects); }
					 * }
					 */
				}
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for Night Stay details");
			LOG.info(e.getMessage().toString());
		}
		return fnlInfo;
	}

	@Override
	@Transactional
	public List<Object[]> geDynamicEventInformation() {
		LOG.info("Service : Started, Dynamic search for event details");
		List<Object[]> info=null;
		List<Object[]> fnlInfo=null;
		try {
			info=eventdetailsRepo.getDynamicEventDeatils();
			if(info!=null) {
				LOG.info("Service : Successfull, Dynamic search for event details : "+info.size());
				fnlInfo=new ArrayList<Object[]>();
				for (Object[] objects : info) {
					fnlInfo.add(objects);
					/*
					 * if(objects[5]==null) { fnlInfo.add(objects); } else {
					 * if(!objects[5].toString().equalsIgnoreCase("VIP")) { fnlInfo.add(objects); }
					 * }
					 */
				}
			}	
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for event details");
			LOG.info(e.getMessage().toString());
		}
		return fnlInfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> geDynamicVisitorInformation() {
		LOG.info("Service : Started, Dynamic search for Visitor details");
		List<Object[]> info=null;
		List<Object[]> fnlInfo=null;
		try {
			info=visitorDetailsRepo.getDynamicVisitorDeatils();
			if(info!=null) {
				LOG.info("Service : Successfull, Dynamic search for Visitor details : "+info.size());
				fnlInfo=new ArrayList<Object[]>();
				for (Object[] objects : info) {
					fnlInfo.add(objects);
					/*
					 * if(objects[5]==null) { fnlInfo.add(objects); } else {
					 * if(!objects[5].toString().equalsIgnoreCase("VIP")) { fnlInfo.add(objects); }
					 * }
					 */
				}
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for Visitor details");
			LOG.info(e.getMessage().toString());
		}
		return fnlInfo;
	}


	@Override
	@Transactional
	public Integer nightStayProMoveToShivapur(Integer nsid) {
		LOG.info("Service : entered and started moving Night stay profile to Shivapur: "+ nsid);
		Integer result=0;
		try {
			LOG.info("Service : calling DAO to only Night stay details update");
			result=nightStayDetailsRepo.nsMoveToShivapur(nsid, "VIP");
		} catch (Exception e) {
			LOG.info("Service : Having issues while moving Night stay profile to Shivapur");
		}
		return result;
	}
	
	@Override
	@Transactional
	public Integer eventProMoveToShivapur(Integer eid) {
		LOG.info("Service : entered and started moving event profile to Shivapur: "+ eid);
		Integer result=0;
		try {
			LOG.info("Service : calling DAO to only event details update");
			result=eventdetailsRepo.evnetMoveToShivapur(eid, "VIP");
		} catch (Exception e) {
			LOG.info("Service : Having issues while moving event profile to Shivapur");
		}
		return result;
	}
	
	@Override
	@Transactional
	public Integer visitorProMoveToShivapur(Integer yid) {
		LOG.info("Service : entered and started moving visitor profile to Shivapur: "+ yid);
		Integer result=0;
		try {
			LOG.info("Service : calling DAO to only visitor details update");
			result=visitorDetailsRepo.visitorMoveToShivapur(yid, "VIP");
		} catch (Exception e) {
			LOG.info("Service : Having issues while moving visitor profile to Shivapur");
		}
		return result;
	}
	
	
	@Override
	@Transactional
	public Integer yogaProMoveToShivapur(Integer yid) {
		LOG.info("Service : entered and started moving yoga profile to Shivapur: "+ yid);
		Integer result=0;
		try {
			LOG.info("Service : calling DAO to only yoga details update");
			result=yogaDetailsRepo.yogaMoveToShivapur(yid,"VIP");
		} catch (Exception e) {
			LOG.info("Service : Having issues while moving yoga profile to Shivapur");
		}
		return result;
	}
	
	@Override
	public List<Object[]> geDynamicSearchOptionYogaInformation(String name) {
		LOG.info("Service : Dynamic Search Option for yoga profile");
		List<Object[]> info=null;
		try {
			info=yogaDetailsRepo.getDynamicSearchOptionForYogaOneOrAll(name);
			if(info.size()>=1)
				LOG.info("Service : Dynamic Search Option Night Stay found with :" + info.size());
		} catch (Exception e) {
			LOG.info("Service : Dynamic Search Option yoga found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}


	@Override
	@Transactional
	public List<Object[]> geDynamicSearchOptionNightStayInformation(String name) {
		LOG.info("Service : Dynamic Search Option for night stay profile");
		List<Object[]> nsinfo=null;
		try {
			nsinfo=nightStayDetailsRepo.getDynamicSearchOptionForNightStayOneOrAll(name);
			if(nsinfo.size()>=1)
				LOG.info("Service : Dynamic Search Option Night Stay found with :" + nsinfo.size());
		} catch (Exception e) {
			LOG.info("Service : Dynamic Search Option night stay found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return nsinfo;
	}


	@Override
	@Transactional
	public List<Object[]> geDynamicSearchOptionEventInformation(String name) {
		LOG.info("Service : Dynamic Search Option for yoga profile");
		List<Object[]> info=null;
		try {
			info=eventdetailsRepo.getDynamicSearchOptionForEventOneOrAll(name);
			if(info.size()>=1)
				LOG.info("Service : Dynamic Search Option Event found with :" + info.size());
		} catch (Exception e) {
			LOG.info("Service : Dynamic Search Option Event found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}


	@Override
	@Transactional
	public List<Object[]> geDynamicSearchOptionVisitorInformation(String name) {
		List<Object[]> info=null;
		try {
			info=visitorDetailsRepo.getDynamicSearchOptionForVisitorOneOrAll(name);
			if(info.size()>=1)
				LOG.info("Service : Dynamic Search Option Visitor found with :" + info.size());
		} catch (Exception e) {
			LOG.info("Service : Dynamic Search Option found Visitor with issues ");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}

	
	//--------------------- Ended Dynamic Search details object---------------------
	
	//--------------------- Started Shivpur Search details object---------------------
	@Override
	public List<Object[]> getShivapurYogaInformation() {
		LOG.info("Service : Started, Shivapur search for yoga details");
		List<Object[]> info=null;
		try {
			String ycode="VIP";
			info=yogaDetailsRepo.getShivapurYogaDeatils(ycode);
			if(info!=null) {
				LOG.info("Service : Successfull, Shivapur search for yoga details : "+info.size());
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for yoga details");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}

	@Override
	public List<Object[]> getShivapurNightStayInformation() {
		LOG.info("Service : Started, Shivapur search for night stay details");
		List<Object[]> info=null;
		try {
			String code="VIP";
			info=nightStayDetailsRepo.getShivapurNightStayDeatils(code);
			if(info!=null) {
				LOG.info("Service : Successfull, Shivapur search for night stay details : "+info.size());
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for night stay details");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}
	
	@Override
	public List<Object[]> getShivapurEventInformation() {
		LOG.info("Service : Started, Shivapur search for event details");
		List<Object[]> info=null;
		try {
			String code="VIP";
			info=eventdetailsRepo.getShivapurEventDeatils(code);
			if(info!=null) {
				LOG.info("Service : Successfull, Shivapur search for event details : "+info.size());
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for event details");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}
	
	@Override
	public List<Object[]> getShivapurVisitorInformation() {
		LOG.info("Service : Started, Shivapur search for visitor details");
		List<Object[]> info=null;
		try {
			String code="VIP";
			info=visitorDetailsRepo.getShivapurVisitorDeatils(code);
			if(info!=null) {
				LOG.info("Service : Successfull, Shivapur search for visitor details : "+info.size());
			}
		} catch (Exception e) {
			LOG.info("Service : found with issues, Dynamic search for visitor details");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}
	//--------------------- Ended Shivapur Search details object---------------------

	//--------------------- Started LINGABHISKEA details object---------------------
	@Override
	public LingaDetails registerLingaDetailsInformation(LingaDetails ed) {
		LOG.info("Service : entered and Started Lingabhiska details registration process");
		LOG.info("Service : Calling to repository to complete Lingabhiska details registration process");
		LingaDetails nsd=null;
		nsd=lingadetailsRepo.save(ed);
		LOG.info("Service : completed, Lingabhiska details registration process and back to controller");
		return nsd;
	}
	
	@Override
	public List<Object[]> allLingaDetailsInformation() {
		LOG.info("Service : entered and Started to get all the Lingabhiska details process");
		List<Object[]> list=null;
		list=lingadetailsRepo.getAllLingaDeatilsSpecficColList();
		LOG.info("Service : completed, Finished fetching all Lingabhiska details Info, and back to controller..");
		return list;
	}
	
	@Override
	@Transactional
	public List<Object[]> getOneOrAllLingaDetailInformation(String lingaoption) {
		List<Object[]> info=null;
		try {
			info=lingadetailsRepo.getOneOrAllLingaDetails(lingaoption);
			if(info.size()==0)
				LOG.info("Lingabhiska found with :" + info.size());
		} catch (Exception e) {
			LOG.info("Lingabhiska found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}
	@Override
	public void getRemoveLingaDetailsById(Integer lid) {
		LOG.info("Service : entered and Started to remove one Lingabhiska process");
		lingadetailsRepo.deleteById(lid);
		LOG.info("Service : completed, Successfull to remove one Lingabhiska profile, and back to controller..");
	}
	
	@Override
	public Optional<LingaDetails> getViewByLingaID(Integer lid) {
		LOG.info("Service : entered and Started to retriving one Lingabhiska profile using Id :" +lid);
		return lingadetailsRepo.findById(lid);
	}
	
	//--------------------- Ended LINGABHISKEA details object---------------------
	
	//-------------------- Ashram Started -------------------
	@Override
	public List<Object[]> getAllAshramVolunteerList() {
		LOG.info("Started fetching all Ashram Volunteer Info...");
		List<Object[]> list=null;
		String name ="ASHRAM_VOLUNTEER";
		list=yogaDetailsRepo.getAllYogaVolunteerList(name);
		LOG.info("Finished fetching all Ashram Volunteer Info...");
		return list;
	}
	//-------------------- ended Ashram ---------------------------
	
	//---------------Donation Started -----------------------
	@Override
	@Transactional
	public DonationDetails registerDonationInformation(DonationDetails donation) {
	LOG.info("Started donation Registering Info...");
		DonationDetails donationinfo=null;
		donationinfo=donationDetailsRepo.save(donation);
		LOG.info("Finish donation Registering Process");
		return donationinfo;
	}
	@Override
	public List<Object[]> getAllDonationList() {
		LOG.info("Started fetching all Donation Info...");
		List<Object[]> list=null;
		list=donationDetailsRepo.getAllDonationList();
		LOG.info("Finished fetching all Donation Info...");
		return list;
	}
	//-------------- Donation Ended -------------
	
	//------------- Started Gurumal -------------
	@Override
	@Transactional
	public GuruMalaDetails registerGuruMalaInformation(GuruMalaDetails gurumala) {
		LOG.info("Started donation Registering Info...");
		GuruMalaDetails gurumalainfo=null;
		gurumalainfo=guruMalaDetailsRepo.save(gurumala);
		LOG.info("Finish donation Registering Process");
		return gurumalainfo;
	}
	@Override
	public List<Object[]> getAllGuruMalaList() {
		LOG.info("Started fetching all guru mala Info...");
		List<Object[]> list=null;
		list=guruMalaDetailsRepo.getAllGuruMalaList();
		LOG.info("Finished fetching all guru mala Info...");
		return list;
	}

	@Override
	public Optional<GuruMalaDetails> getViewEditByGurumalID(Integer gmid) {
		return guruMalaDetailsRepo.findById(gmid);
	}
	
	@Override
	@Transactional
	public String getUpdateGuruMala(GuruMalaDetails editform) {
	
		LOG.info("Started Updateing "+editform.getGmid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getGmimagedata()!=null)
			ydimg=guruMalaDetailsRepo.getUpdateGuruMalaImageDetails(editform.getGmimagedata(),editform.getGmid());
		
		yd=guruMalaDetailsRepo.getUpdateGuruMalaDetails(editform.getGmindate(),editform.getGmfullname(),editform.getGmfatherhusbandname(),editform.getGmgender(),editform.getGmoccupation(),editform.getGmpresentaddress(),editform.getGmmobilenumber(),editform.getGmemergencyno(),editform.getGmprofilecode(),editform.getGmdateofmonth(),editform.getGmdisease(),editform.getGmid());
		
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getGmid() +" : " +value+" :: Gurumal Image count :"+img);
			return "DONE Updateing "+editform.getGmid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getGmid() +" : " +value);
			return "Not DONE Updateing";
		}
	}
	@Override
	public void getRemoveGuruMalaById(Integer id) {
		guruMalaDetailsRepo.deleteById(id);
	}
	
	@Override
	public List<Object[]> getGuruMalaPDFMonthlyWise(String pdfdocmonthlywise) {
		List<Object[]> gurumalInfo=null;
		try {
			gurumalInfo=guruMalaDetailsRepo.getGuruMalaPDFMonthlyWise(pdfdocmonthlywise);
		} catch (Exception e) {
			LOG.info("Gurumal Pdf Search By month found with issues ");
		}
		return gurumalInfo;
	}
	
	@Override
	@Transactional
	public List<Object[]> getGuruMalaSearchByName(String name) {
		List<Object[]> info=null;
		try {
			info=guruMalaDetailsRepo.getGuruMalaSearchByName(name);
		} catch (Exception e) {
			LOG.info("Yoga Volunteer Search By name found with issues ");
		}
		return info;
	}
	
	@Override
	@Transactional
	public String getUpdateEvent(EventDetails editform) {
	
		LOG.info("Started Updateing "+editform.getEid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getEpersonsimagedata()!=null)
			ydimg=eventdetailsRepo.getUpdateEventImageDetails(editform.getEpersonsimagedata(),editform.getEid());
		
		yd=eventdetailsRepo.getUpdateEventDetails(editform.getEdatetime(),editform.getEname(),editform.getEvenue(),editform.getEpersonnames(),editform.getEpersonmobiles(),editform.getEoccupation(),editform.getEpersonaddress(),editform.getEid());
		
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getEid() +" : " +value+" :: Event Image count :"+img);
			return "DONE Updateing "+editform.getEid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getEid() +" : " +value);
			return "Not DONE Updateing";
		}
	}
	
	@Override
	public List<Object[]> getEventPDFMonthlyWise(String pdfdocmonthlywise) {
		List<Object[]> info=null;
		try {
			info=eventdetailsRepo.getEventPDFMonthlyWise(pdfdocmonthlywise);
		} catch (Exception e) {
			LOG.info("event Pdf Search By month found with issues ");
		}
		return info;
	}
	
	@Override
	@Transactional
	public String getUpdateLingabishake(LingaDetails editform) {
		LOG.info("Started Updateing "+editform.getLid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getLpersonimagedata()!=null)
			ydimg=lingadetailsRepo.getUpdateLingaImageDetails(editform.getLpersonimagedata(),editform.getLid());
		
		yd=lingadetailsRepo.getUpdateLingaDetails(editform.getLdatetime(),editform.getLfullname(),editform.getLfamilyname(),editform.getLphonenumber(),editform.getLoccupation(),editform.getLaddress(),editform.getLid());
		
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getLid() +" : " +value+" :: Linga Image count :"+img);
			return "DONE Updateing "+editform.getLid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getLid() +" : " +value);
			return "Not DONE Updateing";
		}
	}
	
	@Override
	public List<Object[]> getLingaPDFMonthlyWise(String pdfdocmonthlywise) {
		List<Object[]> info=null;
		try {
			info=lingadetailsRepo.getLingaPDFMonthlyWise(pdfdocmonthlywise);
		} catch (Exception e) {
			LOG.info("Linga Pdf Search By month found with issues ");
		}
		return info;
	}
	
	@Override
	@Transactional
	public List<Object[]> getOneOrAllDonationDetailInformation(String name) {
		List<Object[]> info=null;
		try {
			info=donationDetailsRepo.getOneDonationDetailsUsingYogaId(name);
			if(info.size()>=1)
				LOG.info("Yoga found with :" + info.size());
		} catch (Exception e) {
			LOG.info("Yoga found with issues ");
			LOG.info(e.getMessage().toString());
		}
		return info;
	}
	
	@Override
	public void getRemoveDonationDetailsById(Integer did) {
		donationDetailsRepo.deleteById(did);
	}
	@Override
	public Optional<DonationDetails> getViewByDonationID(Integer did) {
		return donationDetailsRepo.findById(did);
	}
	

	@Override
	@Transactional
	public String getUpdateDonation(DonationDetails editform) {
		LOG.info("Started Updateing "+editform.getDid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getDimagedata()!=null)
			ydimg=donationDetailsRepo.getUpdateDonationImageDetails(editform.getDimagedata(),editform.getDid());
		
		yd=donationDetailsRepo.getUpdateDonationDetails(editform.getDindate(),editform.getDitems(),editform.getDfullname(),editform.getDfatherhusbandname(),editform.getDmobilenumber(),editform.getDoccupation(),editform.getDpresentaddress(),editform.getDid());
		
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getDid() +" : " +value+" :: Donation Image count :"+img);
			return "DONE Updateing "+editform.getDid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getDid() +" : " +value);
			return "Not DONE Updateing";
		}
	}
	
	@Override
	public List<Object[]> getDonationPDFMonthlyWise(String pdfdocmonthlywise) {
		List<Object[]> info=null;
		try {
			info=donationDetailsRepo.getDonationPDFMonthlyWise(pdfdocmonthlywise);
		} catch (Exception e) {
			LOG.info("Donation Pdf Search By month found with issues ");
		}
		return info;
	}
	
	@Override
	@Transactional
	public String getUpdateVisitor(VisitorDetails editform) {
		LOG.info("Started Updateing "+editform.getVid());
		Integer yd= 0;
		Integer ydimg= 0;
		
		if(editform.getVimagedata()!=null)
			ydimg=visitorDetailsRepo.getUpdateVisiImageDetails(editform.getVimagedata(),editform.getVid());
		
		yd=visitorDetailsRepo.getUpdateVisitorOnlyDetails(editform.getVfname(),editform.getVfatherhusbandname(),editform.getVphonenumber(),editform.getValternetnumber(),editform.getVdistcity(),editform.getVoccupation(),editform.getVid());
		
		int value=yd;
		int img=ydimg;
		if(value==1) {
			LOG.info("DONE Updateing "+editform.getVid() +" : " +value+" :: Visitore Image count :"+img);
			return "DONE Updateing "+editform.getVid() +" : " +value;
		} else {
			LOG.info("not DONE Updateing "+editform.getVid() +" : " +value);
			return "Not DONE Updateing";
		}
	}
	
	@Override
	public List<Object[]> getVistiorPDFMonthlyWise(String pdfdocmonthlywise) {
		List<Object[]> info=null;
		try {
			info=visitorDetailsRepo.getVisitorPDFMonthlyWise(pdfdocmonthlywise);
		} catch (Exception e) {
			LOG.info("Visitore Pdf Search By month found with issues ");
		}
		return info;
	}
	
	
	  @Override 
	  public List<Object[]> getYogaPDFBatchWise(String pdfdocmonthlywise) { 
		  List<Object[]> info=null; 
		  try {
			  	info=yogaDetailsRepo.getYogaPDFBatchWise(pdfdocmonthlywise); 
		  } catch	(Exception e) { 
			  LOG.info("Visitore Pdf Search By month found with issues ");
		  } 
		  return info; 
	}
	 
	//------------- Ended -------------
	//----------DataBase Back up--------
	@Override
	public List<Object[]> getVisitorBackUpInfo() {
		List<Object[]> info=null;
		try {
			info=visitorDetailsRepo.getVisitorDatabaseBackUp();
		} catch (Exception e) {
			LOG.info("Visitore Database Back Up found with issues ");
		}
		return info;
	}
	@Override
	public List<Object[]> getYogaBackUpInfo() {
		List<Object[]> info=null;
		try {
			info=yogaDetailsRepo.getYogaDatabaseBackUp();
		} catch (Exception e) {
			LOG.info("Yoga Database Back Up found with issues ");
		}
		return info;
	}
	@Override
	public List<Object[]> getGuruMalaBackUpInfo() {
		List<Object[]> info=null;
		try {
			info=guruMalaDetailsRepo.getGuruMalaDatabaseBackUp();
		} catch (Exception e) {
			LOG.info("Gurumala Database Back Up found with issues ");
		}
		return info;
	}
	@Override
	public List<Object[]> getDonationBackUpInfo() {
		List<Object[]> info=null;
		try {
			info=donationDetailsRepo.getDonationDatabaseBackUp();
		} catch (Exception e) {
			LOG.info("Yoga Database Back Up found with issues ");
		}
		return info;
	}
	@Override
	public List<Object[]> getEventBackUpInfo() {
		List<Object[]> info=null;
		try {
			info=eventdetailsRepo.getEventDatabaseBackUp();
		} catch (Exception e) {
			LOG.info("Yoga Database Back Up found with issues ");
		}
		return info;
	}
	@Override
	public List<Object[]> getLingaBackUpInfo() {
		List<Object[]> info=null;
		try {
			info=lingadetailsRepo.getLingaDatabaseBackUp();
		} catch (Exception e) {
			LOG.info("Yoga Database Back Up found with issues ");
		}
		return info;
	}
	//Database Backup ended 

	
	
	
	
	
	
	
	
	
	
	
	
	
}
