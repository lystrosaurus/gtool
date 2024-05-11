package io.github.lystrosaurus.geo;

import io.github.lystrosaurus.exceptions.GException;
import java.io.IOException;
import java.util.Optional;
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
public class GeoTools {

  private static final SimpleFeatureSource featureSource;

  static {
    try {
      featureSource = FileDataStoreFinder
          .getDataStore(GeoTools.class.getClassLoader().getResource("gadm/gadm41_CHN_3.shp"))
          .getFeatureSource();
    } catch (IOException e) {
      throw new GException(e);
    }
  }

  private GeoTools() {

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
