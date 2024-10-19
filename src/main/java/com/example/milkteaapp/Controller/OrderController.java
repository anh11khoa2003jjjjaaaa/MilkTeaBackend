package com.example.milkteaapp.Controller;

import com.example.milkteaapp.DTO.OrderDetailDTO;
import com.example.milkteaapp.DTO.OrderDetailsResponse;
import com.example.milkteaapp.DTO.OrderRequest;
import com.example.milkteaapp.Model.OrderDetailModel;
import com.example.milkteaapp.Model.OrderModel;
import com.example.milkteaapp.Model.PaymentModel;
import com.example.milkteaapp.Service.HandleLogic.OrderService;
import com.example.milkteaapp.Service.HandleLogic.PaymentService;
import com.example.milkteaapp.Service.HandleLogic.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/orders")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"})
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;

    public OrderController(OrderService orderService, PaymentService paymentService,ProductService productService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.productService = productService;
    }

    // API lấy tất cả đơn hàng
    @GetMapping("/getAllOrders")
    public ResponseEntity<Object> getAllOrders() {
        try {
            List<OrderModel> orders = orderService.getAllOrders();
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Xảy ra lỗi khi truy vấn dữ liệu!", "message", e.getMessage()));
        }
    }

    // API thêm mới đơn hàng
    @PostMapping("/addOrder")
    public ResponseEntity<Object> addOrder(@RequestBody OrderModel orderModel) {
        try {
            OrderModel newOrder = orderService.addOrder(orderModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Dữ liệu không hợp lệ", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Xảy ra lỗi khi thêm đơn hàng mới!", "message", e.getMessage()));
        }
    }
    ///---------------------------------------------------
    // Xử lý thanh toán và tạo đơn hàng
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest orderRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Tạo đơn hàng mới từ thông tin yêu cầu
            OrderModel order = new OrderModel();
            order.setUserID(orderRequest.getUserID());
            order.setOrderDate(LocalDate.now());
            order.setTotalAmount(orderRequest.getTotalAmount());
            order.setOrderStatus("Pending");
            order.setCancellationReason("");

            // Lưu đơn hàng
            OrderModel savedOrder = orderService.addOrder(order);

            // Lưu chi tiết đơn hàng
            for (OrderDetailModel detail : orderRequest.getOrderDetails()) {
                detail.setOrderID(savedOrder.getOrderID());

                orderService.addOrderDetail(detail);
            }

            // Tạo thanh toán và lưu phương thức thanh toán
            PaymentModel payment = new PaymentModel();
            payment.setOrderID(savedOrder.getOrderID());
            payment.setAmount(orderRequest.getTotalAmount());
            payment.setPaymentMethodID(orderRequest.getPaymentMethodID());
            payment.setPaymentDate(LocalDateTime.now());
            paymentService.createPayment(payment);

            // Tạo phản hồi JSON
            response.put("message", "Order created successfully!");
            response.put("orderId", savedOrder.getOrderID());
            return ResponseEntity.ok(response); // Trả về JSON với mã trạng thái 200

        } catch (Exception e) {
            // Trả về thông báo lỗi trong phản hồi JSON
            response.put("message", "Failed to create order");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Trả về lỗi với mã trạng thái 500
        }
    }






    // API lấy chi tiết đơn hàng theo OrderID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable Long orderId) {
        try {
            // Gọi đến service để lấy chi tiết đơn hàng
            OrderDetailsResponse orderDetails = orderService.getOrderDetails(orderId);

            // Kiểm tra nếu đơn hàng không tồn tại
            if (orderDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Trả về chi tiết đơn hàng
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //hàm lấy tất cả các đơn hàng theo userID
    @GetMapping("/customer/{userID}")
    public ResponseEntity<Object> getAllOrdersByCustomerId(@PathVariable String userID) {
        try {
            // Gọi service để lấy tất cả đơn hàng của khách hàng
            List<OrderDetailsResponse> orderDetails = orderService.getAllOrdersByCustomerId(userID);

            // Nếu danh sách đơn hàng rỗng, trả về HTTP 404 Not Found
            if (orderDetails.isEmpty()) {
                return new ResponseEntity<>("Không có đơn hàng nào cho khách hàng này", HttpStatus.NOT_FOUND);
            }

            // Trả về kết quả dưới dạng ResponseEntity với mã HTTP OK (200)
            return new ResponseEntity<>(orderDetails, HttpStatus.OK);

        } catch (RuntimeException e) {
            // Nếu xảy ra lỗi trong quá trình xử lý, trả về thông điệp lỗi và HTTP 404 Not Found
            return new ResponseEntity<>("Lỗi: " + e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            // Bẫy các lỗi bất ngờ khác và trả về mã HTTP 500 Internal Server Error
            return new ResponseEntity<>("Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ///---------------------------------------------------

    // API cập nhật đơn hàng
    @PutMapping("/updateOrder/{orderID}")
    public ResponseEntity<Object> updateOrder(@PathVariable Long orderID, @RequestBody OrderModel orderModel) {
        try {
            OrderModel updatedOrder = orderService.updateOrder(orderID, orderModel);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy đơn hàng!", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Xảy ra lỗi khi cập nhật đơn hàng!", "message", e.getMessage()));
        }
    }

    // API xóa đơn hàng
    @DeleteMapping("/delete_order/{orderID}")
    public ResponseEntity<Object> deleteOrder(@PathVariable(required = false) Long orderID) {
        boolean deleted = orderService.deleteOrder(orderID);
        if (deleted) {
            // Trả về mã 204 No Content nếu xóa thành công
            return ResponseEntity.noContent().build();
        } else {
            // Trả về mã 404 Not Found nếu không tìm thấy đơn hàng
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // API tìm kiếm đơn hàng theo CustomerID
    @GetMapping("/search")
    public ResponseEntity<Object> searchOrdersByCustomerID(@RequestParam String displayName) {
        try {
            List<OrderModel> orders = orderService.searchOrdersByCustomerID(displayName);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy đơn hàng của khách hàng  " + displayName));
            }
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Xảy ra lỗi khi tìm kiếm đơn hàng!", "message", e.getMessage()));
        }
    }

    // Endpoint hủy đơn hàng
    @PutMapping("/{orderID}/cancel")
    public ResponseEntity<OrderModel> cancelOrder(@PathVariable Long orderID,
                                                  @RequestParam String cancellationReason) {
        try {
            OrderModel cancelledOrder = orderService.cancelOrder(orderID, cancellationReason);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API duyệt đơn hàng (cập nhật trạng thái đơn hàng)
    @PutMapping("/{orderID}/approve/{statusOrder}")
    public ResponseEntity<?> approveOrder(@PathVariable Long orderID, @PathVariable String statusOrder) {
        try {
            // Gọi hàm approveOrder từ service và truyền orderID, statusOrder
            OrderModel approvedOrder = orderService.approveOrder(orderID, statusOrder);
            // Trả về trạng thái 200 cùng với đơn hàng đã được duyệt
            return ResponseEntity.ok(approvedOrder);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu có bất kỳ ngoại lệ nào
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
