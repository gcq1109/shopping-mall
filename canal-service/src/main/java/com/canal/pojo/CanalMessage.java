package com.canal.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author gcq1109
 * @date 2023/7/9 15:52
 * @email gcq1109@126.com
 */
@NoArgsConstructor
@Data
public class CanalMessage<T> {

    @JsonProperty("type")
    private String type;

    @JsonProperty("table")
    private String table;

    @JsonProperty("data")
    private List<Map<String, Object>> data;

    @JsonProperty("database")
    private String database;

    @JsonProperty("es")
    private Long es;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("isDdl")
    private Boolean isDdl;

    @JsonProperty("pkNames")
    private List<String> pkNames;

    @JsonProperty("old")
    private List<T> old;

    @JsonProperty("sql")
    private String sql;

    @JsonProperty("ts")
    private String ts;
}
