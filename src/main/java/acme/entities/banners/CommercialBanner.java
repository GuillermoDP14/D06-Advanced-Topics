
package acme.entities.banners;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CommercialBanner extends Banner {

	// Serialisation identifier -----------------------------------------------

	private static final long serialVersionUID = 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@CreditCardNumber
	private String creditCard;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
