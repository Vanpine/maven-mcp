package com.vanpine.mcpclient.service;

import com.vanpine.mcpclient.entity.Order;
import com.vanpine.mcpclient.entity.User;
import com.vanpine.mcpclient.mapper.OrderMapper;
import com.vanpine.mcpclient.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 根据用户消息做简单意图识别，查库后返回文本，由 Controller 拼进提示词，不走 Tool 调用。
 */
@Service
@RequiredArgsConstructor
public class DatabaseQueryService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;

    private static final Pattern USER_LIST = Pattern.compile("用户|用户列表|有哪些用户|查用户|所有人");
    private static final Pattern ORDER_ALL = Pattern.compile("订单|订单列表|所有订单|查订单");
    private static final Pattern ORDER_BY_USER = Pattern.compile("(?:用户)?\\s*[（(]?\\s*(\\d+)\\s*[）)]?\\s*的订单|用户ID\\s*(\\d+)");
    private static final Pattern ORDER_BY_STATUS = Pattern.compile("(未支付|已支付|已取消|已完成)\\s*的?订单|订单.*(未支付|已支付|已取消|已完成)");

    /**
     * 若用户意图是查库，则执行查询并返回「数据库查询结果」文本；否则返回空字符串。
     */
    public String getDatabaseContext(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "";
        }
        String msg = userMessage.trim();

        try {
            if (USER_LIST.matcher(msg).find()) {
                return "[数据库查询结果]\n" + listUsers();
            }
            if (ORDER_ALL.matcher(msg).find()) {
                return "[数据库查询结果]\n" + listOrders();
            }
            var userMatcher = ORDER_BY_USER.matcher(msg);
            if (userMatcher.find()) {
                String g = userMatcher.group(1);
                if (g == null) g = userMatcher.group(2);
                if (g != null) {
                    long userId = Long.parseLong(g);
                    return "[数据库查询结果]\n" + getOrdersByUserId(userId);
                }
            }
            var statusMatcher = ORDER_BY_STATUS.matcher(msg);
            if (statusMatcher.find()) {
                String status = statusMatcher.group(1);
                if (status == null) status = statusMatcher.group(2);
                if (status != null) {
                    return "[数据库查询结果]\n" + getOrdersByStatus(status.trim());
                }
            }
        } catch (Exception e) {
            return "[数据库查询结果]\n查询异常：" + e.getMessage();
        }
        return "";
    }

    public String listUsers() {
        List<User> list = userMapper.selectAll();
        if (list == null || list.isEmpty()) {
            return "当前没有任何用户数据。";
        }
        return list.stream()
                .map(u -> String.format("用户ID: %d, 用户名: %s, 手机: %s, 创建时间: %s",
                        u.getId(), u.getUserName(), u.getPhone(), u.getCreateTime()))
                .collect(Collectors.joining("\n"));
    }

    public String listOrders() {
        List<Order> list = orderMapper.selectAll();
        if (list == null || list.isEmpty()) {
            return "当前没有任何订单数据。";
        }
        return formatOrders(list);
    }

    public String getOrdersByUserId(Long userId) {
        List<Order> list = orderMapper.selectByUserId(userId);
        if (list == null || list.isEmpty()) {
            return "用户ID " + userId + " 暂无订单。";
        }
        return "用户ID " + userId + " 的订单：\n" + formatOrders(list);
    }

    public String getOrdersByStatus(String status) {
        List<Order> list = orderMapper.selectByStatus(status);
        if (list == null || list.isEmpty()) {
            return "没有状态为「" + status + "」的订单。";
        }
        return "状态为「" + status + "」的订单：\n" + formatOrders(list);
    }

    private String formatOrders(List<Order> list) {
        return list.stream()
                .map(o -> String.format("订单ID: %d, 用户ID: %d, 金额: %s元, 状态: %s, 商品: %s, 创建: %s, 支付: %s",
                        o.getId(), o.getUserId(), o.getAmount(), o.getStatus(), o.getProductName(),
                        o.getCreateTime(), o.getPayTime() != null ? o.getPayTime().toString() : "未支付"))
                .collect(Collectors.joining("\n"));
    }
}
