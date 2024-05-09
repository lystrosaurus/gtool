package io.github.lystrosaurus;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * 按权重获取对象.
 *
 * @author eric 2024/5/9 16:11
 */
public class ExponentialDistribution<T> {

  /**
   * <a href="https://en.wikipedia.org/wiki/Exponential_distribution">...</a>
   *
   * @param weights 权重集合.
   * @param random  随机数实例.
   * @return 随机一个集合元素.
   */
  public T getWeightedRandom(Map<T, Double> weights, Random random) {
    T result = null;
    double bestValue = Double.MAX_VALUE;

    for (Entry<T, Double> entry : weights.entrySet()) {
      double value = -Math.log(random.nextDouble()) / entry.getValue();

      if (value < bestValue) {
        bestValue = value;
        result = entry.getKey();
      }
    }

    return result;
  }
}
