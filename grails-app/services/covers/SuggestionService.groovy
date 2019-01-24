package covers

import grails.gorm.services.Service

@Service(Suggestion)
interface SuggestionService {

  Suggestion get(Serializable id)

  List<Suggestion> list(Map args)

  Long count()
  Long countByDateCreatedGreaterThan(Date dateCreated)
  Long countByIpAddressHashAndDateCreatedGreaterThan(String ipAddressHash, Date dateCreated)

  void delete(Serializable id)

  Suggestion save(Suggestion suggestion)

}