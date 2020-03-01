package hr.tstrelar.flight.frontend.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * OAuth2RestTemplateConfigurer is to create a bean of type {@link OAuth2RestTemplate}.
 * <p>
 *   This configurer is run only when following properties are set in application.properties.
 * </p>
 *
 *   <code>rest.security.enabled=true</code>
 *   <code>security.oauth2.client.grant-type=client_credentials</code>
 *
 */
@Configuration
@Conditional(value = {OAuth2RestTemplateConfigurer.ServiceAccountEnabled.class})
@Log4j2
public class OAuth2RestTemplateConfigurer {

  @Bean
  public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
    OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);

    log.debug("Begin OAuth2RestTemplate: getAccessToken");
    /* To validate if required configurations are in place during startup */
    oAuth2RestTemplate.getAccessToken();
    log.debug("End OAuth2RestTemplate: getAccessToken");
    return oAuth2RestTemplate;
  }

  /**
   * Condition class to configure OAuth2RestTemplate when both security is enabled and
   * client credentials property is set for secured micro-service
   * to micro-service call.
   */
  static class ServiceAccountEnabled extends AllNestedConditions {

    ServiceAccountEnabled() {
      super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(prefix = "rest.security", value = "enabled", havingValue = "true")
    static class SecurityEnabled {}

    @ConditionalOnProperty(
            prefix = "security.oauth2.client",
            value = "grant-type",
            havingValue = "client_credentials")
    static class ClientCredentialConfigurationExists {}

  }
}
