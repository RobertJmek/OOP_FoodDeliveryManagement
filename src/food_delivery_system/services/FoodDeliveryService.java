package food_delivery_system.services;

public class FoodDeliveryService {
    OrderService orderService;
    RestaurantService restaurantService;
    UserService userService;

    public FoodDeliveryService() {
        this.userService = new UserService();
        this.restaurantService = new RestaurantService();
        this.orderService = new OrderService(userService, restaurantService);
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
