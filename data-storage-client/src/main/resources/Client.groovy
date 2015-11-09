import io.vertx.workshop.groovy.data.*

//TODO Change address to the address you chose.
def service = DataStorageService.createProxy(vertx, "devoxx.places")

service.getAllPlaces({
    /**
     * TODO to implement
     */
  result ->
    if (result.failed()) {
      println "Cannot retrieve the list of places : " + result.cause()
    } else {
      println "Done"
      def list = result.result()
      list.each { map -> println map["name"] }
    }
})




