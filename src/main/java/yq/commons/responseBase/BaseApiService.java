package yq.commons.responseBase;


import org.springframework.stereotype.Component;

@Component
public class BaseApiService {

	public ResponseBase setResultError(String message) {
		// 返回错误，可以传msg
		return setResult(Constants.HTTP_RES_CODE_500, message, null);
	}

	//返回成功 可以传递参数和消息
	public ResponseBase setResultSuccessData(Object data,String message) {
		return setResult(Constants.HTTP_RES_CODE_200, message, data);
	}

	// 返回成功，沒有data值
	public ResponseBase setResultSuccess(String message) {
		return setResult(Constants.HTTP_RES_CODE_200, message, null);
	}


	public ResponseBase setResult(Integer status, String message, Object data) {
		return new ResponseBase(status, message, data);
	}

}
