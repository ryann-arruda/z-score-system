package entities;

import java.util.Map;

public interface ZscoreCalculator {
	Map<String, Object> calculateZscore(Child child, Double weightValue);
}
