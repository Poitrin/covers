package covers

import grails.gorm.services.Service

class UtilityService {
  String getCurrentUserIpAddressHash(request) {
    // TODO: truncate IP address?
    // input.substring(0, input.lastIndexOf('.'))
    return request.getRemoteAddr().hashCode().toString();
  }
}