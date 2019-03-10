package covers

import java.util.concurrent.TimeUnit
import static java.lang.Math.toIntExact

class Part {
  CreativeWork creativeWork
  String name
  Integer timing
  String timingHumanReadable

  String getTimingHumanReadable() {
    if (!this.timingHumanReadable) {
      return UtilityService.formatToHumanReadableTiming(timing)
    }

    return this.timingHumanReadable
  }

  void setTimingHumanReadable(String timingHumanReadable) {
    this.timingHumanReadable = timingHumanReadable
    this.timing = UtilityService.formatToTiming(timingHumanReadable)
  }

  Boolean approved = false

  // Timestamps
  Date dateCreated
  Date lastUpdated

  String ipAddressHash

  static constraints = {
    name blank: false, minSize: 3
    timing nullable: true, min: 0, max: toIntExact(TimeUnit.MINUTES.toSeconds(100))
    timingHumanReadable nullable: true, matches: Part.TIMING_REGEX
  }

  static hasMany = [suggestions: Suggestion]

  Suggestion[] approvedSuggestions(String ipAddressHash) {
    // TODO: Use Grails criteria oder something else to filter via SQL instead of Groovy
    return this.suggestions.findAll { it.approved || (it.ipAddressHash == ipAddressHash) }
  }

  static mapping = {
    approved defaultValue: false
    suggestions sort: 'id', order: 'desc'
  }

  static transients = [
    'timingHumanReadable'
  ]

  static String TIMING_REGEX = '^([0-9]{1,3}):([0-5][0-9])$'
}
