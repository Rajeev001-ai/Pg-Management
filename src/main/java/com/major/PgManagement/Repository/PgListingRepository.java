package com.major.pgmanagement.repository;

import com.major.pgmanagement.entity.PgListing;
import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.PgStatus;
import com.major.pgmanagement.entity.enums.PgType;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PgListingRepository extends JpaRepository<PgListing, Long> {

	List<PgListing> findByStatus(PgStatus status);

	List<PgListing> findByOwner(User owner);

	List<PgListing> findByCityContainingIgnoreCaseOrAreaContainingIgnoreCase(String city, String area);

	List<PgListing> findByStatusAndCityContainingIgnoreCaseOrStatusAndAreaContainingIgnoreCase(
			PgStatus status1,
			String city,
			PgStatus status2,
			String area);

	@Query("""
			SELECT pg FROM PgListing pg
			WHERE pg.status = :status
			AND (
				:keyword IS NULL
				OR LOWER(pg.city) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR LOWER(pg.area) LIKE LOWER(CONCAT('%', :keyword, '%'))
			)
			AND (:pgType IS NULL OR pg.pgType = :pgType)
			AND (:minRent IS NULL OR pg.monthlyRentStartingFrom >= :minRent)
			AND (:maxRent IS NULL OR pg.monthlyRentStartingFrom <= :maxRent)
			""")
	List<PgListing> searchApprovedPgListings(
			@Param("status") PgStatus status,
			@Param("keyword") String keyword,
			@Param("pgType") PgType pgType,
			@Param("minRent") BigDecimal minRent,
			@Param("maxRent") BigDecimal maxRent);
}
