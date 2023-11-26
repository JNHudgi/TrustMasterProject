package com.trust.ashram.res.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.trust.ashram.res.model.DonationDetails;

public interface TrustDonationDetailsRepository extends JpaRepository<DonationDetails, Integer> {

	@Query("SELECT did,ditems,dfullname,dmobilenumber,doccupation,dpresentaddress,dimagedata FROM DonationDetails WHERE ROWNUM <= 10")
	List<Object[]> getAllDonationList();

	@Query("SELECT did,ditems,dfullname,dmobilenumber,doccupation,dpresentaddress FROM DonationDetails WHERE dfullname like %:dfullname%")
	List<Object[]> getOneDonationDetailsUsingYogaId(String dfullname);

	@Modifying
	@Query("UPDATE DonationDetails v SET v.dimagedata=:dimagedata WHERE v.did=:did")
	Integer getUpdateDonationImageDetails(byte[] dimagedata, Integer did);

	@Modifying
	@Query("UPDATE DonationDetails v SET v.dindate=:dindate, v.ditems=:ditems, v.dfullname=:dfullname, v.dfatherhusbandname=:dfatherhusbandname ,v.dmobilenumber=:dmobilenumber ,v.doccupation=:doccupation, v.dpresentaddress=:dpresentaddress WHERE v.did=:did")
	Integer getUpdateDonationDetails(String dindate, String ditems, String dfullname, String dfatherhusbandname,
			String dmobilenumber, String doccupation, String dpresentaddress, Integer did);

	@Query("SELECT did,ditems,dfullname,dmobilenumber,doccupation,dpresentaddress,dimagedata FROM DonationDetails WHERE ddateofmonth=:ddateofmonth")
	List<Object[]> getDonationPDFMonthlyWise(String ddateofmonth);

	@Query("SELECT did,dindate,ditems,dfullname,dfatherhusbandname,dmobilenumber,doccupation,dpresentaddress,dprofilecode,ddateofmonth,dimagedata FROM DonationDetails ")
	List<Object[]> getDonationDatabaseBackUp();

}
