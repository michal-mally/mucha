package com.solidbrain.russads;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.getbase.*;
import com.getbase.models.User;
import com.getbase.services.AccountsService;
import com.getbase.services.UsersService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MuchaApplication.class)
@WebAppConfiguration
public class MuchaApplicationTests {


	@Value("${BASECRM_ACCESS_TOKEN}")
	private String BASECRM_ACCESS_TOKEN;

	@Test
	public void contextLoads() {

	}

	@Test
	public void connectionText() {
		Client client = new Client(new Configuration.Builder()
				.accessToken(BASECRM_ACCESS_TOKEN)
				.verbose()
				.build());
		UsersService users=  client.users();

		Map<String, Object> map = new HashMap<>();
		List<User> userList =  users.list(map);
		userList.stream().forEach(System.out::println);
	}

}
