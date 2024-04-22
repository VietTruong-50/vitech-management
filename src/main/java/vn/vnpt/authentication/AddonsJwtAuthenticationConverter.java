package vn.vnpt.authentication;

import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddonsJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

	@Override
	public JwtAuthenticationToken convert(@Nonnull Jwt source) {
		final var authorities = new JwtGrantedAuthoritiesConverter().convert(source);
		final String username = JsonPath.read(source.getClaims(), "preferred_username");
		return new JwtAuthenticationToken(source, authorities, username);
	}
}
