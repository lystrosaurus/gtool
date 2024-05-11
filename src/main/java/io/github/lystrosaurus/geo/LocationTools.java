package io.github.lystrosaurus.geo;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * Created by IntelliJ IDEA.
 *
 * @author eric 2024/5/11 10:56
 */
@Slf4j
public class LocationTools {

  private LocationTools() {
  }

  private static Searcher searcher = null;

  static {
    // 1、从 dbPath 加载整个 xdb 到内存。
    byte[] cBuff = new byte[20971520];
    String dbPath = "ip2region/ip2region.xdb";
    try {
      final String filePath = Objects.requireNonNull(
          LocationTools.class.getClassLoader().getResource(dbPath)).getPath();
      cBuff = Searcher.loadContentFromFile(filePath);
    } catch (Exception e) {
      log.error("failed to load content from {}: {}", dbPath, ExceptionUtils.getStackTrace(e));
    }

    // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。

    try {
      searcher = Searcher.newWithBuffer(cBuff);
    } catch (Exception e) {
      log.error("failed to create content cached searcher: {}", ExceptionUtils.getStackTrace(e));
    }
  }

  public static String getRegion(final String ip) {
    if (Ipv4Util.isInnerIP(ip)) {
      return "内网ip";
    }

    String region = null;
    // 3、查询
    try {
      Map<String, Object> ipParamMap = new LinkedHashMap<>();
      ipParamMap.put("key", "C92BAA7E03099C0B67D9D38C4AF5B708");
      ipParamMap.put("ip", ip);
      // 付费计划才能支持
      // ipParamMap.put("lang", "zh-cn")
      final String ipRegion = HttpUtil.get("https://api.ip2location.io", ipParamMap);
      final JSONObject ipJson = JSONUtil.parseObj(ipRegion);
      final String latitude = ipJson.getStr("latitude");
      final String longitude = ipJson.getStr("longitude");
      final String zipCode = ipJson.getStr("zip_code");
      final String countryName = ipJson.getStr("country_name");
      final String regionName = ipJson.getStr("region_name");
      final String cityName = ipJson.getStr("city_name");

      log.info("latitude {}, longitude {}, zipCode {}, countryName {}, regionName {}, cityName {}",
          latitude, longitude, zipCode, countryName, regionName, cityName);

      Map<String, Object> paramMap = new LinkedHashMap<>();
      paramMap.put("key", "8780449cddb680e5e7175d77cdcde17b");
      paramMap.put("location", longitude + "," + latitude);
      paramMap.put("extensions", "base");
      paramMap.put("roadlevel", 0);
      final String ipLocation = HttpUtil.get("https://restapi.amap.com/v3/geocode/regeo",
          paramMap);
      log.info("ipLocation {}", ipLocation);

      region = JSONUtil.parseObj(ipLocation).getJSONObject("regeocode")
          .getStr("formatted_address");

    } catch (Exception e) {
      log.error("failed to api search({}): {}\n", ip, ExceptionUtils.getStackTrace(e));
    }
    if (region == null || StringUtils.equals("[]", region)) {
      try {
        region = searcher.search(ip);
        log.info("{region: {}, ioCount: {}\n", region, searcher.getIOCount());
      } catch (Exception e) {
        log.error("failed to search({}): {}\n", ip, ExceptionUtils.getStackTrace(e));
      }
    }

    return region;
  }
}
