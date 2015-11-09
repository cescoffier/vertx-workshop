require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.workshop.data.DataStorageService
module VertxMicroserviceWorkshop
  #  Service exposed on the event bus to provide access to
  #  the stored Places.
  class DataStorageService
    # @private
    # @param j_del [::VertxMicroserviceWorkshop::DataStorageService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxMicroserviceWorkshop::DataStorageService] the underlying java delegate
    def j_del
      @j_del
    end
    #  Method called to create a proxy (to consume the service).
    # @param [::Vertx::Vertx] vertx vert.x
    # @param [String] address the address on the vent bus where the service is served.
    # @return [::VertxMicroserviceWorkshop::DataStorageService] the proxy on the {::VertxMicroserviceWorkshop::DataStorageService}
    def self.create_proxy(vertx=nil,address=nil)
      if vertx.class.method_defined?(:j_del) && address.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxWorkshopData::DataStorageService.java_method(:createProxy, [Java::IoVertxCore::Vertx.java_class,Java::java.lang.String.java_class]).call(vertx.j_del,address),::VertxMicroserviceWorkshop::DataStorageService)
      end
      raise ArgumentError, "Invalid arguments when calling create_proxy(vertx,address)"
    end
    #  Retrieves all places.
    # @yield the result handler
    # @return [void]
    def get_all_places
      if block_given?
        return @j_del.java_method(:getAllPlaces, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_all_places()"
    end
    #  Retrieves all places belonging to the given category.
    # @param [String] category the category
    # @yield the result handler
    # @return [void]
    def get_places_for_category(category=nil)
      if category.class == String && block_given?
        return @j_del.java_method(:getPlacesForCategory, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(category,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_places_for_category(category)"
    end
    #  Retrieves all places belonging with the given tag.
    # @param [String] tag the tag
    # @yield the result handler
    # @return [void]
    def get_places_for_tag(tag=nil)
      if tag.class == String && block_given?
        return @j_del.java_method(:getPlacesForTag, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(tag,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| elt != nil ? JSON.parse(elt.toJson.encode) : nil } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling get_places_for_tag(tag)"
    end
    #  Adds a place in the data storage service.
    # @param [Hash] place the place
    # @yield handler called when the insertion is done.
    # @return [void]
    def add_place(place=nil)
      if place.class == Hash && block_given?
        return @j_del.java_method(:addPlace, [Java::IoVertxWorkshopData::Place.java_class,Java::IoVertxCore::Handler.java_class]).call(Java::IoVertxWorkshopData::Place.new(::Vertx::Util::Utils.to_json_object(place)),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling add_place(place)"
    end
  end
end
