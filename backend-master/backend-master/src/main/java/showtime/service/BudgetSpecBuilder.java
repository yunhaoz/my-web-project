package showtime.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import showtime.model.Budget;

import java.util.List;

public class BudgetSpecBuilder extends EventSpecBuilder<Budget> {

    Logger logger = LoggerFactory.getLogger(BudgetSpecBuilder.class);

    public static BudgetSpecBuilder createBuilder() {
        return new BudgetSpecBuilder();
    }

    public BudgetSpecBuilder byAmount(List<Double> amount) {
        spec = spec.and(new SpecEqualOneFrom<>("amount", amount));
        return this;
    }

    public BudgetSpecBuilder byCategory(List<String> category) {
        spec = spec.and(new SpecEqualOneFrom<>("category", category));
        return this;
    }

    public BudgetSpecBuilder byTransactionUserid(List<Integer> transxUserid) {
        spec = spec.and(new SpecEqualOneFrom<>("ebudTransactionUserid", transxUserid));
        return this;
    }

    @Override
    public BudgetSpecBuilder fromMultiValueMap(MultiValueMap<String, String> params) {
        super.fromMultiValueMap(params);
        byAmount( MVPUtil.parseDoubleNoexcept(params.get("amount")) );
        byCategory( MVPUtil.toStringNoexcept(params.get("category")) );
        byTransactionUserid( MVPUtil.parseIntNoexcept(params.get("transactionuserid")) );
        return this;
    }
}
