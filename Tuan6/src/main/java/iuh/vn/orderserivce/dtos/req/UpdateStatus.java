package iuh.vn.orderserivce.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateStatus {

	@NotBlank(message = "status is required")
	private String status;
}
