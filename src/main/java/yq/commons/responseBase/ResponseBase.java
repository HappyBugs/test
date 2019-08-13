package yq.commons.responseBase;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBase {

	private Integer rtnCode;
	private String msg;
	private Object data;

}
