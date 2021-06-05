package alexeyd.com.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static alexeyd.com.consts.Common.ROLE_ADMIN;
import static alexeyd.com.consts.RestNavigation.*;

import static alexeyd.com.consts.Common.ROLE_DRIVER;


@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${spring.security.origins.allow}")
    private String allowOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .allowedOrigins(allowOrigins);
            }
        };
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint)
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, PATH_USER_SIGN_UP).anonymous()

                .antMatchers(HttpMethod.GET, PATH_CHANNELS_GET_ALL_CHANNELS).anonymous()
                .antMatchers(HttpMethod.POST, PATH_CHANNELS_CREATE_NEW_CHANNEL).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, PATH_CHANNELS_DELETE_CHANNEL).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, PATH_CHANNELS_ADD_NEW_MESSAGE).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.PUT, PATH_USER_CHANGE_NICK_NAME).hasAuthority(ROLE_DRIVER)

                .antMatchers(HttpMethod.GET, PATH_AUTH_AUTHENTICATE).anonymous();

        //.antMatchers(HttpMethod.POST, PATH_USER_SIGN_UP).anonymous();

                        /* From my point of view it means that any request except all above will demand basic auth
                        Also this line disables object-info with timestamp, status, error, message, path -fields */
        //.anyRequest().authenticated();

    }

}
