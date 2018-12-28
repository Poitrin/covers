package covers

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class SuggestionServiceSpec extends Specification {

    SuggestionService suggestionService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Suggestion(...).save(flush: true, failOnError: true)
        //new Suggestion(...).save(flush: true, failOnError: true)
        //Suggestion suggestion = new Suggestion(...).save(flush: true, failOnError: true)
        //new Suggestion(...).save(flush: true, failOnError: true)
        //new Suggestion(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //suggestion.id
    }

    void "test get"() {
        setupData()

        expect:
        suggestionService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Suggestion> suggestionList = suggestionService.list(max: 2, offset: 2)

        then:
        suggestionList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        suggestionService.count() == 5
    }

    void "test delete"() {
        Long suggestionId = setupData()

        expect:
        suggestionService.count() == 5

        when:
        suggestionService.delete(suggestionId)
        sessionFactory.currentSession.flush()

        then:
        suggestionService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Suggestion suggestion = new Suggestion()
        suggestionService.save(suggestion)

        then:
        suggestion.id != null
    }
}
