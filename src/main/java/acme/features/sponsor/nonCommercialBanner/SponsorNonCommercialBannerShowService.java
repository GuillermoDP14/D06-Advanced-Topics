
package acme.features.sponsor.nonCommercialBanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.NonCommercialBanner;
import acme.entities.roles.Sponsor;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class SponsorNonCommercialBannerShowService implements AbstractShowService<Sponsor, NonCommercialBanner> {

	//		Internal states ------------------

	@Autowired
	private SponsorNonCommercialBannerRepository repository;


	// AbstractShowService<Sponsor, NonCommercialBanner> interface -----

	@Override
	public boolean authorise(final Request<NonCommercialBanner> request) {
		assert request != null;

		boolean result;
		int commercialBannerId;
		NonCommercialBanner commercialBanner;
		Sponsor sponsor;
		Principal principal;

		commercialBannerId = request.getModel().getInteger("id");
		commercialBanner = this.repository.findNonCommercialBannerById(commercialBannerId);
		principal = request.getPrincipal();
		sponsor = this.repository.findSponsorByUserAccountId(principal.getAccountId());
		result = commercialBanner.getSponsor().getId() == sponsor.getId();

		return result;
	}

	@Override
	public void unbind(final Request<NonCommercialBanner> request, final NonCommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "picture", "slogan", "target", "jingle");

	}

	@Override
	public NonCommercialBanner findOne(final Request<NonCommercialBanner> request) {
		assert request != null;

		NonCommercialBanner result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findNonCommercialBannerById(id);

		return result;
	}
}
