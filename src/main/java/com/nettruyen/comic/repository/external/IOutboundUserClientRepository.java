package com.nettruyen.comic.repository.external;

import com.nettruyen.comic.dto.request.authentication.ExchangeTokenRequest;
import com.nettruyen.comic.dto.response.authentication.ExchangeTokenResponse;
import com.nettruyen.comic.dto.response.user.OutboundUserResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client", url = "https://www.googleapis.com")
public interface IOutboundUserClientRepository {

    @GetMapping(value = "/oauth2/v1/userinfo")
    OutboundUserResponse getUserInfo(@RequestParam("alt") String alt, @RequestParam("access_token") String accessToken);
}
