package edu.jnu.api;

import edu.jnu.api.dto.LockMarketPayOrderRequestDTO;
import edu.jnu.api.dto.LockMarketPayOrderResponseDTO;
import edu.jnu.api.response.Response;

/**
 * 营销交易服务接口
 */
public interface IMarketTradeService {

    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

}
