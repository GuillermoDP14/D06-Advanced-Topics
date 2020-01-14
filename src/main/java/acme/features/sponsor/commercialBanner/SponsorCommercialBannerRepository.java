
package acme.features.sponsor.commercialBanner;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banners.CommercialBanner;
import acme.entities.roles.Sponsor;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface SponsorCommercialBannerRepository extends AbstractRepository {

	@Query("select s from Sponsor s where s.userAccount.id = ?1")
	Sponsor findSponsorByUserAccountId(int sponsorId);

	@Query("select c from CommercialBanner c where c.sponsor.id = ?1")
	Collection<CommercialBanner> findBannerBySponsorId(int id);

	@Query("select c from CommercialBanner c where c.id = ?1")
	CommercialBanner findCommercialBannerById(int id);

}
