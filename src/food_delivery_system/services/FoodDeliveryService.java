package food_delivery_system.services;

public class FoodDeliveryService {
    OrderService orderService;
    RestaurantService restaurantService;
    UserService userService;

    public FoodDeliveryService() {
        this.orderService = new OrderService();
        this.restaurantService = new RestaurantService();
        this.userService = new UserService();
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public RestaurantService getRestaurantService() {
        return restaurantService;
    }

    public UserService getUserService() {
        return userService;
    }


}
