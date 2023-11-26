package com.trust.ashram.res.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.YogaDetails;

public interface TrustYogaDetailsRepository extends JpaRepository<YogaDetails, Integer> {

	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yaadharnumber FROM YogaDetails v WHERE v.yidcard like %:name% OR v.yfullname like %:name% OR v.yaadharnumber like %:name% OR v.ymobilenumber like %:name%")
	List<Object[]> getOneOrAllYogaDetails(String name);

	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yaadharnumber FROM YogaDetails v WHERE v.yidcard=:name")
	List<Object[]> getOneYogaDetailsUsingYogaId(String name);
	
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE v.ybatch=:name ORDER BY v.yidcard ASC")
	List<Object[]> getYogaSearchByBatch(String name);
	
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE v.yoccupation=:name")
	List<Object[]> getYogaSearchByOccuption(String name);
	
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE v.yfullname like %:name% OR v.yfatherhusbandname like %:name% OR v.ymobilenumber like %:name%")
	List<Object[]> getYogaSearchByName(String name);

	@Query("SELECT yid,yidcard,ybatch,yfullname,ymobilenumber,ypermanentaddress, yimagedata FROM YogaDetails WHERE ybatch=:ybatch AND yidcard=:yidcard ORDER BY yidcard")
	List<Object[]> getYogaSearchByBatchAndIds(String ybatch,String yidcard);
	
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE v.ybatch=:name AND yprofilecode=:code")
	List<Object[]> getYogaVolunteerSearchByBatch(String name,String code);
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE v.yoccupation=:name AND yprofilecode=:code")
	List<Object[]> getYogaVolunteerSearchByOccupation(String name,String code);
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE v.yidcard=:name AND yprofilecode=:code")
	List<Object[]> getYogaVolunteerSearchByIds(String name,String code);
	@Query("SELECT v.yid,v.yidcard,v.ybatch,v.yfullname,v.ymobilenumber,v.yoccupation FROM YogaDetails v WHERE yprofilecode=:code AND v.yfullname like %:name% OR v.yfatherhusbandname like %:name% OR v.ymobilenumber like %:name% ")
	List<Object[]> getYogaVolunteerSearchByName(String name,String code);
	
	
	Optional<YogaDetails> findByYidcard(String yogaidcard);
	
	@Modifying
	@Query("UPDATE YogaDetails v SET v.yindate=:yindate, v.youtdate=:youtdate, v.ybatch=:ybatch, v.yfullname=:yfullname,v.yfatherhusbandname=:yfatherhusbandname, v.ydateofbirth=:ydateofbirth,v.yage=:yage,v.ygender=:ygender,v.ymaritualstatus=:ymaritualstatus,v.ybloodgroup=:ybloodgroup,v.ynationality=:ynationality,v.yoccupation=:yoccupation,v.ypresentaddress=:ypresentaddress,v.ypermanentaddress=:ypermanentaddress,v.ydisease=:ydisease,v.yaadharnumber=:yaadharnumber,v.ymobilenumber=:ymobilenumber,v.yemergencyno=:yemergencyno,v.yweight=:yweight,v.ybp=:ybp,v.ypulserate=:ypulserate,v.ysugar=:ysugar,v.yprofilecode=:yprofilecode, v.yidcard=:yidcard, v.yinchargesign=:yinchargesign, v.yattendeesign=:yattendeesign,v.ynameopt=:ynameopt WHERE v.yid=:yid")
	Integer getUpdateYogaDetails(String yindate, String youtdate,String ybatch,String yfullname,
			String yfatherhusbandname, String ydateofbirth,String yage,String ygender,				
			String ymaritualstatus,String ybloodgroup,String ynationality,String yoccupation,		 	
			String ypresentaddress,String ypermanentaddress,String ydisease,String yaadharnumber, 		
			String ymobilenumber,String yemergencyno,String yweight,String ybp,String ypulserate,
			String ysugar, String yprofilecode, String yidcard, String yinchargesign, String yattendeesign, String ynameopt, Integer yid);

	@Modifying
	@Query("UPDATE YogaDetails v SET v.yimagedata=:yimagedata WHERE v.yid=:yid ")
	Integer getUpdateYogaImageDetails(byte[] yimagedata, Integer yid);
	
	@Query("SELECT yid,yidcard,ybatch,yfullname,ymobilenumber,ypermanentaddress,yimagedata FROM YogaDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllYogaSpecficColList();

	@Query("SELECT yid,yidcard,ybatch,yfullname,ymobilenumber,yoccupation FROM YogaDetails WHERE yprofilecode=:name")
	List<Object[]> getAllYogaVolunteerList(String name);
	
	@Query("select count(*) from YogaDetails y where y.ybatch=:ybatch AND y.yidcard=:yidcard")
	Integer getCheckYogaIdCardCount(String ybatch,String yidcard);
	
	@Query("SELECT v.yid,v.yfullname,v.ymobilenumber,v.yprofilecode,v.ypermanentaddress FROM YogaDetails v")
	List<Object[]> getDynamicYogaDeatils();

	@Modifying
	@Query("UPDATE YogaDetails v SET v.ycode=:ycode WHERE v.yid=:yid")
	Integer yogaMoveToShivapur(Integer yid, String ycode);

	@Query("SELECT v.yid,v.yfullname,v.ymobilenumber,v.yaadharnumber,v.yprofilecode,v.ycode FROM YogaDetails v WHERE v.ycode=:ycode")
	List<Object[]> getShivapurYogaDeatils(String ycode);

	@Query("SELECT v.yid,v.yfullname,v.ymobilenumber,v.yaadharnumber,v.yprofilecode FROM YogaDetails v WHERE v.yfullname like %:name% OR v.ymobilenumber like %:name% OR v.yaadharnumber like %:name%")
	List<Object[]> getDynamicSearchOptionForYogaOneOrAll(String name);

	Page<YogaDetails> findByYfullname(String string, Pageable pageable1);

	Page<YogaDetails> findByYidcard(String idcard, Pageable pageable);

	Page<YogaDetails> findByYoccupation(String occupation, Pageable pageable);

	Page<YogaDetails> findByYbatch(String batch, Pageable pageable);
	
	@Query("SELECT yid,yindate,youtdate,yidcard,ybatch,yfullname,yfatherhusbandname,ydateofbirth,ygender,ymaritualstatus,ybloodgroup,yoccupation,ypresentaddress,ydisease,yaadharnumber,ymobilenumber,yemergencyno,yweight,ybp,ypulserate,ysugar,ydateofmonth,yprofilecode,yimagedata FROM YogaDetails")
	List<Object[]> getYogaDatabaseBackUp();

	@Query("SELECT  yid,yidcard,ybatch,yfullname,ymobilenumber,ypresentaddress,yimagedata FROM YogaDetails WHERE ybatch=:ybatch")
	List<Object[]> getYogaPDFBatchWise(String ybatch);

	

	
	 
	/*
	 * @Query(nativeQuery =true,
	 * value="SELECT v.yoga_id,v.yoga_full_name,v.yoga_mobile_number,v.yoga_aadhar_number,v.yoga_profile_code FROM yoga_details_tab v WHERE v.yoga_profile_code NOT IN (:names)"
	 * ) List<Object[]> getDynamicYogaDeatils(@Param("names") String names);
	 */}
