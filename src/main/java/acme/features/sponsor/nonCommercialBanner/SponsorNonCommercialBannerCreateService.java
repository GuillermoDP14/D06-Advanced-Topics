
package acme.features.sponsor.nonCommercialBanner;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.NonCommercialBanner;
import acme.entities.configurations.Configuration;
import acme.entities.roles.Sponsor;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class SponsorNonCommercialBannerCreateService implements AbstractCreateService<Sponsor, NonCommercialBanner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	SponsorNonCommercialBannerRepository repository;


	// AbstractCreateService<Sponsor, NonCommercialBanner> interface ---------------

	@Override
	public boolean authorise(final Request<NonCommercialBanner> request) {
		assert request != null;
		Boolean result = true;

		return result;
	}

	@Override
	public void bind(final Request<NonCommercialBanner> request, final NonCommercialBanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "sponsor");
	}

	@Override
	public void unbind(final Request<NonCommercialBanner> request, final NonCommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "picture", "slogan", "target", "jingle");
	}

	@Override
	public NonCommercialBanner instantiate(final Request<NonCommercialBanner> request) {
		assert request != null;

		NonCommercialBanner result;
		result = new NonCommercialBanner();

		Sponsor sponsor;
		int sponsorId;
		Principal principal;
		principal = request.getPrincipal();
		sponsorId = principal.getAccountId();
		sponsor = this.repository.findSponsorByUserAccountId(sponsorId);
		result.setSponsor(sponsor);

		return result;
	}

	@Override
	public void validate(final Request<NonCommercialBanner> request, final NonCommercialBanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		//Comprueba que las cadenas no tienen spam
		Boolean spam1, spam2, spam3, spam4 = null;
		spam1 = this.esSpam(entity.getPicture());
		spam2 = this.esSpam(entity.getSlogan());
		spam3 = this.esSpam(entity.getTarget());
		spam4 = this.esSpam(entity.getJingle());

		errors.state(request, !spam1, "picture", "sponsor.nonCommercialBanner.error.spam");
		errors.state(request, !spam2, "slogan", "sponsor.nonCommercialBanner.error.spam");
		errors.state(request, !spam3, "target", "sponsor.nonCommercialBanner.error.spam");
		errors.state(request, !spam4, "jingle", "sponsor.nonCommercialBanner.error.spam");
	}

	@Override
	public void create(final Request<NonCommercialBanner> request, final NonCommercialBanner entity) {

		this.repository.save(entity);

	}

	public Boolean esSpam(final String cadena) {
		//Inicializamos la variable de resultado, el contador de palabras y el contador de spam
		Boolean esSpam = false;
		Integer palabrasSpam = 0;

		//Con el repositorio llamamos a la
		Configuration c = this.repository.selectConfiguration();
		String listaSpam = c.getSpamWords();

		//Dividimos las palabras spam por coma
		String[] spam = listaSpam.split(",");

		//Recorremos la lista de spam
		for (String s : spam) {
			//Metemos en una variable Pattern cada palabra de la lista para que se compile
			Pattern p = Pattern.compile(s);
			//Pasamos la cadena de texto en una variable Matcher con el Pattern anterior
			Matcher m = p.matcher(cadena);
			//En el Matcher buscamos si se encuentra el Pattern, es decir el termino de spam actual
			while (m.find()) {
				//Como algunos terminos de spam tienen mas de una palabra con StringTokenizer añadimos el numero de palabras del termino de spam
				StringTokenizer stringTokenizer = new StringTokenizer(s);
				Integer ss = stringTokenizer.countTokens();
				palabrasSpam += ss;
			}
		}

		//Contamos el número total de palabras de la cadena
		StringTokenizer stringTokenizer = new StringTokenizer(cadena);
		Integer palabrasTotales = stringTokenizer.countTokens();

		//Dividimos el número anterior entre el número de palabras spam
		Double porcentajeSpam = (double) palabrasSpam / palabrasTotales;

		//Si el porcentaje de palabras spam que aparece en a cadena es mayor que el threehold
		if (porcentajeSpam >= c.getThreshold()) {
			//Entonces la cadena se considera SPAM
			esSpam = true;
		}

		return esSpam;
	}

}
