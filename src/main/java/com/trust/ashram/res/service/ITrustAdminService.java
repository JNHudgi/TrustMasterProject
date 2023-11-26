package com.trust.ashram.res.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

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

public interface ITrustAdminService {
	
	public AdminDetails registerAdminInformation(AdminDetails info);

	public Optional<AdminDetails> getAdminDetailsByEmailID(String email);

	public VisitorDetails registerVisitorInformation(VisitorDetails vdetails);

	public List<Object[]> allVisitorInformation();
	
	public List<Object[]> singleVisitorInformation(String others);
	
	public List<Object[]> visitorListByDate(String others);
	
	public List<Object[]> visitorListByOccpation(String others);

	public void getRemoveVisitorById(Integer id);

	public List<VisitorDetails> findPaginated(int pageNo, int pageSize);

	public Long getVisitorDetaislCount();

	public boolean isEmployeeExistByEmail(String email);

	public Optional<VisitorDetails> getViewByVisitorID(Integer vid);

	public String getValidateUsedLoginIdAsEmail(String emmail);

	public String getValidatePanNumber(String vpannumber);

	public String getValidateAadharNumber(String vaadher);

	public String getValidatePhoneNumber(String vphonenumber);
	
	public boolean getAdminEmailCount(String aemail);

	public List<AdminDetails> getAllEmployeeList();

	public String getRemoveEmployeeByEmail(Integer aid);
	
	public AdminDetails getEmployeeByAID(Integer aid);

	public Optional<AdminDetails> getViewByEmployeeEmail(String aemail);

	public YogaDetails registerYogaInformation(YogaDetails yoga);
	
	public VolunteerDetails registerYogaVolunteerInformation(VolunteerDetails yogaVolunteer);
	
	public DonationDetails registerDonationInformation(DonationDetails donation);
	public GuruMalaDetails registerGuruMalaInformation(GuruMalaDetails gurumala);

	//public List<YogaDetails> allYogaInformation();
	public List<Object[]> allYogaInformation();
	
	
	public List<Object[]> getAllYogaVolunteerList();
	
	public List<Object[]> getAllAshramVolunteerList();
	public List<Object[]> getAllDonationList();
	public List<Object[]> getAllGuruMalaList();
	
	public Optional<YogaDetails> getViewByYogaID(Integer yid);
	
	public Optional<VolunteerDetails> getViewByYogaVolunteerID(Integer yid);
	
	public Optional<YogaDetails> getViewByYogaIdCard(String yogaidcard);

	public PictureDetails registerPictureInformation(PictureDetails picture);

	public Map<String, PictureDetails> getAllPictures();

	public List<Object[]> getOneOrAllYogaInformation(String name);
	
	public List<Object[]> getYogaSearchByOccuption(String name);
	
	public List<Object[]> getYogaSearchByBatch(String name);
	
	public List<Object[]> getYogaSearchByName(String name);
	
	public List<Object[]> getYogaVolunteerSearchByBatch(String name);
	public List<Object[]> getYogaVolunteerSearchByOccuption(String name);
	public List<Object[]> getYogaVolunteerSearchByIds(String name);
	public List<Object[]> getYogaVolunteerSearchByName(String name);
	
	public String registerYogaEditInformation(YogaDetails editform);
	
	public String registerYogaVolunteerEditViewInformation(VolunteerDetails editform);

	void getRemoveYogaById(Integer id);

	void getRemoveYogaVolunteerById(Integer id);
	
	public NightStayDetails registerNightStayInformation(NightStayDetails ns);

	public List<Object[]> allNightSatyInformation();

	public void getRemoveNightStayById(Integer nsid);

	public List<Object[]> getOneOrAllNightStayInformation(String name);

	public Optional<NightStayDetails> getViewByNightStayID(Integer nightStayId);

	public String registerNightStayEditInformation(NightStayDetails ns);
	
	public EventDetails registerEventDetailsInformation(EventDetails eds);

	public List<Object[]> allEventDetailsInformation();

	public List<Object[]> getOneOrAllEventDetailInformation(String eventoption);

	public void getRemoveEventDetailsById(Integer eid);

	public Optional<EventDetails> getViewByEventID(Integer eventId);

	//public List<Object[]> allDynamicSearchInformation();

	List<Object[]> allVisitoreDetailsInformation();

	public List<Object[]> geDynamicVisitorInformation();

	public List<Object[]> geDynamicYogaInformation();

	public List<Object[]> geDynamicNightStayInformation();

	public List<Object[]> geDynamicEventInformation();

	public Integer nightStayProMoveToShivapur(Integer nsid);

	public Integer yogaProMoveToShivapur(Integer yid);

	Integer eventProMoveToShivapur(Integer eid);
	
	public List<Object[]> getShivapurYogaInformation();
	
	public List<Object[]> getShivapurNightStayInformation();

	public List<Object[]> getShivapurEventInformation();

	public List<Object[]> getShivapurVisitorInformation();

	public Integer visitorProMoveToShivapur(Integer yid);

	public List<Object[]> geDynamicSearchOptionYogaInformation(String dynamicoption);

	public List<Object[]> geDynamicSearchOptionNightStayInformation(String dynamicoption);

	public List<Object[]> geDynamicSearchOptionEventInformation(String dynamicoption);

	public List<Object[]> geDynamicSearchOptionVisitorInformation(String dynamicoption);

	public LingaDetails registerLingaDetailsInformation(LingaDetails ed);

	public List<Object[]> allLingaDetailsInformation();

	public List<Object[]> getOneOrAllLingaDetailInformation(String lingaoption);

	public void getRemoveLingaDetailsById(Integer lid);

	public Optional<LingaDetails> getViewByLingaID(Integer lingabhishekaId);

	//public String registerLingaEditInformation(LingaDetails ns);
	
	public Page<YogaDetails> viewAllYogaPage(int pageNo, int pageSize, String sortField, String sortDirection);
	public Page<YogaDetails> getYogaSearchByBatchWitPagination(int pageNo, int pageSize, String sortField, String sortDirection, String batch);
	public Page<YogaDetails> getYogaSearchByOccupationWitPagination(int pageNo, int pageSize, String sortField,String sortDirection, String occupation);
	public Page<YogaDetails> getYogaSearchByIdcardWitPagination(int pageNo, int pageSize, String sortField,String sortDirection, String idcard);
	public Page<YogaDetails> getYogaSearchByNameWitPagination(int pageNo, int pageSize, String sortField, String sortDirection,String name);

	public String getCheckYogaIdCardCount(String yidcard, String ybatch);

	public List<Object[]> yogaSearchByBatchAndIds(String yogasearchbyids, String yogasearchbybatch);

	public Optional<GuruMalaDetails> getViewEditByGurumalID(Integer gmid);
	public String getUpdateGuruMala(GuruMalaDetails editform);
	public void getRemoveGuruMalaById(Integer id);
	public List<Object[]> getGuruMalaPDFMonthlyWise(String pdfdocmonthlywise);
	public List<Object[]> getGuruMalaSearchByName(String name);

	public String getUpdateEvent(EventDetails editform);
	public List<Object[]> getEventPDFMonthlyWise(String pdfdocmonthlywise);
	
	public String getUpdateLingabishake(LingaDetails editform);

	public List<Object[]> getLingaPDFMonthlyWise(String pdfdocmonthlywise);

	public List<Object[]> getOneOrAllDonationDetailInformation(String donationoption);
	public void getRemoveDonationDetailsById(Integer did);
	public Optional<DonationDetails> getViewByDonationID(Integer did);
	public String getUpdateDonation(DonationDetails editform);

	public List<Object[]> getDonationPDFMonthlyWise(String pdfdocmonthlywise);

	public String getUpdateVisitor(VisitorDetails editform);

	public List<Object[]> getVistiorPDFMonthlyWise(String pdfdocmonthlywise);

	public List<Object[]> getVisitorBackUpInfo();
	public List<Object[]> getYogaBackUpInfo();
	public List<Object[]> getGuruMalaBackUpInfo();
	public List<Object[]> getEventBackUpInfo();
	public List<Object[]> getLingaBackUpInfo();
	public List<Object[]> getDonationBackUpInfo();

	public List<Object[]> getYogaPDFBatchWise(String pdfdocbatchywise);

	
	
}
