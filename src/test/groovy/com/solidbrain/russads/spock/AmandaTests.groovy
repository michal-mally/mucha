package com.solidbrain.russads.spock

import com.getbase.Client
import com.getbase.Configuration
import com.getbase.models.User
import com.getbase.services.UsersService
import spock.lang.Shared
import spock.lang.Specification

class AmandaTests extends Specification {

    @Shared
    String BASECRM_ACCESS_TOKEN = "3cec6c7e60b4030c4dfc08f4007de713adc6417fce0c772522388e9be4ec38da";

    // insert data (usually the database would already contain the data)
    def setupSpec() {
        Client client = new Client(new Configuration.Builder()
                .accessToken(BASECRM_ACCESS_TOKEN)
                .verbose()
                .build());
        UsersService users = client.users();

        Map<String, Object> map = new HashMap<>();
        List<User> userList = users.list(map);
        for (User u : userList) {
            System.out.println(u);
        }
    }

    def "setupSpecOnly"() {
        User user = new User();

        when:
        user.setName("A")

        then:
        user.getName() == "A"

    }

}
