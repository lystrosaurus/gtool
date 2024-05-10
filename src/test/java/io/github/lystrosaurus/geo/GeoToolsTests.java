package io.github.lystrosaurus.geo;

import java.io.IOException;
import java.util.Optional;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author eric 2024/5/10 12:05
 */
class GeoToolsTests {

  @Test
  void getProvinceName() throws IOException, CQLException {
    // 创建查询过滤器
    double latitude = 30.949446;  // 维度
    double longitude = 120.915798;  // 经度
    final long time1 = System.currentTimeMillis();
    final Optional<String> provinceName = GeoTools.getInstance()
        .getProvinceName(latitude, longitude);
    Assertions.assertTrue(provinceName.isPresent());
    Assertions.assertEquals("浙江", provinceName.get());
    System.out.println(System.currentTimeMillis() - time1);

    double latitude2 = 31.005781;  // 维度
    double longitude2 = 121.196918;  // 经度
    final long time2 = System.currentTimeMillis();
    final Optional<String> provinceName2 = GeoTools.getInstance()
        .getProvinceName(latitude2, longitude2);
    Assertions.assertTrue(provinceName2.isPresent());
    Assertions.assertEquals("上海|上海", provinceName2.get());
    System.out.println(System.currentTimeMillis() - time2);

    double latitude3 = 31.401347;  // 维度
    double longitude3 = 120.968905;  // 经度
    final long time3 = System.currentTimeMillis();
    final Optional<String> provinceName3 = GeoTools.getInstance()
        .getProvinceName(latitude3, longitude3);
    Assertions.assertTrue(provinceName3.isPresent());
    Assertions.assertEquals("江蘇|江苏", provinceName3.get());
    System.out.println(System.currentTimeMillis() - time3);
  }
}
