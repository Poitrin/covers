package covers

import grails.gorm.services.Service

class UtilityService {
  static final Date LAST_MIDNIGHT = (new Date()).clearTime()

  String getCurrentUserIpAddressHash(request) {
    // TODO: truncate IP address?
    // input.substring(0, input.lastIndexOf('.'))

    // Nginx setzt einen zusätzlichen Header, mit dem wir die "richtige" remoteAddr herausbekommen
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null) {
      // Direkter Zugriff ohne Proxy
      ipAddress = request.getRemoteAddr();
    }
    return ipAddress.hashCode().toString();
  }
}