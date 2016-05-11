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

import com.getbase.Client;
import com.getbase.Configuration;
import com.getbase.models.*;
import com.getbase.models.Tag;
import com.getbase.models.Task;
import com.getbase.models.User;
import com.getbase.sync.Meta;
import com.getbase.sync.Sync;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MuchaApplication.class)
@WebAppConfiguration
public class SyncTest {

	@Value("${BASECRM_ACCESS_TOKEN}")
	private String BASECRM_ACCESS_TOKEN;


	@Value("${BASECRM_DEVICE_UUID}")
	private String BASECRM_DEVICE_UUID;

	@Test
	public void contextLoads() {

	}

	@Test
	public void syncFirstStep() {
		Client client = new Client(new Configuration.Builder()
				.accessToken(BASECRM_ACCESS_TOKEN)
				.verbose()
				.build());
		Sync sync = new Sync(client, BASECRM_DEVICE_UUID);

		sync.subscribe(Account.class, (meta, account) -> true)
				.subscribe(Address.class, (meta, address) -> true)
				.subscribe(AssociatedContact.class, (meta, associatedContact) -> true)
				.subscribe(Contact.class, (meta, contact) -> true)
				.subscribe(Deal.class, (meta, deal) -> true)
				.subscribe(LossReason.class, (meta, lossReason) -> true)
				.subscribe(Note.class, (meta, note) -> true)
				.subscribe(Pipeline.class, (meta, pipeline) -> true)
				.subscribe(Source.class, (meta, source) -> true)
				.subscribe(Stage.class, (meta, stage) -> true)
				.subscribe(Tag.class, (meta, tag) -> true)
				.subscribe(Task.class, (meta, task) -> true)
				.subscribe(User.class, (meta, user) -> true)
				.subscribe(Lead.class, (meta, lead) -> processLead(meta, lead, client))
				.fetch();
	}


	public boolean processLead(Meta meta, Lead lead , Client client){
		System.out.println(lead);
		return true;
	}
}
