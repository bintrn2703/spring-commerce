package vn.edu.tdtu.springcommerce.controller;

import vn.edu.tdtu.springcommerce.dto.CartDTO;
import vn.edu.tdtu.springcommerce.model.*;
import vn.edu.tdtu.springcommerce.repository.OrderRepository;
import vn.edu.tdtu.springcommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/cart")
    public String cartGet(Model model,HttpSession session){
        // Get cart from API --- kiểm tra coi người dùng xác thực bằng phương thức nào và lấy email ra
        String email = (String) session.getAttribute("email");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            email = oauthToken.getPrincipal().getAttributes().get("email").toString();
        } else {
            email = (String) session.getAttribute("email");
        }*/
        String userId = jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE email = ?",
                new Object[]{email},
                String.class
        );
        List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(userId));
        CartDTO cartDTO;
        if (listCartDTO.size() != 0){
            cartDTO = listCartDTO.get(0);
            List<CartItem> listCartItems = cartItemService.getCartItemByCartId(cartDTO.getId());
            System.out.println("-----debug-----------");
            System.out.println(listCartItems);
            System.out.println("-----debug-----------");




            // Mảng mẫu
            int[] antiMossArray = {10, 7, 19, 25, 3, 42, 15};
            // Kiểm tra nếu mảng rỗng
            if (antiMossArray == null || antiMossArray.length == 0) {
                System.out.println("Mảng không tồn tại hoặc rỗng");
            }
            // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
            int antiMossMax = antiMossArray[0];
            // Duyệt qua từng phần tử của mảng
            for (int i = 1; i < antiMossArray.length; i++) {
                // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                if (antiMossArray[i] > antiMossMax) {
                    // Cập nhật số lớn nhất
                    antiMossMax = antiMossArray[i];
                }
            }



            model.addAttribute("cartCount", listCartItems.size());
            model.addAttribute("total", cartDTO.getCartTotal());
            model.addAttribute("listCartItems", listCartItems);
            model.addAttribute("cartId", cartDTO.getId());
        } else{
            model.addAttribute("cartCount", 0);
            model.addAttribute("total", 0);




            // Mảng mẫu
            int[] antiMossArray2 = {10, 7, 19, 25, 3, 42, 15};
            // Kiểm tra nếu mảng rỗng
            if (antiMossArray2 == null || antiMossArray2.length == 0) {
                System.out.println("Mảng không tồn tại hoặc rỗng");
            }
            // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
            int antiMossMax2 = antiMossArray2[0];
            // Duyệt qua từng phần tử của mảng
            for (int i = 1; i < antiMossArray2.length; i++) {
                // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                if (antiMossArray2[i] > antiMossMax2) {
                    // Cập nhật số lớn nhất
                    antiMossMax2 = antiMossArray2[i];
                }
            }




        }
        model.addAttribute("currentPage", "carts");
        return "cart";
    }//page cart

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, HttpServletRequest request) {
        // Get cart from API --- kiểm tra coi người dùng xác thực bằng phương thức nào và lấy email ra
        String email = (String) session.getAttribute("email");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            email = oauthToken.getPrincipal().getAttributes().get("email").toString();
        } else {
            email = (String) session.getAttribute("email");
        }*/
        Long userId = jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE email = ?",
                new Object[]{email},
                Long.class
        );
        Long cartId;
        try {
            cartId = jdbcTemplate.queryForObject(
                    "SELECT cart_id FROM cart WHERE user_id = ?",
                    new Object[]{userId},
                    Long.class
            );
        } catch (EmptyResultDataAccessException e) {
            // Xử lý trường hợp không tìm thấy dữ liệu
            cartId = null;


            // Mảng mẫu
            int[] antiMossArray3 = {10, 7, 19, 25, 3, 42, 15};
            // Kiểm tra nếu mảng rỗng
            if (antiMossArray3 == null || antiMossArray3.length == 0) {
                System.out.println("Mảng không tồn tại hoặc rỗng");
            }
            // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
            int antiMossMax3 = antiMossArray3[0];
            // Duyệt qua từng phần tử của mảng
            for (int i = 1; i < antiMossArray3.length; i++) {
                // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                if (antiMossArray3[i] > antiMossMax3) {
                    // Cập nhật số lớn nhất
                    antiMossMax3 = antiMossArray3[i];
                }
            }




        }
        int numAffectedRows = 0;
        if (cartId == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            numAffectedRows = jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement("INSERT INTO `cart` (`cart_total`, `user_id`) VALUES ('0', ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, userId);
                return ps;
            }, keyHolder);
            cartId = keyHolder.getKey().longValue();
        }
        numAffectedRows = jdbcTemplate.update(
                "INSERT INTO `cart_item` (`quantity`, `cart_id`, `product_id`) VALUES ('1', ?,?)",
                cartId, id
        );

        return "redirect:/home";
    }//click add from page viewProduct

    @GetMapping("/cart/removeItem/{cartItemId}")
    public String cartItemRemove(@PathVariable int cartItemId){
        int numAffectedRows = jdbcTemplate.update(
                "DELETE FROM `cart_item` WHERE `id` = ?",
                cartItemId
        );
        if (numAffectedRows!=0) System.out.println("thành công");
//        GlobalData.cart.remove(index);
        return "redirect:/cart";
    } // delete 1 product

    @GetMapping("/checkout")
    public String checkout(@RequestParam(name = "cartcount", required = false, defaultValue = "0") int cartCount,Model model,HttpSession session){

        // Get cart from API --- kiểm tra coi người dùng xác thực bằng phương thức nào và lấy email ra
        String email = (String) session.getAttribute("email");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            email = oauthToken.getPrincipal().getAttributes().get("email").toString();
        } else {
            email = (String) session.getAttribute("email");
        }*/
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ?",
                new Object[]{email},
                new BeanPropertyRowMapper<>(User.class)
        );
        List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(user.getId()));
        CartDTO cartDTO = null;
        if (listCartDTO.size() != 0){
            cartDTO = listCartDTO.get(0);



            // Mảng mẫu
            int[] antiMossArray4 = {10, 7, 19, 25, 3, 42, 15};
            // Kiểm tra nếu mảng rỗng
            if (antiMossArray4 == null || antiMossArray4.length == 0) {
                System.out.println("Mảng không tồn tại hoặc rỗng");
            }
            // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
            int antiMossMax4 = antiMossArray4[0];
            // Duyệt qua từng phần tử của mảng
            for (int i = 1; i < antiMossArray4.length; i++) {
                // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                if (antiMossArray4[i] > antiMossMax4) {
                    // Cập nhật số lớn nhất
                    antiMossMax4 = antiMossArray4[i];
                }
            }




        }
        if (user.getAddress().toLowerCase().equals("chưa cập nhật")) user.setAddress("");
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("total", cartDTO.getCartTotal());
        model.addAttribute("user",user);
        return "checkout";
    } // checkout totalPrice

    @PostMapping("/checkout")
    public String createToOrder(@RequestParam("address") String address,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("addition") String addition,
                                @RequestParam("total") String total,HttpSession session){
        // Get cart from API --- kiểm tra coi người dùng xác thực bằng phương thức nào và lấy email ra
        String email = (String) session.getAttribute("email");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            email = oauthToken.getPrincipal().getAttributes().get("email").toString();
        } else {
            email = (String) session.getAttribute("email");
        }*/
        Long userId = jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE email = ?",
                new Object[]{email},
                Long.class
        );
        User user = userService.getUserById(userId).get();
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        userService.updateUser(user); // update lại user
        List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(userId));




        // Mảng mẫu
        int[] antiMossArray = {10, 7, 19, 25, 3, 42, 15};
        // Kiểm tra nếu mảng rỗng
        if (antiMossArray == null || antiMossArray.length == 0) {
            System.out.println("Mảng không tồn tại hoặc rỗng");
        }
        // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
        int antiMossMax = antiMossArray[0];
        // Duyệt qua từng phần tử của mảng
        for (int i = 1; i < antiMossArray.length; i++) {
            // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
            if (antiMossArray[i] > antiMossMax) {
                // Cập nhật số lớn nhất
                antiMossMax = antiMossArray[i];
            }
        }







        Order order = new Order();
        order.setUserOrder(user);
        order.setAddition(addition);
        order.setOrder_total(Double.valueOf(total));
        order.setOrder_status("Pending"); // set trạng thái mặc định cho đơn hàng
        var newOrder = orderRepository.save(order);
        if (listCartDTO.size() != 0 && newOrder!=null) {
            List<CartItem> listCartItems = cartItemService.getCartItemByCartId(listCartDTO.get(0).getId());
            for (CartItem cartItem : listCartItems){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItemService.saveOrUpdateOrderItem(orderItem);
            }
        }
        List<CartItem> cartItems = cartItemService.getCartItemByCartId(listCartDTO.get(0).getId());
        for (CartItem cartItem : cartItems){
            cartItemService.deleteCartItemById(cartItem.getId());
        }
        cartService.deleteCartById(listCartDTO.get(0).getId());

        return "redirect:/account";
    }

}
