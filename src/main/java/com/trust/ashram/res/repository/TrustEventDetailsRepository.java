package com.trust.ashram.res.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.EventDetails;

public interface TrustEventDetailsRepository extends JpaRepository<EventDetails, Integer>{

	@Query("SELECT eid,edatetime,evenue,ename,epersonnames,epersonmobiles,epersonsimagedata FROM EventDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllEventDeatilsSpecficColList();

	@Query("SELECT eid,edatetime,evenue,ename,epersonnames,epersonmobiles FROM EventDetails WHERE ename like %:name% OR epersonnames like %:name% OR evenue like %:name%")
	List<Object[]> getOneOrAllEventDetails(String name);

	@Query("SELECT v.eid,v.epersonnames,v.epersonmobiles,v.eprofilecode,v.epersonaddress FROM EventDetails v")
	List<Object[]> getDynamicEventDeatils();

	@Modifying
	@Query("UPDATE EventDetails v SET v.ecode=:ecode WHERE v.eid=:eid")
	Integer evnetMoveToShivapur(Integer eid, String ecode);

	@Query("SELECT v.eid,v.epersonnames,v.epersonnames,v.epersonaadhar,v.eprofilecode,v.ecode FROM EventDetails v WHERE v.ecode=:ecode")
	List<Object[]> getShivapurEventDeatils(String ecode);

	@Query("SELECT eid,epersonnames,epersonmobiles,epersonaadhar,eprofilecode FROM EventDetails v WHERE v.epersonnames like %:name% OR v.epersonmobiles like %:name% OR v.epersonaadhar like %:name%")
	List<Object[]> getDynamicSearchOptionForEventOneOrAll(String name);

	@Modifying
	@Query("UPDATE EventDetails v SET v.epersonsimagedata=:epersonsimagedata WHERE v.eid=:eid")
	Integer getUpdateEventImageDetails(byte[] epersonsimagedata, Integer eid);

	@Modifying
	@Query("UPDATE EventDetails v SET v.edatetime=:edatetime, v.ename=:ename, v.evenue=:evenue, v.epersonnames=:epersonnames ,v.epersonmobiles=:epersonmobiles ,v.eoccupation=:eoccupation, v.epersonaddress=:epersonaddress WHERE v.eid=:eid")
	Integer getUpdateEventDetails(String edatetime, String ename, String evenue, String epersonnames,
			String epersonmobiles, String eoccupation, String epersonaddress, Integer eid);
	
	@Query("SELECT eid,edatetime,evenue,ename,epersonnames,epersonmobiles,epersonsimagedata FROM EventDetails WHERE edateofmonth=:edateofmonth")
	List<Object[]> getEventPDFMonthlyWise(String edateofmonth);

	@Query("SELECT eid,edatetime,evenue,ename,epersonnames,epersonmobiles,eoccupation,epersonaddress,eprofilecode,edateofmonth,epersonsimagedata FROM EventDetails")
	List<Object[]> getEventDatabaseBackUp();


}
