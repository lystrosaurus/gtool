# gtool

```maven
<dependency>
    <groupId>io.github.lystrosaurus</groupId>
    <artifactId>gtool</artifactId>
    <version>0.0.1</version>
</dependency>
```

- [x] 按权重随机集合元素
  使用参考 [ExponentialDistributionTest.java](src/test/java/io/github/lystrosaurus/ExponentialDistributionTests.java)
- [x] 
  按经纬度查询城市,使用参考 [GeoToolsTests.java](src/test/java/io/github/lystrosaurus/geo/GeoToolsTests.java),
  使用必须在 resource文件下添加gadm文件夹,文件夹下包含[gadm](https://gadm.org/data.html)下载的shp文件,资源路径固定为: gadm/gadm41_CHN_3.shp
