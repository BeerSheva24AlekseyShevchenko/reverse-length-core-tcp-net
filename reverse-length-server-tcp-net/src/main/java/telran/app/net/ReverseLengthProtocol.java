package telran.app.net;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class ReverseLengthProtocol implements Protocol {

    @Override
    public Response getResponse(Request request) {
        ResponseCode responseCode = null;
        String responseData = null;

        try {
            ReverseLengthProtocolRequestType requestType = ReverseLengthProtocolRequestType.valueOf(request.requestType());
            responseCode = ResponseCode.OK;
            responseData = getResponseData(requestType, request.requestData());
        } catch (Exception e) {
            responseCode = ResponseCode.WRONG_TYPE;
            responseData = "Invalid request type";
        }

        return new Response(responseCode, responseData);
    }

    private String getResponseData(ReverseLengthProtocolRequestType requestType, String data) {
        String response = null;
        switch (requestType) {
            case REVERSE:
                response = getReversedString(data);
                break;
            case LENGTH:
                response = getStringLength(data);
                break;
            default:
                break;
        }
        return response;
    }

    private static String getStringLength(String str) {
        return String.valueOf(str.length());
    }

    private static String getReversedString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

}
