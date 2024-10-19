package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.OrderDetailModel;
import com.example.milkteaapp.Service.Interface.IOrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/public/orderDetails")
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;

    public OrderDetailController(IOrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllOrderDetails() {
        try {
            List<OrderDetailModel> orderDetails = orderDetailService.getAllOrderDetails();
            return ResponseEntity.status(HttpStatus.OK).body(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi truy vấn dữ liệu!", "details", e.getMessage()));
        }
    }

    @GetMapping("/getByOrderID")
    public ResponseEntity<Object> getOrderDetailsByOrderID(@RequestParam Long orderID) {
        try {
            List<OrderDetailModel> orderDetails = orderDetailService.getOrderDetailsByOrderID(orderID);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("error", "Không tìm thấy chi tiết đơn hàng với OrderID: " + orderID));
            }
            return ResponseEntity.status(HttpStatus.OK).body(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi truy vấn chi tiết đơn hàng!", "details", e.getMessage()));
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<Object> getOrderDetailById(@RequestParam Long orderDetailID) {
        try {
            OrderDetailModel orderDetail = orderDetailService.getOrderDetailById(orderDetailID);
            return ResponseEntity.status(HttpStatus.OK).body(orderDetail);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Chi tiết đơn hàng không tìm thấy với ID: " + orderDetailID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi truy vấn chi tiết đơn hàng!", "details", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addOrderDetail(@RequestBody OrderDetailModel orderDetailModel) {
        try {
            if (orderDetailModel.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of("error", "Số lượng phải lớn hơn 0"));
            }
            if (orderDetailModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of("error", "Giá phải lớn hơn 0"));
            }
            OrderDetailModel newOrderDetail = orderDetailService.createOrderDetail(orderDetailModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrderDetail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", "Dữ liệu đầu vào không hợp lệ!", "details", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi thêm chi tiết đơn hàng!", "details", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateOrderDetail(@RequestParam Long orderDetailID,
                                                    @RequestBody OrderDetailModel updatedOrderDetail) {
        try {
            OrderDetailModel updatedOrderDetailModel = orderDetailService.updateOrderDetail(orderDetailID, updatedOrderDetail);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("success", "Cập nhật chi tiết đơn hàng thành công!", "details", updatedOrderDetailModel));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Chi tiết đơn hàng không tìm thấy với ID: " + orderDetailID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi cập nhật chi tiết đơn hàng!", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteOrderDetail(@RequestParam Long orderDetailID) {
        try {
            orderDetailService.deleteOrderDetail(orderDetailID);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("success", "Xóa chi tiết đơn hàng thành công!"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Chi tiết đơn hàng không tìm thấy với ID: " + orderDetailID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi xóa chi tiết đơn hàng!", "details", e.getMessage()));
        }
    }
}
