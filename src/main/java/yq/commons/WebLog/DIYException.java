package yq.commons.WebLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yq.commons.responseBase.ResponseBase;

/**
 * 自定义异常
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DIYException extends RuntimeException{

    String message;

}
