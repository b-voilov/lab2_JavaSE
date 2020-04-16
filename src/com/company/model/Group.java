package com.company.model;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Group implements Serializable {
    private String name;
    private HashSet<Product> products;

    public Group(String name) {
        this.name = name;
        products = new HashSet<Product>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ISetModifier<Product> getProductsModifier() {
        return new ProductsModifier();
    }

    public Iterator<Product> getIterator() {
        return products.iterator();
    }


    private class ProductsModifier implements ISetModifier<Product> {

        @Override
        public void add(Product product) {
            products.add(product);
        }

        @Override
        public void remove(String name) {
            try {
                products.remove(new Product(name, "", 0, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void edit(String name, Product newProduct) {
            remove(name);
            products.add(newProduct);
        }
    }

    private class StatisticsProvider implements IStatisticsProvider {
        @Override
        public int getGeneralPriceOfGroup() {
            int res = 0;
            for (Product product : products) {
                res += product.getPrice() * product.getCount();
            }
            return res;
        }
    }


    private class Searcher implements IProductSearcher {

        @Override
        public List<Product> searchByName(String name) {
            return products.stream().filter(product -> product.getName().contains(name)).collect(Collectors.toList());
        }

        @Override
        public List<Product> filterByPrice(int begin, int end) {
            return products.stream().filter(product -> product.getPrice() >= begin && product.getPrice() <= end).collect(Collectors.toList());
        }

        @Override
        public List<Product> filterByQuantity(int begin, int end) {
            return products.stream().filter(product -> product.getCount() >= begin && product.getCount() <= end).collect(Collectors.toList());
        }
    }

}