
package acme.features.sponsor.commercialBanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.CommercialBanner;
import acme.entities.roles.Sponsor;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class SponsorCommercialBannerShowService implements AbstractShowService<Sponsor, CommercialBanner> {

	//		Internal states ------------------

	@Autowired
	private SponsorCommercialBannerRepository repository;


	// AbstractShowService<Sponsor, CommercialBanner> interface -----

	@Override
	public boolean authorise(final Request<CommercialBanner> request) {
		assert request != null;

		boolean result;
		int commercialBannerId;
		CommercialBanner commercialBanner;
		Sponsor sponsor;
		Principal principal;

		commercialBannerId = request.getModel().getInteger("id");
		commercialBanner = this.repository.findCommercialBannerById(commercialBannerId);
		principal = request.getPrincipal();
		sponsor = this.repository.findSponsorByUserAccountId(principal.getAccountId());
		result = commercialBanner.getSponsor().getId() == sponsor.getId();

		return result;
	}

	@Override
	public void unbind(final Request<CommercialBanner> request, final CommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "picture", "slogan", "target", "creditCard");

	}

	@Override
	public CommercialBanner findOne(final Request<CommercialBanner> request) {
		assert request != null;

		CommercialBanner result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findCommercialBannerById(id);

		return result;
	}
}
