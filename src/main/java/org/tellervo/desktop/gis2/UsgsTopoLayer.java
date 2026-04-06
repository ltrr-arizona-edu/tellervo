package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.mercator.MercatorSector;
import gov.nasa.worldwind.util.LevelSet;
import gov.nasa.worldwind.util.Tile;
import gov.nasa.worldwind.util.TileUrlBuilder;

import java.net.MalformedURLException;
import java.net.URL;

public class UsgsTopoLayer extends OpenStreetMapHttpsLayer {

	public UsgsTopoLayer() {
		super(makeLevels());
	}

	private static LevelSet makeLevels() {
		AVList params = new AVListImpl();

		params.setValue(AVKey.TILE_WIDTH, 256);
		params.setValue(AVKey.TILE_HEIGHT, 256);
		params.setValue(AVKey.DATA_CACHE_NAME, "Earth/USGS/Topo");
		params.setValue(AVKey.SERVICE, "https://basemap.nationalmap.gov/arcgis/rest/services/USGSTopo/MapServer/tile/");
		params.setValue(AVKey.DATASET_NAME, "USGSTopo");
		params.setValue(AVKey.FORMAT_SUFFIX, ".jpg");
		params.setValue(AVKey.NUM_LEVELS, 24);
		params.setValue(AVKey.NUM_EMPTY_LEVELS, 0);
		params.setValue(AVKey.LEVEL_ZERO_TILE_DELTA,
				new LatLon(Angle.fromDegrees(22.5d), Angle.fromDegrees(45.0d)));
		params.setValue(AVKey.SECTOR,
				new MercatorSector(-1.0d, 1.0d, Angle.NEG180, Angle.POS180));
		params.setValue(AVKey.TILE_URL_BUILDER, new URLBuilder());

		return new LevelSet(params);
	}

	@Override
	public String toString() {
		return "USGS Topo";
	}

	private static class URLBuilder implements TileUrlBuilder {
		@Override
		public URL getURL(Tile tile, String imageFormat) throws MalformedURLException {
			int zoom = tile.getLevelNumber() + 3;
			int x = tile.getColumn();
			int y = ((1 << zoom) - 1) - tile.getRow();
			return new URL(tile.getLevel().getService() + zoom + "/" + y + "/" + x);
		}
	}
}
