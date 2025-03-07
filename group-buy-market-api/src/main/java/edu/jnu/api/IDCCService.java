package edu.jnu.api;

import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.jnu.api.response.Response;

public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);
}
