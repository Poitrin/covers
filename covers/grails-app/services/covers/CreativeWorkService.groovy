package covers

import grails.gorm.services.Service

@Service(CreativeWork)
interface CreativeWorkService {

  CreativeWork get(Serializable id)

  List<CreativeWork> list(Map args)

  List<CreativeWork> findAllByApprovedOrDateCreatedGreaterThan(Boolean approved, Date dateCreated)

  List<CreativeWork> findAllByApprovedOrIpAddressHash(Boolean approved, String ipAddressHash)

  Long count()

  void delete(Serializable id)

  CreativeWork save(CreativeWork creativeWork)

}