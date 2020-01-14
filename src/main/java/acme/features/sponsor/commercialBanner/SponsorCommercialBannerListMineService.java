
package acme.features.sponsor.commercialBanner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.CommercialBanner;
import acme.entities.roles.Sponsor;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractListService;

@Service
public class SponsorCommercialBannerListMineService implements AbstractListService<Sponsor, CommercialBanner> {

	// Internal interface --------------------

	@Autowired
	SponsorCommercialBannerRepository repository;


	// AbstractListService<Sponsor, CommercialBanner> interface ------

	@Override
	public boolean authorise(final Request<CommercialBanner> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<CommercialBanner> request, final CommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "picture", "slogan", "target");
	}

	@Override
	public Collection<CommercialBanner> findMany(final Request<CommercialBanner> request) {
		assert request != null;

		Collection<CommercialBanner> result;

		Sponsor sponsor;
		int sponsorId;
		Principal principal;
		principal = request.getPrincipal();
		sponsorId = principal.getAccountId();
		sponsor = this.repository.findSponsorByUserAccountId(sponsorId);

		principal = request.getPrincipal();
		result = this.repository.findBannerBySponsorId(sponsor.getId());

		return result;
	}

}
