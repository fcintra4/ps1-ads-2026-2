package br.edu.fatecfranca.api.controllers.dtos;

import java.time.LocalDate;

import br.edu.fatecfranca.api.entities.Customers;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerDto{

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotBlank
	@Size(min = 5, max = 20)
	private String identDocument;

	@PastOrPresent
	private LocalDate birthDate;

	@NotBlank
	@Size(max = 200)
	private String streetName;

	@NotBlank
	@Size(max = 20)
	private String houseNumber;

	private String complements;

	@NotBlank
	@Size(max = 100)
	private String district;

	@NotBlank
	@Size(max = 100)
	private String municipality;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{2}$", message = "state must be two uppercase letters")
	private String state;

	@NotBlank
	@Pattern(regexp = "^[0-9+()\\-\\s]{8,20}$", message = "invalid phone")
	private String phone;

	@NotBlank
	@Email
	private String email;

	public CustomerDto() {}

	public CustomerDto(Customers c) {
		if (c == null) return;
		this.name = c.getName();
		this.identDocument = c.getIdentDocument();
		this.birthDate = c.getBirthDate();
		this.streetName = c.getStreetName();
		this.houseNumber = c.getHouseNumber();
		this.complements = c.getComplements();
		this.district = c.getDistrict();
		this.municipality = c.getMunicipality();
		this.state = c.getState();
		this.phone = c.getPhone();
		this.email = c.getEmail();
	}

	public Customers toEntity() {
		Customers c = new Customers();
		c.setName(this.name);
		c.setIdentDocument(this.identDocument);
		c.setBirthDate(this.birthDate);
		c.setStreetName(this.streetName);
		c.setHouseNumber(this.houseNumber);
		c.setComplements(this.complements);
		c.setDistrict(this.district);
		c.setMunicipality(this.municipality);
		c.setState(this.state);
		c.setPhone(this.phone);
		c.setEmail(this.email);
		return c;
	}

	public boolean isValidForUpdate() {
		return (this.name != null && !this.name.isBlank())
			|| (this.identDocument != null && !this.identDocument.isBlank())
			|| (this.streetName != null && !this.streetName.isBlank())
			|| (this.houseNumber != null && !this.houseNumber.isBlank())
			|| (this.district != null && !this.district.isBlank())
			|| (this.municipality != null && !this.municipality.isBlank())
			|| (this.state != null && !this.state.isBlank())
			|| (this.birthDate != null)
			|| (this.complements != null)
			|| (this.phone != null && !this.phone.isBlank())
			|| (this.email != null && !this.email.isBlank());
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getIdentDocument() { return identDocument; }
	public void setIdentDocument(String identDocument) { this.identDocument = identDocument; }

	public LocalDate getBirthDate() { return birthDate; }
	public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

	public String getStreetName() { return streetName; }
	public void setStreetName(String streetName) { this.streetName = streetName; }

	public String getHouseNumber() { return houseNumber; }
	public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

	public String getComplements() { return complements; }
	public void setComplements(String complements) { this.complements = complements; }

	public String getDistrict() { return district; }
	public void setDistrict(String district) { this.district = district; }

	public String getMunicipality() { return municipality; }
	public void setMunicipality(String municipality) { this.municipality = municipality; }

	public String getState() { return state; }
	public void setState(String state) { this.state = state; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

}
