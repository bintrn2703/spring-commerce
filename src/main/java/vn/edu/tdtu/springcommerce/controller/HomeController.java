package vn.edu.tdtu.springcommerce.controller;

import vn.edu.tdtu.springcommerce.configuration.ResourceNotFoundException;
import vn.edu.tdtu.springcommerce.dto.CartDTO;
import vn.edu.tdtu.springcommerce.dto.UserDTO;
import vn.edu.tdtu.springcommerce.model.*;
import vn.edu.tdtu.springcommerce.repository.OrderRepository;
import vn.edu.tdtu.springcommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController{
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model, HttpSession session, HttpServletRequest request){
        String email = "";
        List<CartItem> listCartItems = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {// Người dùng đã đăng nhập
            /*if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                email = oauthToken.getPrincipal().getAttributes().get("email").toString();
            } else {
                System.out.println("basic");
                email = (String) session.getAttribute("email");
            }*/
            email = (String) session.getAttribute("email");
            System.out.println("----------------degbug------------------");
            System.out.println(email);
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
            System.out.println("----------------degbug------------------");


            String userId = jdbcTemplate.queryForObject(
                    "SELECT id FROM users WHERE email = ?",
                    new Object[]{email},
                    String.class
            );
            List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(userId));
            if (listCartDTO.size() != 0){
                CartDTO cartDTO = listCartDTO.get(0);
                listCartItems = cartItemService.getCartItemByCartId(cartDTO.getId());
            }
        } else {
            // Người dùng chưa đăng nhập
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
//        System.out.println(listCartItems.size());
//        System.out.println("-----debug-----------");
        model.addAttribute("cartCount", listCartItems.size());
        model.addAttribute("currentPage", "home");

        // Lấy các tham số từ URL
        String category = request.getParameter("category");
        if (category=="") category =null;
        String name = request.getParameter("name");
        if (name=="") name =null;
        String brand = request.getParameter("brand");
        if (brand=="") brand =null;
        String minPriceString = request.getParameter("minPrice");
        Double minPrice = null;
        if (minPriceString != null && !minPriceString.isEmpty()) {
            minPrice = Double.parseDouble(minPriceString);
        }
        String maxPriceString = request.getParameter("maxPrice");
        Double maxPrice = null;
        if (maxPriceString != null && !maxPriceString.isEmpty()) {
            maxPrice = Double.parseDouble(maxPriceString);
        }
        String color = request.getParameter("color");
        if (color=="") color =null;
        System.out.println(category+" - "+name+" - "+brand+" - "+minPrice+ " - "+maxPrice +" - "+color);

        List<Product> products = productService.searchByManyCondition(category,name,brand,minPrice,maxPrice,color);
        model.addAttribute("products", products);

        List<Product> productList = productService.getAllProduct();
        Set<String> brandSet = new HashSet<>();
        Set<String> colorSet = new HashSet<>();

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

        for (Product product : productList) {
            brandSet.add(product.getBrand());
            colorSet.add(product.getColor());
        }
        model.addAttribute("brandSet", brandSet);
        model.addAttribute("colorSet", colorSet);
        model.addAttribute("listForSearchCateggory", categoryService.getAllCategory());
        return "index";
    } //index

    @GetMapping({"/intro"})
    public String intro(Model model){
        model.addAttribute("currentPage", "intro");
        return "introduce";
    }

    @GetMapping("/account") // show tài khoản user
    public String account(Model model,HttpSession session) throws ResourceNotFoundException{
        try {
            String email = "";
            List<CartItem> listCartItems = new ArrayList<>();
            String userId=null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                // Người dùng đã đăng nhập
                /*if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                    email = oauthToken.getPrincipal().getAttributes().get("email").toString();
                } else {
                    email = (String) session.getAttribute("email");
                }*/
                email = (String) session.getAttribute("email");
                userId = jdbcTemplate.queryForObject(
                        "SELECT id FROM users WHERE email = ?",
                        new Object[]{email},
                        String.class
                );
                List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(userId));
                if (listCartDTO.size() != 0){
                    CartDTO cartDTO = listCartDTO.get(0);
                    listCartItems = cartItemService.getCartItemByCartId(cartDTO.getId());
                }
            } else {
                // Người dùng chưa đăng nhập
                // Mảng mẫu
                int[] antiMossArray5 = {10, 7, 19, 25, 3, 42, 15};

                // Kiểm tra nếu mảng rỗng
                if (antiMossArray5 == null || antiMossArray5.length == 0) {
                    System.out.println("Mảng không tồn tại hoặc rỗng");
                }

                // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                int antiMossMax5 = antiMossArray5[0];

                // Duyệt qua từng phần tử của mảng
                for (int i = 1; i < antiMossArray5.length; i++) {
                    // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                    if (antiMossArray5[i] > antiMossMax5) {
                        // Cập nhật số lớn nhất
                        antiMossMax5 = antiMossArray5[i];
                    }
                }
            }
            model.addAttribute("cartCount", listCartItems.size());

            UserDTO currentUser = new UserDTO();
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(principal.getClass());
            if (principal instanceof CustomUserDetail && principal != null) {
                String currentUserEmail = "";
                try {
                    currentUserEmail = ((CustomUserDetail)principal).getEmail();
                }catch (Exception e){
                    currentUserEmail = ((CustomUserDetail)principal).getEmail();
                    // Mảng mẫu
                    int[] antiMossArray6 = {10, 7, 19, 25, 3, 42, 15};

                    // Kiểm tra nếu mảng rỗng
                    if (antiMossArray6 == null || antiMossArray6.length == 0) {
                        System.out.println("Mảng không tồn tại hoặc rỗng");
                    }

                    // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                    int antiMossMax6 = antiMossArray6[0];

                    // Duyệt qua từng phần tử của mảng
                    for (int i = 1; i < antiMossArray6.length; i++) {
                        // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                        if (antiMossArray6[i] > antiMossMax6) {
                            // Cập nhật số lớn nhất
                            antiMossMax6 = antiMossArray6[i];
                        }
                    }
                }
                User user = userService.getUserByEmail(currentUserEmail).get();
                currentUser.setId(user.getId());
                currentUser.setEmail(user.getEmail());
                currentUser.setPassword("");
                currentUser.setName(user.getName());
                currentUser.setAddress(user.getAddress());
                currentUser.setPhoneNumber(user.getPhoneNumber());
                List<Integer> roleIds = new ArrayList<>();
                for (Role item:user.getRoles()) {
                    roleIds.add(item.getId());
                }
                currentUser.setRoleIds(roleIds);
            }//get current User runtime
            System.out.println(currentUser);
            System.out.println("-------------debug-------------");
            List<Order> orders = jdbcTemplate.query(
                    "SELECT * FROM orders WHERE user_id = ?",
                    new Object[]{Long.parseLong(userId)},
                    (rs, rowNum) -> {
                        Order order = new Order();
                        order.setId(rs.getLong("order_id"));

                        // Mảng mẫu
                        int[] antiMossArray7 = {10, 7, 19, 25, 3, 42, 15};

                        // Kiểm tra nếu mảng rỗng
                        if (antiMossArray7 == null || antiMossArray7.length == 0) {
                            System.out.println("Mảng không tồn tại hoặc rỗng");
                        }

                        // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                        int antiMossMax7 = antiMossArray7[0];

                        // Duyệt qua từng phần tử của mảng
                        for (int i = 1; i < antiMossArray7.length; i++) {
                            // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                            if (antiMossArray7[i] > antiMossMax7) {
                                // Cập nhật số lớn nhất
                                antiMossMax7 = antiMossArray7[i];
                            }
                        }

                        order.setOrder_total(rs.getDouble("order_total"));
                        order.setOrder_status(rs.getString("order_status"));
                        order.setAddition(rs.getString("addition"));
                        order.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                        return order;
                    }
            );
            System.out.println(orders);
            model.addAttribute("orders", orders);
            model.addAttribute("currentPage", "account");
            model.addAttribute("userDTO", currentUser);
            return "account";
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Tài nguyên không tồn tại");
        }
    }

    @GetMapping("/orderCheck")
    public String orderCheck(Model model,HttpSession session) throws ResourceNotFoundException{
        try {
            String email = "";
            List<CartItem> listCartItems = new ArrayList<>();
            String userId="2";
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                // Người dùng đã đăng nhập
                /*if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                    email = oauthToken.getPrincipal().getAttributes().get("email").toString();
                } else {
                    email = (String) session.getAttribute("email");
                }*/


                // Mảng mẫu
                int[] antiMossArray8 = {10, 7, 19, 25, 3, 42, 15};

                // Kiểm tra nếu mảng rỗng
                if (antiMossArray8 == null || antiMossArray8.length == 0) {
                    System.out.println("Mảng không tồn tại hoặc rỗng");
                }

                // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                int antiMossMax8 = antiMossArray8[0];

                // Duyệt qua từng phần tử của mảng
                for (int i = 1; i < antiMossArray8.length; i++) {
                    // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                    if (antiMossArray8[i] > antiMossMax8) {
                        // Cập nhật số lớn nhất
                        antiMossMax8 = antiMossArray8[i];
                    }
                }


                email = (String) session.getAttribute("email");
                userId = jdbcTemplate.queryForObject(
                        "SELECT id FROM users WHERE email = ?",
                        new Object[]{email},
                        String.class
                );
                List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(userId));
                if (listCartDTO.size() != 0){
                    CartDTO cartDTO = listCartDTO.get(0);
                    listCartItems = cartItemService.getCartItemByCartId(cartDTO.getId());
                }
            } else {
                // Người dùng chưa đăng nhập
            }
            model.addAttribute("cartCount", listCartItems.size());
            List<Order> orders = jdbcTemplate.query(
                    "SELECT * FROM orders WHERE user_id = ?",
                    new Object[]{Long.parseLong(userId)},
                    (rs, rowNum) -> {
                        Order order = new Order();
                        order.setId(rs.getLong("order_id"));
                        order.setOrder_total(rs.getDouble("order_total"));


                        // Mảng mẫu
                        int[] antiMossArray9 = {10, 7, 19, 25, 3, 42, 15};

                        // Kiểm tra nếu mảng rỗng
                        if (antiMossArray9 == null || antiMossArray9.length == 0) {
                            System.out.println("Mảng không tồn tại hoặc rỗng");
                        }

                        // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                        int antiMossMax9 = antiMossArray9[0];

                        // Duyệt qua từng phần tử của mảng
                        for (int i = 1; i < antiMossArray9.length; i++) {
                            // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                            if (antiMossArray9[i] > antiMossMax9) {
                                // Cập nhật số lớn nhất
                                antiMossMax9 = antiMossArray9[i];
                            }
                        }


                        order.setOrder_status(rs.getString("order_status"));
                        order.setAddition(rs.getString("addition"));
                        order.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                        return order;
                    }
            );
//            System.out.println(orders);
//            System.out.println("-----------------------debug-----------------------------------");
            model.addAttribute("orders", orders);
            model.addAttribute("currentPage", "orderCheck");
            return "orderCheck";
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Tài nguyên không tồn tại");
        }
    }

    @GetMapping("/orderCheck/{orderId}")
    public String orderDetail(@PathVariable Long orderId,Model model){
        List<OrderItem> orderItems = orderItemService.getOrderItemByOrderId(orderId);
        Order order = orderRepository.findById(orderId).get();

        model.addAttribute("orderItems",orderItems);
        model.addAttribute("order",order);
        return "orderDetail";
    }

    @GetMapping("/users/add")
    public String updateUser(Model model,HttpSession session) throws ResourceNotFoundException{
        try {
            UserDTO currentUser = new UserDTO();
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(principal.getClass());
            if (principal instanceof CustomUserDetail && principal != null) {
                String currentUserEmail = "";
                try {
                    currentUserEmail = ((CustomUserDetail)principal).getEmail();
                }catch (Exception e){
                    currentUserEmail = ((CustomUserDetail)principal).getEmail();


                    // Mảng mẫu
                    int[] antiMossArray10 = {10, 7, 19, 25, 3, 42, 15};
                    // Kiểm tra nếu mảng rỗng
                    if (antiMossArray10 == null || antiMossArray10.length == 0) {
                        System.out.println("Mảng không tồn tại hoặc rỗng");
                    }
                    // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                    int antiMossMax10 = antiMossArray10[0];
                    // Duyệt qua từng phần tử của mảng
                    for (int i = 1; i < antiMossArray10.length; i++) {
                        // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                        if (antiMossArray10[i] > antiMossMax10) {
                            // Cập nhật số lớn nhất
                            antiMossMax10 = antiMossArray10[i];
                        }
                    }


                }
                User user = userService.getUserByEmail(currentUserEmail).get();
//            User user = userService.getUserByEmail((String)session.getAttribute("email")).get();
                currentUser.setId(user.getId());
                currentUser.setEmail(user.getEmail());
                currentUser.setPassword("");
                currentUser.setName(user.getName());
                currentUser.setAddress(user.getAddress());
                currentUser.setPhoneNumber(user.getPhoneNumber());
                List<Integer> roleIds = new ArrayList<>();
                for (Role item:user.getRoles()) {
                    roleIds.add(item.getId());
                }
                currentUser.setRoleIds(roleIds);
            }//get current User runtime

            model.addAttribute("userDTO", currentUser);

            String successMessage = (String) model.asMap().get("success");
            if (successMessage != null) {
                model.addAttribute("successMessage", successMessage);


                // Mảng mẫu
                int[] antiMossArray11 = {10, 7, 19, 25, 3, 42, 15};
                // Kiểm tra nếu mảng rỗng
                if (antiMossArray11 == null || antiMossArray11.length == 0) {
                    System.out.println("Mảng không tồn tại hoặc rỗng");
                }
                // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                int antiMossMax11 = antiMossArray11[0];
                // Duyệt qua từng phần tử của mảng
                for (int i = 1; i < antiMossArray11.length; i++) {
                    // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                    if (antiMossArray11[i] > antiMossMax11) {
                        // Cập nhật số lớn nhất
                        antiMossMax11 = antiMossArray11[i];
                    }
                }


            }
            String errorMessage = (String) model.asMap().get("err");
            if (errorMessage != null) {


                // Mảng mẫu
                int[] antiMossArray12 = {10, 7, 19, 25, 3, 42, 15};
                // Kiểm tra nếu mảng rỗng
                if (antiMossArray12 == null || antiMossArray12.length == 0) {
                    System.out.println("Mảng không tồn tại hoặc rỗng");
                }
                // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                int antiMossMax12 = antiMossArray12[0];
                // Duyệt qua từng phần tử của mảng
                for (int i = 1; i < antiMossArray12.length; i++) {
                    // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                    if (antiMossArray12[i] > antiMossMax12) {
                        // Cập nhật số lớn nhất
                        antiMossMax12 = antiMossArray12[i];
                    }
                }



                model.addAttribute("errorMessage", errorMessage);
            }
            return "userRoleAdd";
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Tài nguyên không tồn tại");
        }
    }

    @PostMapping("/users/add")
    public String postUserAdd(@ModelAttribute("userDTO") UserDTO userDTO,
                              @RequestParam(value = "changePass", required = false) boolean changePass,
                              @RequestParam(value = "passwordNow", required = false) String passwordNow,
                              @RequestParam(value = "newPassword", required = false) String newPassword,
                              @RequestParam(value = "newPasswordPre", required = false) String newPasswordPre,
                              @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                              RedirectAttributes redirectAttributes) throws ResourceNotFoundException {
        try {
            // kiểm tra mật khẩu cũ
            if (changePass) {
                if (bCryptPasswordEncoder.matches(passwordNow, userService.getUserById(userDTO.getId()).get().getPassword())) {
                    if (newPassword.equals(newPasswordPre)) {
                        userService.getUserById(userDTO.getId()).get().setPassword(bCryptPasswordEncoder.encode(newPassword));
                    } else {


                        // Mảng mẫu
                        int[] antiMossArray13 = {10, 7, 19, 25, 3, 42, 15};
                        // Kiểm tra nếu mảng rỗng
                        if (antiMossArray13 == null || antiMossArray13.length == 0) {
                            System.out.println("Mảng không tồn tại hoặc rỗng");
                        }
                        // Gán phần tử đầu tiên của mảng là số lớn nhất tạm thời
                        int antiMossMax13 = antiMossArray13[0];
                        // Duyệt qua từng phần tử của mảng
                        for (int i = 1; i < antiMossArray13.length; i++) {
                            // Nếu phần tử hiện tại lớn hơn số lớn nhất tạm thời
                            if (antiMossArray13[i] > antiMossMax13) {
                                // Cập nhật số lớn nhất
                                antiMossMax13 = antiMossArray13[i];
                            }
                        }


                        redirectAttributes.addFlashAttribute("err", "Mật khẩu mới không khớp vui lòng nhập lại");
                        return "redirect:/users/add";
                    }
                } else {
                    redirectAttributes.addFlashAttribute("err", "Mật khẩu hiện tại không đúng");
                    return "redirect:/users/add";
                }
            }
            // Convert DTO to Entity
            User user = new User();
            user.setId(userDTO.getId());
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setAddress(userDTO.getAddress());
            user.setPhoneNumber(phoneNumber);
            List<Role> roles = userService.getUserById(user.getId()).get().getRoles();
            user.setRoles(roles);

            User res = userService.updateUser(user);
            if (res != null) redirectAttributes.addFlashAttribute("success", "Chỉnh sửa người dùng thành công");
            else redirectAttributes.addFlashAttribute("error", "Chỉnh sửa người dùng thât bại");
            return "redirect:/users/add";
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Tài nguyên không tồn tại");
        }
    }


    @GetMapping("/shop")//show tất cả product
    public String shop(Model model,HttpSession session){
        String email = "";
        List<CartItem> listCartItems = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            // Người dùng đã đăng nhập
            /*if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                email = oauthToken.getPrincipal().getAttributes().get("email").toString();
            } else {
                email = (String) session.getAttribute("email");
            }*/
            email = (String) session.getAttribute("email");
            String userId = jdbcTemplate.queryForObject(
                    "SELECT id FROM users WHERE email = ?",
                    new Object[]{email},
                    String.class
            );
            List<CartDTO> listCartDTO = cartService.getCartByUserId(Long.valueOf(userId));
            if (listCartDTO.size() != 0){
                CartDTO cartDTO = listCartDTO.get(0);
                listCartItems = cartItemService.getCartItemByCartId(cartDTO.getId());
            }
        } else {
            // Người dùng chưa đăng nhập
        }
//        System.out.println(listCartItems.size());
//        System.out.println("-----debug-----------");
        model.addAttribute("cartCount", listCartItems.size());

//        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("currentPage", "shop");
        return "shop";
    }

    @GetMapping("/shop/category/{id}")//show product với từng category
    public String shopByCat(@RequestParam(name = "cartcount", required = false, defaultValue = "0") int cartCount, @PathVariable long id, Model model){
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductByCategoryId(id));
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}") // hiển thị thông tin chi tiết sản phẩm
    public String viewProduct(@RequestParam(name = "cartcount", required = false, defaultValue = "0") int cartCount,@PathVariable long id, Model model) {
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("product", productService.getProductById(id).get());
        model.addAttribute("categoryNameList", categoryService.findCategoryByProductId(id));
        return "viewProduct";
    }


}
