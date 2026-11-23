package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs;

import java.util.Locale;

import br.edu.fatecfranca.api.entities.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserReq {

	@NotBlank(groups = ValidationGroups.Required.class)
	@Size(min = 2, max = 100)
	@Pattern(regexp = "[\\p{L}\\p{M}][\\p{L}\\p{M} .'-]*", message = "fullname must contain only letters and name separators")
	private String fullname;

	@NotBlank(groups = ValidationGroups.Required.class)
	@Pattern(regexp = "[A-Za-z0-9._-]{3,100}", message = "username must contain 3 to 100 valid characters")
	private String username;

	@NotBlank(groups = ValidationGroups.Required.class)
	@Pattern(regexp = ".*\\S.*", message = "email must not be blank")
	@Email
	@Size(max = 254)
	private String email;

	@NotBlank(groups = ValidationGroups.Required.class)
	@Size(min = 8, max = 72)
	private String password;

	public Users toEntity() {
		Users user = new Users();
		user.setFullname(fullname == null ? null : fullname.trim());
		user.setUsername(username);
		user.setEmail(email == null ? null : email.trim().toLowerCase(Locale.ROOT));
		user.setPassword(password);
		return user;
	}

	public boolean hasAnyField() {
		return fullname != null || username != null || email != null || password != null;
	}

	public String getFullname() { return fullname; }
	public void setFullname(String fullname) { this.fullname = fullname; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
