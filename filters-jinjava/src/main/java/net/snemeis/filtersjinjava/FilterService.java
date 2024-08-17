package net.snemeis.filtersjinjava;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilterService {

    public List<Filter> getSampleFilters() {
        List<FilterValue> filterValues1 = List.of(
                new FilterValue("availability", "available", false, false),
                new FilterValue("availability", "short_on_stock", false, false),
                new FilterValue("availability", "missing", false, false)
        );

        List<FilterValue> filterValues2 = List.of(
                new FilterValue("color", "red", false, false),
                new FilterValue("color", "blue", false, false),
                new FilterValue("color", "green", false, false),
                new FilterValue("color", "yellow", false, false)
        );

        Filter filter1 = new Filter("availability", filterValues1, false);
        Filter filter2 = new Filter("color", filterValues2, false);
        Filter filter3 = new Filter("hidden", List.of(new FilterValue("some_hidden_value", "true", false, true)), true);

        return List.of(filter1, filter2, filter3);
    }

    public List<Filter> consolidateFilterTypes(
            List<Filter> availableFilters,
            Filters appliedFilters
    ) {
        return availableFilters.stream().map(filter -> {
            var consolidatedValues = filter.values.stream().map(value -> {
                boolean hidden = filter.hidden;
                var correctFilterType = (hidden ? appliedFilters.hiddenFilters : appliedFilters.filters);

                // filter is not present, therefore value is not applied and we can return false
                if (!correctFilterType.containsKey(value.filter)) {
                    return new FilterValue(value.filter, value.value, value.excluding, false);
                }

                // get the filter
                var appliedFilter = correctFilterType.get(value.filter);

                // filter value is present or not, so it's applied or not
                var isApplied = appliedFilter.containsKey(value.value);

                return new FilterValue(value.filter, value.value, value.excluding, isApplied);
            }).toList();

            return new Filter(filter.filter, consolidatedValues, filter.hidden);
        }).toList();
    }

    @Data
    public static class Filters {
        // filter1:[value1, value4, ...]
        // filter[filterid][valueid] = true
        private Map<String, Map<String, String>> filters;

        // filter2:[value3, ...]
        private Map<String, Map<String, String>> hiddenFilters;
    }

    @AllArgsConstructor
    @Data
    public static class Filter {
        private String filter; List<FilterValue> values; boolean hidden;
    }

    @AllArgsConstructor
    @Data
    public static class FilterValue {
        private String filter; String value; boolean excluding; boolean applied;
    }
}
