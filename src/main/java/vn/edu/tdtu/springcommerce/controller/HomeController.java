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
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            // Người dùng đã đăng nhập
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
            }
            String errorMessage = (String) model.asMap().get("err");
            if (errorMessage != null) {
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
