package com.solidbrain.russads;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.getbase.Client;
import com.getbase.models.Account;
import com.getbase.models.Address;
import com.getbase.models.AssociatedContact;
import com.getbase.models.Contact;
import com.getbase.models.Deal;
import com.getbase.models.Lead;
import com.getbase.models.LossReason;
import com.getbase.models.Note;
import com.getbase.models.Pipeline;
import com.getbase.models.Source;
import com.getbase.models.Stage;
import com.getbase.models.Tag;
import com.getbase.models.Task;
import com.getbase.models.User;
import com.getbase.services.DealsService;
import com.getbase.sync.Meta;
import com.getbase.sync.Sync;

@Configuration
@EnableScheduling
public class AutomaticTaskCreator {

	@Value("${BASECRM_ACCESS_TOKEN}")
	private String BASECRM_ACCESS_TOKEN;

	@Value("${BASECRM_DEVICE_UUID}")
	private String BASECRM_DEVICE_UUID;

	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	private Client client;

	private List<Long> salesRepIdsWithAutomaticTasks;

	@PostConstruct
	private void initClient() {
		client = new Client(new com.getbase.Configuration.Builder()
				.accessToken(BASECRM_ACCESS_TOKEN)
				.verbose()
				.build());
		salesRepIdsWithAutomaticTasks = new ArrayList<>();
		salesRepIdsWithAutomaticTasks.add(920274L); // Amanda
	}

	@Scheduled(fixedDelay = 5000)
	public void checkForCompanyUpdates() {
		new Sync(client, BASECRM_DEVICE_UUID)
				.subscribe(Contact.class, this::processContact)
				.subscribe(Account.class, (meta, account) -> true)
				.subscribe(Address.class, (meta, address) -> true)
				.subscribe(AssociatedContact.class, (meta, associatedContact) -> true)
				.subscribe(Deal.class, (meta, deal) -> true)
				.subscribe(LossReason.class, (meta, lossReason) -> true)
				.subscribe(Note.class, (meta, note) -> true)
				.subscribe(Pipeline.class, (meta, pipeline) -> true)
				.subscribe(Source.class, (meta, source) -> true)
				.subscribe(Stage.class, (meta, stage) -> true)
				.subscribe(Tag.class, (meta, tag) -> true)
				.subscribe(Task.class, (meta, task) -> true)
				.subscribe(User.class, (meta, user) -> true)
				.subscribe(Lead.class, (meta, lead) -> true)
				.fetch();
	}

	private boolean processContact(Meta meta, Contact contact) {
		if (isCreationEvent(meta) &&
				isContactACompany(contact) &&
				isAutomaticSalesRepId(contact.getOwnerId()) &&
				findDealsByContact(contact.getContactId()).isEmpty()) {
			createDeal(contact);
		}
		return true;
	}

	private void createDeal(Contact contact) {
		Deal deal = new Deal();
		deal.setContactId(contact.getId());
		deal.setOwnerId(contact.getOwnerId());
		deal.setName(createDealName(contact.getName()));
		client.deals().create(deal);
	}

	private String createDealName(String contactName) {
		return contactName + " " + dateFormat.format(new Date());
	}

	private List<Deal> findDealsByContact(Long contactId) {
		return client.deals().list(new DealsService.SearchCriteria().contactId(contactId));
	}

	private boolean isAutomaticSalesRepId(Long userId) {
		return salesRepIdsWithAutomaticTasks.contains(userId);
	}

	private boolean isContactACompany(Contact contact) {
		return Boolean.TRUE.equals(contact.getIsOrganization());
	}

	private boolean isCreationEvent(Meta meta) {
		return meta.getSync().getEventType().equals("created");
	}

}
