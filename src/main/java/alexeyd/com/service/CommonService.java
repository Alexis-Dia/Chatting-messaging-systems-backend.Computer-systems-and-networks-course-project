package alexeyd.com.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Alexey Druzik on 31.05.2021
 */
@Component
@AllArgsConstructor
public class CommonService {

  @Autowired
  private Environment env;

  public int getSecretKey() {
    return Integer.parseInt(env.getProperty("secretKey"));
  }
}
