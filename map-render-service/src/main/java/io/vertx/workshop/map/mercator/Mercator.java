/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.mercator;

import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.floor;
import static java.lang.StrictMath.log;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sinh;
import static java.lang.StrictMath.tan;
import static java.lang.StrictMath.toDegrees;
import static java.lang.StrictMath.toRadians;

import io.vertx.workshop.map.BoundingBox;
import io.vertx.workshop.map.Coordinate;

/**
 * Utility class to handle the Mercator projection calculations.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Mercator_projection">Mercator projection</a>
 * @author Paulo Lopes
 */
public final class Mercator {

	private Mercator() {
		// disable instantiation
	}

	private static int numTiles(double z) {
		return (int) pow(2.0, z);
	}

	private static double sec(double x) {
		return (1.0 / cos(x));
	}

	private static double mercatorToLat(double y) {
		return toDegrees(atan(sinh(y)));
	}

  /**
   * Convert a lat/lon to x,y based on the Mercator Projection
   *
   * @param lat latitude
   * @param lon longitude
   * @param z zoom level
   * @param tilesize size of the generated tiles
   *
   * @return coordinate
   */
	public static Coordinate coord2xy(double lat, double lon, int z, int tilesize) {
		int numTiles_by_tilesize = numTiles(z) * tilesize;
		double rad_lat = toRadians(lat);

		return new Coordinate(
				numTiles_by_tilesize * (lon + 180.0) / 360.0,
				numTiles_by_tilesize * (1.0 - log(tan(rad_lat) + sec(rad_lat)) / PI) / 2.0);
	}

  /**
   * Convert a lat/lon to x,y based on the Mercator Projection without accounting for the tile size and zoom level.
   *
   * @param lat latitude
   * @param lon longitude
   *
   * @return coordinate
   */
	public static Coordinate latlon2relativeXY(double lat, double lon) {
		double rad_lat = toRadians(lat);

		return new Coordinate(
				(lon + 180.0) / 360.0,
				(1 - log(tan(rad_lat) + sec(rad_lat)) / PI) / 2.0);
	}

  /**
   * Convert a lat/lon to x,y based on the Mercator Projection without accounting for the tile size.
   *
   * @param lat latitude
   * @param lon longitude
   * @param z zoom level
   *
   * @return coordinate
   */
	public static Coordinate latlon2xy(double lat, double lon, int z) {
		int numTiles = numTiles(z);
		double rad_lat = toRadians(lat);

		return new Coordinate(
				numTiles * (lon + 180.0) / 360.0, 
				numTiles * (1 - log(tan(rad_lat) + sec(rad_lat)) / PI) / 2);
	}

  /**
   * Calculate the edge of a tile given an arbitrary y and zoom level.
   * @param y y
   * @param z zoom level
   * @return calculate the edges
   */
	public static Coordinate latEdges(int y, int z) {
		double unit = 1.0 / numTiles(z);

		return new Coordinate(
				mercatorToLat(PI * (1.0 - 2.0 * (y * unit))),
				mercatorToLat(PI * (1.0 - 2.0 * (y * unit + unit))));
	}

  /**
   * Calculate the edge of a tile given an arbitrary x and zoom level.
   * @param x x
   * @param z zoom level
   * @return calculate the edges
   */
	public static Coordinate lonEdges(int x, int z) {
		double unit = 360.0 / numTiles(z);

		return new Coordinate(
				-180.0 + (x * unit),
				-180.0 + (x * unit) + unit);
	}

  /**
   * Determine the bounding box of a tile given an arbitrary x, y, z
   * @param x x
   * @param y y
   * @param z zoom level
   * @return calculate the bounding box
   */
	public static BoundingBox tileEdges(int x, int y, int z) {
		BoundingBox result = new BoundingBox();
		Coordinate ret;

		ret = latEdges(y, z);
		result.setNorth(ret.x);
		result.setSouth(ret.y);

		ret = lonEdges(x, z);
		result.setWest(ret.x);
		result.setEast(ret.y);

		return result;
	}

	/**
	 * converts 'slippy maps' tile number to lat & lon in degrees 
	 */
	public static Coordinate tile2latlon(int x, int y, int z) {
		int n = numTiles(z);
		double lat_rad = atan(sinh(PI * (1.0 - 2.0 * y / n)));

		return new Coordinate(
				lat_rad * 180.0 / PI,
				(double) x / (double) n * 360.0 - 180.0);
	}

	/**
	 * converts lon in degrees to a 'slippy maps' x tile number
	 */
	public static int lon2tilex(double lon_deg, int z) {
		double ret = (lon_deg + 180.0) / 360.0 * numTiles(z);

		return (int) floor(ret);
	}

	/**
	 * converts lat in degrees to a 'slippy maps' y tile number 
	 */
	public static int lat2tiley(double lat_deg, int z) {
		int n = numTiles(z);
		double lat_rad = toRadians(lat_deg);
		double ret = (1.0 - log(tan(lat_rad) + sec(lat_rad)) / PI) / 2.0 * n;

		return (int) floor(ret);
	}
}