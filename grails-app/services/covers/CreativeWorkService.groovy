package covers

import grails.gorm.services.Service
import grails.gorm.services.Where

@Service(CreativeWork)
interface CreativeWorkService {

  CreativeWork get(Serializable id)

  List<CreativeWork> list(Map args)

  List<CreativeWork> findAllByApprovedOrIpAddressHash(Boolean approved, String ipAddressHash)

  @Where({
    (approved == true || ipAddressHash == currentIpAddressHash) &&
    (title =~ "%${query}%" || artist =~ "%${query}%")
  })
  List<CreativeWork> searchCreativeWorks(String currentIpAddressHash, String query)

  Long count()
  Long countByDateCreatedGreaterThan(Date dateCreated)
  Long countByIpAddressHashAndDateCreatedGreaterThan(String ipAddressHash, Date dateCreated)

  void delete(Serializable id)

  CreativeWork save(CreativeWork creativeWork)

}