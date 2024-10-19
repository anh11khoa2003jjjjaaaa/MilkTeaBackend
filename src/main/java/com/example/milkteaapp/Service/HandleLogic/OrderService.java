package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.DTO.OrderDetailsResponse;
import com.example.milkteaapp.DTO.OrderRequest;
import com.example.milkteaapp.Model.OrderDetailModel;
import com.example.milkteaapp.Model.OrderModel;
import com.example.milkteaapp.Model.UserModel;
import com.example.milkteaapp.Repository.OrderDetailRepository;
import com.example.milkteaapp.Repository.OrderRepository;
import com.example.milkteaapp.Repository.UserRepository;
import com.example.milkteaapp.Service.Interface.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRespository;


    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, UserRepository userRespository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRespository = userRespository;

    }

    @Override
    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }


    ///-----------------------------------------------
    @Override
    public OrderModel addOrder(OrderModel orderModel) {
        UserModel user = userRespository.findById(orderModel.getUserID()).orElseThrow(() -> new RuntimeException("User not found"));

        OrderModel order = new OrderModel();
        order.setUser(user);
        return orderRepository.save(orderModel);
    }

    @Override
    public OrderDetailModel addOrderDetail(OrderDetailModel orderDetailModel) {
        return orderDetailRepository.save(orderDetailModel);
    }

    //Hàm để lấy tất cả đơn hàng theo userID
    public List<OrderDetailsResponse> getAllOrdersByCustomerId(String userID) {
        // Khởi tạo danh sách chứa các phản hồi chi tiết đơn hàng
        List<OrderDetailsResponse> orderDetailsResponses = new ArrayList<>();

        try {
            // Lấy tất cả các đơn hàng của khách hàng theo userID
            List<OrderModel> orders = orderRepository.findByUserID(userID);

            // Kiểm tra nếu không có đơn hàng nào cho khách hàng này
            if (orders == null || orders.isEmpty()) {
                throw new RuntimeException("Không có đơn hàng nào của khách hàng này");
            }

            // Lặp qua tất cả các đơn hàng
            for (OrderModel order : orders) {
                // Lấy chi tiết đơn hàng theo orderId
                List<OrderDetailModel> orderDetails = orderDetailRepository.findByOrderID(order.getOrderID());
                List<OrderModel> orderList=orderRepository.findByOrderID(order.getOrderID());
                // Kiểm tra nếu không có chi tiết đơn hàng
                if (orderDetails == null || orderDetails.isEmpty()) {
                    throw new RuntimeException("Không tìm thấy chi tiết đơn hàng cho OrderID: " + order.getOrderID());
                }

                // Kiểm tra tổng tiền
                BigDecimal totalAmount = order.getTotalAmount();
                if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("Tổng tiền không hợp lệ cho OrderID: " + order.getOrderID());
                }

                // Thêm phản hồi chi tiết đơn hàng vào danh sách
                orderDetailsResponses.add(new OrderDetailsResponse(orderDetails,orderList,totalAmount));
            }

        } catch (RuntimeException e) {
            // Bẫy các lỗi do dữ liệu không hợp lệ hoặc không tìm thấy đơn hàng, chi tiết đơn hàng
            System.err.println("Lỗi: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            // Bẫy các lỗi khác (lỗi kết nối cơ sở dữ liệu, null pointer, ...)
            System.err.println("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            throw new RuntimeException("Đã xảy ra lỗi không mong muốn, vui lòng thử lại sau.");
        }

        // Trả về danh sách chi tiết của tất cả đơn hàng của khách hàng
        return orderDetailsResponses;
    }



    // Lấy chi tiết đơn hàng theo OrderID
    public OrderDetailsResponse getOrderDetails(Long orderId) {
        // Tìm đơn hàng theo OrderID
        OrderModel order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderModel> orderList=orderRepository.findByOrderID(orderId);
        if(orderList.isEmpty()){
            throw new RuntimeException("Order not found");
        }else{


        // Lấy chi tiết đơn hàng
        List<OrderDetailModel> orderDetails = orderDetailRepository.findByOrderID(orderId);

        // Tính tổng tiền
        BigDecimal totalAmount = order.getTotalAmount();

        return new OrderDetailsResponse(orderDetails,orderList,totalAmount);
    }
    }

//    public OrderDetailsResponse getOrder(Long orderId) {
//        // Tìm đơn hàng theo OrderID
//        OrderModel order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
//
//        // Lấy chi tiết đơn hàng
//        List<OrderDetailModel> orderDetails = orderDetailRepository.findByOrderID(orderId);
//
//        // Tính tổng tiền
//        BigDecimal totalAmount = order.getTotalAmount();
//
//        return new OrderDetailsResponse(orderDetails, totalAmount);
//    }
    ///-----------------------------------------------
    @Override
    public OrderModel updateOrder(Long orderID, OrderModel updatedOrder) {
        Optional<OrderModel> existingOrder = orderRepository.findById(orderID);
        if (existingOrder.isPresent()) {
            OrderModel order = existingOrder.get();
            order.setUserID(updatedOrder.getUserID());
            order.setOrderDate(updatedOrder.getOrderDate());
            order.setTotalAmount(updatedOrder.getTotalAmount());
            order.setOrderStatus(updatedOrder.getOrderStatus());
            order.setCancellationReason(updatedOrder.getCancellationReason());
            return orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Order with ID " + orderID + " not found.");
        }
    }

    public boolean deleteOrder(Long orderID) {
        Optional<OrderModel> order = orderRepository.findById(orderID);
        if (order.isPresent()) {
            // Xóa đơn hàng nếu tồn tại
            orderRepository.deleteById(orderID);
            return true;  // Đã xóa thành công
        } else {
            return false;  // Không tìm thấy đơn hàng
        }
    }

//    public boolean deleteOrder(Long orderID) {
//        if (orderRepository.existsById(orderID)) {
//            orderRepository.deleteById(orderID);
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    public List<OrderModel> searchOrdersByCustomerID(String displayName) {
        if(displayName==null){

            return orderRepository.findAll();
        }else{
            List<OrderModel>listOrderModel=orderRepository.findByUser_DisplayNameContainingIgnoreCase(displayName);
            if (listOrderModel.isEmpty()) {
                throw new EntityNotFoundException("Không tìm thấy " + displayName + "!");
            } else {
                return listOrderModel;
            }
        }

    }
//Hủy và lý do hủy đơn hàng
@Override
public OrderModel cancelOrder(Long orderID, String cancellationReason) {
    // Tìm đơn hàng theo orderID
    OrderModel order = orderRepository.findById(orderID)
            .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

    // Kiểm tra nếu trạng thái đơn hàng đã là "Đã hủy"
    if (order.getOrderStatus().equals("Đã hủy")) {
        throw new RuntimeException("Đơn hàng này đã được hủy trước đó.");
    }

    // Cập nhật trạng thái đơn hàng thành "Đã hủy"
    order.setOrderStatus("Đã hủy");

    // Thêm lý do hủy đơn hàng
    order.setCancellationReason(cancellationReason);

    // Lưu lại đơn hàng đã thay đổi
    return orderRepository.save(order);
}



    // Hàm duyệt đơn hàng (cập nhật trạng thái đơn hàng thành "Đã duyệt")
    @Override
    public OrderModel approveOrder(Long orderID,String statusOrder) {
        // Tìm đơn hàng theo orderID
        OrderModel order = orderRepository.findById(orderID)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // Kiểm tra trạng thái hiện tại của đơn hàng
        if (order.getOrderStatus().equals("Đã duyệt")) {
            throw new RuntimeException("Đơn hàng này đã được duyệt trước đó.");
        }

        // Cập nhật trạng thái đơn hàng thành "Đã duyệt"
        order.setOrderStatus(statusOrder);

        // Lưu lại thay đổi của đơn hàng
        return orderRepository.save(order);
    }

}

