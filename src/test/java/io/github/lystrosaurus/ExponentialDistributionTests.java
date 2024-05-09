package io.github.lystrosaurus;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExponentialDistributionTests {

  @Test
  void testGetWeightedRandom() {
    // 创建模拟的Random对象
    Random mockRandom = Mockito.mock(Random.class);
    // 配置mock以返回预定义的双精度浮点数序列
    when(mockRandom.nextDouble()).thenReturn(0.1, 0.2, 0.3, 0.4);

    // 创建权重映射
    Map<String, Double> weights = new HashMap<>();
    weights.put("A", 2.0);
    weights.put("B", 1.0);
    weights.put("C", 3.0);

    // 实例化指数分布对象
    ExponentialDistribution<String> exponentialDistribution = new ExponentialDistribution<>();

    // 执行测试
    String result1 = exponentialDistribution.getWeightedRandom(weights, mockRandom);
    String result2 = exponentialDistribution.getWeightedRandom(weights, mockRandom);
    String result3 = exponentialDistribution.getWeightedRandom(weights, mockRandom);
    String result4 = exponentialDistribution.getWeightedRandom(weights, mockRandom);

    // 验证结果，由于权重不同，不是每次运行都会得到相同的值
    // 但是可以根据权重预期某些结果出现的频率更高
    boolean isValid = ("A".equals(result1) || "C".equals(result1)) &&
        ("A".equals(result2) || "C".equals(result2)) &&
        ("A".equals(result3) || "C".equals(result3)) &&
        ("A".equals(result4) || "C".equals(result4));

    assertTrue(isValid, "Results do not match expected distribution.");
  }
}
