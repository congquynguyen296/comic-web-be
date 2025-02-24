package com.nettruyen.comic.exception;

import com.nettruyen.comic.dto.request.authentication.IntrospectRequest;
import com.nettruyen.comic.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    // Đây là phương thức tự động decode token ngay khi vào filter của Security
    // Tránh việc phải gọi API để thực hiện decode (introspect ở service)

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Autowired
    private IAuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {

        // Check xem token còn hiệu lực hay không qua hàm introspect
        var response = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build());
        log.error(ErrorCode.INVALID_EXPIRED_TOKEN.getMessage());

        if (!response.isValid()) throw new AppException(ErrorCode.INVALID_EXPIRED_TOKEN);

        // Nếu chưa thì mới tiếp tục chuyển tới cho nimbus thực hiện chức năng của nó là decode token
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
