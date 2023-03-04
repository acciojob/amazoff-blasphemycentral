package com.driver;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository
{
    private Map<String, Order> orderDb;
    private Map<String, DeliveryPartner> partnerDb;
    private Map<String, String> orderPartnerDb;
    private Map<String, List<String>> partnerOrderDb;

    public OrderRepository()
    {
        this.orderDb = new HashMap<>();
        this.partnerDb = new HashMap<>();
        this.orderPartnerDb = new HashMap<>();
        this.partnerOrderDb = new HashMap<>();
    }
    public void addOrder(Order order)
    {
        orderDb.put(order.getId(), order);
    }

    public void addPartner(String partnerId)
    {
        partnerDb.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId)
    {
        if(orderDb.containsKey(orderId) && partnerDb.containsKey(partnerId))
        {
            orderPartnerDb.put(orderId, partnerId);
            List<String> temp = new ArrayList<>();
            if(partnerOrderDb.containsKey(partnerId))
            {
                temp = partnerOrderDb.get(partnerId);
            }
            temp.add(orderId);
            partnerOrderDb.put(partnerId,temp);
            partnerDb.get(partnerId).setNumberOfOrders(partnerDb.get(partnerId).getNumberOfOrders()+1);
        }
    }

    public Order getOrderById(String orderId)
    {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        return partnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId)
    {
        return partnerOrderDb.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        return partnerOrderDb.get(partnerId);
    }

    public List<String> getAllOrders()
    {
        return new ArrayList<>(orderDb.keySet());
    }

    public Integer getCountOfUnassignedOrders()
    {
        return orderDb.size()-orderPartnerDb.size();
    }

    public Integer getOrdersLeftAfterGivenTime(int time, String partnerId)
    {
        int ordersLeft = 0;
        for(String orderId : partnerOrderDb.get(partnerId))
        {
            if(orderDb.get(orderId).getDeliveryTime() > time) ordersLeft++;
        }
        return ordersLeft;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId)
    {
        int ans = 0;
        for(String orderId : partnerOrderDb.get(partnerId))
        {
            ans = Math.max(ans, orderDb.get(orderId).getDeliveryTime());
        }
        return ans;
    }

    public void deletePartnerById(String partnerId)
    {
        partnerDb.remove(partnerId);

        List<String> listOfOrders = partnerOrderDb.get(partnerId);
        partnerOrderDb.remove(partnerId);

        for(String orderId: listOfOrders){
            orderPartnerDb.remove(orderId);
        }
    }

    public void deleteOrderById(String orderId)
    {
        orderDb.remove(orderId);

        String partnerId = orderPartnerDb.get(orderId);
        orderPartnerDb.remove(orderId);

        partnerOrderDb.get(partnerId).remove(orderId);

        partnerDb.get(partnerId).setNumberOfOrders(partnerOrderDb.get(partnerId).size());

    }
}
