package cn.alphahub.eport.signature.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 控制台输出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ConsoleOutput implements Serializable {
    /**
     * 标准输出流信息
     */
    private List<String> stdOutList;
    /**
     * 错误输出流信息
     */
    private List<String> errorOutList;
}
