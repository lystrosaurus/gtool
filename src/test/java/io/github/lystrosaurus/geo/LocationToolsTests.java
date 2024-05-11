package io.github.lystrosaurus.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author eric 2024/5/11 14:02
 */
class LocationToolsTests {

  @Test
  void getRegion() {
    String address = LocationTools.getRegion("8.8.8.8");
    // 国外的ip,解析不到
    Assertions.assertEquals("美国|0|0|0|Level3", address);

    address = LocationTools.getRegion("127.0.0.1");
    Assertions.assertEquals("内网ip", address);

    address = LocationTools.getRegion("43.137.63.255");
    Assertions.assertEquals("北京市东城区东华门街道西长安街", address);

    address = LocationTools.getRegion("36.132.151.0");
    Assertions.assertEquals("黑龙江省哈尔滨市南岗区革新街道东方中山花园", address);
  }
}
