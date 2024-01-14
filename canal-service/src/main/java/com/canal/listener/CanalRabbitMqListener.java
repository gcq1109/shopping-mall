package com.canal.listener;

import com.alibaba.fastjson.JSONObject;
import com.canal.pojo.CanalMessage;
import com.canal.processor.RedisCommonProcessor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@Component
public class CanalRabbitMqListener {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "canal.queue", durable = "true"),
                    exchange = @Exchange(value = "canal.exchange"),
                    key = "canal.routing.key"
            )
    })
    public void handleDataChange(String message) {
        CanalMessage msg = JSONObject.parseObject(message, CanalMessage.class);
        if (msg.getTable().equalsIgnoreCase("user")
                && msg.getDatabase().equalsIgnoreCase("oauth")
                && !msg.getType().equalsIgnoreCase("insert")) {
            List<Map<String, Object>> dataSet = msg.getData();
            for (Map data : dataSet) {
                Integer id = Integer.parseInt((String) data.get("id"));
                if (id != null) {
                    redisCommonProcessor.remove(id + 10000000 + "");
                }
            }
        }
    }
}
