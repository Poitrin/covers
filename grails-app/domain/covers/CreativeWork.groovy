package covers

import java.util.UUID

class CreativeWork {
  String artist
  /**
   * https://musicbrainz.org/doc/MusicBrainz_Identifier
   * Temporarily disabled.
   * Artist search on MusicBrainz is easy to use, but Recording search creates too many results.
   *
   * Example:
   * Coldplay - The Scientist
   * https://musicbrainz.org/search?query=The+Scientist&type=recording&method=indexed
   * has results from all albums, compilations, etc.
   * We could select on "Works", but e.g.
   * Coldplay - Hypnotised
   * is not listed as Work, only as Recording.
   * https://musicbrainz.org/search?query=Hypnotised&type=work&method=direct --> no correct result
   */
  // UUID artistMBID
  String title
  /**
    * https://musicbrainz.org/doc/MusicBrainz_Identifier
    *
    * MusicBrainz has the following schema:
    * Release group (has many) Release (has many) Recording
    * Example:
    * Artist = Coldplay
    * -> Release group = A Rush of Blood to the Head
    *    -> Release of album in GB / Japan / …
    *       -> Recording of "The Scientist"
    */
  // UUID recordingMBID
  Boolean approved = false
  String youtubeVideoId = null

  // Timestamps
  // From the doc: By default when you have properties called dateCreated and/or lastUpdated in a domain class,
  // Grails automatically maintains their state in the database
  Date dateCreated
  Date lastUpdated

  String ipAddressHash

  static constraints = {
    artist blank: false
    // artistMBID blank: false
    title blank: false
    // recordingMBID blank: false

    /**
     * https://webapps.stackexchange.com/questions/13854/are-youtube-codes-guaranteed-to-always-be-11-characters
     * At the moment (2018-03-10), we assume that every YouTube video ID has exactly 11 characters.
     */
    youtubeVideoId blank: false
    youtubeVideoId minSize: 11
    youtubeVideoId maxSize: 11
  }

  static hasMany = [parts: Part]

  /**
   * Returns all parts that the current user is allowed to see.
   * These are the parts that have been approved, or that have been created by the current user (same IP address, hashed)
   */
  Part[] approvedParts(String ipAddressHash) {
    if (ipAddressHash == null) {
      throw new RuntimeException("ipAddressHash must not be null")
    }
    // TODO: Use Grails criteria oder something else to filter via SQL instead of Groovy
    return this.parts.findAll { it.approved || (it.ipAddressHash == ipAddressHash) }
  }

  static mapping = {
    approved defaultValue: "false"
    // Sometimes sorting only works after restarting (and recompiling) the app
    parts sort: 'id', order: 'asc'
  }
}
