package alexeyd.com.security;

import alexeyd.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.Filter;

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

                .antMatchers(HttpMethod.GET, "/channels/getAllChannels").anonymous()
                .antMatchers(HttpMethod.POST, "/channels/createNewChannel").hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/channels/deleteChannel").hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/channels/addNewMessage").hasAuthority(ROLE_DRIVER)

                .antMatchers(HttpMethod.PUT, PATH_USER_EDIT_ME).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.GET, PATH_USER_GET_ME).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.GET, PATH_USER_ALL).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, PATH_USER_ALL_DRIVERS).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, PATH_USER_GET_BY_ID).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, PATH_USER_GET_ADMIN).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, PATH_USER_EDIT).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, PATH_USER_CHANGE_STATUS).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, PATH_USER_DELETE).hasAuthority(ROLE_ADMIN)

                .antMatchers(HttpMethod.GET, PATH_TASK_ALL_MINE).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.GET, PATH_TASK_BY_STATUS).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.GET, PATH_TASK_ALL).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, PATH_TASK_CHANGE_TASK_BY_TASK_ID).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.PUT, PATH_TASK_CHANGE_TASK_TO_REJECTED_OR_FINISHED_STATUS).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, PATH_TASK_TAKE_TASK).hasAnyAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.POST, PATH_TASK_CREATE_NEW).hasAuthority(ROLE_ADMIN)

                .antMatchers(HttpMethod.GET, PATH_REPORT_GET_BY_TASK_ID).authenticated()
                .antMatchers(HttpMethod.POST, PATH_REPORT_CREATE_REPORT).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.GET, PATH_REPORT_ALL).hasAuthority(ROLE_ADMIN)

                .antMatchers(HttpMethod.GET, PATH_CAR_ALL_FREE).hasAuthority(ROLE_DRIVER)
                .antMatchers(HttpMethod.GET, PATH_CAR_ALL).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, PATH_CAR_ADD_NEW).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, PATH_CAR_EDIT).hasAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, PATH_CAR_REMOVE_BY_ID).hasAuthority(ROLE_ADMIN)

                .antMatchers(HttpMethod.GET, PATH_BRAND_ALL).hasAuthority(ROLE_ADMIN)

                .antMatchers(HttpMethod.GET, PATH_AUTH_AUTHENTICATE).anonymous();

        //.antMatchers(HttpMethod.POST, PATH_USER_SIGN_UP).anonymous();

                        /* From my point of view it means that any request except all above will demand basic auth
                        Also this line disables object-info with timestamp, status, error, message, path -fields */
        //.anyRequest().authenticated();

    }

}
