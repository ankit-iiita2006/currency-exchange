package models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


@Entity
public class CurrencyMap {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String currencyCode;

    @Constraints.Required
    public Double valuation;


    public static CurrencyMap findById(Long id) {
        return JPA.em().find(CurrencyMap.class, id);
    }


    public static Map<String,String> getCurrencyOptionsByName() {
        List<CurrencyMap> currencies = JPA.em().createQuery("from CurrencyMap order by name").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(CurrencyMap c: currencies) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }


    public static Map<String,String> getCurrencyOptionsByCode() {
        List<CurrencyMap> currencies = JPA.em().createQuery("from CurrencyMap order by currencyCode").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(CurrencyMap c: currencies) {
            options.put(c.id.toString(), c.currencyCode);
        }
        return options;
    }

    public static Map<String,String>  getCurrencyName(Long id) {
        CurrencyMap currency = findById(id);
        LinkedHashMap<String,String> defaultOption = new LinkedHashMap<String,String>();
        defaultOption.put(currency.id.toString(), currency.name);
        return defaultOption;
    }

    public static Map<String,String>  getCurrencyCode(Long id) {
        CurrencyMap currency = findById(id);
        LinkedHashMap<String,String> defaultOption = new LinkedHashMap<String,String>();
        defaultOption.put(currency.id.toString(), currency.currencyCode);
        return defaultOption;
    }


    public static double getConvertedValue(String valuation, String from, String to) {
        long fromCurrency = Long.parseLong(from);
        long toCurrency = Long.parseLong(to);
        double amountValuation = Double.parseDouble(valuation);

        CurrencyMap fromCurrencyMap = findById(fromCurrency);
        CurrencyMap toCurrencyMap = findById(toCurrency);

        double result = fromCurrencyMap.valuation/toCurrencyMap.valuation ;

        return result * amountValuation;
    }


    public void update(Long id) {
        this.id = id;
        JPA.em().merge(this);
    }

    public void save() {
        JPA.em().persist(this);
    }

    public void delete() {
        JPA.em().remove(this);
    }



    /**
     * Return a page of currency
     *
     * @param page Page to display
     * @param pageSize Number of currencys per page
     * @param sortBy currency property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static Page page(int page, int pageSize, String sortBy, String order, String filter) {
        if(page < 1) page = 1;
        Long total = (Long)JPA.em()
                .createQuery("select count(c) from CurrencyMap c where lower(c.name) like ?")
                .setParameter(1, "%" + filter.toLowerCase() + "%")
                .getSingleResult();
        List<CurrencyMap> data = JPA.em()
                .createQuery("from CurrencyMap c where lower(c.name) like ? order by c." + sortBy + " " + order)
                .setParameter(1, "%" + filter.toLowerCase() + "%")
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize).getResultList();
        return new Page(data, total, page, pageSize);
    }

    /**
     * Used to represent a currency page.
     */
    public static class Page {

        private final int pageSize;
        private final long totalRowCount;
        private final int pageIndex;
        private final List<CurrencyMap> list;

        public Page(List<CurrencyMap> data, long total, int page, int pageSize) {
            this.list = data;
            this.totalRowCount = total;
            this.pageIndex = page;
            this.pageSize = pageSize;
        }

        public long getTotalRowCount() {
            return totalRowCount;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public List<CurrencyMap> getList() {
            return list;
        }

        public boolean hasPrev() {
            return pageIndex > 1;
        }

        public boolean hasNext() {
            return (totalRowCount/pageSize) >= pageIndex;
        }

        public String getDisplayXtoYofZ() {
            int start = ((pageIndex - 1) * pageSize + 1);
            int end = start + Math.min(pageSize, list.size()) - 1;
            return start + " to " + end + " of " + totalRowCount;
        }

    }



}
