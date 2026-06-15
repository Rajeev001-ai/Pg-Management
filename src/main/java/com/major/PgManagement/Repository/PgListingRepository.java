package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PgListingRepository extends JpaRepository<PgListing, Long>, JpaSpecificationExecutor<PgListing> {

	List<PgListing> findByStatus(PgStatus status);

	List<PgListing> findByOwner(User owner);

	List<PgListing> findByCityContainingIgnoreCaseOrAreaContainingIgnoreCase(String city, String area);

	List<PgListing> findByStatusAndCityContainingIgnoreCaseOrStatusAndAreaContainingIgnoreCase(
			PgStatus status1,
			String city,
			PgStatus status2,
			String area);

}
