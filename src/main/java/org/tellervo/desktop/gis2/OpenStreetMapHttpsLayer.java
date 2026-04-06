package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.layers.mercator.BasicMercatorTiledImageLayer;
import gov.nasa.worldwind.layers.mercator.MercatorTextureTile;
import gov.nasa.worldwind.layers.mercator.MercatorSector;
import gov.nasa.worldwind.retrieve.RetrievalPostProcessor;
import gov.nasa.worldwind.retrieve.Retriever;
import gov.nasa.worldwind.retrieve.URLRetriever;
import gov.nasa.worldwind.layers.mercator.MercatorTiledImageLayer;
import gov.nasa.worldwind.util.LevelSet;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.Tile;
import gov.nasa.worldwind.util.TileUrlBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class OpenStreetMapHttpsLayer extends BasicMercatorTiledImageLayer {

	public OpenStreetMapHttpsLayer() {
		super(makeLevels());
		configureRendering();
	}

	protected OpenStreetMapHttpsLayer(LevelSet levelSet) {
		super(levelSet);
		configureRendering();
	}

	private static LevelSet makeLevels() {
		AVList params = new AVListImpl();

		params.setValue(AVKey.TILE_WIDTH, 256);
		params.setValue(AVKey.TILE_HEIGHT, 256);
		params.setValue(AVKey.DATA_CACHE_NAME, "Earth/OSM-Mercator/OpenStreetMap HTTPS");
		params.setValue(AVKey.SERVICE, "https://tile.openstreetmap.org/");
		params.setValue(AVKey.DATASET_NAME, "osm");
		params.setValue(AVKey.FORMAT_SUFFIX, ".png");
		params.setValue(AVKey.NUM_LEVELS, 19);
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
		return "OpenStreetMap";
	}

	@Override
	protected void downloadTexture(MercatorTextureTile tile) {
		if (!WorldWind.getRetrievalService().isAvailable()) {
			return;
		}

		final URL textureUrl;
		try {
			textureUrl = tile.getResourceURL();
			if (textureUrl == null) {
				return;
			}
		} catch (MalformedURLException e) {
			Logging.logger().log(Level.SEVERE,
					Logging.getMessage("layers.TextureLayer.ExceptionCreatingTextureUrl", tile), e);
			return;
		}

		if (WorldWind.getNetworkStatus().isHostUnavailable(textureUrl)) {
			return;
		}

		Retriever retriever = URLRetriever.createRetriever(textureUrl, createDownloadPostProcessor(tile));
		if (retriever == null) {
			Logging.logger().severe(Logging.getMessage("layers.TextureLayer.UnknownRetrievalProtocol", textureUrl.toString()));
			return;
		}

		Integer connectTimeout = AVListImpl.getIntegerValue(this, AVKey.URL_CONNECT_TIMEOUT);
		if (connectTimeout != null && connectTimeout > 0) {
			retriever.setConnectTimeout(connectTimeout);
		}

		Integer readTimeout = AVListImpl.getIntegerValue(this, AVKey.URL_READ_TIMEOUT);
		if (readTimeout != null && readTimeout > 0) {
			retriever.setReadTimeout(readTimeout);
		}

		Integer staleRequestLimit = AVListImpl.getIntegerValue(this, AVKey.RETRIEVAL_QUEUE_STALE_REQUEST_LIMIT);
		if (staleRequestLimit != null && staleRequestLimit > 0) {
			retriever.setStaleRequestLimit(staleRequestLimit);
		}

		WorldWind.getRetrievalService().runRetriever(retriever, tile.getPriority());
	}

	private RetrievalPostProcessor createDownloadPostProcessor(MercatorTextureTile tile) {
		try {
			Class<?> clazz = Class.forName(
					"gov.nasa.worldwind.layers.mercator.BasicMercatorTiledImageLayer$DownloadPostProcessor");
			Constructor<?> ctor = clazz.getDeclaredConstructor(MercatorTextureTile.class,
					BasicMercatorTiledImageLayer.class);
			ctor.setAccessible(true);
			return (RetrievalPostProcessor) ctor.newInstance(tile, this);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to create WorldWind mercator download post processor", e);
		}
	}

	private void configureRendering() {
		setSplitScale(1.1d);
	}

	private void setSplitScale(double splitScale) {
		try {
			Field field = MercatorTiledImageLayer.class.getDeclaredField("splitScale");
			field.setAccessible(true);
			field.setDouble(this, splitScale);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to adjust WorldWind mercator split scale", e);
		}
	}

	private static class URLBuilder implements TileUrlBuilder {
		@Override
		public URL getURL(Tile tile, String imageFormat) throws MalformedURLException {
			int zoom = tile.getLevelNumber() + 3;
			int x = tile.getColumn();
			int y = ((1 << zoom) - 1) - tile.getRow();
			return new URL(tile.getLevel().getService() + zoom + "/" + x + "/" + y + ".png");
		}
	}
}
