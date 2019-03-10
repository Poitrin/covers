package covers

import grails.gorm.services.Service
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

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

  static String formatToHumanReadableTiming(Integer timing) {
    if (timing == null || timing < 0) {
      return ''
    }

    long millis = timing * 1000;
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1);

    String format = String.format("%d:%02d", Math.abs(minutes), Math.abs(seconds));
    
    return format;
  }

  static Integer formatToTiming(String timingHumanReadable) {
    if (timingHumanReadable == null || timingHumanReadable == '') {
      return null;
    }

    def matches = (timingHumanReadable =~ Pattern.compile(Part.TIMING_REGEX))
    if (matches) {
      Integer seconds = matches.group(1).toInteger() * TimeUnit.MINUTES.toSeconds(1) +
        matches.group(2).toInteger()
      return seconds
    }

    return 1;
  }
}