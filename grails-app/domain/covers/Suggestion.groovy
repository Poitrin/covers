package covers

class Suggestion {
  Part part
  String instrument
  Boolean approved = false

  // Timestamps
  Date dateCreated
  Date lastUpdated

  String ipAddressHash

  static belongsTo = [part: Part]

  static constraints = {
    approved defaultValue: false
    instrument blank: false, minSize: 3
  }
}
