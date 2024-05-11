package io.github.lystrosaurus.geo;

import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Created by IntelliJ IDEA.
 *
 * @author eric 2024/5/10 9:30
 */
@Slf4j
public class GeoTools {

  private GeoTools() {

  }

  private static SimpleFeatureSource featureSource;

  static {
    String path = "gadm/gadm41_CHN_3.shp";
    try {
      featureSource = FileDataStoreFinder
          .getDataStore(GeoTools.class.getClassLoader().getResource(path))
          .getFeatureSource();
    } catch (IOException e) {
      log.error("failed to load content from {}", path, e);
    }
  }

  /**
   * 获取城市名称.
   *
   * @param latitude  纬度.
   * @param longitude 经度.
   * @return 经纬度所在城市, 判断失误返回 Optional.empty()
   */
  public static Optional<String> getProvinceName(double latitude, double longitude)
      throws IOException, CQLException {

    // 创建查询过滤器
    Coordinate coordinate = new Coordinate(longitude, latitude);
    Geometry point = new GeometryFactory().createPoint(coordinate);

    String cqlFilter = "INTERSECTS(the_geom, " + point + ")";
    Filter filter = ECQL.toFilter(cqlFilter);
    final SimpleFeatureCollection features = featureSource.getFeatures(filter);

    try (SimpleFeatureIterator featureIterator = features.features()) {
      if (featureIterator.hasNext()) {
        SimpleFeature feature = featureIterator.next();
        final Object nlName1 = feature.getAttribute("NL_NAME_1");
        return Optional.of(nlName1.toString());
      }
    }
    return Optional.empty();
  }
}
