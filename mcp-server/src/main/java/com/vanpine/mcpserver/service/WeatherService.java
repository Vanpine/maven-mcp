package com.vanpine.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherService {


    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherForecast(String cityName) {
        // 模拟天气数据
        Map<String, String> map = Map.of(
                "北京", "111北京今天晴转多云，最低气温8℃，最高气温15℃。",
                "上海", "222上海今天阴转多云，最低气温7℃，最高气温12℃。",
                "广州", "333广州今天多云转晴，最低气温9℃，最高气温14℃。",
                "深圳", "444深圳今天阴转多云，最低气温16℃，最高气温21℃。",
                "杭州", "555杭州今天多云转晴，最低气温7℃，最高气温13℃。"
        );
        
        // 模拟调用天气API获取天气预报
        return map.getOrDefault(cityName, "抱歉，未查询到对应城市！");
    }
}
