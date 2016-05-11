package com.solidbrain.russads.spock

import com.getbase.Client
import com.getbase.Configuration
import com.getbase.models.Contact
import com.getbase.models.Deal
import com.getbase.models.Stage
import com.getbase.models.User
import com.getbase.services.DealsService
import com.getbase.services.StagesService
import spock.lang.Shared
import spock.lang.Specification

class AmandaTests extends Specification {

    @Shared
    String BASECRM_ACCESS_TOKEN = "3cec6c7e60b4030c4dfc08f4007de713adc6417fce0c772522388e9be4ec38da";

    @Shared
    Client client;

    @Shared
    User amanda;
    @Shared
    User sara;
    @Shared
    User berta;
    @Shared
    Stage incomingStage;

    @Shared
    Contact cocaCola;


    def setupSpec() {
        client = new Client(new Configuration.Builder()
                .accessToken(BASECRM_ACCESS_TOKEN)
                .verbose()
                .build());

        amanda = client.users().get(920274);
        sara = client.users().get(920276);
        berta = client.users().get(920275);

        cocaCola = client.contacts().get(126841959)

        incomingStage = client.stages().list(new StagesService.SearchCriteria().name("Incoming")).get(0);

    }

    def "new Company assign to Amanda"() {

        given:
        String companyName = "TEST_COMPANY_" + Math.random();

        when:
        Contact company = createCompanyContact(companyName, amanda.getId())

        then:
        sleep(20000);
        Deal deal = getDealByName(createDealName(companyName));
        deal != null
        deal.ownerId == amanda.getId()
        deal.getStageId() == incomingStage.getId()
        deal.getContactId() == company.getId()

    }

    def "new Company assign to not-Amanda Berta"() {
        given:
        String companyName = "TEST_COMPANY_" + Math.random();

        when:
        createCompanyContact(companyName, berta.getId())

        then:
        sleep(20000);
        Deal deal = getDealByName(createDealName(companyName));
        deal == null
    }


    def "new Person assign to Amanda"() {

        given:
        String personName = "TEST_PERSON_" + Math.random();

        when:
        createPersonContact(personName, amanda.getId())

        then:
        sleep(20000);
        Deal deal = getDealByName(createDealName(personName));
        deal == null

    }


    def "reasing Company to Amanda, no previous Deals"() {

        given:
        cocaCola.setOwnerId(berta.getId())
        cocaCola = client.contacts().update(cocaCola)
        getDealsByContact(cocaCola.getId()).every {
            client.deals().delete(it.id)
        }

        when:
        cocaCola.setOwnerId(amanda.getId())
        cocaCola = client.contacts().update(cocaCola)

        then:
        sleep(20000);
        List<Deal> deals = getDealsByContact(cocaCola.getId())
        deals.isEmpty()

    }

    def "new Company assign to Amanda, second company exists "() {

        given:
        String companyName = "TEST_COMPANY_" + Math.random();
        cocaCola.setOwnerId(amanda.getId())
        cocaCola = client.contacts().update(cocaCola)

        when:
        Contact company = createCompanyContact(companyName, amanda.getId())

        then:
        sleep(20000);
        Deal deal = getDealByName(createDealName(companyName));
        deal != null
        deal.ownerId == amanda.getId()
        deal.getStageId() == incomingStage.getId()
        deal.getContactId() == company.getId()

        Deal dealCocaCola = getDealByName(createDealName(cocaCola.getName()))
        dealCocaCola == null
    }


    Deal getDealByName(String dealName) {
        Map<String, Object> dealSearchByNameMap = new HashMap<>()
        dealSearchByNameMap.put("name", dealName)
        List<Deal> deals = client.deals().list(dealSearchByNameMap)
        if (deals.isEmpty())
            return null
        return deals.get(0)
    }

    List<Deal> getDealsByContact(Long contactId) {
        return client.deals().list(new DealsService.SearchCriteria().contactId(contactId))
    }

    Contact createCompanyContact(String companyName, long ownerId) {
        Contact company = new Contact()
        company.setIsOrganization(true)
        company.setName(companyName)
        company.setOwnerId(ownerId)
        company = client.contacts().create(company)
        return company
    }

    Contact createPersonContact(String personName, long ownerId) {
        Contact company = new Contact()
        company.setIsOrganization(false)
        company.setLastName(personName)
        company.setOwnerId(ownerId)
        company = client.contacts().create(company)
        return company
    }

    String createDealName(String contactName) {
        return contactName + " " +  new Date().format("dd.MM.yyyy")
    }

    def cleanupSpec() {
        //cleanup??
    }

}
