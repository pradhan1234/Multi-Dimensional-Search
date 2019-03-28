package ypp170130;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 *     Team No: 39
 *     @author Pranita Hatte: prh170230
 *     @author Prit Thakkar: pvt170000
 *     @author Shivani Thakkar: sdt170030
 *     @author Yash Pradhan: ypp170130
 *
 *     Long Project 3: Multi-Dimensional search
 *
 *     Updates from previous Submission:
 *     >> Updated PriceHike Logic: converts everything to cents and computes
 *     >> Removed BigDecimal: was slow and unable to execute test6 properly
 *     >> Allocating above -Xms4g, we observed significant speedup in run time
 */

public class MDS {


    /**
     * Uses balanced trees(TreeMap) and HashMap to organize the items in inventory
     * for efficient search, insert, update and delete operations
     *
     * treeMap stores mapping id to item
     * descriptionMap stores description as key with a hash set of corresponding items reference
     * i.e. Set of reference to items containing that description
     */
    private TreeMap<Long, Item> treeMap;
    private HashMap<Long, HashSet<Item>> descriptionMap;
    private Money ZERO;

    /**
     * Item: stores details pertaining to an item
     */
    private class Item{

        private Long id;
        private Money price;
        private HashSet<Long> description;

        //constructor
        Item(Long id, Money price, List<Long> description){
            this.id = id;
            this.price = price;
            this.description = new HashSet<>(description);
        }

        // getters and setters
        public Long getId() {
            return id;
        }

        public Money getPrice() {
            return price;
        }

        public void setPrice(Money m){
            this.price = m;
        }

        public HashSet<Long> getDescription() {
            return description;
        }

        public void setDescription(HashSet<Long> d){
            this.description = d;
        }
    }

    // Constructors
    public MDS() {
        treeMap = new TreeMap<>();
        descriptionMap = new HashMap<>();
        ZERO = new Money();
    }

    /**
     * Public methods of MDS.
     */

    /**
     * Insert(id,price,list): insert a new item whose description is given
     * in the list.  If an entry with the same id already exists, then its
     * description and price are replaced by the new values, unless list
     * is null or empty, in which case, just the price is updated.
     *
     * @param id
     * @param price
     * @param list
     * @return 1 if the item is new, and 0 otherwise.
     */
    public int insert(long id, Money price, List<Long> list) {

        Item item = treeMap.get(id);
        // if item with id exists
        if(item!=null){
            if(list == null || list.size() == 0){
                // update price only
                item.setPrice(price);
                return 0;
            }

            //update price and description
            item.setPrice(price);
            removeItemFromDescription(item);
            item.setDescription(new HashSet<>(list));
            updateDescriptionMap(item, list);
            return 0;
        }

        // add new item
        item = new Item(id, price, list);
        treeMap.put(id, item);


        // update descriptionMap
        updateDescriptionMap(item, list);

        return 1;
    }

    /**
     * helper method to update description
     * @param item whose description is updated
     * @param list new description
     */
    private void updateDescriptionMap(Item item, List<Long> list){
        HashSet<Item> descriptionSet;
        for(Long d: list){
            descriptionSet = descriptionMap.get(d);
            if(descriptionSet == null){
                descriptionMap.put(d, new HashSet<Item>(){{add(item);}});
            }
            else{
                descriptionSet.add(item);
            }
        }
    }

    /**
     * helper method to remove item from descriptionMap
     * @param item
     * @return
     */
    private long removeItemFromDescription(Item item){
        long sum = 0;
        HashSet<Item> descriptionSet;
        for(Long d: item.getDescription()){
            sum += d;
            descriptionSet = descriptionMap.get(d);
            if(descriptionSet.size() > 1){
                descriptionSet.remove(item);
            }
            else{
                descriptionMap.remove(d);
            }
        }
        return sum;
    }

    /**
     * Find(id): lookup item in storage
     * @param id
     * @return price of item with given id (or 0, if not found).
     */
    public Money find(long id) {
        Item item = treeMap.get(id);
        if(item != null){
            return item.getPrice();
        }
        return ZERO;
    }

    /**
     * Delete(id): delete item from storage.
     * @param id
     * @return sum of the long ints that are in the description of the item deleted,
     * or 0, if such an id did not exist.
     */
    public long delete(long id) {
        Item item = treeMap.remove(id);
        if(item != null){
            return removeItemFromDescription(item);
        }
        return 0;
    }

    /**
     * FindMinPrice(n): given a long int, find items whose description
     * contains that number (exact match with one of the long ints in the
     * item's description)
     *
     * @param n
     * @return lowest price of those items, 0 if there is no such item.
     */
    public Money findMinPrice(long n) {
        HashSet<Item> items = descriptionMap.get(n);

        Money minPrice = new Money(Long.MAX_VALUE, Integer.MAX_VALUE);
        if(items==null){
            return ZERO;
        }
        for(Item item: items) {
            if(minPrice.compareTo(item.getPrice())==1) {
                minPrice= item.getPrice();
            }
        }
        return minPrice;
    }

    /**
     * FindMaxPrice(n): given a long int, find items whose description
     * contains that number.
     *
     * @param n
     * @return highest price of those items, 0 if there is no such item.
     */
    public Money findMaxPrice(long n) {
        HashSet<Item> items = descriptionMap.get(n);
        Money maxPrice = new Money(Long.MIN_VALUE, Integer.MIN_VALUE);
        if(items==null){
            return ZERO;
        }
        for(Item item: items) {
            if(maxPrice.compareTo(item.getPrice())==-1) {
                maxPrice= item.getPrice();
            }
        }
        return maxPrice;
    }

    /**
     * FindPriceRange(n,low,high): given a long int n, find the number
     * of items whose description contains n, and in addition,
     * their prices fall within the given range, [low, high].
     *
     * @param n
     * @param low
     * @param high
     * @return number of items in this range
     */
    public int findPriceRange(long n, Money low, Money high) {
        if(high.compareTo(low) == -1){
            return 0;
        }

        Set<Item> itemSet = descriptionMap.get(n);
        if(itemSet == null){
            return 0;
        }

        int count = 0;
        for(Item item: itemSet){
            if(low.compareTo(item.getPrice())!= 1 && item.getPrice().compareTo(high) != 1){
                count++;
            }
        }
        return count;
    }

    /**
     * PriceHike(l,h,r): increase the price of every product, whose id is
     * in the range [l,h] by r%.  Discards any fractional pennies in the new
     * prices of items.
     *
     * Logic: convert everything to cents and then compute the hike
     *
     * @param l
     * @param h
     * @param rate
     * @return the sum of the net increases of the prices.
     */

    public Money priceHike(long l, long h, double rate) {
        if(l<=h){

            Map<Long, Item> treeSubMap = treeMap.subMap(l, true, h, true);
            long total = 0;
            long oldCents, increase, newCents;

            for(Map.Entry<Long, Item> entry : treeSubMap.entrySet()){
                Item item = entry.getValue();
                oldCents = priceToCents(item.getPrice());
                increase = hike(oldCents, rate);
                newCents = oldCents + increase;
                total += increase;
                item.setPrice(centsToMoney(newCents));
            }
            return centsToMoney(total);
        }

        return ZERO;
    }

    private Money centsToMoney(long cents){
        return new Money(cents / 100, (int) (cents % 100));
    }

    private long priceToCents(Money price){
        return price.dollars()*100 + price.cents();
    }

    private long hike(long cents, double rate){
        return (long) (Math.floor(cents * rate) / 100);
    }

    /**
     * RemoveNames(id, list): Remove elements of list from the description of id.
     * Handles case when some of the items in the list are not in the id's description.
     *
     * @param id
     * @param list
     * @return the sum of the numbers that are actually deleted
     * from the description of id, 0 if there is no such id.
     */
    public long removeNames(long id, List<Long> list) {
        Item item = treeMap.get(id);
        if(item != null){
            int count = 0;
            delete(id);

            for(long name: list){
                if(item.getDescription().remove(name)){
                    count+=name;
                }
            }
            insert(id, item.getPrice(), new ArrayList<>(item.getDescription()));
            return count;
        }
        return 0;
    }

    public static class Money implements Comparable<Money> {
        long d;  int c;
        public Money() { d = 0; c = 0; }
        public Money(long d, int c) { this.d = d; this.c = c; }
        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if(len < 1) { d = 0; c = 0; }
            else if(part.length == 1) { d = Long.parseLong(s);  c = 0; }
            else { d = Long.parseLong(part[0]);  c = Integer.parseInt(part[1]); }
        }
        public long dollars() { return d; }
        public int cents() { return c; }
        public int compareTo(Money other) {
            if(this.dollars() < other.dollars()){
                return -1;
            }
            else if(this.dollars() == other.dollars()){
                if(this.cents() < other.cents()){
                    return -1;
                }
                else if(this.cents() == other.cents()){
                    return 0;
                }
                else{
                    return 1;
                }
            }
            else{
                return 1;
            }
        }
        public String toString() { return d + "." + c; }
    }
}
