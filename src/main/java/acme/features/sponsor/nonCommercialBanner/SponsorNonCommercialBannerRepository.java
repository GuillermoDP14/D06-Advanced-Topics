
package acme.features.sponsor.nonCommercialBanner;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banners.NonCommercialBanner;
import acme.entities.configurations.Configuration;
import acme.entities.roles.Sponsor;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface SponsorNonCommercialBannerRepository extends AbstractRepository {

	@Query("select s from Sponsor s where s.userAccount.id = ?1")
	Sponsor findSponsorByUserAccountId(int sponsorId);

	@Query("select c from NonCommercialBanner c where c.sponsor.id = ?1")
	Collection<NonCommercialBanner> findBannerBySponsorId(int id);

	@Query("select c from NonCommercialBanner c where c.id = ?1")
	NonCommercialBanner findNonCommercialBannerById(int id);

	@Query("select c from Configuration c")
	Configuration selectConfiguration();
}
